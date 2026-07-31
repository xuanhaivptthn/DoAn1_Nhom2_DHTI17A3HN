package GiaoDien;

import Model.TaiKhoan;
import Utils.SessionManager;
import GiaoDien.Panels.*;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Khung giao diện chính — Hệ thống Quản lý Hoạt động Cho thuê Sân bóng.
 */
public class MainFrame extends JFrame {

    public static final String SYSTEM_NAME = "Hệ thống Quản lý hoạt động cho thuê sân bóng";

    private final CardLayout cardLayout = new CardLayout();
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();

    private DashboardPanel dashboardPanel;
    private QuanLyKinhDoanhPanel kinhDoanhPanel;
    private QuanLyDichVuPanel dichVuPanel;
    private QuanLyDatLichPanel datLichPanel;
    private QuanLyKhoPanel khoPanel;
    private QuanLyBaoTriPanel baoTriPanel;
    private QuanLyKhuVucPanel khuVucPanel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblTag;
    private javax.swing.JLabel lblUserInfo;
    private javax.swing.JPanel pnlBrand;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlNav;
    private javax.swing.JPanel pnlNavContainer;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlUserBottom;
    // End of variables declaration//GEN-END:variables

    public MainFrame() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSidebar = new javax.swing.JPanel();
        pnlBrand = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblTag = new javax.swing.JLabel();
        pnlNavContainer = new javax.swing.JPanel();
        pnlNav = new javax.swing.JPanel();
        pnlUserBottom = new javax.swing.JPanel();
        lblUserInfo = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton("Đăng xuất phiên");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hệ thống Quản lý hoạt động cho thuê sân bóng");
        setMinimumSize(new java.awt.Dimension(1120, 680));
        setSize(new java.awt.Dimension(1240, 760));
        setLocationRelativeTo(null);

        pnlSidebar.setBackground(UIConstants.PRIMARY_DARK);
        pnlSidebar.setPreferredSize(new java.awt.Dimension(255, 0));
        pnlSidebar.setLayout(new java.awt.BorderLayout());

        pnlBrand.setBackground(UIConstants.PRIMARY);
        pnlBrand.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        pnlBrand.setLayout(new java.awt.BorderLayout());

        lblLogo.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblLogo.setForeground(java.awt.Color.WHITE);
        lblLogo.setText(" Cho thuê sân bóng");
        lblLogo.setIcon(Utils.IconUtils.getBallWhiteIcon(24));
        pnlBrand.add(lblLogo, java.awt.BorderLayout.NORTH);

        lblTag.setFont(UIConstants.FONT_SMALL);
        lblTag.setForeground(new java.awt.Color(200, 230, 201));
        lblTag.setText("<html>Quản lý hoạt động<br>phân loại theo tính năng</html>");
        pnlBrand.add(lblTag, java.awt.BorderLayout.SOUTH);

        pnlSidebar.add(pnlBrand, java.awt.BorderLayout.NORTH);

        pnlNavContainer.setBackground(UIConstants.PRIMARY_DARK);
        pnlNavContainer.setLayout(new java.awt.BorderLayout());

        pnlNav.setBackground(UIConstants.PRIMARY_DARK);
        pnlNav.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pnlNav.setLayout(new java.awt.GridLayout(0, 1, 0, 2));
        pnlNavContainer.add(pnlNav, java.awt.BorderLayout.NORTH);

        pnlSidebar.add(pnlNavContainer, java.awt.BorderLayout.CENTER);

