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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
            "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"
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
        pnlSplit.setLayout(new java.awt.GridLayout(1, 2, 16, 0));

        String todayFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // 1. Left Column: Today's Upcoming Bookings (Lịch đặt sắp tới trong ngày)
        JPanel cardLeft = new javax.swing.JPanel();
        cardLeft.setLayout(new BorderLayout(0, 8));
        JLabel titleLeft = new JLabel("Lịch đặt sắp tới trong ngày (" + todayFormatted + ")");
        titleLeft.setFont(UIConstants.FONT_SUBTITLE);
        titleLeft.setForeground(UIConstants.PRIMARY);
        cardLeft.add(titleLeft, BorderLayout.NORTH);

        todayUpcomingBookingsModel = new DefaultTableModel(
                new String[]{"Mã", "Sân bóng", "Khách hàng", "SĐT", "Khung giờ", "Tổng tiền", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableBookings = new JTable(todayUpcomingBookingsModel);
        PageUI.styleTable(tableBookings);
        tableBookings.getColumnModel().getColumn(0).setMaxWidth(60);
        cardLeft.add(new javax.swing.JScrollPane(tableBookings), BorderLayout.CENTER);
        pnlSplit.add(cardLeft);

        // 2. Right Column: Today's Schedule Matrix Calendar
        JPanel cardRight = new javax.swing.JPanel();
        cardRight.setLayout(new BorderLayout(0, 8));

        JLabel titleRight = new JLabel("Ma trận khung giờ hôm nay (" + todayFormatted + ")");
        titleRight.setFont(UIConstants.FONT_SUBTITLE);
        titleRight.setForeground(UIConstants.PRIMARY);

        JButton btnGoDatLich = new JButton("Chi tiết ➔");
        btnGoDatLich.setFont(UIConstants.FONT_SMALL);
        if (pageNavigator != null) {
            btnGoDatLich.addActionListener(e -> pageNavigator.accept("datlich"));
        }

        JPanel pnlRightHeader = new JPanel(new BorderLayout());
        pnlRightHeader.setOpaque(false);
        pnlRightHeader.add(titleRight, BorderLayout.WEST);
        pnlRightHeader.add(btnGoDatLich, BorderLayout.EAST);
        cardRight.add(pnlRightHeader, BorderLayout.NORTH);

        String[] slots = new String[]{"Khu vực sân", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
        todayCalendarModel = new DefaultTableModel(slots, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        todayCalendarTable = new JTable(todayCalendarModel);
        PageUI.styleTable(todayCalendarTable);
        todayCalendarTable.getColumnModel().getColumn(0).setPreferredWidth(95);
        todayCalendarTable.setDefaultRenderer(Object.class, new DashboardCalendarCellRenderer());

        // CLICK ON EMPTY SLOT TO QUICKLY CREATE SCHEDULE
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

        cardRight.add(new javax.swing.JScrollPane(todayCalendarTable), BorderLayout.CENTER);
        pnlSplit.add(cardRight);
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

        int nextId = DataStore.get().getDatLichs().stream().mapToInt(DatLich::getId).max().orElse(0) + 1;
        String ma = String.format("DL%03d", nextId);
        String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getHoTen() : "Hệ thống";

        DatLich phieu = new DatLich(nextId, ma, selectedSan.getId(), selectedSan.getTenSan(), form.getTenKhach(), form.getSoDienThoai(),
                form.getNgayDat(), form.getGioBatDau(), form.getGioKetThuc(), form.getTongTien(), "ChoXacNhan", nv, form.getGhiChu());
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
                    d.getMaPhieu(), d.getTenSan(), d.getTenKhach(), d.getSoDienThoai(),
                    d.getKhungGio(), String.format("%,.0f VNĐ", (double) (d.getTongTien())), d.getTrangThaiHienThi()
            });
        }

        if (todayUpcomingList.isEmpty()) {
            todayUpcomingBookingsModel.addRow(new Object[]{
                    "-", "✓ Hôm nay chưa có lịch đặt nào", "-", "-", "-", "-", "-"
            });
        }

        // 2. Refresh Today Calendar Matrix Table
        todayCalendarModel.setRowCount(0);

        for (KhuVucSan san : sans) {
            Object[] row = new Object[TIME_SLOTS.length + 1];
            row[0] = san.getTenSan();

            BaoTri maint = DataStore.get().getBaoTris().stream()
                    .filter(b -> b.getKhuVucId() == san.getId()
                            && !"DaHuy".equalsIgnoreCase(b.getTrangThai())
                            && !"Huy".equalsIgnoreCase(b.getTrangThai())
                            && isDateInMaintenanceRange(todayStr, b.getNgayBatDau(), b.getNgayKetThuc()))
                    .findFirst().orElse(null);

            if (maint != null || "BaoTri".equalsIgnoreCase(san.getTrangThai())) {
                for (int t = 0; t < TIME_SLOTS.length; t++) {
                    row[t + 1] = "🔧 Bảo trì";
                }
            } else {
                for (int t = 0; t < TIME_SLOTS.length; t++) {
                    String slotTime = TIME_SLOTS[t];
                    int slotMin = toMinutes(slotTime);

                    DatLich found = null;
                    for (DatLich d : datLichs) {
                        if (d.getKhuVucId() == san.getId()
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
                        row[t + 1] = "✓ Trống";
                    } else if ("ChoXacNhan".equalsIgnoreCase(found.getTrangThai())) {
                        row[t + 1] = "🟡 " + found.getTenKhach() + " (" + found.getGioBatDau() + " - " + found.getGioKetThuc() + ")";
                    } else {
                        row[t + 1] = "🔴 " + found.getTenKhach() + " (" + found.getGioBatDau() + " - " + found.getGioKetThuc() + ")";
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

        int nextId = DataStore.get().getDatLichs().stream().mapToInt(DatLich::getId).max().orElse(0) + 1;
        String ma = String.format("DL%03d", nextId);
        String nv = SessionManager.get().getCurrentUser() != null ? SessionManager.get().getCurrentUser().getHoTen() : "Hệ thống";

        DatLich phieu = new DatLich(nextId, ma, san.getId(), san.getTenSan(), form.getTenKhach(), form.getSoDienThoai(),
                form.getNgayDat(), form.getGioBatDau(), form.getGioKetThuc(), form.getTongTien(), "ChoXacNhan", nv, form.getGhiChu());
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
            JOptionPane.showMessageDialog(this, "Đã bán " + qty + "x " + dv.getTenDichVu() + " cho phiếu " + selectedBooking.getMaPhieu(),
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

            int nextId = DataStore.get().getBaoTris().stream().mapToInt(BaoTri::getId).max().orElse(0) + 1;
            String ma = String.format("BT%03d", nextId);

            BaoTri record = new BaoTri(nextId, ma, san != null ? san.getId() : 1, san != null ? san.getTenSan() : "Sân 1",
                    bt.getNoiDung(), bt.getNguoiPhuTrach(), bt.getNgayBatDau(), bt.getNgayKetThuc(), bt.getChiPhi(), "ChoXuLy");

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
                return c;
            }

            String str = value != null ? value.toString() : "";
            c.setFont(UIConstants.FONT_SMALL);
            if (c instanceof javax.swing.JComponent jc) {
                jc.setToolTipText(str);
            }

            if (str.startsWith("🔴")) {
                c.setBackground(new Color(255, 235, 235));
                c.setForeground(new Color(180, 40, 40));
            } else if (str.startsWith("🟡")) {
                c.setBackground(new Color(255, 248, 220));
                c.setForeground(new Color(180, 100, 0));
            } else if (str.startsWith("🔧")) {
                c.setBackground(new Color(240, 240, 240));
                c.setForeground(new Color(120, 120, 120));
            } else {
                c.setBackground(new Color(238, 250, 240));
                c.setForeground(new Color(30, 130, 60));
            }

            return c;
        }
    }
}
