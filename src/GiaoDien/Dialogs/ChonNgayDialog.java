package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.DatLich;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dialog chọn nhanh ngày bằng Bảng chọn lịch cả tháng (Full Month Calendar Picker).
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class ChonNgayDialog extends JDialog {

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private boolean confirmed;

    private javax.swing.JComboBox<String> cboMonth;
    private javax.swing.JComboBox<Integer> cboYear;
    private JLabel lblSelectedPreview;
    private JTable tableCalendar;
    private DefaultTableModel modelCalendar;

    private LocalDate[][] gridDates = new LocalDate[6][7];

    private static final DateTimeFormatter FMT_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FMT_DISPLAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public ChonNgayDialog() {
        this(null, LocalDate.now());
    }

    public ChonNgayDialog(JFrame parent, LocalDate currentDate) {
        super(parent, "Chọn ngày xem lịch - Lịch tháng", true);
        this.selectedDate = currentDate != null ? currentDate : LocalDate.now();
        this.currentYearMonth = YearMonth.from(selectedDate);

        initComponents();
        customInit(parent);
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        pnlCenterWrap = new javax.swing.JPanel();
        pnlFormCard = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chọn ngày xem lịch - Lịch tháng");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Chọn ngày xem lịch (Lịch tháng)");
        pnlHeader.add(lblHeaderTitle, java.awt.BorderLayout.WEST);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenterWrap.setBackground(UIConstants.BG);
        pnlCenterWrap.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 8, 16));
        pnlCenterWrap.setLayout(new java.awt.BorderLayout());

        pnlFormCard.setLayout(new java.awt.BorderLayout(0, 8));
        pnlCenterWrap.add(pnlFormCard, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlCenterWrap, java.awt.BorderLayout.CENTER);

        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 12, 16));
        pnlFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 12));
        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit(JFrame parent) {
        java.awt.Rectangle maxBounds = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int targetWidth = 540;
        int targetHeight = 580;
        if (maxBounds.height < 650) {
            targetHeight = (int) (maxBounds.height * 0.95);
        }
        setSize(targetWidth, targetHeight);
        if (parent != null) setLocationRelativeTo(parent);

        // Top Month Navigator Bar
        JPanel pnlMonthNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        pnlMonthNav.setOpaque(false);

        JButton btnPrevYear = new javax.swing.JButton();
        btnPrevYear.setIcon(Utils.IconUtils.getFirstIcon(16));
        btnPrevYear.setToolTipText("Năm trước");
        btnPrevYear.setPreferredSize(new Dimension(50, 32));
        btnPrevYear.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusYears(1);
            updateMonthGrid();
        });

        JButton btnPrevMonth = new javax.swing.JButton();
        btnPrevMonth.setIcon(Utils.IconUtils.getPrevIcon(16));
        btnPrevMonth.setToolTipText("Tháng trước");
        btnPrevMonth.setPreferredSize(new Dimension(45, 32));
        btnPrevMonth.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateMonthGrid();
        });

        // Quick Month ComboBox
        String[] months = {"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        cboMonth = new javax.swing.JComboBox<>(months);
        cboMonth.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cboMonth.setBackground(Color.WHITE);
        cboMonth.setPreferredSize(new Dimension(115, 32));

        // Quick Year ComboBox (2020 -> 2035)
        Integer[] years = new Integer[16];
        for (int i = 0; i < 16; i++) years[i] = 2020 + i;
        cboYear = new javax.swing.JComboBox<>(years);
        cboYear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cboYear.setBackground(Color.WHITE);
        cboYear.setPreferredSize(new Dimension(90, 32));

        cboMonth.addActionListener(e -> {
            int m = cboMonth.getSelectedIndex() + 1;
            int y = currentYearMonth.getYear();
            currentYearMonth = YearMonth.of(y, m);
            updateMonthGrid();
        });

        cboYear.addActionListener(e -> {
            if (cboYear.getSelectedItem() != null) {
                int y = (Integer) cboYear.getSelectedItem();
                int m = currentYearMonth.getMonthValue();
                currentYearMonth = YearMonth.of(y, m);
                updateMonthGrid();
            }
        });

        JButton btnNextMonth = new javax.swing.JButton();
        btnNextMonth.setIcon(Utils.IconUtils.getNextIcon(16));
        btnNextMonth.setToolTipText("Tháng sau");
        btnNextMonth.setPreferredSize(new Dimension(45, 32));
        btnNextMonth.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateMonthGrid();
        });

        JButton btnNextYear = new javax.swing.JButton();
        btnNextYear.setIcon(Utils.IconUtils.getLastIcon(16));
        btnNextYear.setToolTipText("Năm sau");
        btnNextYear.setPreferredSize(new Dimension(50, 32));
        btnNextYear.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusYears(1);
            updateMonthGrid();
        });

        pnlMonthNav.add(btnPrevYear);
        pnlMonthNav.add(btnPrevMonth);
        pnlMonthNav.add(cboMonth);
        pnlMonthNav.add(cboYear);
        pnlMonthNav.add(btnNextMonth);
        pnlMonthNav.add(btnNextYear);

        // Quick Presets Bar
        JPanel pnlPresets = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        pnlPresets.setOpaque(false);

        JButton btnToday = new javax.swing.JButton("Hôm nay");
        btnToday.addActionListener(e -> selectDate(LocalDate.now()));

        JButton btnTomorrow = new javax.swing.JButton("Ngày mai");
        btnTomorrow.addActionListener(e -> selectDate(LocalDate.now().plusDays(1)));

        pnlPresets.add(btnToday);
        pnlPresets.add(btnTomorrow);

        JPanel pnlTopWrap = new JPanel(new BorderLayout(0, 6));
        pnlTopWrap.setOpaque(false);
        pnlTopWrap.add(pnlMonthNav, BorderLayout.NORTH);
        pnlTopWrap.add(pnlPresets, BorderLayout.SOUTH);

        pnlFormCard.add(pnlTopWrap, BorderLayout.NORTH);

        // Calendar Month Table Grid
        String[] dayHeaders = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        modelCalendar = new DefaultTableModel(dayHeaders, 6) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableCalendar = new JTable(modelCalendar);
        tableCalendar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableCalendar.setRowHeight(38);
        tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCalendar.setCellSelectionEnabled(true);
        tableCalendar.setShowGrid(true);
        tableCalendar.setGridColor(new Color(230, 230, 230));

        JTableHeader th = tableCalendar.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(UIConstants.PRIMARY);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 32));
        th.setReorderingAllowed(false);

        tableCalendar.setDefaultRenderer(Object.class, new CalendarCellRenderer());

        tableCalendar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tableCalendar.getSelectedRow();
                int c = tableCalendar.getSelectedColumn();
                if (r >= 0 && c >= 0 && gridDates[r][c] != null) {
                    selectDate(gridDates[r][c]);
                }
            }
        });

        javax.swing.JScrollPane spCalendar = new javax.swing.JScrollPane(tableCalendar);
        spCalendar.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        spCalendar.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pnlFormCard.add(spCalendar, BorderLayout.CENTER);

        // Bottom Selected Preview Bar
        JPanel pnlBottomPreview = new JPanel(new BorderLayout());
        pnlBottomPreview.setOpaque(false);
        pnlBottomPreview.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));

        lblSelectedPreview = new JLabel("Đã chọn: -");
        lblSelectedPreview.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSelectedPreview.setForeground(UIConstants.PRIMARY_DARK);

        JLabel legendNote = new JLabel(" Ngày có lịch đặt sân");
        legendNote.setIcon(Utils.IconUtils.getDotIcon(12));
        legendNote.setFont(UIConstants.FONT_SMALL);
        legendNote.setForeground(UIConstants.TEXT_SECONDARY);

        pnlBottomPreview.add(lblSelectedPreview, BorderLayout.WEST);
        pnlBottomPreview.add(legendNote, BorderLayout.EAST);

        pnlFormCard.add(pnlBottomPreview, BorderLayout.SOUTH);

        // Footer buttons
        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnConfirm = new javax.swing.JButton("Đồng ý");
        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnConfirm);

        updateMonthGrid();
        getRootPane().setDefaultButton(btnConfirm);
    }

    private boolean isUpdatingGrid = false;

    private void updateMonthGrid() {
        if (isUpdatingGrid) return;
        isUpdatingGrid = true;
        try {
            if (cboMonth != null) cboMonth.setSelectedIndex(currentYearMonth.getMonthValue() - 1);
            if (cboYear != null) cboYear.setSelectedItem(currentYearMonth.getYear());

            LocalDate firstOfMonth = currentYearMonth.atDay(1);
            int dayOfWeekVal = firstOfMonth.getDayOfWeek().getValue(); // 1 = Mon, ..., 7 = Sun
            LocalDate startDate = firstOfMonth.minusDays(dayOfWeekVal - 1);

            List<DatLich> allBookings = DataStore.get().getDatLichs();

            LocalDate curr = startDate;
            for (int r = 0; r < 6; r++) {
                for (int c = 0; c < 7; c++) {
                    gridDates[r][c] = curr;
                    final String dStr = curr.toString();

                    boolean hasBooking = allBookings.stream().anyMatch(b -> dStr.equals(b.getNgayDat()) && !"DaHuy".equals(b.getTrangThai()));
                    String cellText = String.valueOf(curr.getDayOfMonth()) + (hasBooking ? " *" : "");
                    modelCalendar.setValueAt(cellText, r, c);

                    curr = curr.plusDays(1);
                }
            }

            updatePreviewLabel();
            if (tableCalendar != null) tableCalendar.repaint();
        } finally {
            isUpdatingGrid = false;
        }
    }

    private void selectDate(LocalDate date) {
        this.selectedDate = date;
        if (!YearMonth.from(date).equals(currentYearMonth)) {
            this.currentYearMonth = YearMonth.from(date);
            updateMonthGrid();
        } else {
            updatePreviewLabel();
            tableCalendar.repaint();
        }
    }

    private void updatePreviewLabel() {
        String dayOfWeekVN = switch (selectedDate.getDayOfWeek()) {
            case MONDAY -> "Thứ 2";
            case TUESDAY -> "Thứ 3";
            case WEDNESDAY -> "Thứ 4";
            case THURSDAY -> "Thứ 5";
            case FRIDAY -> "Thứ 6";
            case SATURDAY -> "Thứ 7";
            case SUNDAY -> "Chủ Nhật";
        };
        lblSelectedPreview.setText("Đã chọn: " + selectedDate.format(FMT_DISPLAY) + " (" + dayOfWeekVN + ")");
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    /**
     * Custom Cell Renderer cho Bảng Lịch Tháng.
     */
    private class CalendarCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 14));

            LocalDate date = gridDates[row][col];
            if (date == null) return c;

            boolean isCurrentMonth = date.getMonth().equals(currentYearMonth.getMonth());
            boolean isSelectedDate = date.equals(selectedDate);
            boolean isToday = date.equals(LocalDate.now());

            if (isSelectedDate) {
                c.setBackground(new Color(37, 99, 235)); // Primary Blue
                c.setForeground(Color.WHITE);
            } else if (isToday) {
                c.setBackground(new Color(220, 252, 231)); // Light Green
                c.setForeground(new Color(22, 163, 74));
            } else if (isCurrentMonth) {
                c.setBackground(Color.WHITE);
                c.setForeground(new Color(30, 41, 59)); // Slate Dark
            } else {
                c.setBackground(new Color(245, 245, 245));
                c.setForeground(new Color(160, 160, 160)); // Muted Gray
            }

            return c;
        }
    }
}
