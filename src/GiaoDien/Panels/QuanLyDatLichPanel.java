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
 * Giao diện Đặt sân / Lịch sân theo dạng Lưới khung giờ (Timeline Matrix).
 * Hỗ trợ chọn ô xem chi tiết, thanh toán, đổi lịch, hủy lịch.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyDatLichPanel extends javax.swing.JPanel {

    private static final String[] TIME_SLOTS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    private LocalDate selectedDate = LocalDate.now();
    private final DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JLabel lblDateTitle;
    private JTable tableSchedule;
    private DefaultTableModel modelSchedule;
    private List<KhuVucSan> courtList = new ArrayList<>();

    // Inspector Details Panel components
    private JLabel lblDetailSlotHeader;
    private JLabel lblDetailKhach;
    private JLabel lblDetailSdt;
    private JLabel lblDetailLoaiSan;
    private JLabel lblDetailTienSan;
    private JLabel lblDetailDichVu;
    private JLabel lblDetailGhiChu;
    private JLabel lblDetailConLai;

    private JButton btnChangeStatus;
    private JButton btnSellSvc;
    private JButton btnChangeSchedule;
    private JButton btnCancelBooking;
    private JButton btnExportInvoice;

    private JLabel lblStatusLeft;
    private JLabel lblStatusRight;

    private DatLich currentlySelectedBooking = null;
    private int selectedCourtIndex = -1;
    private int selectedTimeIndex = -1;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlBottomBar;
    private javax.swing.JPanel pnlDetailCard;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlMainContent;
    private javax.swing.JPanel pnlScheduleGridWrap;
    private javax.swing.JPanel pnlTopControls;
    // End of variables declaration//GEN-END:variables

    public QuanLyDatLichPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
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

    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Đặt sân & Lịch sân bóng",
                "Xem trực quan ma trận lịch đặt sân theo khung giờ — Mặc định lịch hôm nay"), BorderLayout.CENTER);

        buildTopControls();
        buildScheduleGrid();
        buildDetailInspector();
        buildStatusBar();

        reloadSchedule();
    }

    private void buildTopControls() {
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        left.setOpaque(false);

        JButton btnPrevDay = new javax.swing.JButton();
        btnPrevDay.setIcon(Utils.IconUtils.getPrevIcon(16));
        PageUI.styleSecondaryButton(btnPrevDay);
        btnPrevDay.setPreferredSize(new Dimension(45, 32));
        btnPrevDay.addActionListener(e -> {
            selectedDate = selectedDate.minusDays(1);
            updateDateTitle();
            reloadSchedule();
        });

        JButton btnToday = new javax.swing.JButton("Hôm nay");
        PageUI.styleSecondaryButton(btnToday);
        btnToday.setPreferredSize(new Dimension(85, 32));
        btnToday.addActionListener(e -> {
            selectedDate = LocalDate.now();
            updateDateTitle();
            reloadSchedule();
        });

        JButton btnNextDay = new javax.swing.JButton();
        btnNextDay.setIcon(Utils.IconUtils.getNextIcon(16));
        PageUI.styleSecondaryButton(btnNextDay);
        btnNextDay.setPreferredSize(new Dimension(45, 32));
        btnNextDay.addActionListener(e -> {
            selectedDate = selectedDate.plusDays(1);
            updateDateTitle();
            reloadSchedule();
        });

        JButton btnPickDate = new javax.swing.JButton("Chọn ngày");
        PageUI.styleSecondaryButton(btnPickDate);
        btnPickDate.setPreferredSize(new Dimension(120, 32));
        btnPickDate.addActionListener(e -> onQuickPickDate());

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

        JButton btnNewBooking = new javax.swing.JButton("+ Tạo lịch đặt");
        PageUI.stylePrimaryButton(btnNewBooking);
        btnNewBooking.setPreferredSize(new Dimension(135, 34));
        btnNewBooking.addActionListener(e -> onBookNew());

        right.add(btnNewBooking);

        pnlTopControls.add(left, BorderLayout.WEST);
        pnlTopControls.add(right, BorderLayout.EAST);
    }

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

        tableSchedule.setDefaultRenderer(Object.class, new ScheduleMatrixCellRenderer());

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

        // Legend bar
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        pnlLegend.setOpaque(false);
        pnlLegend.add(createLegendItem(Color.WHITE, UIConstants.BORDER, "Trống"));
        pnlLegend.add(createLegendItem(new Color(219, 234, 254), new Color(37, 99, 235), "Đã đặt"));
        pnlLegend.add(createLegendItem(new Color(220, 252, 231), new Color(22, 163, 74), "Đã thanh toán"));
        pnlLegend.add(createLegendItem(new Color(254, 243, 199), new Color(180, 83, 9), "Bảo trì"));

        pnlScheduleGridWrap.add(pnlLegend, BorderLayout.SOUTH);
    }

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

        // Fields panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        r = addInspectorRow(form, gbc, r, "Khách:", lblDetailKhach);
        r = addInspectorRow(form, gbc, r, "SĐT:", lblDetailSdt);
        r = addInspectorRow(form, gbc, r, "Loại sân:", lblDetailLoaiSan);
        r = addInspectorRow(form, gbc, r, "Tiền sân:", lblDetailTienSan);
        r = addInspectorRow(form, gbc, r, "Dịch vụ:", lblDetailDichVu);
        r = addInspectorRow(form, gbc, r, "Ghi chú:", lblDetailGhiChu);

        gbc.gridx = 0; gbc.gridy = r++; gbc.gridwidth = 2;
        form.add(new javax.swing.JSeparator(), gbc);

        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel("Còn lại:"), gbc);
        gbc.gridx = 1;
        form.add(lblDetailConLai, gbc);

        JPanel pnlCenterWrap = new JPanel(new BorderLayout(0, 6));
        pnlCenterWrap.setOpaque(false);
        pnlCenterWrap.add(pnlTop, BorderLayout.NORTH);
        pnlCenterWrap.add(form, BorderLayout.CENTER);

        // Actions panel with GridLayout for guaranteed non-zero rendering
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

    private JLabel createBoldValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BOLD);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        return l;
    }

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

    public void reloadSchedule() {
        courtList = DataStore.get().getKhuVucs();
        modelSchedule.setRowCount(0);

        String curDateStr = selectedDate.toString();
        List<DatLich> dayBookings = DataStore.get().getDatLichs().stream()
                .filter(d -> curDateStr.equals(d.getNgayDat()) && !"DaHuy".equals(d.getTrangThai()))
                .toList();

        List<BaoTri> dayMaints = DataStore.get().getBaoTris().stream()
                .filter(b -> !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"Huy".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HUY".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HoanThanh".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HOAN_THANH".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && isDateInMaintenanceRange(curDateStr, b.getNgayBatDau(), b.getNgayKetThuc()))
                .toList();

        for (String slotTime : TIME_SLOTS) {
            Object[] rowData = new Object[courtList.size() + 1];
            rowData[0] = slotTime;

            for (int col = 0; col < courtList.size(); col++) {
                KhuVucSan court = courtList.get(col);

                // Check maintenance status for court
                BaoTri maint = dayMaints.stream().filter(b -> court.getMaSan() != null && court.getMaSan().equals(b.getMaSan())).findFirst().orElse(null);
                if (maint != null || DataStore.get().isSanBaoTriVoiNgay(court, curDateStr)) {
                    rowData[col + 1] = maint != null ? "Bảo trì - " + maint.getNoiDung() : "Đang bảo trì";
                    continue;
                }

                // Check booking
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

        // Summary stats for bottom bar
        long activeCount = dayBookings.size();
        double totalRev = dayBookings.stream().mapToDouble(DatLich::getTongTien).sum();
        lblStatusRight.setText("Hôm nay: " + activeCount + " lịch  •  Doanh thu ước tính: " + String.format("%,.0f VNĐ", (double) (totalRev)));

        // Auto select first occupied or empty cell if none selected
        if (selectedCourtIndex >= 0 && selectedTimeIndex >= 0) {
            onSelectSlot(selectedTimeIndex, selectedCourtIndex);
        } else {
            // Find slot at 18:00 for Court 2 if available
            onSelectSlot(2, 1);
        }
    }

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

    private void onSelectSlot(int timeIdx, int courtIdx) {
        if (courtIdx < 0 || courtIdx >= courtList.size() || timeIdx < 0 || timeIdx >= TIME_SLOTS.length) return;

        this.selectedTimeIndex = timeIdx;
        this.selectedCourtIndex = courtIdx;

        KhuVucSan court = courtList.get(courtIdx);
        String slotTime = TIME_SLOTS[timeIdx];
        String curDateStr = selectedDate.toString();

        lblDetailSlotHeader.setText(court.getTenSan() + " · " + slotTime + "–" + getNextHour(slotTime));

        currentlySelectedBooking = DataStore.get().getDatLichs().stream()
                .filter(d -> curDateStr.equals(d.getNgayDat())
                        && court.getMaSan() != null && court.getMaSan().equals(d.getMaSan())
                        && !"DaHuy".equals(d.getTrangThai())
                        && isTimeOverlap(d.getGioBatDau(), d.getGioKetThuc(), slotTime))
                .findFirst().orElse(null);

        if (currentlySelectedBooking != null) {
            lblDetailKhach.setText(currentlySelectedBooking.getTenKhach());
            lblDetailSdt.setText(currentlySelectedBooking.getSoDienThoaiKhach());
            lblDetailLoaiSan.setText(court.getLoaiSanHienThi());
            lblDetailTienSan.setText(String.format("%,.0f VNĐ", (double) (currentlySelectedBooking.getTienSan())));
            lblDetailDichVu.setText(String.format("%,.0f VNĐ", (double) (currentlySelectedBooking.getTienDichVu())));
            String ghiChu = currentlySelectedBooking.getGhiChu();
            lblDetailGhiChu.setText(ghiChu != null && !ghiChu.isBlank() ? ghiChu : "—");
            lblDetailConLai.setText(String.format("%,.0f VNĐ", (double) (currentlySelectedBooking.getConLai())));

            boolean isPaid = "DaThanhToan".equals(currentlySelectedBooking.getTrangThaiTT());

            btnChangeStatus.setEnabled(true);
            btnSellSvc.setEnabled(!isPaid);
            btnChangeSchedule.setEnabled(true);
            btnExportInvoice.setEnabled(true);
            btnCancelBooking.setEnabled(true);
        } else {
            lblDetailKhach.setText("(Sân trống)");
            lblDetailSdt.setText("—");
            lblDetailLoaiSan.setText(court.getLoaiSanHienThi());
            lblDetailTienSan.setText(String.format("%,.0f VNĐ", (double) (court.getGiaThueTheoGio())));
            lblDetailDichVu.setText("0đ");
            lblDetailGhiChu.setText("—");
            lblDetailConLai.setText(String.format("%,.0f VNĐ", (double) (court.getGiaThueTheoGio())));

            btnChangeStatus.setEnabled(false);
            btnSellSvc.setEnabled(false);
            btnChangeSchedule.setEnabled(false);
            btnExportInvoice.setEnabled(false);
            btnCancelBooking.setEnabled(false);
        }
    }

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

        switch (selected) {
            case "Chờ xác nhận" -> {
                currentlySelectedBooking.setTrangThai("ChoXacNhan");
                currentlySelectedBooking.setTrangThaiTT("ChuaThanhToan");
            }
            case "Đã xác nhận" -> {
                currentlySelectedBooking.setTrangThai("DaXacNhan");
                currentlySelectedBooking.setTrangThaiTT("ChuaThanhToan");
            }
            case "Hoàn thành (Đã thanh toán)" -> {
                currentlySelectedBooking.setTrangThai("HoanThanh");
                currentlySelectedBooking.setTrangThaiTT("DaThanhToan");

                int choice = JOptionPane.showConfirmDialog(this,
                        "Đã chuyển trạng thái sang Hoàn thành.\nBạn có muốn xuất Hóa đơn thanh toán ngay bây giờ không?",
                        "Xuất hóa đơn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    onExportInvoice("Tiền mặt");
                }
            }
            case "Đã hủy" -> {
                currentlySelectedBooking.setTrangThai("DaHuy");
            }
        }

        if (DataStore.isUseDatabase()) {
            try { new DAO.DatLichDAO().update(currentlySelectedBooking); } catch (Exception ignored) {}
        }

        String displayStatus = currentlySelectedBooking.getTrangThaiHienThi();
        reloadSchedule();
        JOptionPane.showMessageDialog(this,
                "Đã thay đổi trạng thái phiếu " + maPhieu + " thành: " + displayStatus,
                "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getNextHour(String slotTime) {
        try {
            int h = Integer.parseInt(slotTime.split(":")[0]) + 1;
            return String.format("%02d:00", h);
        } catch (Exception e) {
            return slotTime;
        }
    }

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
        DataStore.get().getDatLichs().add(phieu);
        if (DataStore.isUseDatabase()) {
            try { new DAO.DatLichDAO().insert(phieu); } catch (Exception ignored) {}
        }
        reloadSchedule();
        JOptionPane.showMessageDialog(this, "Đã tạo mới lịch đặt sân " + ma + " thành công!", "Kết quả đặt lịch", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onQuickBookEmptySlot(int timeIdx, int courtIdx) {
        if (courtIdx < 0 || courtIdx >= courtList.size() || timeIdx < 0 || timeIdx >= TIME_SLOTS.length) return;

        KhuVucSan court = courtList.get(courtIdx);
        String curDateStr = selectedDate.toString();

        if (DataStore.get().isSanBaoTriVoiNgay(court, curDateStr)) {
            JOptionPane.showMessageDialog(this,
                    "[!] SÂN ĐANG BẢO TRÌ!\n\nSân " + court.getTenSan() + " hiện đang trong trạng thái bảo trì cơ sở vật chất.\nKhông thể tạo mới lịch đặt cho sân này!",
                    "Cảnh báo bảo trì sân", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String slotTime = TIME_SLOTS[timeIdx];

        DatLich existing = DataStore.get().getDatLichs().stream()
                .filter(d -> curDateStr.equals(d.getNgayDat())
                        && court.getMaSan() != null && court.getMaSan().equals(d.getMaSan())
                        && !"DaHuy".equals(d.getTrangThai())
                        && isTimeOverlap(d.getGioBatDau(), d.getGioKetThuc(), slotTime))
                .findFirst().orElse(null);

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
            DataStore.get().getDatLichs().add(phieu);
            if (DataStore.isUseDatabase()) {
                try { new DAO.DatLichDAO().insert(phieu); } catch (Exception ignored) {}
            }
            reloadSchedule();
            JOptionPane.showMessageDialog(this, "Đã tạo mới lịch đặt sân " + ma + " (" + san.getTenSan() + " - " + form.getKhungGio() + ") thành công!", "Kết quả đặt lịch", JOptionPane.INFORMATION_MESSAGE);
        }
    }



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
            dv.xuatKho(qty);
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

        if (DataStore.isUseDatabase()) {
            try { new DAO.DatLichDAO().update(currentlySelectedBooking); } catch (Exception ignored) {}
        }

        reloadSchedule();
        JOptionPane.showMessageDialog(this,
                "Đã bán thành công cho phiếu " + currentlySelectedBooking.getMaLichDat() + ":\n"
                + soldInfo + "\nPhát sinh thêm: " + String.format("%,.0f VNĐ", totalAdded),
                "Bán dịch vụ thành công", JOptionPane.INFORMATION_MESSAGE);
    }

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
        if (DataStore.isUseDatabase()) {
            try { new DAO.DatLichDAO().update(currentlySelectedBooking); } catch (Exception ignored) {}
        }
        String maPhieu = currentlySelectedBooking.getMaLichDat();
        reloadSchedule();
        JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin phiếu đặt sân " + maPhieu + " thành công!", "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onExportInvoice() {
        onExportInvoice("Tiền mặt");
    }

    private void onExportInvoice(String phuongThucTT) {
        if (currentlySelectedBooking == null) return;
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        HoaDonDialog dialog = new HoaDonDialog(parent, currentlySelectedBooking, phuongThucTT);
        dialog.setVisible(true);
    }

    private void onCancelBooking() {
        if (currentlySelectedBooking == null) return;
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy phiếu " + currentlySelectedBooking.getMaLichDat() + " của khách " + currentlySelectedBooking.getTenKhach() + "?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            currentlySelectedBooking.setTrangThai("DaHuy");
            if (DataStore.isUseDatabase()) {
                try { new DAO.DatLichDAO().update(currentlySelectedBooking); } catch (Exception ignored) {}
            }
            reloadSchedule();
            JOptionPane.showMessageDialog(this, "Đã hủy phiếu đặt lịch.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Custom Table Cell Renderer for Matrix Timeline Schedule Grid.
     */
    private class ScheduleMatrixCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String text = value != null ? value.toString() : "";
            setHorizontalAlignment(column == 0 ? CENTER : LEFT);
            setFont(column == 0 ? UIConstants.FONT_BOLD : UIConstants.FONT_NORMAL);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

            if (column == 0) {
                c.setBackground(new Color(245, 245, 245));
                c.setForeground(UIConstants.TEXT_PRIMARY);
                return c;
            }

            if (isSelected) {
                c.setBackground(new Color(37, 99, 235));
                c.setForeground(Color.WHITE);
                setFont(UIConstants.FONT_BOLD);
                return c;
            }

            if (text.contains("Bảo trì")) {
                c.setBackground(new Color(254, 243, 199)); // Yellow
                c.setForeground(new Color(180, 83, 9));
                setFont(UIConstants.FONT_BOLD);
            } else if (text.contains("ĐTT")) {
                c.setBackground(new Color(220, 252, 231)); // Green
                c.setForeground(new Color(22, 163, 74));
                setFont(UIConstants.FONT_BOLD);
            } else if (!text.contains("— trống —")) {
                c.setBackground(new Color(219, 234, 254)); // Light Blue
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
