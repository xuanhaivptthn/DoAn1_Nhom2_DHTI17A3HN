package GiaoDien.Dialogs;

import Model.DatLich;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dialog chọn phiếu đặt sân để bán dịch vụ đi kèm.
 * Hiển thị bảng thông tin đơn đặt rõ ràng, hỗ trợ tìm kiếm nhanh.
 */
public class ChonDonDialog extends JDialog {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtSearch;
    private JLabel lblInfo;

    private final List<DatLich> datLichList;
    private DatLich selectedDon;
    private boolean confirmed = false;

    public ChonDonDialog(JFrame parent, List<DatLich> datLichList) {
        super(parent, "Chọn phiếu đặt sân để bán dịch vụ", true);
        this.datLichList = datLichList;
        initComponents(parent);
        reloadTable("");
    }

    private void initComponents(JFrame parent) {
        setSize(820, 520);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if (parent != null) setLocationRelativeTo(parent);
        getContentPane().setBackground(UIConstants.BG);
        getContentPane().setLayout(new BorderLayout());

        // ── HEADER ──────────────────────────────────────────────────────────
        JPanel pnlHeader = PageUI.createPageHeader(
                "Chọn phiếu đặt sân",
                "Chọn phiếu đặt sân cần bán thêm dịch vụ / đồ ăn đi kèm"
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // ── BODY ─────────────────────────────────────────────────────────────
        JPanel pnlBody = new JPanel(new BorderLayout(0, 10));
        pnlBody.setOpaque(false);
        pnlBody.setBorder(new EmptyBorder(14, 16, 8, 16));

        // Search bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearchIcon = new JLabel("Tìm nhanh:");
        lblSearchIcon.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearchIcon.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearchIcon);

        txtSearch = new JTextField(22);
        txtSearch.setPreferredSize(new Dimension(240, 32));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.putClientProperty("JTextField.placeholderText", "Mã đơn, Tên khách, SĐT, Sân...");
        txtSearch.getDocument().addDocumentListener(new Utils.SimpleDocListener(() ->
                reloadTable(txtSearch.getText().trim().toLowerCase())
        ));
        pnlSearch.add(txtSearch);

        // Info label count
        lblInfo = new JLabel();
        lblInfo.setFont(UIConstants.FONT_SMALL);
        lblInfo.setForeground(UIConstants.TEXT_SECONDARY);
        pnlSearch.add(Box.createHorizontalStrut(16));
        pnlSearch.add(lblInfo);

        pnlBody.add(pnlSearch, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new String[]{"Mã phiếu", "Sân bóng", "Khách hàng", "SĐT", "Ngày đặt", "Khung giờ", "Tổng tiền", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        PageUI.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(36);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(110);

        // Status column custom renderer
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

        // Double click to confirm
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) onConfirm();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        pnlBody.add(scroll, BorderLayout.CENTER);

        // Info hint panel
        JPanel pnlHint = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        pnlHint.setOpaque(false);
        JLabel lblHint = new JLabel("💡 Nhấp đúp vào đơn hoặc chọn rồi nhấn \"Chọn phiếu này\"");
        lblHint.setFont(UIConstants.FONT_SMALL);
        lblHint.setForeground(UIConstants.TEXT_SECONDARY);
        pnlHint.add(lblHint);
        pnlBody.add(pnlHint, BorderLayout.SOUTH);

        getContentPane().add(pnlBody, BorderLayout.CENTER);

        // ── FOOTER ───────────────────────────────────────────────────────────
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton btnCancel = new JButton("Hủy bỏ");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSelect = new JButton(" Chọn phiếu này");
        btnSelect.setIcon(Utils.IconUtils.getCheckIcon(16));
        Utils.PageUI.stylePrimaryButton(btnSelect);
        btnSelect.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSelect);
        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnSelect);
    }

    private void reloadTable(String keyword) {
        tableModel.setRowCount(0);
        int count = 0;
        for (DatLich d : datLichList) {
            boolean match = keyword.isEmpty()
                    || (d.getMaLichDat() != null && d.getMaLichDat().toLowerCase().contains(keyword))
                    || (d.getTenKhach() != null && d.getTenKhach().toLowerCase().contains(keyword))
                    || (d.getSoDienThoaiKhach() != null && d.getSoDienThoaiKhach().contains(keyword))
                    || (d.getTenSan() != null && d.getTenSan().toLowerCase().contains(keyword))
                    || (d.getNgayDat() != null && d.getNgayDat().contains(keyword));
            if (match) {
                tableModel.addRow(new Object[]{
                        d.getMaLichDat(),
                        d.getTenSan() != null ? d.getTenSan() : "-",
                        d.getTenKhach() != null ? d.getTenKhach() : "-",
                        d.getSoDienThoaiKhach() != null ? d.getSoDienThoaiKhach() : "-",
                        formatDate(d.getNgayDat()),
                        d.getKhungGio(),
                        String.format("%,.0f VNĐ", d.getTongTien()),
                        d.getTrangThaiHienThi()
                });
                count++;
            }
        }
        lblInfo.setText("Hiển thị " + count + " / " + datLichList.size() + " phiếu");
    }

    private String formatDate(String raw) {
        if (raw == null || raw.isBlank()) return "-";
        // yyyy-MM-dd → dd/MM/yyyy
        try {
            String[] parts = raw.trim().split("-");
            if (parts.length == 3) return parts[2] + "/" + parts[1] + "/" + parts[0];
        } catch (Exception ignored) {}
        return raw;
    }

    private void onConfirm() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một phiếu đặt sân từ danh sách!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ma = tableModel.getValueAt(row, 0).toString();
        selectedDon = datLichList.stream()
                .filter(d -> ma.equals(d.getMaLichDat()))
                .findFirst().orElse(null);
        if (selectedDon == null) return;
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public DatLich getSelectedDon() { return selectedDon; }

    /** Custom renderer cho cột Trạng thái */
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                        boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            if (!isSelected && value != null) {
                String s = value.toString();
                if (s.contains("Hoàn thành") || s.contains("Đã thanh toán")) {
                    setForeground(UIConstants.SUCCESS);
                    setFont(UIConstants.FONT_BOLD);
                } else if (s.contains("Xác nhận") || s.contains("Đang")) {
                    setForeground(UIConstants.INFO);
                    setFont(UIConstants.FONT_BOLD);
                } else if (s.contains("Chờ")) {
                    setForeground(UIConstants.WARNING);
                    setFont(UIConstants.FONT_BOLD);
                } else if (s.contains("Hủy")) {
                    setForeground(UIConstants.DANGER);
                    setFont(UIConstants.FONT_BOLD);
                } else {
                    setForeground(UIConstants.TEXT_PRIMARY);
                    setFont(UIConstants.FONT_NORMAL);
                }
            }
            return c;
        }
    }
}
