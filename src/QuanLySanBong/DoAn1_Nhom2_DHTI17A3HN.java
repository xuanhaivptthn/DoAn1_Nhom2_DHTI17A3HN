package QuanLySanBong;

import GiaoDien.MainFrame;
import GiaoDien.Panels.LoginPanel;
import Utils.UIConstants;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * Lớp khởi chạy chính (Main Entry Point) — Hệ thống Quản lý Hoạt động Cho thuê Sân bóng.
 * <p>
 * Lớp này thực hiện thiết lập giao diện đồ họa LookAndFeel hệ thống, cài đặt font chữ mặc định,
 * cấu hình màu nền ô bảng và kích hoạt luồng Swing Event Dispatch Thread (EDT) để hiển thị cửa sổ Đăng nhập.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class DoAn1_Nhom2_DHTI17A3HN {

    /**
     * Khởi tạo đối tượng {@code DoAn1_Nhom2_DHTI17A3HN} mặc định.
     */
    public DoAn1_Nhom2_DHTI17A3HN() {}

    /**
     * Phương thức khởi chạy chính của toàn bộ chương trình Java Swing.
     *
     * @param args Các tham số dòng lệnh truyền vào (nếu có).
     */
    public static void main(String[] args) {
        try {
            // Đặt giao diện hiển thị LookAndFeel theo hệ điều hành đang chạy (Windows/Linux/Mac)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Bỏ qua nếu có lỗi phát sinh trong quá trình nạp LookAndFeel
        }

        // Đặt thuộc tính cấu hình màu dòng xen kẽ và chiều cao dòng cho bảng JTable
        UIManager.put("Table.alternateRowColor", UIConstants.TABLE_ROW_ALT);
        UIManager.put("Table.rowHeight", 30);

        // Đặt font chữ Segoe UIPlain 13pt mặc định cho toàn bộ các UI Component trong Swing
        setUIFont(UIConstants.FONT_NORMAL);

        // Kích hoạt việc tạo và hiển thị màn hình Đăng nhập trên luồng Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(DoAn1_Nhom2_DHTI17A3HN::showLogin);
    }

    /**
     * Khởi tạo và hiển thị cửa sổ Đăng nhập (Login Window).
     * <p>
     * Khi đăng nhập thành công, cửa sổ đăng nhập sẽ đóng lại và khởi chạy màn hình ứng dụng chính {@link MainFrame}.
     * </p>
     */
    public static void showLogin() {
        // Tạo cửa sổ JFrame đăng nhập với tiêu đề hệ thống
        JFrame loginFrame = new JFrame(MainFrame.SYSTEM_NAME);
        loginFrame.setIconImage(Utils.IconUtils.getBallIcon(32).getImage()); // Gán icon quả bóng cho ứng dụng
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setMinimumSize(new Dimension(840, 580)); // Kích thước tối thiểu
        loginFrame.setSize(940, 660); // Kích thước hiển thị ban đầu
        loginFrame.setLocationRelativeTo(null); // Hiển thị chính giữa màn hình

        // Thiết lập ContentPane là LoginPanel kèm xử lý callback khi đăng nhập thành công
        loginFrame.setContentPane(new LoginPanel(v -> {
            loginFrame.dispose(); // Giải phóng và đóng cửa sổ đăng nhập
            MainFrame main = new MainFrame(); // Tạo mới màn hình ứng dụng chính
            main.setIconImage(Utils.IconUtils.getBallIcon(32).getImage());
            main.setVisible(true); // Hiển thị giao diện chính
        }));
        loginFrame.setVisible(true); // Cho phép hiển thị cửa sổ đăng nhập
    }

    /**
     * Hàm phụ trợ duyệt qua tất cả các giá trị của UIManager để thiết lập font chữ thống nhất cho ứng dụng.
     *
     * @param font Font chữ {@link java.awt.Font} cần áp dụng.
     */
    private static void setUIFont(java.awt.Font font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
