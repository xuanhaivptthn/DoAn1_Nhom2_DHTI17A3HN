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
 * Dashboard tổng quan — thiết kế đơn giản, sạch sẽ.
 * Stat cards → Ma trận khung giờ → Lịch hôm nay → Hành động nhanh.
 */
public class DashboardPanel extends JPanel {

    private Consumer<String> pageNavigator;

    // Stat card value labels
    private JLabel lblSanValue;
    private JLabel lblDatLichValue;
    private JLabel lblDoanhThuValue;

    private DefaultTableModel todayUpcomingBookingsModel;
    private DefaultTableModel todayCalendarModel;
    private JTable todayCalendarTable;

    private static final String[] TIME_SLOTS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    // Keep unused panel fields to not break GEN-BEGIN/END block
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlQuickActionsCard;
    private javax.swing.JPanel pnlSplit;
    private javax.swing.JPanel pnlStatsGrid;
    // End of variables declaration//GEN-END:variables

    public DashboardPanel() { this(null); }

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

    private void buildUI() {
        setBackground(UIConstants.BG);
        setLayout(new BorderLayout());

        // Page header — fixed at top
        add(buildHeader(), BorderLayout.NORTH);

        // Body — fills remaining space, no outer scroll
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(UIConstants.BG);
        body.setBorder(new EmptyBorder(14, 18, 14, 18));

        // TOP: stat cards row — fixed height
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(buildStatsRow(), BorderLayout.CENTER);
        top.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
        body.add(top, BorderLayout.NORTH);

        // CENTER: calendar + bookings stacked — fills all remaining height
        JPanel center = new JPanel(new GridLayout(2, 1, 0, 12));
        center.setOpaque(false);
        center.add(buildCalendarCard());
        center.add(buildBookingsCard());
        body.add(center, BorderLayout.CENTER);

        // SOUTH: quick actions — fixed height
        JPanel south = new JPanel(new BorderLayout(0, 0));
        south.setOpaque(false);
        south.setBorder(new EmptyBorder(12, 0, 0, 0));
        south.add(buildActionsCard(), BorderLayout.CENTER);
        body.add(south, BorderLayout.SOUTH);

        add(body, BorderLayout.CENTER);

        refresh();
    }

    // ─── HEADER ──────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 3));
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("Dashboard Tổng quan");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setIcon(Utils.IconUtils.getBallWhiteIcon(24));
        lblTitle.setIconTextGap(10);

        JLabel lblSub = new JLabel("Hôm nay: " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", java.util.Locale.of("vi"))));
        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(new Color(200, 230, 201));

        header.add(lblTitle, BorderLayout.NORTH);
        header.add(lblSub, BorderLayout.SOUTH);
        return header;
    }

    // ─── STAT CARDS ──────────────────────────────────────────────────────────

    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setOpaque(false);

        row.add(buildStatCard("Sân bóng", "—", Utils.IconUtils.getBallBlackIcon(18), UIConstants.PRIMARY));
        row.add(buildStatCard("Lịch đặt hôm nay", "—", Utils.IconUtils.getCalendarIcon(18), UIConstants.INFO));
        row.add(buildStatCard("Doanh thu đã thu", "—", Utils.IconUtils.getMoneyIcon(18), UIConstants.SUCCESS));

