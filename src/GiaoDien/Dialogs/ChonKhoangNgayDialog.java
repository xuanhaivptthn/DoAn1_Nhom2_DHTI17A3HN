package GiaoDien.Dialogs;

import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dialog chọn Khoảng thời gian cụ thể (Từ ngày ... Đến ngày) cho Báo cáo tài chính và thống kê.
 * <p>
 * Hỗ trợ chọn nhanh bằng nút (7 ngày qua, 30 ngày qua, Tháng này) hoặc chọn ngày chi tiết qua ChonNgayDialog.
 * Tiến hành kiểm tra ràng buộc Từ ngày phải nhỏ hơn hoặc bằng Đến ngày.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChonKhoangNgayDialog extends JDialog {

    /** Định dạng ngày ISO yyyy-MM-dd */
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Ô hiển thị/nhập ngày bắt đầu */
    private JTextField txtFromDate;

    /** Ô hiển thị/nhập ngày kết thúc */
    private JTextField txtToDate;

    /** Giá trị Từ ngày (LocalDate) */
    private LocalDate fromDate;

    /** Giá trị Đến ngày (LocalDate) */
    private LocalDate toDate;

    /** Cờ xác nhận đã bấm nút Đồng ý */
    private boolean confirmed = false;

    /**
     * Khởi tạo thoại chọn khoảng thời gian.
     *
     * @param parent      Cửa sổ cha (JFrame)
     * @param initialFrom Ngày bắt đầu mặc định (nếu null lấy 7 ngày trước)
     * @param initialTo   Ngày kết thúc mặc định (nếu null lấy ngày hiện tại)
     */
    public ChonKhoangNgayDialog(JFrame parent, LocalDate initialFrom, LocalDate initialTo) {
        super(parent, "Chọn Khoảng Thời Gian Báo Cáo", true);
        this.fromDate = initialFrom != null ? initialFrom : LocalDate.now().minusDays(7);
        this.toDate = initialTo != null ? initialTo : LocalDate.now();
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Bố trí giao diện người dùng dialog và gắn các bộ lắng nghe sự kiện.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // ── 1. Header Panel ──────────────────────────────────────────────────
        JPanel pnlHeader = PageUI.createPageHeader("Chọn Khoảng Thời Gian Báo Cáo",
                "Vui lòng chọn Từ ngày đến Đến ngày để tổng hợp báo cáo tài chính");
        add(pnlHeader, BorderLayout.NORTH);

        // ── 2. Card Content Panel ────────────────────────────────────────────
        JPanel pnlCard = new JPanel(new GridBagLayout());
        pnlCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        pnlCard.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Trường "Từ ngày"
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        pnlCard.add(new JLabel("Từ ngày (yyyy-MM-dd) *"), gbc);

        txtFromDate = new JTextField(14);
        txtFromDate.setFont(UIConstants.FONT_NORMAL);
        txtFromDate.setText(fromDate.format(fmt));
        txtFromDate.setPreferredSize(new Dimension(180, 34));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        pnlCard.add(txtFromDate, gbc);

        // Nút mở lịch chọn cho Từ ngày
        JButton btnPickFrom = new JButton();
        btnPickFrom.setIcon(Utils.IconUtils.getCalendarIcon(16));
        btnPickFrom.setPreferredSize(new Dimension(45, 34));
        btnPickFrom.addActionListener(e -> {
            ChonNgayDialog dialog = new ChonNgayDialog((JFrame) getOwner(), fromDate);
            dialog.setVisible(true);
            if (dialog.isConfirmed() && dialog.getSelectedDate() != null) {
                fromDate = dialog.getSelectedDate();
                txtFromDate.setText(fromDate.format(fmt));
            }
        });
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0;
        pnlCard.add(btnPickFrom, gbc);

        txtFromDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnPickFrom.doClick();
            }
        });

        // Trường "Đến ngày"
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        pnlCard.add(new JLabel("Đến ngày (yyyy-MM-dd) *"), gbc);

        txtToDate = new JTextField(14);
        txtToDate.setFont(UIConstants.FONT_NORMAL);
        txtToDate.setText(toDate.format(fmt));
        txtToDate.setPreferredSize(new Dimension(180, 34));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.65;
        pnlCard.add(txtToDate, gbc);

        // Nút mở lịch chọn cho Đến ngày
        JButton btnPickTo = new JButton();
        btnPickTo.setIcon(Utils.IconUtils.getCalendarIcon(16));
        btnPickTo.setPreferredSize(new Dimension(45, 34));
        btnPickTo.addActionListener(e -> {
            ChonNgayDialog dialog = new ChonNgayDialog((JFrame) getOwner(), toDate);
            dialog.setVisible(true);
            if (dialog.isConfirmed() && dialog.getSelectedDate() != null) {
                toDate = dialog.getSelectedDate();
                txtToDate.setText(toDate.format(fmt));
            }
        });
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.0;
        pnlCard.add(btnPickTo, gbc);

        txtToDate.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnPickTo.doClick();
            }
        });

        // ── 3. Quick Buttons Panel (Chọn nhanh khoảng thời gian) ────────────
        JPanel pnlQuick = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        pnlQuick.setOpaque(false);

        // Chọn nhanh 7 ngày qua
        JButton btn7Days = new JButton("7 ngày qua");
        PageUI.styleSecondaryButton(btn7Days);
        btn7Days.addActionListener(e -> {
            toDate = LocalDate.now();
            fromDate = toDate.minusDays(7);
            txtFromDate.setText(fromDate.format(fmt));
            txtToDate.setText(toDate.format(fmt));
        });

        // Chọn nhanh 30 ngày qua
        JButton btn30Days = new JButton("30 ngày qua");
        PageUI.styleSecondaryButton(btn30Days);
        btn30Days.addActionListener(e -> {
            toDate = LocalDate.now();
            fromDate = toDate.minusDays(30);
            txtFromDate.setText(fromDate.format(fmt));
            txtToDate.setText(toDate.format(fmt));
        });

        // Chọn nhanh Tháng này
        JButton btnThisMonth = new JButton("Tháng này");
        PageUI.styleSecondaryButton(btnThisMonth);
        btnThisMonth.addActionListener(e -> {
            toDate = LocalDate.now();
            fromDate = toDate.withDayOfMonth(1);
            txtFromDate.setText(fromDate.format(fmt));
            txtToDate.setText(toDate.format(fmt));
        });

        pnlQuick.add(btn7Days);
        pnlQuick.add(btn30Days);
        pnlQuick.add(btnThisMonth);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        gbc.insets = new Insets(12, 0, 4, 0);
        pnlCard.add(pnlQuick, gbc);

        add(pnlCard, BorderLayout.CENTER);

        // ── 4. Footer Buttons Panel ──────────────────────────────────────────
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlFooter.setBackground(UIConstants.BG);

        JButton btnCancel = new JButton("Hủy");
        PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnConfirm = new JButton("Đồng ý");
        PageUI.stylePrimaryButton(btnConfirm);
        btnConfirm.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnConfirm);
        add(pnlFooter, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnConfirm);
    }

    /**
     * Xử lý xác nhận dữ liệu khoảng thời gian đã nhập.
     * Validate định dạng ngày YYYY-MM-DD và kiểm tra logic Từ ngày <= Đến ngày.
     */
    private void onConfirm() {
        try {
            fromDate = LocalDate.parse(txtFromDate.getText().trim(), fmt);
            toDate = LocalDate.parse(txtToDate.getText().trim(), fmt);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng yyyy-MM-dd.",
                    "Lỗi ngày tháng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fromDate.isAfter(toDate)) {
            JOptionPane.showMessageDialog(this, "Từ ngày không được lớn hơn Đến ngày!",
                    "Lỗi ngày tháng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    /**
     * Kiểm tra trạng thái đã bấm nút Đồng ý thành công hay chưa.
     *
     * @return true nếu người dùng chọn ngày hợp lệ và xác nhận
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Lấy giá trị ngày bắt đầu (Từ ngày).
     *
     * @return Đối tượng LocalDate đại diện cho Từ ngày
     */
    public LocalDate getFromDate() {
        return fromDate;
    }

    /**
     * Lấy giá trị ngày kết thúc (Đến ngày).
     *
     * @return Đối tượng LocalDate đại diện cho Đến ngày
     */
    public LocalDate getToDate() {
        return toDate;
    }
}
