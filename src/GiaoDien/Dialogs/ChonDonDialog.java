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
 * <p>
 * Hiển thị danh sách các đơn đặt sân bóng dưới dạng bảng rõ ràng,
 * hỗ trợ tìm kiếm nhanh theo mã đơn, tên khách hàng, số điện thoại hoặc sân bóng.
 * Cho phép nhấp đúp chuột hoặc bấm nút để lựa chọn phiếu đặt.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChonDonDialog extends JDialog {

    /** Model quản lý dữ liệu của bảng phiếu đặt sân */
    private DefaultTableModel tableModel;

    /** Bảng hiển thị danh sách phiếu đặt sân */
    private JTable table;

    /** Ô nhập liệu từ khóa tìm kiếm nhanh */
    private JTextField txtSearch;

    /** Nhãn hiển thị số lượng bản ghi đơn đặt sân hiện tại */
    private JLabel lblInfo;

    /** Danh sách toàn bộ phiếu đặt sân nguồn */
    private final List<DatLich> datLichList;

    /** Đơn đặt sân được lựa chọn */
    private DatLich selectedDon;

    /** Cờ xác nhận người dùng đã chọn đơn thành công */
    private boolean confirmed = false;

    /**
     * Khởi tạo thoại chọn phiếu đặt sân.
     *
     * @param parent      Cửa sổ cha (JFrame)
     * @param datLichList Danh sách các phiếu đặt sân khả dụng
     */
    public ChonDonDialog(JFrame parent, List<DatLich> datLichList) {
        super(parent, "Chọn phiếu đặt sân để bán dịch vụ", true);
        this.datLichList = datLichList;
        initComponents(parent);
        reloadTable("");
    }

    /**
     * Khởi tạo giao diện dialog, cấu hình bảng, thanh tìm kiếm và các nút chức năng.
     *
     * @param parent Cửa sổ cha
     */
    private void initComponents(JFrame parent) {
        setSize(820, 520);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if (parent != null) setLocationRelativeTo(parent);
        getContentPane().setBackground(UIConstants.BG);
        getContentPane().setLayout(new BorderLayout());

        // ── 1. HEADER ──────────────────────────────────────────────────────────
        JPanel pnlHeader = PageUI.createPageHeader(
                "Chọn phiếu đặt sân",
                "Chọn phiếu đặt sân cần bán thêm dịch vụ / đồ ăn đi kèm"
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // ── 2. BODY ─────────────────────────────────────────────────────────────
        JPanel pnlBody = new JPanel(new BorderLayout(0, 10));
        pnlBody.setOpaque(false);
        pnlBody.setBorder(new EmptyBorder(14, 16, 8, 16));

        // Thanh tìm kiếm nhanh
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

        // Nhãn số lượng đơn hiển thị
        lblInfo = new JLabel();
        lblInfo.setFont(UIConstants.FONT_SMALL);
        lblInfo.setForeground(UIConstants.TEXT_SECONDARY);
        pnlSearch.add(Box.createHorizontalStrut(16));
        pnlSearch.add(lblInfo);

        pnlBody.add(pnlSearch, BorderLayout.NORTH);

        // Cấu hình Bảng hiển thị đơn đặt sân
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

        // Đăng ký renderer tùy chỉnh màu sắc cho cột Trạng thái
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

        // Bắt sự kiện nhấp đúp chuột để chọn đơn
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

        // Nhãn hướng dẫn thao tác
        JPanel pnlHint = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        pnlHint.setOpaque(false);
        JLabel lblHint = new JLabel("💡 Nhấp đúp vào đơn hoặc chọn rồi nhấn \"Chọn phiếu này\"");
        lblHint.setFont(UIConstants.FONT_SMALL);
        lblHint.setForeground(UIConstants.TEXT_SECONDARY);
        pnlHint.add(lblHint);
        pnlBody.add(pnlHint, BorderLayout.SOUTH);

        getContentPane().add(pnlBody, BorderLayout.CENTER);

        // ── 3. FOOTER ───────────────────────────────────────────────────────────
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

    /**
     * Tải lại danh sách đơn trên bảng dựa theo từ khóa lọc tìm kiếm.
     *
     * @param keyword Từ khóa tìm kiếm nhập từ textfield
     */
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

    /**
     * Chuyển đổi định dạng ngày yyyy-MM-dd sang dd/MM/yyyy để hiển thị giao diện.
     *
     * @param raw Chuỗi ngày gốc yyyy-MM-dd
     * @return Chuỗi ngày dạng dd/MM/yyyy
     */
    private String formatDate(String raw) {
        if (raw == null || raw.isBlank()) return "-";
        try {
            String[] parts = raw.trim().split("-");
            if (parts.length == 3) return parts[2] + "/" + parts[1] + "/" + parts[0];
        } catch (Exception ignored) {}
        return raw;
    }

    /**
     * Xử lý xác nhận chọn dòng đang Highlight trên bảng.
     */
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

    /**
     * Kiểm tra người dùng đã xác nhận thành công hay chưa.
     *
     * @return true nếu đã chọn đơn
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy đối tượng phiếu đặt sân DatLich được chọn.
     *
     * @return Đơn DatLich được chọn
     */
    public DatLich getSelectedDon() { return selectedDon; }

    /**
     * Bộ tô màu và định dạng dữ liệu (Renderer) tùy chỉnh cho cột Trạng thái trên bảng.
     */
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
