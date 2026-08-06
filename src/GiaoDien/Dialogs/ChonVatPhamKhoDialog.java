package GiaoDien.Dialogs;

import Model.DichVu;
import Utils.DataStore;
import Utils.PageUI;
import Utils.SimpleDocListener;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dialog chọn Đồ ăn & Cho thuê Vật phẩm kho (Bóng, Giày, Áo lưới, Nước suối...).
 * <p>
 * Cho phép lọc sản phẩm theo tên/mô tả, tăng/giảm số lượng chọn trực tiếp bằng nút hoặc nhập vào ô trên bảng.
 * Tự động kiểm tra giới hạn tồn kho để ngăn chọn vượt quá số lượng hiện có.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChonVatPhamKhoDialog extends JDialog {

    /**
     * Lớp lưu trữ một mục vật phẩm kho / đồ ăn được chọn kèm số lượng đăng ký mua hoặc thuê.
     */
    public static class SelectedItem {
        /** Đối tượng mặt hàng kho (nước giải khát, đồ ăn, vật phẩm cho thuê) */
        private final DichVu dichVu;

        /** Số lượng đã đăng ký chọn mua/thuê */
        private final int soLuong;

        /**
         * Khởi tạo mục vật phẩm kho được chọn.
         *
         * @param dv Đối tượng DichVu đại diện sản phẩm kho
         * @param sl Số lượng chọn
         */
        public SelectedItem(DichVu dv, int sl) {
            this.dichVu = dv;
            this.soLuong = sl;
        }

        /**
         * Lấy đối tượng DichVu đại diện mặt hàng kho.
         *
         * @return Đối tượng DichVu
         */
        public DichVu getDichVu() { return dichVu; }

        /**
         * Lấy số lượng đăng ký mua hoặc thuê.
         *
         * @return Số lượng chọn
         */
        public int getSoLuong() { return soLuong; }

        /**
         * Tính thành tiền của mục sản phẩm này.
         *
         * @return Thành tiền = Đơn giá x Số lượng
         */
        public double getThanhTien() { return dichVu.getDonGia() * soLuong; }
    }

    /** Model dữ liệu bảng mặt hàng kho */
    private DefaultTableModel modelKho;

    /** Bảng hiển thị danh sách đồ ăn và vật phẩm kho */
    private JTable tableKho;

    /** Ô nhập liệu từ khóa tìm kiếm tên/mô tả sản phẩm */
    private JTextField txtSearch;

    /** Map lưu số lượng chọn theo ID mặt hàng kho */
    private final Map<Integer, Integer> selectedQtyMap = new HashMap<>();

    /** Danh sách các đối tượng SelectedItem được chốt chọn */
    private final List<SelectedItem> selectedItems = new ArrayList<>();

    /** Tổng chi phí của các món / vật phẩm kho đã chọn */
    private double totalCost = 0;

    /** Cờ xác nhận đã bấm Hoàn tất chọn */
    private boolean confirmed = false;

    /**
     * Khởi tạo thoại chọn đồ ăn & vật phẩm kho.
     *
     * @param parent Cửa sổ cha (JFrame)
     */
    public ChonVatPhamKhoDialog(JFrame parent) {
        super(parent, "Chọn Đồ ăn & Cho thuê Vật phẩm kho", true);
        initComponents(parent);
    }

    /**
     * Khởi tạo số lượng chọn sẵn từ map ban đầu.
     *
     * @param initMap Map lưu ID kho -> Số lượng chọn ban đầu
     */
    public void setInitialQuantities(Map<Integer, Integer> initMap) {
        if (initMap != null) {
            this.selectedQtyMap.putAll(initMap);
            reloadTable();
        }
    }

    /**
     * Khởi tạo và thiết lập các thành phần trên giao diện người dùng.
     *
     * @param parent Cửa sổ cha
     */
    private void initComponents(JFrame parent) {
        setSize(700, 460);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // ── 1. Header Panel ──────────────────────────────────────────────────
        JPanel pnlHeader = PageUI.createPageHeader(
                "Chọn Đồ ăn & Vật phẩm kho",
                "Chọn Nước giải khát / Đồ ăn hoặc Cho thuê vật phẩm kho (Áo lưới, Bóng đá, Giày...)"
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // ── 2. Center Panel ──────────────────────────────────────────────────
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Thanh tìm kiếm và các nút tăng/giảm nhanh số lượng
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm sản phẩm:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(16);
        txtSearch.setPreferredSize(new Dimension(190, 32));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.getDocument().addDocumentListener(new SimpleDocListener(this::reloadTable));
        pnlSearch.add(txtSearch);

        // Nút tăng 1 đơn vị
        JButton btnAdd = new JButton("+1 Đơn vị");
        btnAdd.setFont(UIConstants.FONT_BUTTON);
        btnAdd.addActionListener(e -> changeQty(1));

        // Nút giảm 1 đơn vị
        JButton btnSub = new JButton("−1 Đơn vị");
        btnSub.setFont(UIConstants.FONT_BUTTON);
        btnSub.addActionListener(e -> changeQty(-1));

        pnlSearch.add(btnAdd);
        pnlSearch.add(btnSub);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // ── 3. Table Bảng đồ ăn / vật phẩm kho ─────────────────────────────
        modelKho = new DefaultTableModel(
                new String[]{"Mã SP", "Tên đồ ăn / vật phẩm", "Đơn giá", "Đơn vị", "Tồn kho", "Số lượng chọn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 5; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return Integer.class;
                return String.class;
            }
        };

        tableKho = new JTable(modelKho);
        PageUI.styleTable(tableKho);
        tableKho.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableKho.getColumnModel().getColumn(1).setPreferredWidth(230);
        tableKho.getColumnModel().getColumn(2).setPreferredWidth(110);
        tableKho.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableKho.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableKho.getColumnModel().getColumn(5).setPreferredWidth(110);

        // Lắng nghe chỉnh sửa số lượng trực tiếp trên ô cột 5 của bảng
        modelKho.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelKho.getRowCount()) {
                    String maStr = modelKho.getValueAt(r, 0).toString();
                    DichVu item = DataStore.get().getKhoItems().stream()
                            .filter(k -> maStr.equalsIgnoreCase(k.getMaDichVu()))
                            .findFirst().orElse(null);

                    if (item != null) {
                        Object val = modelKho.getValueAt(r, 5);
                        int qty = (val instanceof Integer num) ? num : 0;

                        // Kiểm tra nếu chọn vượt quá tồn kho hiện tại
                        if (qty > item.getSoLuongTon()) {
                            JOptionPane.showMessageDialog(this,
                                    "Số lượng chọn (" + qty + ") vượt quá số lượng tồn kho (" + item.getSoLuongTon() + ")!",
                                    "Cảnh báo tồn kho", JOptionPane.WARNING_MESSAGE);
                            qty = item.getSoLuongTon();
                            modelKho.setValueAt(qty, r, 5);
                        }

                        selectedQtyMap.put(item.getId(), Math.max(0, qty));
                    }
                }
            }
        });

        pnlCenter.add(new JScrollPane(tableKho), BorderLayout.CENTER);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // ── 4. Footer Panel ──────────────────────────────────────────────────
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton btnCancel = new JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnDone = new JButton(" Hoàn tất chọn");
        btnDone.setIcon(Utils.IconUtils.getCheckIcon(16));
        Utils.PageUI.stylePrimaryButton(btnDone);
        btnDone.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnDone);
        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        reloadTable();
    }

    /**
     * Tải lại bảng dữ liệu vật phẩm kho tương ứng với từ khóa lọc tìm kiếm.
     */
    private void reloadTable() {
        String kw = txtSearch.getText().trim().toLowerCase();
        modelKho.setRowCount(0);

        for (DichVu item : DataStore.get().getKhoItems()) {
            boolean matchName = item.getTenDichVu() != null && item.getTenDichVu().toLowerCase().contains(kw);
            boolean matchDesc = item.getMoTa() != null && item.getMoTa().toLowerCase().contains(kw);
            if (kw.isEmpty() || matchName || matchDesc) {
                int currentQty = selectedQtyMap.getOrDefault(item.getId(), 0);
                modelKho.addRow(new Object[]{
                        item.getMaDichVu(),
                        item.getTenDichVu(),
                        String.format("%,.0f đ", item.getDonGia()),
                        item.getDonVi() != null ? item.getDonVi() : "cái",
                        item.getSoLuongTon(),
                        currentQty
                });
            }
        }
    }

    /**
     * Thay đổi tăng hoặc giảm số lượng chọn của mặt hàng đang chọn dòng trên bảng.
     *
     * @param delta +1 để tăng hoặc -1 để giảm số lượng
     */
    private void changeQty(int delta) {
        int selectedRow = tableKho.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 sản phẩm / vật phẩm trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maStr = modelKho.getValueAt(selectedRow, 0).toString();
        DichVu item = DataStore.get().getKhoItems().stream()
                .filter(k -> maStr.equalsIgnoreCase(k.getMaDichVu()))
                .findFirst().orElse(null);
        if (item == null) return;

        int id = item.getId();
        int oldVal = selectedQtyMap.getOrDefault(id, 0);
        int newVal = Math.max(0, oldVal + delta);
        if (newVal > item.getSoLuongTon()) {
            JOptionPane.showMessageDialog(this,
                    "Không thể chọn quá số lượng tồn kho (" + item.getSoLuongTon() + " " + item.getDonVi() + ")!",
                    "Cảnh báo kho", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedQtyMap.put(id, newVal);
        modelKho.setValueAt(newVal, selectedRow, 5);
    }

    /**
     * Xử lý khi nhấn nút Hoàn tất chọn.
     * Tổng hợp danh sách SelectedItem có số lượng > 0 và tính toán tổng tiền.
     */
    private void onConfirm() {
        selectedItems.clear();
        totalCost = 0;

        for (DichVu item : DataStore.get().getKhoItems()) {
            int qty = selectedQtyMap.getOrDefault(item.getId(), 0);
            if (qty > 0) {
                SelectedItem s = new SelectedItem(item, qty);
                selectedItems.add(s);
                totalCost += s.getThanhTien();
            }
        }

        confirmed = true;
        dispose();
    }

    /**
     * Kiểm tra dialog có được xác nhận hoàn tất hay không.
     *
     * @return true nếu người dùng bấm nút Hoàn tất chọn
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy bản đồ lưu số lượng chọn theo ID vật phẩm kho.
     *
     * @return Map (ID kho -> Số lượng chọn)
     */
    public Map<Integer, Integer> getSelectedQtyMap() { return selectedQtyMap; }

    /**
     * Lấy danh sách các đối tượng SelectedItem được chọn mua/thuê.
     *
     * @return Danh sách SelectedItem
     */
    public List<SelectedItem> getSelectedItems() { return selectedItems; }

    /**
     * Lấy tổng số tiền của toàn bộ sản phẩm/vật phẩm kho được chọn.
     *
     * @return Tổng chi phí (VNĐ)
     */
    public double getTotalCost() { return totalCost; }
}
