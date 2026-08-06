package GiaoDien.Panels;

import Model.BaoTri;
import Model.DatLich;
import Model.DichVu;
import Model.KhuVucSan;
import Utils.DataStore;
import Utils.SessionManager;
import GiaoDien.Dialogs.BanDichVuDialog;
import GiaoDien.Dialogs.BaoTriFormDialog;
import GiaoDien.Dialogs.ChonDonDialog;
import GiaoDien.Dialogs.ChonDichVuDialog;
import GiaoDien.Dialogs.DatLichFormDialog;
import GiaoDien.Dialogs.KiemTraSanDialog;
import Utils.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Panel Dashboard Tổng quan — Giao diện màn hình chính cung cấp góc nhìn tổng thể hoạt động sân bóng.
 * <p>
 * Bố cục tổng quan bao gồm: các thẻ chỉ số KPI (Thẻ thống kê sân bóng, lịch đặt hôm nay, doanh thu đã thu),
 * Ma trận khung giờ hoạt động trong ngày (Timeline matrix), Bảng danh sách lịch đặt trong ngày và thanh thao tác nhanh.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class DashboardPanel extends JPanel {

    /**
     * Hàm gọi lại (Callback Consumer) dùng để kích hoạt chuyển đổi trang trên cửa sổ chính MainFrame.
     */
    private Consumer<String> pageNavigator;

    /**
     * Nhãn hiển thị giá trị số lượng sân (sẵn sàng / đang thuê) trên thẻ KPI.
     */
    private JLabel lblSanValue;

    /**
     * Nhãn hiển thị giá trị số lượng lịch đặt hôm nay (chờ duyệt / đã duyệt) trên thẻ KPI.
     */
    private JLabel lblDatLichValue;

    /**
     * Nhãn hiển thị giá trị tổng doanh thu đã thu trong ngày trên thẻ KPI.
     */
    private JLabel lblDoanhThuValue;

    /**
     * Model dữ liệu bảng danh sách các lịch đặt trong ngày hôm nay.
     */
    private DefaultTableModel todayUpcomingBookingsModel;

    /**
     * Model dữ liệu bảng ma trận khung giờ hoạt động hôm nay.
     */
    private DefaultTableModel todayCalendarModel;

    /**
     * Bảng hiển thị ma trận khung giờ hoạt động (Timeline Schedule Grid).
     */
    private JTable todayCalendarTable;

    /**
     * Danh sách cố định các mốc khung giờ phục vụ trong ngày (từ 06:00 đến 23:00).
     */
    private static final String[] TIME_SLOTS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    // Keep unused panel fields to not break GEN-BEGIN/END block
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Panel chứa thân nội dung chính */
    private javax.swing.JPanel pnlBody;
    /** Panel bao bọc phần tiêu đề header */
    private javax.swing.JPanel pnlHeaderWrap;
    /** Panel chứa các nút bấm thao tác nhanh */
    private javax.swing.JPanel pnlQuickActionsCard;
    /** Panel phân chia nội dung bố cục */
    private javax.swing.JPanel pnlSplit;
    /** Panel lưới chứa các thẻ thống kê KPI */
    private javax.swing.JPanel pnlStatsGrid;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo DashboardPanel không truyền tham số điều hướng.
     */
    public DashboardPanel() { this(null); }

    /**
     * Khởi tạo DashboardPanel với trình điều hướng trang.
     * 
     * @param pageNavigator Consumer tiếp nhận key tên trang để điều hướng trên MainFrame
     */
    public DashboardPanel(Consumer<String> pageNavigator) {
        this.pageNavigator = pageNavigator;
        buildUI();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlHeaderWrap     = new javax.swing.JPanel();
        pnlBody           = new javax.swing.JPanel();
        pnlStatsGrid      = new javax.swing.JPanel();
        pnlQuickActionsCard = new javax.swing.JPanel();
        pnlSplit          = new javax.swing.JPanel();
    }// </editor-fold>//GEN-END:initComponents

    // ─── BUILD FULL UI ────────────────────────────────────────────────────────

    /**
     * Xây dựng toàn bộ bố cục và linh kiện UI cho Dashboard Panel.
     */
    private void buildUI() {
        setBackground(UIConstants.BG);
        setLayout(new BorderLayout());

        // Tiêu đề trang cố định ở phía bắc (NORTH)
        add(buildHeader(), BorderLayout.NORTH);

        // Thân panel chứa dữ liệu chính
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(UIConstants.BG);
        body.setBorder(new EmptyBorder(14, 18, 14, 18));

        // PHẦN TRÊN: Dòng thẻ KPI thống kê tổng quan
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(buildStatsRow(), BorderLayout.CENTER);
        top.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        body.add(top, BorderLayout.NORTH);

        // PHẦN GIỮA: Chứa ma trận khung giờ và bảng danh sách lịch đặt được xếp chồng 2 hàng
        JPanel center = new JPanel(new GridLayout(2, 1, 0, 12));
        center.setOpaque(false);
        center.add(buildCalendarCard());
        center.add(buildBookingsCard());
        body.add(center, BorderLayout.CENTER);

        // PHẦN DƯỚI: Thanh nút bấm thao tác nhanh
        JPanel south = new JPanel(new BorderLayout(0, 0));
        south.setOpaque(false);
        south.setBorder(new EmptyBorder(12, 0, 0, 0));
        south.add(buildActionsCard(), BorderLayout.CENTER);
        body.add(south, BorderLayout.SOUTH);

        add(body, BorderLayout.CENTER);

        // Nạp và đồng bộ dữ liệu ban đầu
        refresh();
    }

    // ─── HEADER ──────────────────────────────────────────────────────────────

    /**
     * Xây dựng panel tiêu đề header phía trên của Dashboard.
     * 
     * @return JPanel chứa tiêu đề và ngày tháng hiện tại
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 3));
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("Dashboard Tổng quan");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setIcon(Utils.IconUtils.getBallWhiteIcon(24));
        lblTitle.setIconTextGap(10);

        // Hiển thị ngày tháng hiện tại định dạng tiếng Việt
        JLabel lblSub = new JLabel("Hôm nay: " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", java.util.Locale.of("vi"))));
        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(new Color(200, 230, 201));

        header.add(lblTitle, BorderLayout.NORTH);
        header.add(lblSub, BorderLayout.SOUTH);
        return header;
    }

    // ─── STAT CARDS ──────────────────────────────────────────────────────────

    /**
     * Xây dựng hàng chứa 3 thẻ thống kê chỉ số KPI.
     * 
     * @return JPanel chứa 3 thẻ stat card
     */
    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setOpaque(false);

        row.add(buildStatCard("Sân bóng", "—", Utils.IconUtils.getBallBlackIcon(18), UIConstants.PRIMARY));
        row.add(buildStatCard("Lịch đặt hôm nay", "—", Utils.IconUtils.getCalendarIcon(18), UIConstants.INFO));
        row.add(buildStatCard("Doanh thu đã thu", "—", Utils.IconUtils.getMoneyIcon(18), UIConstants.SUCCESS));

        return row;
    }

    /**
     * Tạo một card giao diện hiển thị chỉ số thống kê đơn lẻ.
     * 
     * @param label  Tên chỉ số thống kê
     * @param value  Giá trị ban đầu
     * @param icon   Biểu tượng đại diện
     * @param accent Màu viền nhấn của card
     * @return JPanel tượng trưng cho 1 thẻ KPI
     */
    private JPanel buildStatCard(String label, String value, Icon icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 2));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                        new EmptyBorder(10, 14, 10, 14)
                )
        ));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(UIConstants.FONT_SMALL);
        lblLabel.setForeground(UIConstants.TEXT_SECONDARY);
        if (icon != null) {
            lblLabel.setIcon(icon);
            lblLabel.setIconTextGap(6);
        }

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblValue.setForeground(UIConstants.TEXT_PRIMARY);

        // Lưu biến tham chiếu tới các nhãn giá trị để cập nhật sau khi nạp dữ liệu
        if (label.startsWith("Sân")) lblSanValue = lblValue;
        else if (label.startsWith("Lịch")) lblDatLichValue = lblValue;
        else if (label.startsWith("Doanh")) lblDoanhThuValue = lblValue;

        card.add(lblLabel, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    // ─── CALENDAR MATRIX ─────────────────────────────────────────────────────

    /**
     * Xây dựng card chứa ma trận khung giờ hôm nay và thanh chú thích màu sắc.
     * 
     * @return JPanel chứa bảng ma trận khung giờ
     */
    private JPanel buildCalendarCard() {
        JPanel card = buildCard();

        // Tiêu đề và nút liên kết xem chi tiết
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel title = new JLabel("Ma trận khung giờ hôm nay");
        title.setFont(UIConstants.FONT_SUBTITLE);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JButton btnGo = new JButton("Xem chi tiết →");
        btnGo.setFont(UIConstants.FONT_SMALL);
        btnGo.setFocusPainted(false);
        btnGo.setBorderPainted(false);
        btnGo.setBackground(null);
        btnGo.setForeground(UIConstants.PRIMARY);
        btnGo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (pageNavigator != null) btnGo.addActionListener(e -> pageNavigator.accept("datlich"));

        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(btnGo, BorderLayout.EAST);
        card.add(titleRow, BorderLayout.NORTH);

        // Khởi tạo các cột cho bảng ma trận khung giờ
        List<String> headers = new java.util.ArrayList<>();
        headers.add("Khu vực sân");
        for (String s : TIME_SLOTS) headers.add(s);

        todayCalendarModel = new DefaultTableModel(headers.toArray(new String[0]), 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        todayCalendarTable = new JTable(todayCalendarModel);
        todayCalendarTable.setFont(UIConstants.FONT_NORMAL);
        todayCalendarTable.setRowHeight(30);
        todayCalendarTable.getTableHeader().setFont(UIConstants.FONT_BOLD);
        todayCalendarTable.getTableHeader().setBackground(new Color(245, 247, 250));
        todayCalendarTable.getTableHeader().setForeground(UIConstants.TEXT_SECONDARY);
        todayCalendarTable.setShowGrid(true);
        todayCalendarTable.setGridColor(UIConstants.BORDER);
        todayCalendarTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        todayCalendarTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        for (int i = 1; i <= TIME_SLOTS.length; i++)
            todayCalendarTable.getColumnModel().getColumn(i).setPreferredWidth(66);
        todayCalendarTable.setDefaultRenderer(Object.class, new CalendarCellRenderer());

        // Sự kiện nhấp đúp ô Trống để thực hiện đặt sân nhanh
        todayCalendarTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = todayCalendarTable.rowAtPoint(e.getPoint());
                int c = todayCalendarTable.columnAtPoint(e.getPoint());
                if (r >= 0 && c >= 1 && e.getClickCount() == 2) {
                    Object val = todayCalendarModel.getValueAt(r, c);
                    if (val != null && val.toString().startsWith("Trống")) {
                        onQuickBookSlot(r, c - 1);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(todayCalendarTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        // Thanh chú thích (Legend) đại diện màu sắc trạng thái
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        legend.setOpaque(false);
        legend.setBorder(new EmptyBorder(6, 0, 0, 0));
        legend.add(legendChip("Trống", new Color(220, 252, 231), new Color(22, 163, 74)));
        legend.add(legendChip("Đã đặt", new Color(254, 243, 199), new Color(217, 119, 6)));
        legend.add(legendChip("Đang đá", new Color(219, 234, 254), new Color(37, 99, 235)));
        legend.add(legendChip("Bảo trì", new Color(241, 245, 249), new Color(100, 116, 139)));
        JLabel hint = new JLabel("  · Nhấp đúp ô Trống để đặt nhanh");
        hint.setFont(UIConstants.FONT_SMALL);
        hint.setForeground(UIConstants.TEXT_SECONDARY);
        legend.add(hint);
        card.add(legend, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Tạo chip chú thích trạng thái màu sắc.
     * 
     * @param text Tên trạng thái
     * @param bg   Màu nền
     * @param fg   Màu chữ
     * @return JLabel được vẽ bo góc tùy chỉnh
     */
    private JLabel legendChip(String text, Color bg, Color fg) {
        JLabel lbl = new JLabel("  " + text + "  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(fg);
        lbl.setOpaque(false);
        return lbl;
    }

    // ─── BOOKINGS TABLE ───────────────────────────────────────────────────────

    /**
     * Xây dựng card chứa bảng danh sách lịch đặt sân trong ngày.
     * 
     * @return JPanel chứa JTable danh sách lịch đặt
     */
    private JPanel buildBookingsCard() {
        JPanel card = buildCard();

        JLabel title = new JLabel("Lịch đặt trong ngày");
        title.setFont(UIConstants.FONT_SUBTITLE);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        card.add(title, BorderLayout.NORTH);

        todayUpcomingBookingsModel = new DefaultTableModel(
                new String[]{"Mã", "Sân bóng", "Khách hàng", "SĐT", "Khung giờ", "Tổng tiền", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(todayUpcomingBookingsModel);
        table.setFont(UIConstants.FONT_NORMAL);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(UIConstants.BORDER);
        table.getTableHeader().setFont(UIConstants.FONT_BOLD);
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(UIConstants.TEXT_SECONDARY);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(65);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        applySimpleCellRenderer(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    // ─── QUICK ACTIONS ────────────────────────────────────────────────────────

    /**
     * Xây dựng card chứa danh sách nút bấm thao tác nhanh công việc thường gặp.
     * 
     * @return JPanel chứa các nút bấm hành động
     */
    private JPanel buildActionsCard() {
        JPanel card = buildCard();

        JLabel title = new JLabel("Thao tác nhanh");
        title.setFont(UIConstants.FONT_SUBTITLE);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlBtns.setOpaque(false);

        // Các nút bấm thực thi tác vụ trực tiếp
        pnlBtns.add(actionBtn("Thêm lịch đặt", Utils.IconUtils.getAddIcon(15), UIConstants.PRIMARY, e -> onQuickBook()));
        pnlBtns.add(actionBtn("Bán dịch vụ", Utils.IconUtils.getOpenIcon(15), new Color(14, 122, 200), e -> onQuickSell()));
        pnlBtns.add(actionBtn("Kiểm tra sân", Utils.IconUtils.getCheckIcon(15), UIConstants.SUCCESS, e -> onCheckSan()));
        pnlBtns.add(actionBtn("Tạo bảo trì", Utils.IconUtils.getMaintenanceIcon(15), UIConstants.WARNING, e -> onQuickMaint()));

        // Nút tắt chuyển hướng trang nếu được khởi tạo với navigator
        if (pageNavigator != null) {
            pnlBtns.add(Box.createHorizontalStrut(8));
            pnlBtns.add(plainBtn("→ Đặt lịch", e -> pageNavigator.accept("datlich")));
            // Chỉ chủ sân (admin) mới thấy shortcut vào Quản lý dịch vụ & kho
            if (SessionManager.get().isAdmin()) {
                pnlBtns.add(plainBtn("→ Dịch vụ", e -> pageNavigator.accept("dichvu")));
                pnlBtns.add(plainBtn("→ Kho hàng", e -> pageNavigator.accept("kho")));
            }
        }

        card.add(pnlBtns, BorderLayout.CENTER);
        pnlQuickActionsCard = card; // Lưu tham chiếu
        return card;
    }

    /**
     * Tạo một nút bấm thao tác nhanh chính có màu nền và biểu tượng.
     */
    private JButton actionBtn(String text, Icon icon, Color bg, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setIcon(icon);
        btn.setIconTextGap(6);
        btn.setFont(UIConstants.FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    /**
     * Tạo một nút liên kết phẳng không nền (Text-only link button).
     */
    private JButton plainBtn(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_SMALL);
        btn.setForeground(UIConstants.TEXT_SECONDARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(null);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    // ─── CARD CONTAINER ──────────────────────────────────────────────────────

    /**
     * Hàm tiện ích dựng khung card trắng có viền xám mỏng.
     */
    private JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                new EmptyBorder(12, 16, 12, 16)
        ));
        return card;
    }

    // ─── SIMPLE TABLE CELL RENDERER ──────────────────────────────────────────

    /**
     * Áp dụng cell renderer định dạng màu sắc dòng và căn chỉnh cho bảng lịch đặt hôm nay.
     */
    private void applySimpleCellRenderer(JTable table) {
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setFont(UIConstants.FONT_NORMAL);
                if (!isSelected) {
                    // Tô màu xen kẽ giữa các dòng
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 251, 252));
                    c.setForeground(UIConstants.TEXT_PRIMARY);
                    String s = value != null ? value.toString() : "";
                    if (s.contains("Hoàn thành") || s.contains("Sẵn sàng")) setForeground(UIConstants.SUCCESS);
                    else if (s.contains("Chờ")) setForeground(UIConstants.WARNING);
                    else if (s.contains("Xác nhận") || s.contains("Đang")) setForeground(UIConstants.INFO);
                    else if (s.contains("Hủy")) setForeground(UIConstants.DANGER);
                }
                // Căn chỉnh lề theo từng cột
                if (col == 0 || col == 3 || col == 4) setHorizontalAlignment(CENTER);
                else if (col == 5) setHorizontalAlignment(RIGHT);
                else setHorizontalAlignment(LEFT);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(cr);
    }

    // ─── REFRESH DATA ─────────────────────────────────────────────────────────

    /**
     * Nạp lại toàn bộ dữ liệu thống kê, bảng ma trận khung giờ và danh sách lịch đặt hôm nay từ DataStore.
     */
    public void refresh() {
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        List<DatLich> datLichs = DataStore.get().getDatLichs();
        String todayStr = LocalDate.now().toString();

        // 1. Thống kê dữ liệu thẻ KPI
        long sanReady   = sans.stream().filter(k -> "SanSang".equalsIgnoreCase(k.getTrangThai()) || "HOAT_DONG".equalsIgnoreCase(k.getTrangThai())).count();
        long sanRenting = sans.stream().filter(k -> "DangThue".equalsIgnoreCase(k.getTrangThai()) || "DANG_THUE".equalsIgnoreCase(k.getTrangThai())).count();
        long pending    = datLichs.stream().filter(d -> "ChoXacNhan".equals(d.getTrangThai())).count();
        long active     = datLichs.stream().filter(d -> "DaXacNhan".equals(d.getTrangThai())).count();
        double revenue  = datLichs.stream()
                .filter(d -> "DaThanhToan".equals(d.getTrangThai()) || "HoanThanh".equals(d.getTrangThai()))
                .mapToDouble(DatLich::getTongTien).sum();

        if (lblSanValue     != null) lblSanValue.setText(sanReady + " sẵn sàng · " + sanRenting + " đang thuê");
        if (lblDatLichValue != null) lblDatLichValue.setText(pending + " chờ · " + active + " đã duyệt");
        if (lblDoanhThuValue!= null) lblDoanhThuValue.setText(String.format("%,.0f đ", revenue));

        // 2. Cập nhật bảng danh sách lịch đặt trong ngày
        if (todayUpcomingBookingsModel != null) {
            todayUpcomingBookingsModel.setRowCount(0);
            List<DatLich> todayList = datLichs.stream()
                    .filter(d -> todayStr.equalsIgnoreCase(d.getNgayDat().trim()) && !"DaHuy".equalsIgnoreCase(d.getTrangThai()))
                    .sorted(Comparator.comparing(DatLich::getGioBatDau))
                    .toList();
            for (DatLich d : todayList) {
                todayUpcomingBookingsModel.addRow(new Object[]{
                        d.getMaLichDat(), d.getTenSan(), d.getTenKhach(), d.getSoDienThoaiKhach(),
                        d.getKhungGio(), String.format("%,.0f đ", d.getTongTien()), d.getTrangThaiHienThi()
                });
            }
            if (todayList.isEmpty()) {
                todayUpcomingBookingsModel.addRow(new Object[]{
                        "—", "Hôm nay chưa có lịch đặt nào", "—", "—", "—", "—", "—"
                });
            }
        }

        // 3. Cập nhật ma trận khung giờ hoạt động (Timeline matrix)
        if (todayCalendarModel != null) {
            todayCalendarModel.setRowCount(0);
            for (KhuVucSan san : sans) {
                Object[] row = new Object[TIME_SLOTS.length + 1];
                row[0] = san.getTenSan();

                // Kiểm tra xem sân có thuộc danh sách bảo trì hay không
                boolean inMaint = DataStore.get().getBaoTris().stream()
                        .anyMatch(b -> san.getMaSan() != null && san.getMaSan().equals(b.getMaSan())
                                && !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu())
                                && !"Huy".equalsIgnoreCase(b.getTrangThaiPhieu())
                                && isDateInRange(todayStr, b.getNgayBatDau(), b.getNgayKetThuc()));

                for (int t = 0; t < TIME_SLOTS.length; t++) {
                    if (inMaint || "BaoTri".equalsIgnoreCase(san.getTrangThai())) {
                        row[t + 1] = "Bảo trì";
                    } else {
                        int slotMin = toMinutes(TIME_SLOTS[t]);
                        DatLich found = null;
                        for (DatLich d : datLichs) {
                            if (san.getMaSan() != null && san.getMaSan().equals(d.getMaSan())
                                    && todayStr.equalsIgnoreCase(d.getNgayDat().trim())
                                    && !"DaHuy".equalsIgnoreCase(d.getTrangThai())) {
                                int s = toMinutes(d.getGioBatDau()), e = toMinutes(d.getGioKetThuc());
                                if (slotMin >= s && slotMin < e) { found = d; break; }
                            }
                        }
                        if (found == null) row[t + 1] = "Trống";
                        else if ("ChoXacNhan".equalsIgnoreCase(found.getTrangThai())) row[t + 1] = "Đã đặt";
                        else row[t + 1] = "Đang đá";
                    }
                }
                todayCalendarModel.addRow(row);
            }
        }
    }

    // ─── ACTIONS ──────────────────────────────────────────────────────────────

    /**
     * Đặt sân nhanh từ ô ma trận được chọn.
     * 
     * @param courtIndex    Chỉ số sân bóng tương ứng với hàng trong bảng
     * @param timeSlotIndex Chỉ số khung giờ tương ứng với cột trong bảng
     */
    private void onQuickBookSlot(int courtIndex, int timeSlotIndex) {
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        if (courtIndex < 0 || courtIndex >= sans.size()) return;
        KhuVucSan san = sans.get(courtIndex);
        if (DataStore.get().isSanBaoTriVoiNgay(san, LocalDate.now().toString())) {
            JOptionPane.showMessageDialog(this,
                    "Sân " + san.getTenSan() + " đang bảo trì, không thể đặt lịch.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String start = TIME_SLOTS[timeSlotIndex];
        String end   = getNextHour(start);
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, san, LocalDate.now().toString(), start, end);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;
        saveNewBooking(dialog.getResult(), dialog.getSelectedSan());
    }

    /**
     * Mở thoại tạo lịch đặt mới từ nút bấm thao tác nhanh.
     */
    private void onQuickBook() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, (DatLich) null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;
        saveNewBooking(dialog.getResult(), dialog.getSelectedSan());
    }

    /**
     * Lưu phiếu đặt sân mới được khởi tạo vào DataStore và nạp lại Dashboard.
     * 
     * @param form Thông tin phiếu đặt từ hộp thoại
     * @param san  Đối tượng sân bóng được chọn
     */
    private void saveNewBooking(DatLich form, KhuVucSan san) {
        if (form == null || san == null) return;
        String ma = Utils.CodeGen.next("DL", DataStore.get().getDatLichs().stream().map(DatLich::getMaLichDat).toList(), 3);
        String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getTenDangNhap() : "Hệ thống";
        DatLich phieu = new DatLich();
        phieu.setMaLichDat(ma); phieu.setMaSan(san.getMaSan()); phieu.setTenSan(san.getTenSan());
        phieu.setMaKhachHang(form.getMaKhachHang()); phieu.setTenKhach(form.getTenKhach());
        phieu.setSoDienThoaiKhach(form.getSoDienThoaiKhach()); phieu.setNgayDat(form.getNgayDat());
        phieu.setGioBatDau(form.getGioBatDau()); phieu.setGioKetThuc(form.getGioKetThuc());
        phieu.setTrangThai("ChoXacNhan"); phieu.setMaTaiKhoan(nv); phieu.setGhiChu(form.getGhiChu());
        phieu.setTienSan(form.getTienSan()); phieu.setTienDichVu(form.getTienDichVu());
        phieu.setDichVuKem(form.getDichVuKem());
        DataStore.get().getDatLichs().add(phieu);
        refresh();
        JOptionPane.showMessageDialog(this, "Đã tạo phiếu " + ma + " (" + san.getTenSan() + " — " + form.getKhungGio() + ") thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Bán bổ sung dịch vụ / đồ ăn cho phiếu đặt sân đang hoạt động.
     */
    private void onQuickSell() {
        List<DatLich> activeList = DataStore.get().getDatLichs().stream()
                .filter(d -> !"DaHuy".equals(d.getTrangThai()) && !"DaThanhToan".equals(d.getTrangThai()))
                .toList();
        if (activeList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiện không có phiếu đặt sân nào đang hoạt động.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChonDonDialog chonDon = new ChonDonDialog(parent, activeList);
        chonDon.setVisible(true);
        if (!chonDon.isConfirmed() || chonDon.getSelectedDon() == null) return;

        DatLich booking = chonDon.getSelectedDon();
        BanDichVuDialog dialog = new BanDichVuDialog(parent, booking);
        dialog.setVisible(true);
        if (!dialog.isConfirmed() || dialog.getSelectedItems().isEmpty()) return;

        StringBuilder sold = new StringBuilder(); double total = 0;
        for (ChonDichVuDialog.SelectedItem item : dialog.getSelectedItems()) {
            DichVu dv = item.getDichVu(); int qty = item.getSoLuong();
            dv.xuatKho(qty);
            double cost = dv.getDonGia() * qty; total += cost;
            booking.addDichVuKem(dv.getTenDichVu(), qty, cost);
            if (sold.length() > 0) sold.append(", ");
            sold.append(qty).append("x ").append(dv.getTenDichVu());
        }
        refresh();
        JOptionPane.showMessageDialog(this,
                "Đã bán cho phiếu " + booking.getMaLichDat() + ": " + sold
                + "\nPhát sinh thêm: " + String.format("%,.0f đ", total),
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mở hộp thoại kiểm tra tình trạng sử dụng sân hiện tại.
     */
    private void onCheckSan() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        new KiemTraSanDialog(parent).setVisible(true);
    }

    /**
     * Mở thoại tạo phiếu bảo trì nhanh cho sân bóng.
     */
    private void onQuickMaint() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BaoTriFormDialog dialog = new BaoTriFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed() || dialog.getResult() == null) return;
        BaoTri bt  = dialog.getResult();
        KhuVucSan san = dialog.getSelectedSan();
        String ma = Utils.CodeGen.next("BT", DataStore.get().getBaoTris().stream().map(BaoTri::getMaPhieuBaoTri).toList(), 3);
        BaoTri record = new BaoTri();
        record.setMaPhieuBaoTri(ma); record.setMaSan(san != null ? san.getMaSan() : null);
        record.setTenSan(san != null ? san.getTenSan() : "Sân"); record.setNoiDung(bt.getNoiDung());
        record.setNgayBatDau(bt.getNgayBatDau()); record.setNgayKetThuc(bt.getNgayKetThuc());
        record.setTrangThaiPhieu("DANG_BAO_TRI");
        DataStore.get().getBaoTris().add(record);
        if (san != null) san.setTrangThai("BaoTri");
        refresh();
        JOptionPane.showMessageDialog(this, "Đã tạo phiếu bảo trì " + ma + " thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    /**
     * Chuyển đổi chuỗi giờ dạng "HH:mm" thành số phút trong ngày.
     */
    private static int toMinutes(String t) {
        if (t == null || !t.contains(":")) return 0;
        try { String[] p = t.trim().split(":"); return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]); }
        catch (Exception e) { return 0; }
    }

    /**
     * Lấy chuỗi mốc giờ kết tiếp (cộng 1 giờ) từ chuỗi giờ hiện tại.
     */
    private static String getNextHour(String t) {
        try { return String.format("%02d:00", Integer.parseInt(t.split(":")[0]) + 1); }
        catch (Exception e) { return t; }
    }

    /**
     * Kiểm tra xem ngày mục tiêu có nằm trong khoảng ngày bắt đầu và kết thúc hay không.
     */
    private boolean isDateInRange(String target, String start, String end) {
        if (target == null || start == null || target.isBlank() || start.isBlank()) return false;
        try {
            LocalDate t  = LocalDate.parse(target.trim());
            LocalDate s  = LocalDate.parse(start.trim());
            LocalDate e  = (end != null && !end.isBlank()) ? LocalDate.parse(end.trim()) : s;
            return !t.isBefore(s) && !t.isAfter(e);
        } catch (Exception ex) { return target.trim().equalsIgnoreCase(start.trim()); }
    }

    // ─── CALENDAR CELL RENDERER ──────────────────────────────────────────────

    /**
     * Trình vẽ ô giao diện tùy chỉnh (Cell Renderer) cho bảng ma trận khung giờ.
     * Cung cấp màu nền tô rõ ràng theo trạng thái: Trống (Xanh lá), Đặt trước (Vàng), Đang đá (Xanh dương), Bảo trì (Xám).
     */
    private class CalendarCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(0, 4, 0, 4));
            setFont(UIConstants.FONT_NORMAL);

            // Cột 0: Tên sân bóng
            if (column == 0) {
                setFont(UIConstants.FONT_BOLD);
                setHorizontalAlignment(LEFT);
                if (!isSelected) { c.setBackground(new Color(248, 249, 250)); c.setForeground(UIConstants.TEXT_PRIMARY); }
                return c;
            }

            setHorizontalAlignment(CENTER);
            String str = value != null ? value.toString().trim() : "";

            // Hiển thị tooltip hướng dẫn thao tác
            if (c instanceof JComponent jc) {
                String court = table.getValueAt(row, 0) != null ? table.getValueAt(row, 0).toString() : "";
                String slot  = column <= TIME_SLOTS.length ? TIME_SLOTS[column - 1] : "";
                jc.setToolTipText(court + " [" + slot + "]: " + str + " — Nhấp đúp để đặt");
            }

            // Định dạng màu nền theo trạng thái ô
            if (!isSelected) {
                if      (str.startsWith("Đang đá")) { c.setBackground(new Color(219, 234, 254)); c.setForeground(new Color(37,  99,  235)); }
                else if (str.startsWith("Đã đặt"))  { c.setBackground(new Color(254, 243, 199)); c.setForeground(new Color(217, 119, 6));   }
                else if (str.contains("Bảo trì")) { c.setBackground(new Color(241, 245, 249)); c.setForeground(new Color(100, 116, 139)); }
                else                                 { c.setBackground(new Color(240, 253, 244)); c.setForeground(new Color(22,  163, 74));  }
            }
            return c;
        }
    }
}
