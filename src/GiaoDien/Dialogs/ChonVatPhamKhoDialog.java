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
 * Dialog chọn Đồ ăn & Cho thuê Vật phẩm Kho hàng (Bóng, Giày, Áo lưới, Nước suối...).
 * Hỗ trợ cho thuê vật phẩm trong kho khi tạo phiếu đặt sân.
 */
public class ChonVatPhamKhoDialog extends JDialog {

    public static class SelectedItem {
        private final DichVu dichVu;
        private final int soLuong;

        public SelectedItem(DichVu dv, int sl) {
            this.dichVu = dv;
            this.soLuong = sl;
        }

        public DichVu getDichVu() { return dichVu; }
        public int getSoLuong() { return soLuong; }
        public double getThanhTien() { return dichVu.getDonGia() * soLuong; }
    }

    private DefaultTableModel modelKho;
    private JTable tableKho;
    private JTextField txtSearch;

    private final Map<Integer, Integer> selectedQtyMap = new HashMap<>();
    private final List<SelectedItem> selectedItems = new ArrayList<>();
    private double totalCost = 0;
    private boolean confirmed = false;

    public ChonVatPhamKhoDialog(JFrame parent) {
        super(parent, "Chọn Đồ ăn & Cho thuê Vật phẩm kho", true);
        initComponents(parent);
    }

    public void setInitialQuantities(Map<Integer, Integer> initMap) {
        if (initMap != null) {
            this.selectedQtyMap.putAll(initMap);
            reloadTable();
        }
    }

    private void initComponents(JFrame parent) {
        setSize(760, 500);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // Header Panel
        JPanel pnlHeader = PageUI.createPageHeader(
                "Chọn Đồ ăn & Cho thuê Vật phẩm Kho hàng",
                "Chọn Đồ ăn/Nước giải khát hoặc Cho thuê vật phẩm kho (Áo lưới, Bóng đá, Giày thi đấu...)"
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // Center Panel
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Search Bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm hàng kho / vật phẩm:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(18);
        txtSearch.setPreferredSize(new Dimension(200, 32));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.getDocument().addDocumentListener(new SimpleDocListener(this::reloadTable));
        pnlSearch.add(txtSearch);

        JButton btnAdd = new JButton("+1 Đơn vị");
        btnAdd.setFont(UIConstants.FONT_BUTTON);
        btnAdd.addActionListener(e -> changeQty(1));

        JButton btnSub = new JButton("−1 Đơn vị");
        btnSub.setFont(UIConstants.FONT_BUTTON);
        btnSub.addActionListener(e -> changeQty(-1));

        pnlSearch.add(btnAdd);
        pnlSearch.add(btnSub);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // Table
        modelKho = new DefaultTableModel(
                new String[]{"ID", "Tên đồ ăn / vật phẩm", "Hình thức", "Đơn giá", "Đơn vị", "Tồn kho", "Số lượng chọn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 6; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) return Integer.class;
                return String.class;
            }
        };

        tableKho = new JTable(modelKho);
        PageUI.styleTable(tableKho);
        tableKho.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableKho.getColumnModel().getColumn(1).setPreferredWidth(75);
        tableKho.getColumnModel().getColumn(2).setPreferredWidth(170);
        tableKho.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableKho.getColumnModel().getColumn(4).setPreferredWidth(95);
        tableKho.getColumnModel().getColumn(5).setPreferredWidth(95);
        tableKho.getColumnModel().getColumn(6).setPreferredWidth(90);
        tableKho.getColumnModel().getColumn(6).setPreferredWidth(75);
        tableKho.getColumnModel().getColumn(7).setPreferredWidth(110);

        modelKho.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelKho.getRowCount()) {
                    int id = (Integer) modelKho.getValueAt(r, 0);
                    Object val = modelKho.getValueAt(r, 6);
                    int qty = (val instanceof Integer num) ? num : 0;
                    
                    DichVu item = DataStore.get().getKhoItems().stream()
                            .filter(k -> k.getId() == id)
                            .findFirst().orElse(null);

                    if (item != null && qty > item.getSoLuongTon()) {
                        JOptionPane.showMessageDialog(this,
                                "Số lượng chọn (" + qty + ") vượt quá số lượng tồn kho (" + item.getSoLuongTon() + ")!",
                                "Cảnh báo tồn kho", JOptionPane.WARNING_MESSAGE);
                        qty = item.getSoLuongTon();
                        modelKho.setValueAt(qty, r, 6);
                    }

                    selectedQtyMap.put(id, Math.max(0, qty));
                }
            }
        });

        pnlCenter.add(new JScrollPane(tableKho), BorderLayout.CENTER);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // Footer
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(UIConstants.FONT_BUTTON);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnDone = new JButton(" Hoàn tất chọn");
        btnDone.setIcon(Utils.IconUtils.getCheckIcon(16));
        btnDone.setFont(UIConstants.FONT_BUTTON);
        btnDone.setBackground(UIConstants.PRIMARY);
        btnDone.setForeground(Color.WHITE);
        btnDone.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnDone);
        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        reloadTable();
    }

    private void reloadTable() {
        String kw = txtSearch.getText().trim().toLowerCase();
        modelKho.setRowCount(0);

        for (DichVu item : DataStore.get().getKhoItems()) {
            boolean matchName = item.getTenDichVu() != null && item.getTenDichVu().toLowerCase().contains(kw);
            boolean matchDesc = item.getMoTa() != null && item.getMoTa().toLowerCase().contains(kw);
            if (kw.isEmpty() || matchName || matchDesc) {
                int currentQty = selectedQtyMap.getOrDefault(item.getId(), 0);
                String hinhThuc = isRentalItem(item) ? "Cho thuê" : "Bán giải khát";
                modelKho.addRow(new Object[]{
                        item.getId(),
                        item.getTenDichVu(),
                        hinhThuc,
                        String.format("%,.0f đ", item.getDonGia()),
                        item.getDonVi() != null ? item.getDonVi() : "cái",
                        item.getSoLuongTon(),
                        currentQty
                });
            }
        }
    }

    private boolean isRentalItem(DichVu item) {
        if (item == null || item.getTenDichVu() == null) return false;
        String name = item.getTenDichVu().toLowerCase();
        return name.contains("áo") || name.contains("bóng") || name.contains("găng") || name.contains("giày") || name.contains("thuê");
    }

    private void changeQty(int delta) {
        int selectedRow = tableKho.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hàng kho / vật phẩm trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) modelKho.getValueAt(selectedRow, 0);
        DichVu item = DataStore.get().getKhoItems().stream().filter(k -> k.getId() == id).findFirst().orElse(null);
        if (item == null) return;

        int oldVal = selectedQtyMap.getOrDefault(id, 0);
        int newVal = Math.max(0, oldVal + delta);
        if (newVal > item.getSoLuongTon()) {
            JOptionPane.showMessageDialog(this,
                    "Không thể chọn quá số lượng tồn kho (" + item.getSoLuongTon() + " " + item.getDonVi() + ")!",
                    "Cảnh báo kho", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedQtyMap.put(id, newVal);
        modelKho.setValueAt(newVal, selectedRow, 6);
    }

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

    public boolean isConfirmed() { return confirmed; }
    public Map<Integer, Integer> getSelectedQtyMap() { return selectedQtyMap; }
    public List<SelectedItem> getSelectedItems() { return selectedItems; }
    public double getTotalCost() { return totalCost; }
}
