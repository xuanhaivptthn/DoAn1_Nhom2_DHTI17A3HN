package GiaoDien.Dialogs;

import Model.DatLich;
import Model.DichVu;
import Utils.DataStore;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Hộp thoại xác nhận và tích lại dịch vụ / đồ ăn theo Ghi chú
 * khi chuyển trạng thái lịch đặt sang "Hoàn thành (Đã thanh toán)".
 */
public class XacNhanDichVuThanhToanDialog extends JDialog {

    private final DatLich booking;
    private boolean confirmed = false;

    private final Map<Integer, Integer> checkedDvMap = new HashMap<>();
    private final Map<Integer, Integer> checkedDoAnMap = new HashMap<>();

    private DefaultTableModel modelDv;
    private DefaultTableModel modelDoAn;
    private JTable tableDv;
    private JTable tableDoAn;

    private JLabel lblTienSan;
    private JLabel lblTienDichVu;
    private JLabel lblTongTien;

    private double totalSvcCost = 0;

    public XacNhanDichVuThanhToanDialog(JFrame parent, DatLich booking) {
        super(parent, "Xác nhận & Tích lại dịch vụ thanh toán - " + (booking != null ? booking.getMaLichDat() : ""), true);
        this.booking = booking;

        if (booking != null) {
            if (booking.getSelectedDvMap() != null) checkedDvMap.putAll(booking.getSelectedDvMap());
            if (booking.getSelectedDoAnMap() != null) checkedDoAnMap.putAll(booking.getSelectedDoAnMap());
        }

        initUI(parent);
    }

    private void initUI(JFrame parent) {
        setSize(760, 680);
        if (parent != null) setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel pnlHeader = PageUI.createPageHeader(
                " TÍCH CHỌN DỊCH VỤ THEO GHI CHÚ KHI THANH TOÁN",
                "Phiếu đặt: " + (booking != null ? booking.getMaLichDat() : "") + " | Khách: " + (booking != null ? booking.getTenKhach() : "")
        );
        pnlHeader.setBackground(UIConstants.PRIMARY);
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // Center Panel
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Top Info & Note Card
        JPanel pnlInfoNoteCard = new JPanel(new BorderLayout(8, 8));
        pnlInfoNoteCard.setBackground(Color.WHITE);
        pnlInfoNoteCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        String noteText = (booking != null && booking.getGhiChu() != null && !booking.getGhiChu().isBlank())
                ? booking.getGhiChu() : "(Không có ghi chú thêm)";

        JLabel lblNoteHeader = new JLabel("📌 Ghi chú lần đặt sân:");
        lblNoteHeader.setFont(UIConstants.FONT_BOLD);
        lblNoteHeader.setForeground(UIConstants.PRIMARY);

        JTextArea txtNoteDisplay = new JTextArea(noteText);
        txtNoteDisplay.setFont(UIConstants.FONT_NORMAL);
        txtNoteDisplay.setEditable(false);
        txtNoteDisplay.setLineWrap(true);
        txtNoteDisplay.setWrapStyleWord(true);
        txtNoteDisplay.setBackground(new Color(248, 249, 250));
        txtNoteDisplay.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        pnlInfoNoteCard.add(lblNoteHeader, BorderLayout.NORTH);
        pnlInfoNoteCard.add(txtNoteDisplay, BorderLayout.CENTER);
        pnlCenter.add(pnlInfoNoteCard, BorderLayout.NORTH);

        // Tabs cho Dịch vụ & Đồ ăn kho
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(UIConstants.FONT_BOLD);

        // Tab 1: Dịch vụ sân bóng
        JPanel pnlDvTab = new JPanel(new BorderLayout());
        modelDv = new DefaultTableModel(new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá", "Tích chọn (Thanh toán)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 3; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Boolean.class : String.class;
            }
        };
        tableDv = new JTable(modelDv);
        PageUI.styleTable(tableDv);
        tableDv.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableDv.getColumnModel().getColumn(1).setPreferredWidth(280);
        tableDv.getColumnModel().getColumn(2).setPreferredWidth(140);
        tableDv.getColumnModel().getColumn(3).setPreferredWidth(120);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        tableDv.getColumnModel().getColumn(0).setCellRenderer(centerRender);

