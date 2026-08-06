package GiaoDien;

import Model.TaiKhoan;
import Utils.DataStore;
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
 * Khung giao diện chính (MainFrame) — Hệ thống Quản lý Hoạt động Cho thuê Sân bóng.
 * <p>
 * Lớp này chịu trách nhiệm khởi tạo cửa sổ chính của ứng dụng Swing, chứa thanh
 * sidebar điều hướng động dựa trên phân quyền người dùng (Chủ sân / Nhân viên)
 * và vùng nội dung trung tâm sử dụng {@link CardLayout} để chuyển đổi linh hoạt giữa các panel chức năng.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class MainFrame extends JFrame {

    /**
     * Tên hiển thị mặc định của ứng dụng trên thanh tiêu đề cửa sổ.
     */
    public static final String SYSTEM_NAME = "Hệ thống Quản lý hoạt động cho thuê sân bóng";

    /**
     * Trình quản lý bố cục dạng CardLayout cho vùng nội dung chính.
     */
    private final CardLayout cardLayout = new CardLayout();

    /**
     * Danh sách lưu trữ các nút điều hướng menu thanh bên, định dạng (Key -> JButton).
     */
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();

    /**
     * Panel Quản lý tài chính, báo cáo kinh doanh và thống kê doanh thu.
     */
    private QuanLyKinhDoanhPanel kinhDoanhPanel;

    /**
     * Panel Quản lý danh mục gói dịch vụ & tiện ích sân bóng.
     */
    private QuanLyDichVuPanel dichVuPanel;

    /**
     * Panel Quản lý đặt lịch, ma trận khung giờ và thao tác phiếu đặt sân.
     */
    private QuanLyDatLichPanel datLichPanel;

    /**
     * Panel Quản lý kho hàng, vật tư và nguyên liệu kinh doanh.
     */
    private QuanLyKhoPanel khoPanel;

    /**
     * Panel Quản lý lịch trình và phiếu bảo trì cơ sở vật chất sân bóng.
     */
    private QuanLyBaoTriPanel baoTriPanel;

    /**
     * Panel Quản lý danh sách khu vực và thông tin sân bóng.
     */
    private QuanLyKhuVucPanel khuVucPanel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nút bấm thực hiện đăng xuất phiên làm việc */
    private javax.swing.JButton btnLogout;
    /** Nhãn biểu tượng và tiêu đề thương hiệu ứng dụng */
    private javax.swing.JLabel lblLogo;
    /** Nhãn hiển thị mô tả ngắn và nguồn kết nối CSDL */
    private javax.swing.JLabel lblTag;
    /** Nhãn hiển thị thông tin người dùng đang đăng nhập */
    private javax.swing.JLabel lblUserInfo;
    /** Panel thương hiệu phía trên thanh sidebar */
    private javax.swing.JPanel pnlBrand;
    /** Panel chứa nội dung chính sử dụng CardLayout */
    private javax.swing.JPanel pnlContent;
    /** Panel chứa danh sách nút điều hướng menu */
    private javax.swing.JPanel pnlNav;
    /** Panel bao bọc pnlNav để căn chỉnh layout */
    private javax.swing.JPanel pnlNavContainer;
    /** Panel thanh sidebar điều hướng bên trái */
    private javax.swing.JPanel pnlSidebar;
    /** Panel chứa thông tin người dùng và nút đăng xuất ở đáy sidebar */
    private javax.swing.JPanel pnlUserBottom;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo một đối tượng cửa sổ chính MainFrame mới.
     * Thực hiện khởi tạo các thành phần giao diện kéo thả NetBeans và cấu hình tùy chỉnh.
     */
    public MainFrame() {
        // Khởi tạo linh kiện giao diện NetBeans GUI Builder
        initComponents();
        // Thiết lập giao diện tùy chỉnh và nạp dữ liệu khởi tạo
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Mã khởi tạo linh kiện giao diện tự động sinh bởi NetBeans.
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

    /**
     * Cấu hình khởi tạo tùy chỉnh bổ sung cho giao diện MainFrame.
     * Thiết lập icon logo, hiển thị chế độ kết nối CSDL (MySQL hoặc In-Memory),
     * tái dựng thanh điều hướng sidebar, khởi tạo các trang giao diện con và mặc định mở trang Đặt lịch.
     */
    private void customInit() {
        // Cấu hình biểu tượng logo tiêu đề thương hiệu
        lblLogo.setIcon(Utils.IconUtils.getBallWhiteIcon(28));
        lblLogo.setIconTextGap(8);

        // Hiển thị trạng thái nguồn dữ liệu trên tiêu đề cửa sổ
        String modeTag = DataStore.isUseDatabase() ? "CSDL MySQL (XAMPP)" : "Dữ liệu mẫu (DataStore)";
        setTitle(SYSTEM_NAME + " — [" + modeTag + "]");

        // Định dạng nhãn hiển thị chế độ dữ liệu dưới logo
        String modeHtml = DataStore.isUseDatabase()
                ? "<span style='color:#81C784; font-weight:bold;'>● CSDL MySQL (XAMPP)</span>"
                : "<span style='color:#FFB74D; font-weight:bold;'>● Dữ liệu mẫu (DataStore)</span>";
        lblTag.setText("<html>Quản lý hoạt động sân bóng<br>Nguồn: " + modeHtml + "</html>");

        // Dựng danh sách điều hướng sidebar theo quyền hạn
        rebuildSidebar();
        // Khởi tạo các panel con và thêm vào CardLayout
        buildPages();
        // Cập nhật thông tin tài khoản người dùng ở góc dưới
        refreshUserBar();
        // Mặc định điều hướng hiển thị trang Đặt lịch
        showPage("datlich");
    }

    /**
     * Xây dựng lại menu điều hướng trên thanh sidebar tùy thuộc vào vai trò và quyền hạn
     * của tài khoản đang làm việc (Chủ sân / Admin hoặc Nhân viên).
     */
    private void rebuildSidebar() {
        // Dọn dẹp danh sách nút cũ
        pnlNav.removeAll();
        navButtons.clear();

        SessionManager sm = SessionManager.get();

        // NHÓM 2: QUẢN LÝ SÂN BÓNG & LỊCH ĐẶT (Tất cả vai trò đều xem được đặt lịch & bảo trì)
        addSectionLabel(pnlNav, "— SÂN BÓNG & LỊCH ĐẶT —");
        addNav(pnlNav, "datlich", "Quản lý đặt lịch sân");
        if (sm.isAdmin()) {
            // Chỉ Chủ sân mới được quản lý khu vực sân
            addNav(pnlNav, "khuvuc", "Quản lý khu vực sân bóng");
        }
        addNav(pnlNav, "baotri", "Quản lý bảo trì sân bóng");

        // NHÓM 3: DỊCH VỤ & KHO HÀNG VẬT TƯ — Chỉ dành cho Chủ sân (Admin)
        if (sm.isAdmin()) {
            addSectionLabel(pnlNav, "— DỊCH VỤ & KHO HÀNG —");
            addNav(pnlNav, "dichvu", "Quản lý dịch vụ");
            addNav(pnlNav, "kho", "Quản lý kho hàng & vật tư");
        }

        // NHÓM 4: TÀI CHÍNH & QUẢN TRỊ HỆ THỐNG — Chỉ dành cho Chủ sân (Admin)
        if (sm.isAdmin()) {
            addSectionLabel(pnlNav, "— TÀI CHÍNH & QUẢN TRỊ —");
            addNav(pnlNav, "kinhdoanh", "Quản lý tài chính & báo cáo");
            addNav(pnlNav, "taikhoan", "Quản lý tài khoản hệ thống");
        }

        // Cập nhật lại vẽ lại giao diện sidebar
        pnlNav.revalidate();
        pnlNav.repaint();
    }

    /**
     * Thêm một tiêu đề phân nhóm (Section Header) vào panel chứa danh sách điều hướng.
     * 
     * @param nav  Panel chứa các nút điều hướng
     * @param text Nội dung văn bản tiêu đề phân nhóm
     */
    private void addSectionLabel(JPanel nav, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(145, 215, 148));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 8, 2, 8));
        nav.add(lbl);
    }

    /**
     * Tạo nút điều hướng menu mới và thêm vào panel thanh bên.
     * 
     * @param nav   Panel chứa danh sách điều hướng
     * @param key   Mã khóa đại diện cho trang (ví dụ: "datlich", "kho")
     * @param label Nhãn văn bản hiển thị trên nút bấm
     */
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

    /**
     * Định dạng kiểu dáng màu sắc cho nút điều hướng thanh bên tùy theo trạng thái được chọn (active).
     * 
     * @param btn    Nút bấm cần định dạng kiểu dáng
     * @param active True nếu nút đang được chọn đại diện cho trang hiển thị hiện tại, ngược lại False
     */
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

    /**
     * Khởi tạo danh sách tất cả các panel chức năng con và đăng ký vào {@link CardLayout} container.
     */
    private void buildPages() {
        // Tạo trang Quản lý dịch vụ
        dichVuPanel = new QuanLyDichVuPanel();
        pnlContent.add(dichVuPanel, "dichvu");

        // Tạo trang Quản lý kho hàng & vật tư
        khoPanel = new QuanLyKhoPanel();
        pnlContent.add(khoPanel, "kho");

        // Tạo trang Quản lý tài chính & báo cáo
        kinhDoanhPanel = new QuanLyKinhDoanhPanel();
        pnlContent.add(kinhDoanhPanel, "kinhdoanh");

        // Tạo trang Quản lý khu vực sân bóng
        khuVucPanel = new QuanLyKhuVucPanel();
        pnlContent.add(khuVucPanel, "khuvuc");

        // Tạo trang Quản lý đặt lịch sân bóng
        datLichPanel = new QuanLyDatLichPanel();
        pnlContent.add(datLichPanel, "datlich");

        // Tạo trang Quản lý bảo trì sân bóng
        baoTriPanel = new QuanLyBaoTriPanel();
        pnlContent.add(baoTriPanel, "baotri");

        // Tạo trang Quản lý tài khoản hệ thống
        pnlContent.add(new QuanLyTaiKhoanPanel(true), "taikhoan");
    }

    /**
     * Hiển thị trang chức năng tương ứng với mã khóa {@code key}.
     * Thực hiện kiểm tra quyền hạn của tài khoản đăng nhập trước khi chuyển đổi trang,
     * đồng thời kích hoạt tự động làm mới dữ liệu cho panel mục tiêu.
     * 
     * @param key Mã định danh của trang cần chuyển đến (ví dụ: "datlich", "kinhdoanh", "kho"...)
     */
    public void showPage(String key) {
        // Đồng bộ trạng thái sân bảo trì trước khi chuyển trang
        DataStore.get().syncTrangThaiSanBaoTri();
        SessionManager sm = SessionManager.get();

        // Kiểm tra phân quyền truy cập dành cho tài khoản Nhân viên
        if (sm.isNhanVienOnly()) {
            if ("kinhdoanh".equals(key) || "khuvuc".equals(key) || "taikhoan".equals(key)
                    || "dichvu".equals(key) || "kho".equals(key)) {
                JOptionPane.showMessageDialog(this,
                        "Tài khoản Nhân viên không được phép truy cập chức năng này.\n"
                        + "Nhân viên chỉ có quyền:\n"
                        + "  • Quản lý đặt lịch sân\n"
                        + "  • Quản lý bảo trì sân\n"
                        + "  • Bán dịch vụ / đồ ăn qua giao diện đặt sân",
                        "Phân quyền hệ thống", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (!sm.isAdmin() && !sm.isNhanVien()) {
            if (!"datlich".equals(key)) {
                JOptionPane.showMessageDialog(this,
                        "Bạn không có quyền truy cập chức năng này.",
                        "Phân quyền hệ thống", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Chuyển card hiển thị trang mục tiêu
        cardLayout.show(pnlContent, key);
        // Cập nhật trạng thái active highlight cho các nút menu sidebar
        navButtons.forEach((k, b) -> styleNav(b, k.equals(key)));

        // TỰ ĐỘNG TẢI LẠI DỮ LIỆU CỦA PANEL ĐƯỢC CHỌN KHI CHUYỂN TRANG
        if ("datlich".equals(key) && datLichPanel != null) datLichPanel.reloadSchedule();
        if ("kinhdoanh".equals(key) && kinhDoanhPanel != null) kinhDoanhPanel.refresh();
        if ("dichvu".equals(key) && dichVuPanel != null) dichVuPanel.reload();
        if ("kho".equals(key) && khoPanel != null) khoPanel.reload();
        if ("baotri".equals(key) && baoTriPanel != null) baoTriPanel.reload();
        if ("khuvuc".equals(key) && khuVucPanel != null) khuVucPanel.reload();
    }

    /**
     * Làm mới dữ liệu hiển thị trên các panel dùng chung dữ liệu sân (Đặt lịch, Bảo trì, Khu vực).
     */
    public void refreshDataPanels() {
        DataStore.get().syncTrangThaiSanBaoTri();
        if (datLichPanel != null) datLichPanel.reloadSchedule();
        if (baoTriPanel != null) baoTriPanel.reload();
        if (khuVucPanel != null) khuVucPanel.reload();
    }

    /**
     * Cập nhật thông tin hiển thị tài khoản người dùng và chế độ CSDL ở góc dưới thanh sidebar.
     */
    private void refreshUserBar() {
        rebuildSidebar();
        TaiKhoan u = SessionManager.get().getCurrentUser();
        if (u == null) {
            lblUserInfo.setText("Chưa đăng nhập");
            return;
        }
        String roleNote = "ADMIN".equals(u.getQuyenHan()) ? "Chủ sân" : u.getQuyenHanHienThi();
        String dbNote = DataStore.isUseDatabase()
                ? "<br><span style='color:#81C784; font-size:10px;'>● CSDL: MySQL</span>"
                : "<br><span style='color:#FFB74D; font-size:10px;'>● CSDL: DataStore (Memory)</span>";

        lblUserInfo.setText("<html><b>" + u.getTenDangNhap() + "</b><br>"
                + roleNote + dbNote + "</html>");
    }

    /**
     * Xử lý hành động đăng xuất khỏi phiên làm việc hiện tại,
     * hủy cửa sổ chính và quay về màn hình đăng nhập ứng dụng.
     */
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