        pnlUserBottom.setBackground(new java.awt.Color(10, 40, 14));
        pnlUserBottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 14, 12));
        pnlUserBottom.setLayout(new java.awt.BorderLayout(0, 8));

        lblUserInfo.setFont(UIConstants.FONT_SMALL);
        lblUserInfo.setForeground(new java.awt.Color(200, 230, 201));
        lblUserInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnlUserBottom.add(lblUserInfo, java.awt.BorderLayout.CENTER);

        btnLogout.setPreferredSize(new java.awt.Dimension(0, 36));
        btnLogout.addActionListener(e -> doLogout());
        pnlUserBottom.add(btnLogout, java.awt.BorderLayout.SOUTH);

        pnlSidebar.add(pnlUserBottom, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnlSidebar, java.awt.BorderLayout.WEST);

        pnlContent = new javax.swing.JPanel(cardLayout);
        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit() {
        rebuildSidebar();
        buildPages();
        refreshUserBar();
        showPage("dashboard");
    }

    private void rebuildSidebar() {
        pnlNav.removeAll();
        navButtons.clear();

        SessionManager sm = SessionManager.get();

        // NHÓM 1: TỔNG QUAN HỆ THỐNG
        addSectionLabel(pnlNav, "— TỔNG QUAN HỆ THỐNG —");
        addNav(pnlNav, "dashboard", "Dashboard & Hành động");

        // NHÓM 2: QUẢN LÝ SÂN BÓNG & LỊCH ĐẶT
        addSectionLabel(pnlNav, "— SÂN BÓNG & LỊCH ĐẶT —");
        addNav(pnlNav, "datlich", "Quản lý đặt lịch sân");
        if (sm.isAdmin()) {
            addNav(pnlNav, "khuvuc", "Quản lý khu vực sân bóng");
        }
        addNav(pnlNav, "baotri", "Quản lý bảo trì sân bóng");

        // NHÓM 3: DỊCH VỤ & KHO HÀNG VẬT TƯ
        addSectionLabel(pnlNav, "— DỊCH VỤ & KHO HÀNG —");
        addNav(pnlNav, "dichvu", "Quản lý dịch vụ");
        addNav(pnlNav, "kho", "Quản lý kho hàng & vật tư");

        // NHÓM 4: TÀI CHÍNH & QUẢN TRỊ HỆ THỐNG
        if (sm.isAdmin()) {
            addSectionLabel(pnlNav, "— TÀI CHÍNH & QUẢN TRỊ —");
            addNav(pnlNav, "kinhdoanh", "Quản lý tài chính & báo cáo");
            addNav(pnlNav, "taikhoan", "Quản lý tài khoản hệ thống");
        }

        pnlNav.revalidate();
        pnlNav.repaint();
    }

    private void addSectionLabel(JPanel nav, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(145, 215, 148));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 8, 2, 8));
        nav.add(lbl);
    }

    private void addNav(JPanel nav, String key, String label) {
        JButton btn = new JButton(label);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.addActionListener(e -> showPage(key));
        navButtons.put(key, btn);
        nav.add(btn);
        styleNav(btn, false);
    }

    private void styleNav(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(UIConstants.PRIMARY_LIGHT);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
        } else {
            btn.setBackground(UIConstants.PRIMARY_DARK);
            btn.setForeground(new Color(220, 237, 200));
            btn.setOpaque(true);
        }
    }

    private void buildPages() {
        dashboardPanel = new DashboardPanel(this::showPage);
        pnlContent.add(dashboardPanel, "dashboard");

        dichVuPanel = new QuanLyDichVuPanel();
        pnlContent.add(dichVuPanel, "dichvu");

        khoPanel = new QuanLyKhoPanel();
        pnlContent.add(khoPanel, "kho");

        kinhDoanhPanel = new QuanLyKinhDoanhPanel();
        pnlContent.add(kinhDoanhPanel, "kinhdoanh");

        khuVucPanel = new QuanLyKhuVucPanel();
        pnlContent.add(khuVucPanel, "khuvuc");

        datLichPanel = new QuanLyDatLichPanel();
        pnlContent.add(datLichPanel, "datlich");

        baoTriPanel = new QuanLyBaoTriPanel();
        pnlContent.add(baoTriPanel, "baotri");

        pnlContent.add(new QuanLyTaiKhoanPanel(true), "taikhoan");
    }

    public void showPage(String key) {
        SessionManager sm = SessionManager.get();

        if (sm.isNhanVienOnly()) {
            if ("kinhdoanh".equals(key) || "khuvuc".equals(key) || "taikhoan".equals(key)) {
                JOptionPane.showMessageDialog(this,
                        "Tài khoản Nhân viên không được phép truy cập chức năng này.\nChỉ được phép truy cập:\n  • Quản lý dịch vụ\n  • Quản lý kho\n  • Quản lý đặt lịch\n  • Quản lý bảo trì",
                        "Phân quyền hệ thống", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (!sm.isAdmin() && !sm.isNhanVien()) {
            if (!"dashboard".equals(key)) {
                JOptionPane.showMessageDialog(this,
                        "Bạn không có quyền truy cập chức năng này.",
                        "Phân quyền hệ thống", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        cardLayout.show(pnlContent, key);
        navButtons.forEach((k, b) -> styleNav(b, k.equals(key)));

        // AUTO-RELOAD TARGET PANELS ON PAGE SWITCH
        if ("dashboard".equals(key) && dashboardPanel != null) dashboardPanel.refresh();
        if ("datlich".equals(key) && datLichPanel != null) datLichPanel.reloadSchedule();
        if ("kinhdoanh".equals(key) && kinhDoanhPanel != null) kinhDoanhPanel.refresh();
        if ("dichvu".equals(key) && dichVuPanel != null) dichVuPanel.reload();
        if ("kho".equals(key) && khoPanel != null) khoPanel.reload();
        if ("baotri".equals(key) && baoTriPanel != null) baoTriPanel.reload();
        if ("khuvuc".equals(key) && khuVucPanel != null) khuVucPanel.reload();
    }

    public void refreshDataPanels() {
        if (datLichPanel != null) datLichPanel.reloadSchedule();
        if (baoTriPanel != null) baoTriPanel.reload();
        if (dashboardPanel != null) dashboardPanel.refresh();
        if (khuVucPanel != null) khuVucPanel.reload();
    }

    private void refreshUserBar() {
        rebuildSidebar();
        TaiKhoan u = SessionManager.get().getCurrentUser();
        if (u == null) {
            lblUserInfo.setText("Chưa đăng nhập");
            return;
        }
        String roleNote = "ADMIN".equals(u.getQuyenHan()) ? "Chủ sân" : u.getQuyenHanHienThi();
        lblUserInfo.setText("<html><b>" + u.getTenDangNhap() + "</b><br>"
                + roleNote + "</html>");
    }

    private void doLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất phiên hoạt động hiện tại?",
                "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            SessionManager.get().logout();
            dispose();
            QuanLySanBong.DoAn1_Nhom2_DHTI17A3HN.showLogin();
        }
    }
}
