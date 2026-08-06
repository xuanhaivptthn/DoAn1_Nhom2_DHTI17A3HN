package GiaoDien.Panels;

import GiaoDien.Dialogs.*;
import Utils.PageUI;

import Model.BaoTri;
import Model.DatLich;
import Model.DichVu;
import Model.KhuVucSan;
import Utils.DataStore;
import Utils.SessionManager;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Giao diện Đặt sân bóng & Quản lý lịch sân theo dạng Lưới khung giờ (Timeline Matrix Schedule Grid).
 * <p>
 * Cho phép xem trực quan trạng thái từng khung giờ theo các khu vực sân bóng,
 * tương tác nhấp đúp đặt lịch nhanh, xem thông tin chi tiết trên Inspector Panel,
 * bán dịch vụ/đồ ăn kèm, chuyển đổi trạng thái, sửa phiếu đặt, hủy phiếu và xuất hóa đơn.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class QuanLyDatLichPanel extends javax.swing.JPanel {

    /**
     * Mảng chứa các mốc khung giờ phục vụ trong ngày từ 06:00 đến 23:00.
     */
    private static final String[] TIME_SLOTS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    /**
     * Ngày được chọn xem lịch hiện tại (mặc định là ngày hôm nay).
     */
    private LocalDate selectedDate = LocalDate.now();

    /**
     * Trình định dạng hiển thị ngày dd/MM/yyyy.
     */
    private final DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Nhãn hiển thị tiêu đề ngày kèm thứ trong tuần.
     */
    private JLabel lblDateTitle;

    /**
     * Bảng hiển thị ma trận khung giờ lịch đặt sân bóng.
     */
    private JTable tableSchedule;

    /**
     * Model dữ liệu bảng ma trận khung giờ lịch đặt.
     */
    private DefaultTableModel modelSchedule;

    /**
     * Danh sách khu vực sân bóng đang có trong hệ thống.
     */
    private List<KhuVucSan> courtList = new ArrayList<>();

    // Thành phần trên Inspector Panel thông tin chi tiết
    /** Nhãn tiêu đề tên sân và khung giờ được chọn trên Inspector */
    private JLabel lblDetailSlotHeader;
    /** Nhãn hiển thị trạng thái phiếu đặt lịch trên Inspector */
    private JLabel lblDetailTrangThai;
    /** Nhãn hiển thị tên khách hàng trên Inspector */
    private JLabel lblDetailKhach;
    /** Nhãn hiển thị số điện thoại khách hàng trên Inspector */
    private JLabel lblDetailSdt;
    /** Nhãn hiển thị loại sân bóng trên Inspector */
    private JLabel lblDetailLoaiSan;
    /** Nhãn hiển thị tiền thuê sân bóng trên Inspector */
    private JLabel lblDetailTienSan;
    /** Nhãn hiển thị tổng tiền dịch vụ & đồ ăn kèm trên Inspector */
    private JLabel lblDetailDichVu;
    /** Nhãn hiển thị ghi chú của phiếu đặt trên Inspector */
    private JLabel lblDetailGhiChu;
    /** Nhãn hiển thị tổng số tiền của phiếu đặt sân trên Inspector */
    private JLabel lblDetailConLai;

    /** Nút đổi trạng thái lịch đặt */
    private JButton btnChangeStatus;
    /** Nút bán dịch vụ & đồ ăn bổ sung */
    private JButton btnSellSvc;
    /** Nút sửa thông tin phiếu đặt */
    private JButton btnChangeSchedule;
    /** Nút hủy phiếu đặt */
    private JButton btnCancelBooking;
    /** Nút xem và xuất hóa đơn thanh toán */
    private JButton btnExportInvoice;

    /** Nhãn hiển thị tài khoản làm việc bên trái thanh trạng thái */
    private JLabel lblStatusLeft;
    /** Nhãn hiển thị thống kê tổng quan ngày bên phải thanh trạng thái */
    private JLabel lblStatusRight;

    /**
     * Phiếu đặt lịch hiện đang được chọn trên ma trận khung giờ.
     */
    private DatLich currentlySelectedBooking = null;

    /**
     * Chỉ số cột sân bóng đang chọn trên ma trận (-1 nếu không chọn).
     */
    private int selectedCourtIndex = -1;

    /**
     * Chỉ số hàng khung giờ đang chọn trên ma trận (-1 nếu không chọn).
     */
    private int selectedTimeIndex = -1;

    /** Controller xử lý nghiệp vụ đặt lịch sân bóng */
    private final Controller.DatLichController datLichController = new Controller.DatLichController();
    /** Controller xử lý nghiệp vụ hóa đơn thanh toán */
    private final Controller.HoaDonController hoaDonController = new Controller.HoaDonController();
    /** Controller xử lý nghiệp vụ kho hàng & vật tư */
    private final Controller.KhoController khoController = new Controller.KhoController();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Panel thân nội dung chính */
    private javax.swing.JPanel pnlBody;
    /** Panel thanh trạng thái dưới cùng */
    private javax.swing.JPanel pnlBottomBar;
    /** Panel inspector hiển thị chi tiết bên phải */
    private javax.swing.JPanel pnlDetailCard;
    /** Panel bao bọc header tiêu đề trang */
    private javax.swing.JPanel pnlHeaderWrap;
    /** Panel chứa vùng ma trận và vùng inspector */
    private javax.swing.JPanel pnlMainContent;
    /** Panel chứa ma trận lưới khung giờ và thanh chú thích */
    private javax.swing.JPanel pnlScheduleGridWrap;
    /** Panel điều khiển thời gian và ngày phía trên */
    private javax.swing.JPanel pnlTopControls;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo giao diện Quản lý đặt lịch sân bóng mới.
     */
    public QuanLyDatLichPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Khởi tạo linh kiện giao diện tự động bởi NetBeans GUI Builder.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeaderWrap = new javax.swing.JPanel();
        pnlBody = new javax.swing.JPanel();
        pnlTopControls = new javax.swing.JPanel();
        pnlMainContent = new javax.swing.JPanel();
        pnlScheduleGridWrap = new javax.swing.JPanel();
        pnlDetailCard = new javax.swing.JPanel();
        pnlBottomBar = new javax.swing.JPanel();

        setBackground(UIConstants.BG);
        setLayout(new java.awt.BorderLayout());

        pnlHeaderWrap.setOpaque(false);
        pnlHeaderWrap.setLayout(new java.awt.BorderLayout());
        add(pnlHeaderWrap, java.awt.BorderLayout.NORTH);

        pnlBody.setBackground(UIConstants.BG);
        pnlBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 8, 16));
        pnlBody.setLayout(new java.awt.BorderLayout(0, 8));

        pnlTopControls.setBackground(UIConstants.BG);
        pnlTopControls.setLayout(new java.awt.BorderLayout());
        pnlBody.add(pnlTopControls, java.awt.BorderLayout.NORTH);

        pnlMainContent.setOpaque(false);
        pnlMainContent.setLayout(new java.awt.BorderLayout(12, 0));

        pnlScheduleGridWrap.setLayout(new java.awt.BorderLayout(0, 6));
        pnlMainContent.add(pnlScheduleGridWrap, java.awt.BorderLayout.CENTER);

        pnlDetailCard.setPreferredSize(new java.awt.Dimension(300, 0));
        pnlDetailCard.setLayout(new java.awt.BorderLayout(0, 12));
        pnlMainContent.add(pnlDetailCard, java.awt.BorderLayout.EAST);

        pnlBody.add(pnlMainContent, java.awt.BorderLayout.CENTER);

        pnlBottomBar.setBackground(new java.awt.Color(240, 240, 240));
        pnlBottomBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));
        pnlBottomBar.setLayout(new java.awt.BorderLayout());
        pnlBody.add(pnlBottomBar, java.awt.BorderLayout.SOUTH);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cấu hình thiết lập giao diện tùy chỉnh và khởi tạo ma trận lịch đặt sân bóng.
     */
    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Đặt sân & Lịch sân bóng",
                "Xem trực quan ma trận lịch đặt sân theo khung giờ — Mặc định lịch hôm nay"), BorderLayout.CENTER);

        // Khởi tạo các phần giao diện chính
        buildTopControls();
        buildScheduleGrid();
        buildDetailInspector();
        buildStatusBar();

        // Nạp ma trận lịch đặt
        reloadSchedule();
    }

    /**
     * Xây dựng thanh công cụ chuyển đổi ngày xem lịch (Trước, Hôm nay, Sau, Chọn ngày)
     * và nút bấm tạo mới lịch đặt.
     */
    private void buildTopControls() {
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        left.setOpaque(false);

        // Nút chuyển sang ngày hôm trước
        JButton btnPrevDay = new javax.swing.JButton();
        btnPrevDay.setIcon(Utils.IconUtils.getPrevIcon(16));
        btnPrevDay.setPreferredSize(new Dimension(45, 32));
        btnPrevDay.addActionListener(e -> {
            selectedDate = selectedDate.minusDays(1);
            updateDateTitle();
            reloadSchedule();
        });

        // Nút quay về ngày hôm nay
        JButton btnToday = new javax.swing.JButton("Hôm nay");
        btnToday.setPreferredSize(new Dimension(85, 32));
        btnToday.addActionListener(e -> {
            selectedDate = LocalDate.now();
            updateDateTitle();
            reloadSchedule();
        });

        // Nút chuyển sang ngày kế tiếp
        JButton btnNextDay = new javax.swing.JButton();
        btnNextDay.setIcon(Utils.IconUtils.getNextIcon(16));
        btnNextDay.setPreferredSize(new Dimension(45, 32));
        btnNextDay.addActionListener(e -> {
            selectedDate = selectedDate.plusDays(1);
            updateDateTitle();
            reloadSchedule();
        });

        // Nút mở hộp thoại chọn ngày nhanh
        JButton btnPickDate = new javax.swing.JButton("Chọn ngày");
        btnPickDate.setPreferredSize(new Dimension(120, 32));
        btnPickDate.addActionListener(e -> onQuickPickDate());

        // Nhãn tiêu đề hiển thị ngày xem lịch
        lblDateTitle = new JLabel();
        lblDateTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDateTitle.setForeground(UIConstants.PRIMARY_DARK);
        lblDateTitle.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 8));
        lblDateTitle.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        lblDateTitle.setToolTipText("Nhấn để mở Hộp thoại chọn nhanh ngày");
        lblDateTitle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onQuickPickDate();
            }
        });
        updateDateTitle();

        left.add(btnPrevDay);
        left.add(btnToday);
        left.add(btnNextDay);
        left.add(btnPickDate);
        left.add(lblDateTitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        right.setOpaque(false);

        // Nút tạo mới lịch đặt sân
        JButton btnNewBooking = new javax.swing.JButton("+ Tạo lịch đặt");
        btnNewBooking.setPreferredSize(new Dimension(135, 34));
        btnNewBooking.addActionListener(e -> onBookNew());

        right.add(btnNewBooking);

        pnlTopControls.add(left, BorderLayout.WEST);
        pnlTopControls.add(right, BorderLayout.EAST);
    }

    /**
     * Mở hộp thoại ChonNgayDialog để cho phép người dùng chọn một ngày bất kỳ.
     */
    private void onQuickPickDate() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChonNgayDialog dialog = new ChonNgayDialog(parent, selectedDate);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            selectedDate = dialog.getSelectedDate();
            updateDateTitle();
            reloadSchedule();
        }
    }

    /**
     * Cập nhật văn bản hiển thị tiêu đề ngày kèm thứ trong tuần tiếng Việt.
     */
    private void updateDateTitle() {
        String dayOfWeekVN = switch (selectedDate.getDayOfWeek()) {
            case MONDAY -> "Thứ 2";
            case TUESDAY -> "Thứ 3";
            case WEDNESDAY -> "Thứ 4";
            case THURSDAY -> "Thứ 5";
            case FRIDAY -> "Thứ 6";
            case SATURDAY -> "Thứ 7";
            case SUNDAY -> "Chủ Nhật";
        };
        lblDateTitle.setText(selectedDate.format(fmtDate) + " (" + dayOfWeekVN + ")");
    }

    /**
     * Xây dựng ma trận bảng lưới khung giờ hiển thị lịch các sân bóng và thanh ghi chú legend.
     */
    private void buildScheduleGrid() {
        courtList = DataStore.get().getKhuVucs();
        List<String> headers = new ArrayList<>();
        headers.add("Giờ");
        for (KhuVucSan k : courtList) {
            headers.add(k.getMaSan() + " (" + (k.getLoaiSan().contains("5") ? "S5" : k.getLoaiSan().contains("7") ? "S7" : "S11") + ")");
        }

        modelSchedule = new DefaultTableModel(headers.toArray(new String[0]), 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableSchedule = new JTable(modelSchedule);
        tableSchedule.setFont(UIConstants.FONT_NORMAL);
        tableSchedule.setRowHeight(42);
        tableSchedule.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSchedule.setCellSelectionEnabled(true);
        tableSchedule.setFillsViewportHeight(true);

        JTableHeader th = tableSchedule.getTableHeader();
        th.setFont(UIConstants.FONT_TABLE_HEADER);
        th.setBackground(new Color(230, 230, 230));
        th.setForeground(UIConstants.TEXT_PRIMARY);
        th.setPreferredSize(new Dimension(0, 36));
        th.setReorderingAllowed(false);

        tableSchedule.getColumnModel().getColumn(0).setPreferredWidth(75);

        // Áp dụng renderer tô màu ma trận khung giờ tùy chỉnh
        tableSchedule.setDefaultRenderer(Object.class, new ScheduleMatrixCellRenderer());

        // Lắng nghe sự kiện nhấp chuột chọn ô và nhấp đúp ô trống
        tableSchedule.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tableSchedule.rowAtPoint(e.getPoint());
                int c = tableSchedule.columnAtPoint(e.getPoint());
                if (r >= 0 && c >= 1) {
                    onSelectSlot(r, c - 1);
                    if (e.getClickCount() == 2 && currentlySelectedBooking == null) {
                        onQuickBookEmptySlot(r, c - 1);
                    }
                }
            }
        });

        pnlScheduleGridWrap.add(new javax.swing.JScrollPane(tableSchedule), BorderLayout.CENTER);

        // Thanh chú thích legend quy định màu sắc
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        pnlLegend.setOpaque(false);
        pnlLegend.add(createLegendItem(Color.WHITE, UIConstants.BORDER, "Trống"));
        pnlLegend.add(createLegendItem(new Color(219, 234, 254), new Color(37, 99, 235), "Đã đặt"));
        pnlLegend.add(createLegendItem(new Color(220, 252, 231), new Color(22, 163, 74), "Đã thanh toán"));
        pnlLegend.add(createLegendItem(new Color(254, 243, 199), new Color(180, 83, 9), "Bảo trì"));

        pnlScheduleGridWrap.add(pnlLegend, BorderLayout.SOUTH);
    }

    /**
     * Tạo một ô ghi chú quy định màu sắc trong thanh chú thích legend.
     */
    private JPanel createLegendItem(Color bg, Color border, String label) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);
        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(14, 14));
        box.setBackground(bg);
        box.setBorder(BorderFactory.createLineBorder(border, 1));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(UIConstants.TEXT_PRIMARY);
        item.add(box);
        item.add(lbl);
        return item;
    }

    /**
     * Xây dựng Panel Inspector hiển thị thông tin chi tiết lịch đặt bên phải giao diện.
     */
    private void buildDetailInspector() {
        pnlDetailCard.removeAll();
        pnlDetailCard.setLayout(new BorderLayout(0, 10));
        pnlDetailCard.setBackground(Color.WHITE);
        pnlDetailCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        JLabel title = new JLabel("Chi tiết lịch đặt");
        title.setFont(UIConstants.FONT_SUBTITLE);
        title.setForeground(UIConstants.PRIMARY);

        lblDetailSlotHeader = new JLabel("Chưa chọn ô lịch");
        lblDetailSlotHeader.setFont(UIConstants.FONT_SMALL);
        lblDetailSlotHeader.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel pnlTop = new JPanel(new BorderLayout(0, 2));
        pnlTop.setOpaque(false);
        pnlTop.add(title, BorderLayout.NORTH);
        pnlTop.add(lblDetailSlotHeader, BorderLayout.SOUTH);

        // Bố cục thông tin chi tiết theo danh sách trường
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblDetailTrangThai = createBoldValue("—");
        lblDetailKhach = createBoldValue("—");
        lblDetailSdt = createBoldValue("—");
        lblDetailLoaiSan = createBoldValue("—");
        lblDetailTienSan = createBoldValue("0đ");
        lblDetailDichVu = createBoldValue("0đ");
        lblDetailGhiChu = new JLabel("—");
        lblDetailGhiChu.setFont(UIConstants.FONT_NORMAL);
        lblDetailGhiChu.setForeground(UIConstants.TEXT_PRIMARY);

        lblDetailConLai = new JLabel("0đ");
        lblDetailConLai.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDetailConLai.setForeground(UIConstants.PRIMARY);

        int r = 0;
        r = addInspectorRow(form, gbc, r, "Trạng thái:", lblDetailTrangThai);
        r = addInspectorRow(form, gbc, r, "Khách:", lblDetailKhach);
        r = addInspectorRow(form, gbc, r, "SĐT:", lblDetailSdt);
        r = addInspectorRow(form, gbc, r, "Loại sân:", lblDetailLoaiSan);
        r = addInspectorRow(form, gbc, r, "Tiền sân:", lblDetailTienSan);
        r = addInspectorRow(form, gbc, r, "DV & Vật tư:", lblDetailDichVu);
        r = addInspectorRow(form, gbc, r, "Ghi chú:", lblDetailGhiChu);

        gbc.gridx = 0; gbc.gridy = r++; gbc.gridwidth = 2;
        form.add(new javax.swing.JSeparator(), gbc);

        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel("Tổng tiền:"), gbc);
        gbc.gridx = 1;
        form.add(lblDetailConLai, gbc);

        JPanel pnlCenterWrap = new JPanel(new BorderLayout(0, 6));
        pnlCenterWrap.setOpaque(false);
        pnlCenterWrap.add(pnlTop, BorderLayout.NORTH);
        pnlCenterWrap.add(form, BorderLayout.CENTER);

        // Danh sách các nút thao tác nghiệp vụ trên Inspector
        JPanel actions = new JPanel(new java.awt.GridLayout(5, 1, 0, 6));
        actions.setOpaque(false);

        btnChangeStatus = new javax.swing.JButton("Thay đổi trạng thái lịch đặt");
        btnChangeStatus.setIcon(Utils.IconUtils.getStatusIcon(16));
        PageUI.stylePrimaryButton(btnChangeStatus);
        btnChangeStatus.setPreferredSize(new Dimension(240, 34));
        btnChangeStatus.addActionListener(e -> onChangeStatus());

        btnSellSvc = new javax.swing.JButton("Bán DV / Vật phẩm");
        btnSellSvc.setIcon(Utils.IconUtils.getOpenIcon(16));
        PageUI.styleSecondaryButton(btnSellSvc);
        btnSellSvc.setPreferredSize(new Dimension(240, 34));
        btnSellSvc.addActionListener(e -> onSellSvc());

        btnChangeSchedule = new javax.swing.JButton("Sửa phiếu đặt");
        btnChangeSchedule.setIcon(Utils.IconUtils.getEditIcon(16));
        PageUI.styleSecondaryButton(btnChangeSchedule);
        btnChangeSchedule.setPreferredSize(new Dimension(240, 34));
        btnChangeSchedule.addActionListener(e -> onChangeSchedule());

        btnExportInvoice = new javax.swing.JButton("Xuất hóa đơn");
        btnExportInvoice.setIcon(Utils.IconUtils.getExportIcon(16));
        PageUI.styleSecondaryButton(btnExportInvoice);
        btnExportInvoice.setPreferredSize(new Dimension(240, 34));
        btnExportInvoice.addActionListener(e -> onExportInvoice());

        btnCancelBooking = new javax.swing.JButton("Hủy lịch đặt");
        btnCancelBooking.setIcon(Utils.IconUtils.getDeleteIcon(16));
        PageUI.styleDangerButton(btnCancelBooking);
        btnCancelBooking.setPreferredSize(new Dimension(240, 34));
        btnCancelBooking.addActionListener(e -> onCancelBooking());

        actions.add(btnChangeStatus);
        actions.add(btnSellSvc);
        actions.add(btnChangeSchedule);
        actions.add(btnExportInvoice);
        actions.add(btnCancelBooking);

        pnlDetailCard.add(pnlCenterWrap, BorderLayout.NORTH);
        pnlDetailCard.add(actions, BorderLayout.SOUTH);
    }

    /**
     * Thêm một hàng thuộc tính nhãn-giá trị trên Inspector Panel.
     */
    private int addInspectorRow(JPanel form, GridBagConstraints gbc, int row, String label, JLabel val) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        form.add(val, gbc);
        return row + 1;
    }

    /**
     * Tạo nhãn chữ in đậm định dạng giá trị.
     */
    private JLabel createBoldValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BOLD);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        return l;
    }

    /**
     * Xây dựng thanh trạng thái dưới cùng của panel.
     */
    private void buildStatusBar() {
        lblStatusLeft = new JLabel("Đăng nhập: " + (SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getTenDangNhap() : "Admin"));
        lblStatusLeft.setFont(UIConstants.FONT_SMALL);
        lblStatusLeft.setForeground(UIConstants.TEXT_SECONDARY);

        lblStatusRight = new JLabel("Hôm nay: 0 lịch  •  Doanh thu ước tính: 0đ");
        lblStatusRight.setFont(UIConstants.FONT_SMALL);
        lblStatusRight.setForeground(UIConstants.TEXT_PRIMARY);

        pnlBottomBar.add(lblStatusLeft, BorderLayout.WEST);
        pnlBottomBar.add(lblStatusRight, BorderLayout.EAST);
    }

    /**
     * Nạp lại dữ liệu ma trận lưới khung giờ theo ngày được chọn hiện tại.
     */
    public void reloadSchedule() {
        courtList = DataStore.get().getKhuVucs();
        modelSchedule.setRowCount(0);

        String curDateStr = selectedDate.toString();
        // Lấy danh sách phiếu đặt không bị hủy trong ngày
        List<DatLich> dayBookings = DataStore.get().getDatLichs().stream()
                .filter(d -> curDateStr.equals(d.getNgayDat()) && !"DaHuy".equals(d.getTrangThai()))
                .toList();

        // Lấy danh sách các phiếu bảo trì đang áp dụng trong ngày
        List<BaoTri> dayMaints = DataStore.get().getBaoTris().stream()
                .filter(b -> !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"Huy".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HUY".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HoanThanh".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HOAN_THANH".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && isDateInMaintenanceRange(curDateStr, b.getNgayBatDau(), b.getNgayKetThuc()))
                .toList();

        // Duyệt từng mốc khung giờ và điền ô tương ứng từng sân
        for (String slotTime : TIME_SLOTS) {
            Object[] rowData = new Object[courtList.size() + 1];
            rowData[0] = slotTime;

            for (int col = 0; col < courtList.size(); col++) {
                KhuVucSan court = courtList.get(col);

                // Kiểm tra trạng thái bảo trì của sân
                BaoTri maint = dayMaints.stream().filter(b -> court.getMaSan() != null && court.getMaSan().equals(b.getMaSan())).findFirst().orElse(null);
                if (maint != null || DataStore.get().isSanBaoTriVoiNgay(court, curDateStr)) {
                    rowData[col + 1] = maint != null ? "Bảo trì - " + maint.getNoiDung() : "Đang bảo trì";
                    continue;
                }

                // Kiểm tra phiếu đặt giao thoa với khung giờ
                DatLich booking = dayBookings.stream()
                        .filter(d -> court.getMaSan() != null && court.getMaSan().equals(d.getMaSan()) && isTimeOverlap(d.getGioBatDau(), d.getGioKetThuc(), slotTime))
                        .findFirst().orElse(null);

                if (booking != null) {
                    if ("DaThanhToan".equals(booking.getTrangThaiTT())) {
                        rowData[col + 1] = booking.getTenKhach() + " · ĐTT";
                    } else {
                        rowData[col + 1] = booking.getTenKhach() + " - " + slotTime;
                    }
                } else {
                    rowData[col + 1] = "— trống —";
                }
            }
            modelSchedule.addRow(rowData);
        }

        // Cập nhật nhãn thống kê trên thanh trạng thái
        long activeCount = dayBookings.size();
        double totalRev = dayBookings.stream().mapToDouble(DatLich::getTongTien).sum();
        lblStatusRight.setText("Hôm nay: " + activeCount + " lịch  •  Doanh thu ước tính: " + String.format("%,.0f VNĐ", (double) (totalRev)));

        // Tự động chọn lại vị trí ô được chọn trước đó
        if (selectedCourtIndex >= 0 && selectedTimeIndex >= 0) {
            onSelectSlot(selectedTimeIndex, selectedCourtIndex);
        } else {
            onSelectSlot(2, 1);
        }
    }

    /**
     * Kiểm tra xem khung giờ mốc có trùng khớp hay nằm trong khoảng giờ bắt đầu và kết thúc hay không.
     */
    private boolean isTimeOverlap(String start, String end, String slotTime) {
        try {
            int slotH = Integer.parseInt(slotTime.split(":")[0]);
            int sH = Integer.parseInt(start.split(":")[0]);
            int eH = Integer.parseInt(end.split(":")[0]);
            return slotH >= sH && slotH < eH;
        } catch (Exception e) {
            return start.startsWith(slotTime);
        }
    }

    /**
     * Kiểm tra xem ngày chỉ định có thuộc khoảng thời gian bắt đầu và kết thúc bảo trì hay không.
     */
    private boolean isDateInMaintenanceRange(String targetDateStr, String startDateStr, String endDateStr) {
        if (targetDateStr == null || targetDateStr.isBlank()) return false;
        if (startDateStr == null || startDateStr.isBlank()) return false;

        try {
            LocalDate targetDate = LocalDate.parse(targetDateStr.trim());
            LocalDate startDate = LocalDate.parse(startDateStr.trim());
            LocalDate endDate = (endDateStr != null && !endDateStr.isBlank())
                    ? LocalDate.parse(endDateStr.trim())
                    : startDate;

            return (!targetDate.isBefore(startDate)) && (!targetDate.isAfter(endDate));
        } catch (Exception e) {
            if (targetDateStr.trim().equalsIgnoreCase(startDateStr.trim())) return true;
            return endDateStr != null && targetDateStr.trim().equalsIgnoreCase(endDateStr.trim());
        }
    }

    /**
     * Cập nhật thông tin chi tiết trên Inspector Panel khi người dùng nhấp chọn ô trên ma trận.
     * 
     * @param timeIdx  Chỉ số khung giờ (hàng trong bảng)
     * @param courtIdx Chỉ số sân bóng (cột trong bảng)
     */
    private void onSelectSlot(int timeIdx, int courtIdx) {
        if (courtIdx < 0 || courtIdx >= courtList.size() || timeIdx < 0 || timeIdx >= TIME_SLOTS.length) return;

        this.selectedTimeIndex = timeIdx;
        this.selectedCourtIndex = courtIdx;

        KhuVucSan court = courtList.get(courtIdx);
        String slotTime = TIME_SLOTS[timeIdx];
        String curDateStr = selectedDate.toString();

        lblDetailSlotHeader.setText(court.getTenSan() + " · " + slotTime + "–" + getNextHour(slotTime));

        // Tìm kiếm phiếu đặt sân tương ứng tại ô ma trận
        currentlySelectedBooking = DataStore.get().getDatLichs().stream()
                .filter(d -> curDateStr.equals(d.getNgayDat())
                        && court.getMaSan() != null && court.getMaSan().equals(d.getMaSan())
                        && !"DaHuy".equals(d.getTrangThai())
                        && isTimeOverlap(d.getGioBatDau(), d.getGioKetThuc(), slotTime))
                .findFirst().orElse(null);

        if (currentlySelectedBooking != null) {
            // Nạp thông tin phiếu đặt sân đang có
            String tt = currentlySelectedBooking.getTrangThaiHienThi();
            if ("DaThanhToan".equalsIgnoreCase(currentlySelectedBooking.getTrangThaiTT())) {
                tt += " (Đã TT)";
            } else if ("ChuaThanhToan".equalsIgnoreCase(currentlySelectedBooking.getTrangThaiTT())) {
                tt += " (Chưa TT)";
            }
            lblDetailTrangThai.setText(tt);
            lblDetailKhach.setText(currentlySelectedBooking.getTenKhach());
            lblDetailSdt.setText(currentlySelectedBooking.getSoDienThoaiKhach());
            lblDetailLoaiSan.setText(court.getLoaiSanHienThi());
            lblDetailTienSan.setText(String.format("%,.0f VNĐ", (double) (currentlySelectedBooking.getTienSan())));

            String dvText = String.format("%,.0f VNĐ", (double) (currentlySelectedBooking.getTienDichVu()));
            if (currentlySelectedBooking.getDichVuKem() != null && !currentlySelectedBooking.getDichVuKem().isBlank()) {
                dvText += " (" + currentlySelectedBooking.getDichVuKem().replace("\n", ", ") + ")";
            }
            lblDetailDichVu.setText(dvText);

            String ghiChu = currentlySelectedBooking.getGhiChu();
            lblDetailGhiChu.setText(ghiChu != null && !ghiChu.isBlank() ? ghiChu : "—");
            lblDetailConLai.setText(String.format("%,.0f VNĐ", (double) (currentlySelectedBooking.getTongTien())));

            boolean isPaid = "DaThanhToan".equals(currentlySelectedBooking.getTrangThaiTT());

            // Kích hoạt các nút bấm chức năng trên Inspector
            btnChangeStatus.setEnabled(true);
            btnSellSvc.setEnabled(!isPaid);
            btnChangeSchedule.setEnabled(true);
            btnExportInvoice.setEnabled(true);
            btnCancelBooking.setEnabled(true);
        } else {
            // Trường hợp ô trống không có lịch đặt
            lblDetailTrangThai.setText("Sân trống");
            lblDetailKhach.setText("(Chưa đặt)");
            lblDetailSdt.setText("—");
            lblDetailLoaiSan.setText(court.getLoaiSanHienThi());
            lblDetailTienSan.setText(String.format("%,.0f VNĐ", (double) (court.getGiaThueTheoGio())));
            lblDetailDichVu.setText("0đ");
            lblDetailGhiChu.setText("—");
            lblDetailConLai.setText(String.format("%,.0f VNĐ", (double) (court.getGiaThueTheoGio())));

            // Vô hiệu hóa các nút chức năng đối với ô trống
            btnChangeStatus.setEnabled(false);
            btnSellSvc.setEnabled(false);
            btnChangeSchedule.setEnabled(false);
            btnExportInvoice.setEnabled(false);
            btnCancelBooking.setEnabled(false);
        }
    }

    /**
     * Xử lý thay đổi trạng thái của phiếu đặt sân được chọn.
     */
    private void onChangeStatus() {
        if (currentlySelectedBooking == null) return;

        String[] options = {
                "Chờ xác nhận",
                "Đã xác nhận",
                "Hoàn thành (Đã thanh toán)",
                "Đã hủy"
        };

        String currentDisplay = currentlySelectedBooking.getTrangThaiHienThi();
        if ("DaThanhToan".equals(currentlySelectedBooking.getTrangThaiTT())) {
            currentDisplay = "Hoàn thành (Đã thanh toán)";
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Chọn trạng thái mới cho phiếu " + currentlySelectedBooking.getMaLichDat() + " (Khách: " + currentlySelectedBooking.getTenKhach() + "):",
                "Thay đổi trạng thái lịch đặt",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentDisplay
        );

        if (selected == null) return;

        String maPhieu = currentlySelectedBooking.getMaLichDat();

        String statusKey = switch (selected) {
            case "Chờ xác nhận" -> "ChoXacNhan";
            case "Đã xác nhận" -> "DaXacNhan";
            case "Hoàn thành (Đã thanh toán)" -> "HoanThanh";
            case "Đã hủy" -> "DaHuy";
            default -> null;
        };

        if (statusKey != null) {
            datLichController.updateBookingStatus(currentlySelectedBooking, statusKey, "Tiền mặt");
            if ("HoanThanh".equals(statusKey)) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Đã chuyển trạng thái sang Hoàn thành (Đã thanh toán) và lưu Hóa đơn thành công.\nBạn có muốn xuất Hóa đơn thanh toán ngay bây giờ không?",
                        "Xuất hóa đơn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    onExportInvoice("Tiền mặt");
                }
            }
        }

        String displayStatus = currentlySelectedBooking.getTrangThaiHienThi();
        reloadSchedule();
        JOptionPane.showMessageDialog(this,
                "Đã thay đổi trạng thái phiếu " + maPhieu + " thành: " + displayStatus,
                "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Lấy mốc giờ kế tiếp bằng cách cộng 1 giờ.
     */
    private String getNextHour(String slotTime) {
        try {
            int h = Integer.parseInt(slotTime.split(":")[0]) + 1;
            return String.format("%02d:00", h);
        } catch (Exception e) {
            return slotTime;
        }
    }

    /**
     * Mở hộp thoại lập mới phiếu đặt sân bóng.
     */
    private void onBookNew() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DatLich form = dialog.getResult();
        KhuVucSan san = dialog.getSelectedSan();
        if (san == null) return;

        String ma = Utils.CodeGen.next("DL", DataStore.get().getDatLichs().stream().map(DatLich::getMaLichDat).toList(), 3);
        String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getTenDangNhap() : "Admin";

        DatLich phieu = new DatLich();
        phieu.setMaLichDat(ma);
        phieu.setMaSan(san.getMaSan());
        phieu.setTenSan(san.getTenSan());
        phieu.setMaKhachHang(form.getMaKhachHang());
        phieu.setTenKhach(form.getTenKhach());
        phieu.setSoDienThoaiKhach(form.getSoDienThoaiKhach());
        phieu.setNgayDat(form.getNgayDat());
        phieu.setGioBatDau(form.getGioBatDau());
        phieu.setGioKetThuc(form.getGioKetThuc());
        phieu.setTrangThai("ChoXacNhan");
        phieu.setMaTaiKhoan(nv);
        phieu.setGhiChu(form.getGhiChu());
        phieu.setTienSan(form.getTienSan());
        phieu.setTienDichVu(form.getTienDichVu());
        phieu.setDichVuKem(form.getDichVuKem());
        phieu.setSelectedDvMap(form.getSelectedDvMap());
        phieu.setSelectedDoAnMap(form.getSelectedDoAnMap());
        datLichController.createBooking(phieu);
        reloadSchedule();
        JOptionPane.showMessageDialog(this, "Đã tạo mới lịch đặt sân " + ma + " thành công!", "Kết quả đặt lịch", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Thực hiện đặt sân nhanh khi người dùng nhấp đúp vào một ô trống trên ma trận.
     * 
     * @param timeIdx  Chỉ số khung giờ tương ứng với hàng
     * @param courtIdx Chỉ số sân tương ứng với cột
     */
    private void onQuickBookEmptySlot(int timeIdx, int courtIdx) {
        if (courtIdx < 0 || courtIdx >= courtList.size() || timeIdx < 0 || timeIdx >= TIME_SLOTS.length) return;

        KhuVucSan court = courtList.get(courtIdx);
        String curDateStr = selectedDate.toString();

        if (datLichController.isSanBaoTriVoiNgay(court, curDateStr)) {
            JOptionPane.showMessageDialog(this,
                    "[!] SÂN ĐANG BẢO TRÌ!\n\nSân " + court.getTenSan() + " hiện đang trong trạng thái bảo trì cơ sở vật chất.\nKhông thể tạo mới lịch đặt cho sân này!",
                    "Cảnh báo bảo trì sân", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String slotTime = TIME_SLOTS[timeIdx];

        DatLich existing = datLichController.findOverlapBooking(court.getMaSan(), curDateStr, slotTime, getNextHour(slotTime), null);

        if (existing == null) {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            DatLichFormDialog dialog = new DatLichFormDialog(parent, court, curDateStr, slotTime, getNextHour(slotTime));
            dialog.setVisible(true);
            if (!dialog.isConfirmed()) return;

            DatLich form = dialog.getResult();
            KhuVucSan san = dialog.getSelectedSan();
            if (san == null) return;

            String ma = Utils.CodeGen.next("DL", DataStore.get().getDatLichs().stream().map(DatLich::getMaLichDat).toList(), 3);
            String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getTenDangNhap() : "Admin";

            DatLich phieu = new DatLich();
            phieu.setMaLichDat(ma);
            phieu.setMaSan(san.getMaSan());
            phieu.setTenSan(san.getTenSan());
            phieu.setMaKhachHang(form.getMaKhachHang());
            phieu.setTenKhach(form.getTenKhach());
            phieu.setSoDienThoaiKhach(form.getSoDienThoaiKhach());
            phieu.setNgayDat(form.getNgayDat());
            phieu.setGioBatDau(form.getGioBatDau());
            phieu.setGioKetThuc(form.getGioKetThuc());
            phieu.setTrangThai("ChoXacNhan");
            phieu.setMaTaiKhoan(nv);
            phieu.setGhiChu(form.getGhiChu());
            phieu.setTienSan(form.getTienSan());
            phieu.setTienDichVu(form.getTienDichVu());
            phieu.setDichVuKem(form.getDichVuKem());
            phieu.setSelectedDvMap(form.getSelectedDvMap());
            phieu.setSelectedDoAnMap(form.getSelectedDoAnMap());
            datLichController.createBooking(phieu);
            reloadSchedule();
            JOptionPane.showMessageDialog(this, "Đã tạo mới lịch đặt sân " + ma + " (" + san.getTenSan() + " - " + form.getKhungGio() + ") thành công!", "Kết quả đặt lịch", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Mở thoại bán bổ sung dịch vụ / vật tư / đồ ăn cho phiếu đặt sân đang chọn.
     */
    private void onSellSvc() {
        if (currentlySelectedBooking == null) return;
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BanDichVuDialog dialog = new BanDichVuDialog(parent, currentlySelectedBooking);
        dialog.setVisible(true);
        if (!dialog.isConfirmed() || dialog.getSelectedItems().isEmpty()) return;

        StringBuilder soldInfo = new StringBuilder();
        double totalAdded = 0;

        for (ChonDichVuDialog.SelectedItem item : dialog.getSelectedItems()) {
            DichVu dv = item.getDichVu();
            int qty   = item.getSoLuong();
            khoController.giamStock(dv, qty);
            double cost = dv.getDonGia() * qty;
            totalAdded += cost;
            currentlySelectedBooking.addDichVuKem(dv.getTenDichVu(), qty, cost);
            
            if ("Vật tư kho".equalsIgnoreCase(dv.getLoaiDichVu())) {
                int oldQty = currentlySelectedBooking.getSelectedDoAnMap().getOrDefault(dv.getId(), 0);
                currentlySelectedBooking.getSelectedDoAnMap().put(dv.getId(), oldQty + qty);
            } else {
                int oldQty = currentlySelectedBooking.getSelectedDvMap().getOrDefault(dv.getId(), 0);
                currentlySelectedBooking.getSelectedDvMap().put(dv.getId(), oldQty + qty);
            }

            if (soldInfo.length() > 0) soldInfo.append(", ");
            soldInfo.append(qty).append("x ").append(dv.getTenDichVu());
        }

        datLichController.updateBooking(currentlySelectedBooking);

        reloadSchedule();
        JOptionPane.showMessageDialog(this,
                "Đã bán thành công cho phiếu " + currentlySelectedBooking.getMaLichDat() + ":\n"
                + soldInfo + "\nPhát sinh thêm: " + String.format("%,.0f VNĐ", totalAdded),
                "Bán dịch vụ thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mở thoại chỉnh sửa thông tin của phiếu đặt sân hiện tại.
     */
    private void onChangeSchedule() {
        if (currentlySelectedBooking == null) return;
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, currentlySelectedBooking);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DatLich updated = dialog.getResult();
        currentlySelectedBooking.setMaSan(updated.getMaSan());
        currentlySelectedBooking.setTenSan(updated.getTenSan());
        currentlySelectedBooking.setMaKhachHang(updated.getMaKhachHang());
        currentlySelectedBooking.setTenKhach(updated.getTenKhach());
        currentlySelectedBooking.setSoDienThoaiKhach(updated.getSoDienThoaiKhach());
        currentlySelectedBooking.setNgayDat(updated.getNgayDat());
        currentlySelectedBooking.setGioBatDau(updated.getGioBatDau());
        currentlySelectedBooking.setGioKetThuc(updated.getGioKetThuc());
        currentlySelectedBooking.setGhiChu(updated.getGhiChu());
        currentlySelectedBooking.setTienSan(updated.getTienSan());
        currentlySelectedBooking.setTienDichVu(updated.getTienDichVu());
        currentlySelectedBooking.setTongTien(updated.getTongTien());
        currentlySelectedBooking.setDichVuKem(updated.getDichVuKem());
        currentlySelectedBooking.setSelectedDvMap(updated.getSelectedDvMap());
        currentlySelectedBooking.setSelectedDoAnMap(updated.getSelectedDoAnMap());
        datLichController.updateBooking(currentlySelectedBooking);
        String maPhieu = currentlySelectedBooking.getMaLichDat();
        reloadSchedule();
        JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin phiếu đặt sân " + maPhieu + " thành công!", "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xuất hóa đơn mặc định thanh toán tiền mặt.
     */
    private void onExportInvoice() {
        onExportInvoice("Tiền mặt");
    }

    /**
     * Mở hộp thoại xem và in/xuất hóa đơn thanh toán cho phiếu đặt lịch.
     * 
     * @param phuongThucTT Phương thức thanh toán (Tiền mặt, Chuyển khoản...)
     */
    private void onExportInvoice(String phuongThucTT) {
        if (currentlySelectedBooking == null) return;
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        HoaDonDialog dialog = new HoaDonDialog(parent, currentlySelectedBooking, phuongThucTT);
        dialog.setVisible(true);
    }

    /**
     * Xử lý xác nhận và thực hiện hủy phiếu đặt lịch sân bóng.
     */
    private void onCancelBooking() {
        if (currentlySelectedBooking == null) return;
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy phiếu " + currentlySelectedBooking.getMaLichDat() + " của khách " + currentlySelectedBooking.getTenKhach() + "?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            datLichController.updateBookingStatus(currentlySelectedBooking, "DaHuy", "Tiền mặt");
            reloadSchedule();
            JOptionPane.showMessageDialog(this, "Đã hủy phiếu đặt lịch.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Trình vẽ ô giao diện tùy chỉnh (Cell Renderer) cho ma trận lưới khung giờ (Timeline Matrix Grid).
     * Tô màu nền linh hoạt: Trống (Trắng), Bảo trì (Vàng), Đã thanh toán (Xanh lá), Đặt trước (Xanh dương).
     */
    private class ScheduleMatrixCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String text = value != null ? value.toString() : "";
            setHorizontalAlignment(column == 0 ? CENTER : LEFT);
            setFont(column == 0 ? UIConstants.FONT_BOLD : UIConstants.FONT_NORMAL);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

            // Định dạng cột mốc khung giờ (Cột 0)
            if (column == 0) {
                c.setBackground(new Color(245, 245, 245));
                c.setForeground(UIConstants.TEXT_PRIMARY);
                return c;
            }

            // Định dạng màu highlight khi ô đang được chọn
            if (isSelected) {
                c.setBackground(new Color(37, 99, 235));
                c.setForeground(Color.WHITE);
                setFont(UIConstants.FONT_BOLD);
                return c;
            }

            // Định dạng màu nền ô theo nội dung trạng thái
            if (text.contains("Bảo trì")) {
                c.setBackground(new Color(254, 243, 199)); // Vàng nhạt
                c.setForeground(new Color(180, 83, 9));
                setFont(UIConstants.FONT_BOLD);
            } else if (text.contains("ĐTT")) {
                c.setBackground(new Color(220, 252, 231)); // Xanh lá nhạt
                c.setForeground(new Color(22, 163, 74));
                setFont(UIConstants.FONT_BOLD);
            } else if (!text.contains("— trống —")) {
                c.setBackground(new Color(219, 234, 254)); // Xanh dương nhạt
                c.setForeground(new Color(29, 78, 216));
                setFont(UIConstants.FONT_BOLD);
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(new Color(160, 160, 160));
            }

            return c;
        }
    }
}