        modelDv.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDv.getRowCount()) {
                    String maStr = modelDv.getValueAt(r, 0).toString();
                    DichVu item = DataStore.get().getDichVus().stream()
                            .filter(d -> maStr.equalsIgnoreCase(d.getMaDichVu()))
                            .findFirst().orElse(null);
                    if (item != null) {
                        boolean isChecked = Boolean.TRUE.equals(modelDv.getValueAt(r, 3));
                        checkedDvMap.put(item.getId(), isChecked ? 1 : 0);
                        recalcTotal();
                    }
                }
            }
        });
        pnlDvTab.add(new JScrollPane(tableDv), BorderLayout.CENTER);
        tabPane.addTab("1. Dịch vụ riêng (Trọng tài, HLV, Quay phim...)", pnlDvTab);

        // Tab 2: Đồ ăn / Vật tư kho
        JPanel pnlDoAnTab = new JPanel(new BorderLayout());
        modelDoAn = new DefaultTableModel(new String[]{"Mã SP", "Tên sản phẩm / đồ ăn", "Đơn giá", "Tồn kho", "Số lượng dùng (Thanh toán)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Integer.class : String.class;
            }
        };
        tableDoAn = new JTable(modelDoAn);
        PageUI.styleTable(tableDoAn);
        tableDoAn.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableDoAn.getColumnModel().getColumn(1).setPreferredWidth(260);
        tableDoAn.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableDoAn.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableDoAn.getColumnModel().getColumn(4).setPreferredWidth(140);
        tableDoAn.getColumnModel().getColumn(0).setCellRenderer(centerRender);

        modelDoAn.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDoAn.getRowCount()) {
                    String maStr = modelDoAn.getValueAt(r, 0).toString();
                    DichVu item = DataStore.get().getKhoItems().stream()
                            .filter(k -> maStr.equalsIgnoreCase(k.getMaDichVu()))
                            .findFirst().orElse(null);
                    if (item != null) {
                        Object val = modelDoAn.getValueAt(r, 4);
                        int qty = (val instanceof Integer num) ? num : 0;
                        if (qty > item.getSoLuongTon()) {
                            JOptionPane.showMessageDialog(this, "Số lượng chọn vượt quá số tồn kho (" + item.getSoLuongTon() + ")!", "Cảnh báo kho", JOptionPane.WARNING_MESSAGE);
                            qty = item.getSoLuongTon();
                            modelDoAn.setValueAt(qty, r, 4);
                        }
                        checkedDoAnMap.put(item.getId(), Math.max(0, qty));
                        recalcTotal();
                    }
                }
            }
        });
        pnlDoAnTab.add(new JScrollPane(tableDoAn), BorderLayout.CENTER);
        tabPane.addTab("2. Đồ ăn / Vật phẩm kho (Nước, Áo bib, Bóng...)", pnlDoAnTab);

        pnlCenter.add(tabPane, BorderLayout.CENTER);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // Footer Summary & Buttons
        JPanel pnlFooter = new JPanel(new BorderLayout(12, 0));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JPanel pnlSummary = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        pnlSummary.setOpaque(false);

        double tienSanVal = booking != null ? booking.getTienSan() : 0;
        lblTienSan = new JLabel(String.format("Tiền sân: %,.0f đ", tienSanVal));
        lblTienSan.setFont(UIConstants.FONT_BOLD);

        lblTienDichVu = new JLabel("Tiền dịch vụ/kho: 0 đ");
        lblTienDichVu.setFont(UIConstants.FONT_BOLD);
        lblTienDichVu.setForeground(UIConstants.PRIMARY);

        lblTongTien = new JLabel("Tổng thanh toán: 0 đ");
        lblTongTien.setFont(UIConstants.FONT_TITLE);
        lblTongTien.setForeground(UIConstants.SUCCESS);

        pnlSummary.add(lblTienSan);
        pnlSummary.add(new JLabel(" | "));
        pnlSummary.add(lblTienDichVu);
        pnlSummary.add(new JLabel(" | "));
        pnlSummary.add(lblTongTien);

        pnlFooter.add(pnlSummary, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(UIConstants.FONT_BUTTON);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnConfirm = new JButton("✔ Xác nhận & Thanh toán");
        btnConfirm.setFont(UIConstants.FONT_BUTTON);
        btnConfirm.setBackground(UIConstants.SUCCESS);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> onConfirm());

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnConfirm);

        pnlFooter.add(pnlButtons, BorderLayout.EAST);
        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        reloadTables();
    }

    private void reloadTables() {
        modelDv.setRowCount(0);
        for (DichVu dv : DataStore.get().getDichVus()) {
            int qty = checkedDvMap.getOrDefault(dv.getId(), 0);
            modelDv.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    String.format("%,.0f đ", dv.getDonGia()),
                    qty > 0
            });
        }

        modelDoAn.setRowCount(0);
        for (DichVu item : DataStore.get().getKhoItems()) {
            int qty = checkedDoAnMap.getOrDefault(item.getId(), 0);
            modelDoAn.addRow(new Object[]{
                    item.getMaDichVu(),
                    item.getTenDichVu(),
                    String.format("%,.0f đ", item.getDonGia()),
                    item.getSoLuongTon(),
                    qty
            });
        }

        recalcTotal();
    }

    private void recalcTotal() {
        totalSvcCost = 0;
        for (DichVu dv : DataStore.get().getDichVus()) {
            int qty = checkedDvMap.getOrDefault(dv.getId(), 0);
            if (qty > 0) {
                totalSvcCost += dv.getDonGia() * qty;
            }
        }
        for (DichVu item : DataStore.get().getKhoItems()) {
            int qty = checkedDoAnMap.getOrDefault(item.getId(), 0);
            if (qty > 0) {
                totalSvcCost += item.getDonGia() * qty;
            }
        }

        double tienSanVal = booking != null ? booking.getTienSan() : 0;
        double totalVal = tienSanVal + totalSvcCost;

        if (lblTienDichVu != null) lblTienDichVu.setText(String.format("Tiền dịch vụ/kho: %,.0f VNĐ", totalSvcCost));
        if (lblTongTien != null) lblTongTien.setText(String.format("Tổng thanh toán: %,.0f VNĐ", totalVal));
    }

    private void onConfirm() {
        if (booking != null) {
            booking.setSelectedDvMap(checkedDvMap);
            booking.setSelectedDoAnMap(checkedDoAnMap);

            StringBuilder newDichVuKem = new StringBuilder();
            double newSvcTotal = 0;

            for (DichVu dv : DataStore.get().getDichVus()) {
                int qty = checkedDvMap.getOrDefault(dv.getId(), 0);
                if (qty > 0) {
                    double c = dv.getDonGia() * qty;
                    newSvcTotal += c;
                    if (newDichVuKem.length() > 0) newDichVuKem.append("\n");
                    newDichVuKem.append(String.format("%s (x%d): %,.0f VNĐ", dv.getTenDichVu(), qty, c));
                }
            }

            for (DichVu item : DataStore.get().getKhoItems()) {
                int qty = checkedDoAnMap.getOrDefault(item.getId(), 0);
                if (qty > 0) {
                    double c = item.getDonGia() * qty;
                    newSvcTotal += c;
                    if (newDichVuKem.length() > 0) newDichVuKem.append("\n");
                    newDichVuKem.append(String.format("%s (x%d): %,.0f VNĐ", item.getTenDichVu(), qty, c));
                }
            }

            booking.setDichVuKem(newDichVuKem.toString());
            booking.setTienDichVu(newSvcTotal);
            booking.recalc();
        }

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
