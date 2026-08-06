package GiaoDien.Panels;

import Utils.SessionManager;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Màn hình đăng nhập phiên làm việc hệ thống (LoginPanel).
 * <p>
 * Cho phép người dùng nhập tên tài khoản, mật khẩu và lựa chọn nguồn kết nối dữ liệu
 * (CSDL MySQL kết nối XAMPP hoặc Dữ liệu mẫu lưu trữ trên bộ nhớ in-memory DataStore).
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class LoginPanel extends javax.swing.JPanel {

    /**
     * Hàm callback xử lý khi đăng nhập thông tin tài khoản thành công.
     */
    private Consumer<Void> onLoginSuccess;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nút thực hiện xác thực đăng nhập */
    private javax.swing.JButton btnLogin;
    /** ComboBox chọn nguồn dữ liệu kết nối (MySQL hoặc In-Memory DataStore) */
    private javax.swing.JComboBox<String> cboDataSource;
    /** Nhãn hiển thị thông báo lỗi khi xác thực thất bại */
    private javax.swing.JLabel lblError;
    /** Nhãn chân trang thông tin hệ thống */
    private javax.swing.JLabel lblFooter;
    /** Nhãn gợi ý tài khoản đăng nhập demo hệ thống */
    private javax.swing.JLabel lblHint;
    /** Nhãn chứa biểu tượng quả bóng logo */
    private javax.swing.JLabel lblIcon;
    /** Nhãn tiêu đề ô nhập mật khẩu */
    private javax.swing.JLabel lblPassLabel;
    /** Nhãn tiêu đề phụ màn hình đăng nhập */
    private javax.swing.JLabel lblSub;
    /** Nhãn tiêu đề chính của hệ thống */
    private javax.swing.JLabel lblTitle;
    /** Nhãn tiêu đề ô nhập tên đăng nhập */
    private javax.swing.JLabel lblUserLabel;
    /** Panel hình thẻ bo góc chứa form đăng nhập */
    private javax.swing.JPanel pnlCard;
    /** Panel căn giữa nội dung màn hình */
    private javax.swing.JPanel pnlCenter;
    /** Panel chứa các trường nhập liệu form đăng nhập */
    private javax.swing.JPanel pnlForm;
    /** Panel chứa thông tin gợi ý đăng nhập */
    private javax.swing.JPanel pnlHint;
    /** Panel chứa tiêu đề chính và phụ */
    private javax.swing.JPanel pnlTitles;
    /** Panel chứa logo và tiêu đề phía trên thẻ */
    private javax.swing.JPanel pnlTop;
    /** Ô nhập mật khẩu người dùng */
    private javax.swing.JPasswordField txtPass;
    /** Ô nhập tên đăng nhập người dùng */
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo màn hình đăng nhập mặc định.
     */
    public LoginPanel() {
        this(null);
    }

    /**
     * Khởi tạo màn hình đăng nhập với callback thành công.
     * 
     * @param onLoginSuccess Hàm thực thi sau khi đăng nhập tài khoản thành công
     */
    public LoginPanel(Consumer<Void> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Khởi tạo các thành phần giao diện được thiết kế từ giao diện kéo thả NetBeans.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlCenter = new javax.swing.JPanel();
        // Tùy biến vẽ khung pnlCard với hình chữ nhật bo tròn góc
        pnlCard = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        pnlTop = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        pnlTitles = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        lblUserLabel = new javax.swing.JLabel("Tên đăng nhập");
        txtUser = new javax.swing.JTextField();
        lblPassLabel = new javax.swing.JLabel("Mật khẩu");
        txtPass = new javax.swing.JPasswordField();
        lblError = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton("Đăng nhập");
        pnlHint = new javax.swing.JPanel();
        lblHint = new javax.swing.JLabel();
        lblFooter = new javax.swing.JLabel();

        setBackground(UIConstants.PRIMARY_DARK);
        setLayout(new java.awt.BorderLayout());

        pnlCenter.setOpaque(false);
        pnlCenter.setLayout(new java.awt.GridBagLayout());

        pnlCard.setOpaque(false);
        pnlCard.setPreferredSize(new java.awt.Dimension(430, 550));
        pnlCard.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 28));
        pnlCard.setLayout(new java.awt.BorderLayout());

        pnlTop.setOpaque(false);
        pnlTop.setLayout(new java.awt.BorderLayout(0, 4));

        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        javax.swing.ImageIcon ballIcon = Utils.IconUtils.getBallIcon(48);
        if (ballIcon != null) {
            lblIcon.setIcon(ballIcon);
            lblIcon.setText("");
        } else {
            lblIcon.setFont(new java.awt.Font("Segoe UI", 1, 22));
            lblIcon.setText("SAN BONG MANAGER");
        }
        pnlTop.add(lblIcon, java.awt.BorderLayout.NORTH);

        pnlTitles.setOpaque(false);
        pnlTitles.setLayout(new java.awt.BorderLayout(0, 2));

        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.PRIMARY);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Cho Thuê Sân Bóng");
        pnlTitles.add(lblTitle, java.awt.BorderLayout.NORTH);

        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(UIConstants.TEXT_SECONDARY);
        lblSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSub.setText("Đăng nhập vào hệ thống");
        pnlTitles.add(lblSub, java.awt.BorderLayout.SOUTH);

        pnlTop.add(pnlTitles, java.awt.BorderLayout.CENTER);

        pnlCard.add(pnlTop, java.awt.BorderLayout.NORTH);

        pnlForm.setOpaque(false);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        pnlForm.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        pnlForm.add(lblUserLabel, gridBagConstraints);

        txtUser.setPreferredSize(new java.awt.Dimension(0, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlForm.add(txtUser, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 2, 0);
        pnlForm.add(lblPassLabel, gridBagConstraints);

        txtPass.setPreferredSize(new java.awt.Dimension(0, 36));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        pnlForm.add(txtPass, gridBagConstraints);

        JLabel lblModeLabel = new JLabel("Nguồn dữ liệu kết nối:");
        lblModeLabel.setFont(UIConstants.FONT_SMALL);
        lblModeLabel.setForeground(UIConstants.TEXT_SECONDARY);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 2, 0);
        pnlForm.add(lblModeLabel, gridBagConstraints);

        cboDataSource = new javax.swing.JComboBox<>(new String[]{
                "CSDL MySQL (DAO / XAMPP)",
                "Dữ liệu mẫu (DataStore / In-Memory)"
        });
        cboDataSource.setFont(UIConstants.FONT_SMALL);
        cboDataSource.setPreferredSize(new java.awt.Dimension(0, 34));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlForm.add(cboDataSource, gridBagConstraints);

        lblError.setFont(UIConstants.FONT_SMALL);
        lblError.setForeground(UIConstants.DANGER);
        lblError.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        pnlForm.add(lblError, gridBagConstraints);

        btnLogin.setPreferredSize(new java.awt.Dimension(0, 38));
        Utils.PageUI.stylePrimaryButton(btnLogin);
        btnLogin.addActionListener(e -> doLogin());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlForm.add(btnLogin, gridBagConstraints);

        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlHint.setOpaque(false);
        pnlHint.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        pnlHint.setLayout(new java.awt.BorderLayout());

        lblHint.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblHint.setForeground(UIConstants.TEXT_SECONDARY);
        lblHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHint.setText("<html><center><span style='color:#64748b;'>Demo:</span> <b>admin</b>/admin123 &bull; <b>nhanvien01</b>/nv123456</center></html>");
        pnlHint.add(lblHint, java.awt.BorderLayout.CENTER);

        pnlCard.add(pnlHint, java.awt.BorderLayout.SOUTH);

        pnlCenter.add(pnlCard, new java.awt.GridBagConstraints());

        add(pnlCenter, java.awt.BorderLayout.CENTER);

        lblFooter.setFont(UIConstants.FONT_SMALL);
        lblFooter.setForeground(new java.awt.Color(165, 214, 167));
        lblFooter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFooter.setText("Hệ thống quản lý hoạt động cho thuê sân bóng");
        lblFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        add(lblFooter, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cấu hình sự kiện bấm phím Enter cho ô nhập tài khoản và mật khẩu.
     */
    private void customInit() {
        // Nhấn Enter ở ô mật khẩu thực hiện đăng nhập
        txtPass.addActionListener(e -> doLogin());
        // Nhấn Enter ở ô tên đăng nhập chuyển con trỏ sang ô mật khẩu
        txtUser.addActionListener(e -> txtPass.requestFocus());
    }

    /**
     * Xử lý xác thực thông tin tài khoản đăng nhập khi nhấn nút "Đăng nhập".
     * Kiểm tra thiết lập kết nối CSDL, gọi TaiKhoanController thực hiện xác thực,
     * và thông báo lỗi hoặc chuyển màn hình chính.
     */
    private void doLogin() {
        // Thiết lập cấu hình nguồn CSDL dựa trên giá trị được chọn trên ComboBox
        boolean useDb = cboDataSource != null && cboDataSource.getSelectedIndex() == 0;
        Utils.DataStore.setUseDatabase(useDb);

        // Gọi controller thực hiện đăng nhập
        Optional<String> error = new Controller.TaiKhoanController().login(txtUser.getText(), new String(txtPass.getPassword()));
        if (error.isPresent()) {
            // Hiển thị thông báo lỗi bằng HTML nếu xác thực không thành công
            lblError.setText("<html><table width='350' style='color: #dc2626; word-wrap: break-word; table-layout: fixed;'><tr><td><b>[!]</b> " + error.get() + "</td></tr></table></html>");
            pnlCard.revalidate();
            pnlCard.repaint();
            return;
        }

        // Đăng nhập thành công, xóa câu thông báo lỗi
        lblError.setText(" ");
        if (onLoginSuccess != null) {
            // Gửi thông báo gọi lại để chuyển giao diện sang MainFrame
            onLoginSuccess.accept(null);
        }
    }
}
