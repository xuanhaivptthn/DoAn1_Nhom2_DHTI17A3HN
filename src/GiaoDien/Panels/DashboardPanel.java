package GiaoDien.Panels;

import Model.BaoTri;
import Model.DatLich;
import Model.DichVu;
import Model.KhuVucSan;
import Utils.DataStore;
import Utils.SessionManager;
import GiaoDien.Dialogs.BanDichVuDialog;
import GiaoDien.Dialogs.BaoTriFormDialog;
import GiaoDien.Dialogs.DatLichFormDialog;
import GiaoDien.Dialogs.KiemTraSanDialog;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Trang Dashboard thông tin chung, Lịch đặt sân sắp tới trong ngày & Hành động nhanh hệ thống.
 * Hỗ trợ bấm vào ô khung giờ trống trên ma trận lịch để tạo nhanh phiếu đặt sân.
 */
public class DashboardPanel extends javax.swing.JPanel {

    private Consumer<String> pageNavigator;

    private JPanel cardSan;
    private JPanel cardDatLich;
    private JPanel cardDoanhThu;
    private JPanel cardDichVu;

    private DefaultTableModel todayUpcomingBookingsModel;
    private DefaultTableModel todayCalendarModel;
    private JTable todayCalendarTable;

    private static final String[] TIME_SLOTS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlQuickActionsCard;
    private javax.swing.JPanel pnlSplit;
    private javax.swing.JPanel pnlStatsGrid;
    // End of variables declaration//GEN-END:variables

    public DashboardPanel() {
        this(null);
    }

