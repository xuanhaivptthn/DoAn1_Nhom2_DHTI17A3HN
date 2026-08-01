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
 * Dialog chọn Dịch vụ đi kèm đặt sân (Trọng tài, Huấn luyện viên, Quay phim...).
 * Hiển thị thông tin cơ bản rõ ràng, dễ sử dụng.
 */
public class ChonDichVuDialog extends JDialog {

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

    private DefaultTableModel modelDichVu;
    private JTable tableDichVu;
    private JTextField txtSearch;

    private final Map<Integer, Integer> selectedQtyMap = new HashMap<>();
    private final List<SelectedItem> selectedServices = new ArrayList<>();
    private double totalCost = 0;
    private boolean confirmed = false;

    public ChonDichVuDialog(JFrame parent) {
        super(parent, "Chọn Dịch vụ đi kèm", true);
        initComponents(parent);
    }

    public void setInitialQuantities(Map<Integer, Integer> initMap) {
        if (initMap != null) {
            this.selectedQtyMap.putAll(initMap);
            reloadTable();
        }
    }

    private void initComponents(JFrame parent) {
        setSize(680, 460);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // Header Panel
        JPanel pnlHeader = PageUI.createPageHeader(
                "Chọn Dịch vụ đi kèm",
                "Chọn các dịch vụ sân bóng: Trọng tài, Huấn luyện viên, Đèn chiếu sáng..."
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // Center Panel
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Search Bar & Action Buttons
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm dịch vụ:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(16);
        txtSearch.setPreferredSize(new Dimension(190, 32));
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

        // Table với thông tin cơ bản: Mã DV, Tên dịch vụ, Đơn giá, Đơn vị, Số lượng chọn
        modelDichVu = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá", "Đơn vị tính", "Số lượng chọn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Integer.class;
                return String.class;
            }
        };

        tableDichVu = new JTable(modelDichVu);
        PageUI.styleTable(tableDichVu);
        tableDichVu.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableDichVu.getColumnModel().getColumn(1).setPreferredWidth(240);
        tableDichVu.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableDichVu.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableDichVu.getColumnModel().getColumn(4).setPreferredWidth(110);

        modelDichVu.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDichVu.getRowCount()) {
                    String maStr = modelDichVu.getValueAt(r, 0).toString();
                    int id = parseIdFromMaStr(maStr);
                    Object val = modelDichVu.getValueAt(r, 4);
                    int qty = (val instanceof Integer num) ? num : 0;
                    selectedQtyMap.put(id, Math.max(0, qty));
                }
            }
        });

        pnlCenter.add(new JScrollPane(tableDichVu), BorderLayout.CENTER);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // Footer Panel
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

    private int parseIdFromMaStr(String maStr) {
        try {
            return Integer.parseInt(maStr.replaceAll("\\D", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private void reloadTable() {
        String kw = txtSearch.getText().trim().toLowerCase();
        modelDichVu.setRowCount(0);

        for (DichVu dv : DataStore.get().getDichVus()) {
            boolean matchName = dv.getTenDichVu() != null && dv.getTenDichVu().toLowerCase().contains(kw);
            boolean matchDesc = dv.getMoTa() != null && dv.getMoTa().toLowerCase().contains(kw);
            if (kw.isEmpty() || matchName || matchDesc) {
                int currentQty = selectedQtyMap.getOrDefault(dv.getId(), 0);
                String maStr = String.format("DV%02d", dv.getId());
                modelDichVu.addRow(new Object[]{
                        maStr,
                        dv.getTenDichVu(),
                        String.format("%,.0f đ", dv.getDonGia()),
                        dv.getDonVi() != null ? dv.getDonVi() : "Lần",
                        currentQty
                });
            }
        }
    }

    private void changeQty(int delta) {
        int selectedRow = tableDichVu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dịch vụ trong bảng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maStr = modelDichVu.getValueAt(selectedRow, 0).toString();
        int id = parseIdFromMaStr(maStr);
        int oldVal = selectedQtyMap.getOrDefault(id, 0);
        int newVal = Math.max(0, oldVal + delta);
        selectedQtyMap.put(id, newVal);
        modelDichVu.setValueAt(newVal, selectedRow, 4);
    }

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

    public boolean isConfirmed() { return confirmed; }
    public Map<Integer, Integer> getSelectedQtyMap() { return selectedQtyMap; }
    public List<SelectedItem> getSelectedServices() { return selectedServices; }
    public double getTotalCost() { return totalCost; }
}
