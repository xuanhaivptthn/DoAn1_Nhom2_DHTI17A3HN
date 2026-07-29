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
 * Màn hình đăng nhập phiên làm việc hệ thống.
 */
public class LoginPanel extends javax.swing.JPanel {

    private Consumer<Void> onLoginSuccess;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JComboBox<String> cboDataSource;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblPassLabel;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserLabel;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlHint;
    private javax.swing.JPanel pnlTitles;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    public LoginPanel() {
        this(null);
    }

    public LoginPanel(Consumer<Void> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlCenter = new javax.swing.JPanel();
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
        pnlCard.setPreferredSize(new java.awt.Dimension(440, 540));
        pnlCard.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));
        pnlCard.setLayout(new java.awt.BorderLayout());

        pnlTop.setOpaque(false);
        pnlTop.setLayout(new java.awt.BorderLayout(0, 6));

        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        javax.swing.ImageIcon ballIcon = Utils.IconUtils.getBallBlackIcon(44);
        if (ballIcon != null) {
            lblIcon.setIcon(ballIcon);
            lblIcon.setText("");
        } else {
            lblIcon.setFont(new java.awt.Font("Segoe UI", 1, 24));
            lblIcon.setText("SAN BONG MANAGER");
        }
        pnlTop.add(lblIcon, java.awt.BorderLayout.NORTH);

        pnlTitles.setOpaque(false);
        pnlTitles.setLayout(new java.awt.BorderLayout(0, 2));

        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.PRIMARY);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Cho thuê sân bóng");
        pnlTitles.add(lblTitle, java.awt.BorderLayout.NORTH);

        lblSub.setFont(UIConstants.FONT_SMALL);
        lblSub.setForeground(UIConstants.TEXT_SECONDARY);
        lblSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSub.setText("Đăng nhập vào hệ thống");
        pnlTitles.add(lblSub, java.awt.BorderLayout.SOUTH);

        pnlTop.add(pnlTitles, java.awt.BorderLayout.CENTER);

        pnlCard.add(pnlTop, java.awt.BorderLayout.NORTH);

        pnlForm.setOpaque(false);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
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
                "🗄️ CSDL MySQL (DAO / XAMPP)",
                "📦 Dữ liệu mẫu (DataStore / In-Memory)"
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

        btnLogin.setPreferredSize(new java.awt.Dimension(0, 40));
        btnLogin.addActionListener(e -> doLogin());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        pnlForm.add(btnLogin, gridBagConstraints);

        pnlCard.add(pnlForm, java.awt.BorderLayout.CENTER);

        pnlHint.setOpaque(false);
        pnlHint.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        pnlHint.setLayout(new java.awt.BorderLayout());

        lblHint.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblHint.setForeground(UIConstants.TEXT_SECONDARY);
        lblHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHint.setText("<html><center><b>Tài khoản demo</b><br>admin / admin123 (Chủ sân)<br>nhanvien01 / nv123456 (Nhân viên)</center></html>");
        pnlHint.add(lblHint, java.awt.BorderLayout.CENTER);

        pnlCard.add(pnlHint, java.awt.BorderLayout.SOUTH);

        pnlCenter.add(pnlCard, new java.awt.GridBagConstraints());

        add(pnlCenter, java.awt.BorderLayout.CENTER);

        lblFooter.setFont(UIConstants.FONT_SMALL);
        lblFooter.setForeground(new java.awt.Color(165, 214, 167));
        lblFooter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFooter.setText("Hệ thống quản lý hoạt động cho thuê sân bóng");
        lblFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(lblFooter, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit() {
        txtPass.addActionListener(e -> doLogin());
        txtUser.addActionListener(e -> txtPass.requestFocus());
    }

    private void doLogin() {
        boolean useDb = cboDataSource != null && cboDataSource.getSelectedIndex() == 0;
        Utils.DataStore.setUseDatabase(useDb);

        Optional<String> error = SessionManager.get().login(txtUser.getText(), new String(txtPass.getPassword()));
        if (error.isPresent()) {
            lblError.setText("[!] " + error.get());
            return;
        }
        lblError.setText(" ");
        if (onLoginSuccess != null) {
            onLoginSuccess.accept(null);
        }
    }
}