    public DashboardPanel(Consumer<String> pageNavigator) {
        this.pageNavigator = pageNavigator;
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
        pnlStatsGrid = new javax.swing.JPanel();
        pnlQuickActionsCard = new javax.swing.JPanel();
        pnlSplit = new javax.swing.JPanel();

        setBackground(UIConstants.BG);
        setLayout(new java.awt.BorderLayout());

        pnlHeaderWrap.setOpaque(false);
        pnlHeaderWrap.setLayout(new java.awt.BorderLayout());
        add(pnlHeaderWrap, java.awt.BorderLayout.NORTH);

        pnlBody.setBackground(UIConstants.BG);
        pnlBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 20, 20));
        pnlBody.setLayout(new java.awt.BorderLayout(0, 16));

        pnlStatsGrid.setOpaque(false);
        pnlStatsGrid.setLayout(new java.awt.GridLayout(1, 3, 16, 0));
        pnlBody.add(pnlStatsGrid, java.awt.BorderLayout.NORTH);

        pnlQuickActionsCard.setLayout(new java.awt.BorderLayout(0, 12));
        pnlBody.add(pnlQuickActionsCard, java.awt.BorderLayout.SOUTH);

        pnlSplit.setOpaque(false);
        pnlSplit.setLayout(new java.awt.GridLayout(1, 2, 16, 0));
        pnlBody.add(pnlSplit, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Dashboard Tổng quan & Lịch hôm nay",
                "Hiển thị lịch đặt sắp tới trong ngày · Sơ đồ khung giờ · Nhấp ô trống để tạo lịch nhanh"), BorderLayout.CENTER);

        buildStatsCards();
        buildSplitTables();
        buildQuickActions();
        refresh();
    }

    private void buildSplitTables() {
        pnlSplit.removeAll();
        pnlSplit.setLayout(new java.awt.GridLayout(2, 1, 0, 16));

        String todayFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // 1. TOP CARD: Today's Schedule Matrix Calendar (Ma trận khung giờ hôm nay)
        JPanel cardMatrix = new JPanel(new BorderLayout(0, 8));
        cardMatrix.setBackground(Color.WHITE);
        cardMatrix.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel titleTop = new JLabel("Ma trận khung giờ hôm nay (" + todayFormatted + ")");
        titleTop.setFont(UIConstants.FONT_SUBTITLE);
        titleTop.setForeground(UIConstants.PRIMARY);

        JButton btnGoDatLich = new JButton("Chi tiết ");
        btnGoDatLich.setIcon(Utils.IconUtils.getArrowRightIcon(16));
        btnGoDatLich.setFont(UIConstants.FONT_SMALL);
        if (pageNavigator != null) {
            btnGoDatLich.addActionListener(e -> pageNavigator.accept("datlich"));
        }

        JPanel pnlTopHeader = new JPanel(new BorderLayout());
        pnlTopHeader.setOpaque(false);
        pnlTopHeader.add(titleTop, BorderLayout.WEST);
        pnlTopHeader.add(btnGoDatLich, BorderLayout.EAST);
        cardMatrix.add(pnlTopHeader, BorderLayout.NORTH);

        List<String> slotHeaders = new java.util.ArrayList<>();
        slotHeaders.add("Khu vực sân");
        for (String slot : TIME_SLOTS) {
            slotHeaders.add(slot);
        }
        todayCalendarModel = new DefaultTableModel(slotHeaders.toArray(new String[0]), 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        todayCalendarTable = new JTable(todayCalendarModel);
        PageUI.styleTable(todayCalendarTable);
        todayCalendarTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        todayCalendarTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        for (int i = 1; i <= TIME_SLOTS.length; i++) {
            todayCalendarTable.getColumnModel().getColumn(i).setPreferredWidth(75);
        }
        todayCalendarTable.setDefaultRenderer(Object.class, new DashboardCalendarCellRenderer());

        todayCalendarTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = todayCalendarTable.rowAtPoint(e.getPoint());
                int c = todayCalendarTable.columnAtPoint(e.getPoint());
                if (r >= 0 && c >= 1 && e.getClickCount() == 2) {
                    Object val = todayCalendarModel.getValueAt(r, c);
                    if (val != null && val.toString().contains("Trống")) {
                        onQuickBookSlot(r, c - 1);
                    }
                }
            }
        });

        JScrollPane scrollMatrix = new JScrollPane(todayCalendarTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMatrix.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scrollMatrix.getViewport().setBackground(Color.WHITE);
        cardMatrix.add(scrollMatrix, BorderLayout.CENTER);

        // 2. BOTTOM CARD: Today's Upcoming Bookings (Lịch đặt sắp tới trong ngày)
        JPanel cardBookings = new JPanel(new BorderLayout(0, 8));
        cardBookings.setBackground(Color.WHITE);
        cardBookings.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel titleBottom = new JLabel("Lịch đặt sắp tới trong ngày (" + todayFormatted + ")");
        titleBottom.setFont(UIConstants.FONT_SUBTITLE);
        titleBottom.setForeground(UIConstants.PRIMARY);
        cardBookings.add(titleBottom, BorderLayout.NORTH);

        todayUpcomingBookingsModel = new DefaultTableModel(
                new String[]{"Mã", "Sân bóng", "Khách hàng", "SĐT", "Khung giờ", "Tổng tiền", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableBookings = new JTable(todayUpcomingBookingsModel);
        PageUI.styleTable(tableBookings);
        tableBookings.getColumnModel().getColumn(0).setPreferredWidth(60);

        JScrollPane scrollBookings = new JScrollPane(tableBookings);
        scrollBookings.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scrollBookings.getViewport().setBackground(Color.WHITE);
        cardBookings.add(scrollBookings, BorderLayout.CENTER);

        // Add Matrix on Top, Bookings List on Bottom
        pnlSplit.add(cardMatrix);
        pnlSplit.add(cardBookings);
    }

    private void onQuickBookSlot(int courtIndex, int timeSlotIndex) {
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        if (courtIndex < 0 || courtIndex >= sans.size() || timeSlotIndex < 0 || timeSlotIndex >= TIME_SLOTS.length) return;

        KhuVucSan san = sans.get(courtIndex);
        if ("BaoTri".equalsIgnoreCase(san.getTrangThai())) {
            JOptionPane.showMessageDialog(this,
                    "[!] SÂN ĐANG BẢO TRÌ!\n\nSân " + san.getTenSan() + " hiện đang tạm dừng hoạt động để bảo trì cơ sở vật chất.\nKhông thể tạo mới lịch đặt cho sân này!",
                    "Cảnh báo bảo trì sân", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String slotStart = TIME_SLOTS[timeSlotIndex];
        String slotEnd = getNextHour(slotStart);
        String dateStr = LocalDate.now().toString();

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, san, dateStr, slotStart, slotEnd);
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) return;

        DatLich form = dialog.getResult();
        KhuVucSan selectedSan = dialog.getSelectedSan();
        if (selectedSan == null) return;

        String ma = Utils.CodeGen.next("DL", DataStore.get().getDatLichs().stream().map(DatLich::getMaLichDat).toList(), 3);
        String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getTenDangNhap() : "Hệ thống";

        DatLich phieu = new DatLich();
        phieu.setMaLichDat(ma);
        phieu.setMaSan(selectedSan.getMaSan());
        phieu.setTenSan(selectedSan.getTenSan());
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
        DataStore.get().getDatLichs().add(phieu);

        refresh();
        JOptionPane.showMessageDialog(this, "Đã tạo phiếu đặt sân " + ma + " (" + selectedSan.getTenSan() + " - " + form.getKhungGio() + ") thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getNextHour(String slotTime) {
        try {
            int h = Integer.parseInt(slotTime.split(":")[0]) + 1;
            return String.format("%02d:00", h);
        } catch (Exception e) {
            return slotTime;
        }
    }

    public void refresh() {
        buildQuickActions();

        // Stat Cards Calculations
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        long sanReady = sans.stream().filter(k -> "SanSang".equals(k.getTrangThai())).count();
        long sanRenting = sans.stream().filter(k -> "DangThue".equals(k.getTrangThai())).count();

        List<DatLich> datLichs = DataStore.get().getDatLichs();
        long pendingBookings = datLichs.stream().filter(d -> "ChoXacNhan".equals(d.getTrangThai())).count();
        long activeBookings = datLichs.stream().filter(d -> "DaXacNhan".equals(d.getTrangThai())).count();

        double totalRevenue = datLichs.stream()
                .filter(d -> "DaThanhToan".equals(d.getTrangThai()) || "HoanThanh".equals(d.getTrangThai()))
                .mapToDouble(DatLich::getTongTien).sum();

        // Update Cards Text
        PageUI.updateStatCard(cardSan, sanReady + " sẵn sàng · " + sanRenting + " đang thuê");
        PageUI.updateStatCard(cardDatLich, pendingBookings + " chờ · " + activeBookings + " đã duyệt");
        PageUI.updateStatCard(cardDoanhThu, String.format("%,.0f VNĐ", (double) (totalRevenue)));

        // 1. Refresh Today's Upcoming Bookings Table
        todayUpcomingBookingsModel.setRowCount(0);
        String todayStr = LocalDate.now().toString();

        List<DatLich> todayUpcomingList = datLichs.stream()
                .filter(d -> todayStr.equalsIgnoreCase(d.getNgayDat().trim()) && !"DaHuy".equalsIgnoreCase(d.getTrangThai()))
                .sorted(Comparator.comparing(DatLich::getGioBatDau))
                .toList();

        for (DatLich d : todayUpcomingList) {
            todayUpcomingBookingsModel.addRow(new Object[]{
                    d.getMaLichDat(), d.getTenSan(), d.getTenKhach(), d.getSoDienThoaiKhach(),
                    d.getKhungGio(), String.format("%,.0f VNĐ", (double) (d.getTongTien())), d.getTrangThaiHienThi()
            });
        }

        if (todayUpcomingList.isEmpty()) {
            todayUpcomingBookingsModel.addRow(new Object[]{
                    "-", "Hôm nay chưa có lịch đặt nào", "-", "-", "-", "-", "-"
            });
        }

        // 2. Refresh Today Calendar Matrix Table
        todayCalendarModel.setRowCount(0);

        for (KhuVucSan san : sans) {
            Object[] row = new Object[TIME_SLOTS.length + 1];
            row[0] = san.getTenSan();

            BaoTri maint = DataStore.get().getBaoTris().stream()
                    .filter(b -> san.getMaSan() != null && san.getMaSan().equals(b.getMaSan())
                            && !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu())
                            && !"Huy".equalsIgnoreCase(b.getTrangThaiPhieu())
                            && isDateInMaintenanceRange(todayStr, b.getNgayBatDau(), b.getNgayKetThuc()))
                    .findFirst().orElse(null);

            if (maint != null || "BaoTri".equalsIgnoreCase(san.getTrangThai())) {
                for (int t = 0; t < TIME_SLOTS.length; t++) {
                    row[t + 1] = "Bảo trì";
                }
            } else {
                for (int t = 0; t < TIME_SLOTS.length; t++) {
                    String slotTime = TIME_SLOTS[t];
                    int slotMin = toMinutes(slotTime);

                    DatLich found = null;
                    for (DatLich d : datLichs) {
                        if (san.getMaSan() != null && san.getMaSan().equals(d.getMaSan())
                                && todayStr.equalsIgnoreCase(d.getNgayDat().trim())
                                && !"DaHuy".equalsIgnoreCase(d.getTrangThai())) {
                            int bStart = toMinutes(d.getGioBatDau());
                            int bEnd = toMinutes(d.getGioKetThuc());
                            if (slotMin >= bStart && slotMin < bEnd) {
                                found = d;
                                break;
                            }
                        }
                    }

                    if (found == null) {
                        row[t + 1] = "Trống";
                    } else if ("ChoXacNhan".equalsIgnoreCase(found.getTrangThai())) {
                        row[t + 1] = "Đã đặt";
                    } else {
                        row[t + 1] = "Đang đá";
                    }
                }
            }
            todayCalendarModel.addRow(row);
        }
    }

    private static int toMinutes(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        try {
            String[] parts = timeStr.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
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

    private void onQuickBook() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DatLich form = dialog.getResult();
        KhuVucSan san = dialog.getSelectedSan();
        if (san == null) return;

        String ma = Utils.CodeGen.next("DL", DataStore.get().getDatLichs().stream().map(DatLich::getMaLichDat).toList(), 3);
        String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getTenDangNhap() : "Hệ thống";

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
        DataStore.get().getDatLichs().add(phieu);

        refresh();
        JOptionPane.showMessageDialog(this, "Đã tạo phiếu đặt sân " + ma + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onQuickSell() {
        List<DatLich> activeList = DataStore.get().getDatLichs().stream()
                .filter(d -> !"DaHuy".equals(d.getTrangThai()) && !"DaThanhToan".equals(d.getTrangThai()))
                .toList();

        if (activeList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiện không có phiếu đặt sân nào đang hoạt động để bán dịch vụ đi kèm.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DatLich selectedBooking = (DatLich) JOptionPane.showInputDialog(this,
                "Chọn phiếu đặt sân để bán dịch vụ đi kèm:",
                "Bán dịch vụ đi kèm", JOptionPane.QUESTION_MESSAGE, null,
                activeList.toArray(), activeList.get(0));

        if (selectedBooking == null) return;

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BanDichVuDialog dialog = new BanDichVuDialog(parent, selectedBooking);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getSelectedDichVu() != null) {
            DichVu dv = dialog.getSelectedDichVu();
            int qty = dialog.getSoLuong();
            dv.xuatKho(qty);
            double addMoney = dv.getDonGia() * qty;
            selectedBooking.setTongTien(selectedBooking.getTongTien() + addMoney);

            refresh();
            JOptionPane.showMessageDialog(this, "Đã bán " + qty + "x " + dv.getTenDichVu() + " cho phiếu " + selectedBooking.getMaLichDat(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCheckSan() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KiemTraSanDialog dialog = new KiemTraSanDialog(parent);
        dialog.setVisible(true);
    }

    private void onQuickMaint() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BaoTriFormDialog dialog = new BaoTriFormDialog(parent, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getResult() != null) {
            BaoTri bt = dialog.getResult();
            KhuVucSan san = dialog.getSelectedSan();

            String ma = Utils.CodeGen.next("BT", DataStore.get().getBaoTris().stream().map(BaoTri::getMaPhieuBaoTri).toList(), 3);

            BaoTri record = new BaoTri();
            record.setMaPhieuBaoTri(ma);
            record.setMaSan(san != null ? san.getMaSan() : null);
            record.setTenSan(san != null ? san.getTenSan() : "Sân 1");
            record.setNoiDung(bt.getNoiDung());
            record.setNgayBatDau(bt.getNgayBatDau());
            record.setNgayKetThuc(bt.getNgayKetThuc());
            record.setTrangThaiPhieu("DANG_BAO_TRI");

            DataStore.get().getBaoTris().add(record);
            if (san != null) san.setTrangThai("BaoTri");

            refresh();
            JOptionPane.showMessageDialog(this, "Đã thêm phiếu bảo trì " + ma + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buildStatsCards() {
        pnlStatsGrid.removeAll();

        cardSan = PageUI.createStatCard("SÂN BÓNG", "0 sẵn sàng", UIConstants.PRIMARY);
        cardDatLich = PageUI.createStatCard("ĐẶT LỊCH HÔM NAY", "0 phiếu", UIConstants.INFO);
        cardDoanhThu = PageUI.createStatCard("DOANH THU ĐÃ THU", "0 đ", UIConstants.SUCCESS);

        pnlStatsGrid.add(cardSan);
        pnlStatsGrid.add(cardDatLich);
        pnlStatsGrid.add(cardDoanhThu);
    }

    private void buildQuickActions() {
        pnlQuickActionsCard.removeAll();

        JLabel title = new JLabel("Hành động nhanh & Phím tắt nghiệp vụ");
        title.setFont(UIConstants.FONT_SUBTITLE);
        title.setForeground(UIConstants.PRIMARY);
        pnlQuickActionsCard.add(title, BorderLayout.NORTH);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));

        JButton btnBook = new javax.swing.JButton("Thêm lịch đặt mới");
        btnBook.setIcon(Utils.IconUtils.getAddIcon(16));
        btnBook.addActionListener(e -> onQuickBook());
        pnlBtns.add(btnBook);

        JButton btnSell = new javax.swing.JButton("Bán dịch vụ");
        btnSell.setIcon(Utils.IconUtils.getOpenIcon(16));
        btnSell.addActionListener(e -> onQuickSell());
        pnlBtns.add(btnSell);

        JButton btnCheck = new javax.swing.JButton("Kiểm tra sân trống");
        btnCheck.setIcon(Utils.IconUtils.getCheckIcon(16));
        btnCheck.addActionListener(e -> onCheckSan());
        pnlBtns.add(btnCheck);

        JButton btnMaint = new javax.swing.JButton("Thêm lịch bảo trì");
        btnMaint.setIcon(Utils.IconUtils.getAddIcon(16));
        btnMaint.addActionListener(e -> onQuickMaint());
        pnlBtns.add(btnMaint);

        if (pageNavigator != null) {
            JButton btnNavDatLich = new javax.swing.JButton("Xem lịch đặt sân");
            btnNavDatLich.setIcon(Utils.IconUtils.getOpenIcon(16));
            btnNavDatLich.addActionListener(e -> pageNavigator.accept("datlich"));
            pnlBtns.add(btnNavDatLich);

            JButton btnNavDichVu = new javax.swing.JButton("Kho dịch vụ");
            btnNavDichVu.setIcon(Utils.IconUtils.getOpenIcon(16));
            btnNavDichVu.addActionListener(e -> pageNavigator.accept("dichvu"));
            pnlBtns.add(btnNavDichVu);
        }

        pnlQuickActionsCard.add(pnlBtns, BorderLayout.CENTER);
        pnlQuickActionsCard.revalidate();
        pnlQuickActionsCard.repaint();
    }

    /**
     * Custom Table Cell Renderer for Dashboard Today's Schedule Matrix.
     */
    private static class DashboardCalendarCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 0) {
                c.setFont(UIConstants.FONT_BOLD);
                c.setBackground(new Color(245, 247, 250));
                c.setForeground(UIConstants.PRIMARY);
                setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }

            String str = value != null ? value.toString().trim() : "";
            c.setFont(UIConstants.FONT_BOLD);
            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

            if (c instanceof javax.swing.JComponent jc) {
                String courtName = table.getValueAt(row, 0) != null ? table.getValueAt(row, 0).toString() : "Sân";
                String slotTime = column <= TIME_SLOTS.length ? TIME_SLOTS[column - 1] : "";
                jc.setToolTipText(courtName + " [" + slotTime + "]: " + str + " — Nhấp đúp để chọn");
            }

            if (str.startsWith("Đang đá")) {
                c.setBackground(new Color(219, 234, 254)); // Soft sky blue
                c.setForeground(new Color(37, 99, 235));   // Dark blue
            } else if (str.startsWith("Đã đặt")) {
                c.setBackground(new Color(254, 243, 199)); // Soft amber
                c.setForeground(new Color(217, 119, 6));   // Dark amber
            } else if (str.startsWith("Bảo trì")) {
                c.setBackground(new Color(241, 245, 249)); // Soft grey
                c.setForeground(new Color(100, 116, 139));  // Dark grey
            } else { // Trống
                c.setBackground(new Color(220, 252, 231)); // Soft emerald green
                c.setForeground(new Color(22, 163, 74));   // Dark green
            }

            return c;
        }
    }
}
