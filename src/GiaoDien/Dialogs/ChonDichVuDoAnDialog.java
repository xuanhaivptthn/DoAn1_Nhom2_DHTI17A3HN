package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

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
 * Dialog chọn Dịch vụ (Panel 1) và chọn Đồ ăn/Kho (Panel 2) tách biệt,
 * tích hợp Ô tìm kiếm (Search box) cho cả 2 panel.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class ChonDichVuDoAnDialog extends JDialog {

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
    private DefaultTableModel modelDoAn;
    private JTable tableDichVu;
    private JTable tableDoAn;

    private JTextField txtSearchDichVu;
    private JTextField txtSearchDoAn;

    private final Map<Integer, Integer> selectedQtyMapDichVu = new HashMap<>();
    private final Map<Integer, Integer> selectedQtyMapDoAn = new HashMap<>();

    private final List<SelectedItem> selectedServices = new ArrayList<>();
    private final List<SelectedItem> selectedFoodItems = new ArrayList<>();
    private String summaryText = "";
    private double totalAddonCost = 0;
    private boolean confirmed = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDone;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTabDichVu;
    private javax.swing.JPanel pnlTabDoAn;
    private javax.swing.JTabbedPane tabMain;
    // End of variables declaration//GEN-END:variables

    public ChonDichVuDoAnDialog() {
        this(null);
    }

    public ChonDichVuDoAnDialog(JFrame parent) {
        super(parent, "Thêm Dịch vụ & Đồ ăn", true);
        initComponents();
        customInit(parent);
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        pnlCenter = new javax.swing.JPanel();
        tabMain = new javax.swing.JTabbedPane();
        pnlTabDichVu = new javax.swing.JPanel();
        pnlTabDoAn = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnDone = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thêm Dịch vụ & Đồ ăn");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(java.awt.Color.WHITE);
        lblTitle.setText("[+] Thêm Dịch vụ & Đồ ăn kèm theo");
        pnlHeader.add(lblTitle, java.awt.BorderLayout.WEST);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        pnlCenter.setLayout(new java.awt.BorderLayout());

        pnlTabDichVu.setLayout(new java.awt.BorderLayout());
        tabMain.addTab("1. Chọn Dịch vụ", pnlTabDichVu);

        pnlTabDoAn.setLayout(new java.awt.BorderLayout());
        tabMain.addTab("2. Chọn Đồ ăn & Vật tư kho", pnlTabDoAn);

        pnlCenter.add(tabMain, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlCenter, java.awt.BorderLayout.CENTER);

        pnlFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 16, 16, 16));
        pnlFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 0));

        btnCancel.setText("Hủy");
        pnlFooter.add(btnCancel);

        btnDone.setText("Hoàn tất");
        pnlFooter.add(btnDone);

        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit(JFrame parent) {
        setSize(720, 540);
        if (parent != null) setLocationRelativeTo(parent);

        buildTabDichVu();
        buildTabDoAn();

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        btnDone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnDone.setBackground(UIConstants.PRIMARY);
        btnDone.setForeground(Color.WHITE);
        btnDone.addActionListener(e -> onDone());

        getRootPane().setDefaultButton(btnDone);
    }

    private void buildTabDichVu() {
        pnlTabDichVu.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        modelDichVu = new DefaultTableModel(
                new String[]{"ID", "Tên dịch vụ", "Mô tả", "Đơn giá", "Đơn vị", "Số lượng chọn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 5; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return Integer.class;
                return String.class;
            }
        };
        tableDichVu = new JTable(modelDichVu);
        PageUI.styleTable(tableDichVu);
        tableDichVu.getColumnModel().getColumn(0).setMaxWidth(45);
        tableDichVu.getColumnModel().getColumn(5).setPreferredWidth(100);

        modelDichVu.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDichVu.getRowCount()) {
                    int id = (Integer) modelDichVu.getValueAt(r, 0);
                    Object val = modelDichVu.getValueAt(r, 5);
                    int qty = (val instanceof Integer num) ? num : 0;
                    selectedQtyMapDichVu.put(id, Math.max(0, qty));
                }
            }
        });

        // Search Bar Panel 1 (Dịch vụ)
        JPanel pnlTopDV = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlTopDV.add(new JLabel("🔍 Tìm dịch vụ:"));
        txtSearchDichVu = new JTextField(14);
        txtSearchDichVu.setPreferredSize(new Dimension(150, 30));
        txtSearchDichVu.getDocument().addDocumentListener(new SimpleDocListener(this::reloadDichVuTable));
        pnlTopDV.add(txtSearchDichVu);

        JButton btnAdd = new JButton("+1 Đơn vị");
        btnAdd.addActionListener(e -> changeQty(tableDichVu, modelDichVu, selectedQtyMapDichVu, 1));
        JButton btnSub = new JButton("−1 Đơn vị");
        btnSub.addActionListener(e -> changeQty(tableDichVu, modelDichVu, selectedQtyMapDichVu, -1));
        pnlTopDV.add(btnAdd);
        pnlTopDV.add(btnSub);

        pnlTabDichVu.add(pnlTopDV, BorderLayout.NORTH);
        pnlTabDichVu.add(new JScrollPane(tableDichVu), BorderLayout.CENTER);

        reloadDichVuTable();
    }

    private void buildTabDoAn() {
        pnlTabDoAn.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

        modelDoAn = new DefaultTableModel(
                new String[]{"ID", "Tên đồ ăn/hàng kho", "Mô tả", "Đơn giá", "Đơn vị", "Tồn kho", "Số lượng chọn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 6; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) return Integer.class;
                return String.class;
            }
        };
        tableDoAn = new JTable(modelDoAn);
        PageUI.styleTable(tableDoAn);
        tableDoAn.getColumnModel().getColumn(0).setMaxWidth(45);
        tableDoAn.getColumnModel().getColumn(6).setPreferredWidth(100);

        modelDoAn.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 6) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDoAn.getRowCount()) {
                    int id = (Integer) modelDoAn.getValueAt(r, 0);
                    Object val = modelDoAn.getValueAt(r, 6);
                    int qty = (val instanceof Integer num) ? num : 0;
                    selectedQtyMapDoAn.put(id, Math.max(0, qty));
                }
            }
        });

        // Search Bar Panel 2 (Đồ ăn / Vật tư)
        JPanel pnlTopDoAn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlTopDoAn.add(new JLabel("🔍 Tìm đồ ăn / vật tư:"));
        txtSearchDoAn = new JTextField(14);
        txtSearchDoAn.setPreferredSize(new Dimension(150, 30));
        txtSearchDoAn.getDocument().addDocumentListener(new SimpleDocListener(this::reloadDoAnTable));
        pnlTopDoAn.add(txtSearchDoAn);

        JButton btnAdd = new JButton("+1 Số lượng");
        btnAdd.addActionListener(e -> changeQty(tableDoAn, modelDoAn, selectedQtyMapDoAn, 1));
        JButton btnSub = new JButton("−1 Số lượng");
        btnSub.addActionListener(e -> changeQty(tableDoAn, modelDoAn, selectedQtyMapDoAn, -1));
        pnlTopDoAn.add(btnAdd);
        pnlTopDoAn.add(btnSub);

        pnlTabDoAn.add(pnlTopDoAn, BorderLayout.NORTH);
        pnlTabDoAn.add(new JScrollPane(tableDoAn), BorderLayout.CENTER);

        reloadDoAnTable();
    }

    private void reloadDichVuTable() {
        String kw = txtSearchDichVu.getText().trim().toLowerCase();
        modelDichVu.setRowCount(0);
        List<DichVu> list = DataStore.get().getDichVus().stream().filter(x -> "DangBan".equals(x.getTrangThai())).toList();
        for (DichVu d : list) {
            if (!kw.isEmpty()
                    && !d.getTenDichVu().toLowerCase().contains(kw)
                    && !(d.getMoTa() != null && d.getMoTa().toLowerCase().contains(kw))) {
                continue;
            }
            int currentQty = selectedQtyMapDichVu.getOrDefault(d.getId(), 0);
            modelDichVu.addRow(new Object[]{
                    d.getId(), d.getTenDichVu(), d.getMoTa(),
                    String.format("%,.0f VNĐ", (double) d.getDonGia()), d.getDonVi(), currentQty
            });
        }
    }

    private void reloadDoAnTable() {
        String kw = txtSearchDoAn.getText().trim().toLowerCase();
        modelDoAn.setRowCount(0);
        List<DichVu> list = DataStore.get().getKhoItems().stream().filter(x -> "DangBan".equals(x.getTrangThai())).toList();
        for (DichVu d : list) {
            if (!kw.isEmpty()
                    && !d.getTenDichVu().toLowerCase().contains(kw)
                    && !(d.getMoTa() != null && d.getMoTa().toLowerCase().contains(kw))) {
                continue;
            }
            int currentQty = selectedQtyMapDoAn.getOrDefault(d.getId(), 0);
            modelDoAn.addRow(new Object[]{
                    d.getId(), d.getTenDichVu(), d.getMoTa(),
                    String.format("%,.0f VNĐ", (double) d.getDonGia()), d.getDonVi(), d.getSoLuongTon(), currentQty
            });
        }
    }

    private void changeQty(JTable table, DefaultTableModel model, Map<Integer, Integer> map, int delta) {
        int r = table.getSelectedRow();
        if (r < 0) return;
        int id = (Integer) model.getValueAt(r, 0);
        int qtyCol = model.getColumnCount() - 1;
        Object val = model.getValueAt(r, qtyCol);
        int current = (val instanceof Integer i) ? i : 0;
        int next = Math.max(0, current + delta);

        if (table == tableDoAn) {
            int ton = (Integer) model.getValueAt(r, 5);
            if (next > ton) {
                JOptionPane.showMessageDialog(this, "Số lượng chọn không vượt quá tồn kho (" + ton + ").",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        model.setValueAt(next, r, qtyCol);
        map.put(id, next);
    }

    private void onDone() {
        if (tableDichVu.isEditing()) tableDichVu.getCellEditor().stopCellEditing();
        if (tableDoAn.isEditing()) tableDoAn.getCellEditor().stopCellEditing();

        selectedServices.clear();
        selectedFoodItems.clear();
        totalAddonCost = 0;

        StringBuilder sb = new StringBuilder("📋 TỔNG HỢP DỊCH VỤ & ĐỒ ĂN CẤU HÌNH:\n");

        // 1. Collect Services
        boolean hasService = false;
        List<DichVu> services = DataStore.get().getDichVus();
        for (Map.Entry<Integer, Integer> entry : selectedQtyMapDichVu.entrySet()) {
            int id = entry.getKey();
            int qty = entry.getValue();
            if (qty > 0) {
                DichVu dv = services.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                if (dv != null) {
                    SelectedItem item = new SelectedItem(dv, qty);
                    selectedServices.add(item);
                    if (!hasService) {
                        sb.append("DỊCH VỤ TRẢI NGHIỆM:\n");
                        hasService = true;
                    }
                    sb.append(String.format("  • %s x%d = %,.0f VNĐ\n", dv.getTenDichVu(), qty, item.getThanhTien()));
                    totalAddonCost += item.getThanhTien();
                }
            }
        }

        // 2. Collect Food & Items
        boolean hasFood = false;
        List<DichVu> items = DataStore.get().getKhoItems();
        for (Map.Entry<Integer, Integer> entry : selectedQtyMapDoAn.entrySet()) {
            int id = entry.getKey();
            int qty = entry.getValue();
            if (qty > 0) {
                DichVu dv = items.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                if (dv != null) {
                    SelectedItem item = new SelectedItem(dv, qty);
                    selectedFoodItems.add(item);
                    if (!hasFood) {
                        sb.append("🍱 ĐỒ ĂN & VẬT TƯ KHO:\n");
                        hasFood = true;
                    }
                    sb.append(String.format("  • %s x%d = %,.0f VNĐ\n", dv.getTenDichVu(), qty, item.getThanhTien()));
                    totalAddonCost += item.getThanhTien();
                }
            }
        }

        if (!hasService && !hasFood) {
            summaryText = "Chưa chọn dịch vụ / đồ ăn nào.";
        } else {
            sb.append("------------------------------------\n");
            sb.append(String.format("➔ TỔNG TIỀN ĐỒ KÈM & DV: %,.0f VNĐ", totalAddonCost));
            summaryText = sb.toString();
        }

        confirmed = true;
        dispose();
    }

    public void setInitialQuantities(Map<Integer, Integer> dvMap, Map<Integer, Integer> doAnMap) {
        if (dvMap != null) {
            selectedQtyMapDichVu.putAll(dvMap);
        }
        if (doAnMap != null) {
            selectedQtyMapDoAn.putAll(doAnMap);
        }
        reloadDichVuTable();
        reloadDoAnTable();
    }

    public boolean isConfirmed() { return confirmed; }
    public List<SelectedItem> getSelectedServices() { return selectedServices; }
    public List<SelectedItem> getSelectedFoodItems() { return selectedFoodItems; }
    public Map<Integer, Integer> getSelectedQtyMapDichVu() { return selectedQtyMapDichVu; }
    public Map<Integer, Integer> getSelectedQtyMapDoAn() { return selectedQtyMapDoAn; }
    public String getSummaryText() { return summaryText; }
    public double getTotalAddonCost() { return totalAddonCost; }
}