        return row;
    }

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

        // Store reference for updates
        if (label.startsWith("Sân")) lblSanValue = lblValue;
        else if (label.startsWith("Lịch")) lblDatLichValue = lblValue;
        else if (label.startsWith("Doanh")) lblDoanhThuValue = lblValue;

        card.add(lblLabel, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    // ─── CALENDAR MATRIX ─────────────────────────────────────────────────────

    private JPanel buildCalendarCard() {
        JPanel card = buildCard();

        // Title row
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

        // Table
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

        // Legend
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

    private JPanel buildActionsCard() {
        JPanel card = buildCard();

        JLabel title = new JLabel("Thao tác nhanh");
        title.setFont(UIConstants.FONT_SUBTITLE);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlBtns.setOpaque(false);

        pnlBtns.add(actionBtn("Thêm lịch đặt", Utils.IconUtils.getAddIcon(15), UIConstants.PRIMARY, e -> onQuickBook()));
        pnlBtns.add(actionBtn("Bán dịch vụ", Utils.IconUtils.getOpenIcon(15), new Color(14, 122, 200), e -> onQuickSell()));
        pnlBtns.add(actionBtn("Kiểm tra sân", Utils.IconUtils.getCheckIcon(15), UIConstants.SUCCESS, e -> onCheckSan()));
        pnlBtns.add(actionBtn("Tạo bảo trì", Utils.IconUtils.getMaintenanceIcon(15), UIConstants.WARNING, e -> onQuickMaint()));

        if (pageNavigator != null) {
            pnlBtns.add(Box.createHorizontalStrut(8));
            pnlBtns.add(plainBtn("→ Đặt lịch", e -> pageNavigator.accept("datlich")));
            pnlBtns.add(plainBtn("→ Dịch vụ", e -> pageNavigator.accept("dichvu")));
            pnlBtns.add(plainBtn("→ Kho hàng", e -> pageNavigator.accept("kho")));
        }

        card.add(pnlBtns, BorderLayout.CENTER);
        pnlQuickActionsCard = card; // keep reference
        return card;
    }

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

    private void applySimpleCellRenderer(JTable table) {
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setFont(UIConstants.FONT_NORMAL);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 251, 252));
                    c.setForeground(UIConstants.TEXT_PRIMARY);
                    String s = value != null ? value.toString() : "";
                    if (s.contains("Hoàn thành") || s.contains("Sẵn sàng")) setForeground(UIConstants.SUCCESS);
                    else if (s.contains("Chờ")) setForeground(UIConstants.WARNING);
                    else if (s.contains("Xác nhận") || s.contains("Đang")) setForeground(UIConstants.INFO);
                    else if (s.contains("Hủy")) setForeground(UIConstants.DANGER);
                }
                // Align
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

    public void refresh() {
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        List<DatLich> datLichs = DataStore.get().getDatLichs();
        String todayStr = LocalDate.now().toString();

        // Stat cards
        long sanReady   = sans.stream().filter(k -> "SanSang".equals(k.getTrangThai())).count();
        long sanRenting = sans.stream().filter(k -> "DangThue".equals(k.getTrangThai())).count();
        long pending    = datLichs.stream().filter(d -> "ChoXacNhan".equals(d.getTrangThai())).count();
        long active     = datLichs.stream().filter(d -> "DaXacNhan".equals(d.getTrangThai())).count();
        double revenue  = datLichs.stream()
                .filter(d -> "DaThanhToan".equals(d.getTrangThai()) || "HoanThanh".equals(d.getTrangThai()))
                .mapToDouble(DatLich::getTongTien).sum();

        if (lblSanValue     != null) lblSanValue.setText(sanReady + " sẵn sàng · " + sanRenting + " đang thuê");
        if (lblDatLichValue != null) lblDatLichValue.setText(pending + " chờ · " + active + " đã duyệt");
        if (lblDoanhThuValue!= null) lblDoanhThuValue.setText(String.format("%,.0f đ", revenue));

        // Today bookings table
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

        // Calendar matrix
        if (todayCalendarModel != null) {
            todayCalendarModel.setRowCount(0);
            for (KhuVucSan san : sans) {
                Object[] row = new Object[TIME_SLOTS.length + 1];
                row[0] = san.getTenSan();

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

    private void onQuickBookSlot(int courtIndex, int timeSlotIndex) {
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        if (courtIndex < 0 || courtIndex >= sans.size()) return;
        KhuVucSan san = sans.get(courtIndex);
        if ("BaoTri".equalsIgnoreCase(san.getTrangThai())) {
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

    private void onQuickBook() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DatLichFormDialog dialog = new DatLichFormDialog(parent, (DatLich) null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;
        saveNewBooking(dialog.getResult(), dialog.getSelectedSan());
    }

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

    private void onCheckSan() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        new KiemTraSanDialog(parent).setVisible(true);
    }

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

    private static int toMinutes(String t) {
        if (t == null || !t.contains(":")) return 0;
        try { String[] p = t.trim().split(":"); return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]); }
        catch (Exception e) { return 0; }
    }

    private static String getNextHour(String t) {
        try { return String.format("%02d:00", Integer.parseInt(t.split(":")[0]) + 1); }
        catch (Exception e) { return t; }
    }

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

    private class CalendarCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(0, 4, 0, 4));
            setFont(UIConstants.FONT_NORMAL);

            if (column == 0) {
                setFont(UIConstants.FONT_BOLD);
                setHorizontalAlignment(LEFT);
                if (!isSelected) { c.setBackground(new Color(248, 249, 250)); c.setForeground(UIConstants.TEXT_PRIMARY); }
                return c;
            }

            setHorizontalAlignment(CENTER);
            String str = value != null ? value.toString().trim() : "";

            if (c instanceof JComponent jc) {
                String court = table.getValueAt(row, 0) != null ? table.getValueAt(row, 0).toString() : "";
                String slot  = column <= TIME_SLOTS.length ? TIME_SLOTS[column - 1] : "";
                jc.setToolTipText(court + " [" + slot + "]: " + str + " — Nhấp đúp để đặt");
            }

            if (!isSelected) {
                if      (str.startsWith("Đang đá")) { c.setBackground(new Color(219, 234, 254)); c.setForeground(new Color(37,  99,  235)); }
                else if (str.startsWith("Đã đặt"))  { c.setBackground(new Color(254, 243, 199)); c.setForeground(new Color(217, 119, 6));   }
                else if (str.startsWith("Bảo trì")) { c.setBackground(new Color(241, 245, 249)); c.setForeground(new Color(100, 116, 139)); }
                else                                 { c.setBackground(new Color(240, 253, 244)); c.setForeground(new Color(22,  163, 74));  }
            }
            return c;
        }
    }
}
