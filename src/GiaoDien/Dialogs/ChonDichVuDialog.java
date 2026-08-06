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
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
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
 * Dialog chọn Dịch vụ đi kèm đặt sân (Trọng tài, Huấn luyện viên, Quay phim...).
 * <p>
 * Hỗ trợ lọc tìm kiếm thời gian thực theo tên hoặc mô tả dịch vụ.
 * Giao diện sử dụng bảng JTable có tích chọn checkbox 'Chọn đặt' đơn giản, tiện lợi.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChonDichVuDialog extends JDialog {

    /**
     * Lớp đại diện cho một mục dịch vụ đã được người dùng chọn mua cùng với số lượng.
     */
    public static class SelectedItem {
        /** Đối tượng dịch vụ */
        private final DichVu dichVu;
        /** Số lượng đã đăng ký chọn */
        private final int soLuong;

        /**
         * Khởi tạo một mục dịch vụ được chọn.
         *
         * @param dv Đối tượng DichVu
         * @param sl Số lượng chọn
         */
        public SelectedItem(DichVu dv, int sl) {
            this.dichVu = dv;
            this.soLuong = sl;
        }

        /**
         * Lấy đối tượng dịch vụ.
         *
         * @return Đối tượng DichVu
         */
        public DichVu getDichVu() { return dichVu; }

        /**
         * Lấy số lượng dịch vụ.
         *
         * @return Số lượng chọn
         */
        public int getSoLuong() { return soLuong; }

        /**
         * Tính tổng thành tiền của mục dịch vụ này.
         *
         * @return Thành tiền = Đơn giá x Số lượng
         */
        public double getThanhTien() { return dichVu.getDonGia() * soLuong; }
    }

    /** Model quản lý dữ liệu hiển thị trên bảng dịch vụ */
    private DefaultTableModel modelDichVu;

    /** Bảng hiển thị danh sách các gói dịch vụ */
    private JTable tableDichVu;

    /** Ô nhập từ khóa tìm kiếm dịch vụ */
    private JTextField txtSearch;

    /** Map lưu trữ số lượng chọn theo ID dịch vụ */
    private final Map<Integer, Integer> selectedQtyMap = new HashMap<>();

    /** Danh sách các mục dịch vụ được người dùng chốt chọn */
    private final List<SelectedItem> selectedServices = new ArrayList<>();

    /** Tổng chi phí của các dịch vụ đã chọn */
    private double totalCost = 0;

    /** Cờ xác nhận đã bấm nút Hoàn tất chọn */
    private boolean confirmed = false;

    /**
     * Khởi tạo hộp thoại chọn dịch vụ đi kèm.
     *
     * @param parent Cửa sổ cha (JFrame)
     */
    public ChonDichVuDialog(JFrame parent) {
        super(parent, "Chọn Dịch vụ đi kèm", true);
        initComponents(parent);
    }

    /**
     * Thiết lập danh sách số lượng đã chọn sẵn từ trước.
     *
     * @param initMap Map chứa mã ID dịch vụ và số lượng ban đầu
     */
    public void setInitialQuantities(Map<Integer, Integer> initMap) {
        if (initMap != null) {
            this.selectedQtyMap.putAll(initMap);
            reloadTable();
        }
    }

    /**
     * Xây dựng và thiết lập toàn bộ thành phần giao diện người dùng.
     *
     * @param parent Cửa sổ cha
     */
    private void initComponents(JFrame parent) {
        setSize(680, 460);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // ── 1. Header Panel ──────────────────────────────────────────────────
        JPanel pnlHeader = PageUI.createPageHeader(
                "Chọn Dịch vụ đi kèm",
                "Chọn các dịch vụ sân bóng đi kèm: Trọng tài, Huấn luyện viên, Đèn chiếu sáng, Quay phim..."
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // ── 2. Center Panel ──────────────────────────────────────────────────
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Thanh tìm kiếm dịch vụ
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm dịch vụ:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(240, 32));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.getDocument().addDocumentListener(new SimpleDocListener(this::reloadTable));
        pnlSearch.add(txtSearch);

        JLabel lblHint = new JLabel("(Tích chọn ô 'Chọn đặt' để thêm dịch vụ)");
        lblHint.setFont(UIConstants.FONT_SMALL);
        lblHint.setForeground(UIConstants.TEXT_SECONDARY);
        pnlSearch.add(lblHint);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // ── 3. Table với 4 cột: Mã DV, Tên dịch vụ, Đơn giá, Chọn đặt ────────
        modelDichVu = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá", "Chọn đặt"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 3; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Boolean.class;
                return String.class;
            }
        };

        tableDichVu = new JTable(modelDichVu);
        PageUI.styleTable(tableDichVu);
        tableDichVu.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableDichVu.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableDichVu.getColumnModel().getColumn(2).setPreferredWidth(140);
        tableDichVu.getColumnModel().getColumn(3).setPreferredWidth(100);

        tableDichVu.getColumnModel().getColumn(3).setCellRenderer(tableDichVu.getDefaultRenderer(Boolean.class));
        tableDichVu.getColumnModel().getColumn(3).setCellEditor(tableDichVu.getDefaultEditor(Boolean.class));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableDichVu.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Lắng nghe sự kiện tích chọn trên bảng để cập nhật map selectedQtyMap
        modelDichVu.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDichVu.getRowCount()) {
                    String maStr = modelDichVu.getValueAt(r, 0).toString();
                    DichVu item = DataStore.get().getDichVus().stream()
                            .filter(d -> maStr.equalsIgnoreCase(d.getMaDichVu()))
                            .findFirst().orElse(null);
                    if (item != null) {
                        Object val = modelDichVu.getValueAt(r, 3);
                        boolean isChecked = Boolean.TRUE.equals(val);
                        selectedQtyMap.put(item.getId(), isChecked ? 1 : 0);
                    }
                }
            }
        });

        pnlCenter.add(new JScrollPane(tableDichVu), BorderLayout.CENTER);
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

        getRootPane().setDefaultButton(btnDone);

        reloadTable();
    }

    /**
     * Tải lại nội dung bảng danh sách dịch vụ dựa trên từ khóa tìm kiếm.
     */
    private void reloadTable() {
        String kw = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";
        modelDichVu.setRowCount(0);

        for (DichVu dv : DataStore.get().getDichVus()) {
            boolean matchName = dv.getTenDichVu() != null && dv.getTenDichVu().toLowerCase().contains(kw);
            boolean matchDesc = dv.getMoTa() != null && dv.getMoTa().toLowerCase().contains(kw);
            if (kw.isEmpty() || matchName || matchDesc) {
                int currentQty = selectedQtyMap.getOrDefault(dv.getId(), 0);
                modelDichVu.addRow(new Object[]{
                        dv.getMaDichVu(),
                        dv.getTenDichVu(),
                        String.format("%,.0f đ", dv.getDonGia()),
                        currentQty > 0
                });
            }
        }
    }

    /**
     * Xử lý xác nhận lưu danh sách dịch vụ đã chọn.
     * Duyệt qua map để tổng hợp danh sách đối tượng SelectedItem và tổng tiền.
     */
    private void onConfirm() {
        selectedServices.clear();
        totalCost = 0;

        for (DichVu dv : DataStore.get().getDichVus()) {
            int qty = selectedQtyMap.getOrDefault(dv.getId(), 0);
            if (qty > 0) {
                SelectedItem item = new SelectedItem(dv, qty);
                selectedServices.add(item);
                totalCost += item.getThanhTien();
            }
        }

        confirmed = true;
        dispose();
    }

    /**
     * Kiểm tra trạng thái đã bấm xác nhận hay chưa.
     *
     * @return true nếu người dùng hoàn tất chọn thành công
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy bản đồ lưu số lượng chọn theo ID dịch vụ.
     *
     * @return Map (ID dịch vụ -> số lượng chọn)
     */
    public Map<Integer, Integer> getSelectedQtyMap() { return selectedQtyMap; }

    /**
     * Lấy danh sách các đối tượng dịch vụ kèm số lượng đã chọn.
     *
     * @return Danh sách SelectedItem
     */
    public List<SelectedItem> getSelectedServices() { return selectedServices; }

    /**
     * Lấy tổng số tiền của toàn bộ dịch vụ đã chọn.
     *
     * @return Tổng số tiền (VNĐ)
     */
    public double getTotalCost() { return totalCost; }
}
