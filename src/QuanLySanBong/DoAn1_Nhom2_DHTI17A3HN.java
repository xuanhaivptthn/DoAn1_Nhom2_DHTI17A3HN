package QuanLySanBong;

import GiaoDien.MainFrame;
import GiaoDien.Panels.LoginPanel;
import Utils.UIConstants;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * Điểm khởi chạy chính — Hệ thống Quản lý Hoạt động Cho thuê Sân bóng.
 */
public class DoAn1_Nhom2_DHTI17A3HN {

    public static void main(String[] args) {
        // Cấu hình giao diện Nimbus mặc định của Java Swing
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fallback nếu không hỗ trợ System Look & Feel
            }
        }

        UIManager.put("Table.alternateRowColor", UIConstants.TABLE_ROW_ALT);
        UIManager.put("Table.rowHeight", 30);

        setUIFont(UIConstants.FONT_NORMAL);

        SwingUtilities.invokeLater(DoAn1_Nhom2_DHTI17A3HN::showLogin);
    }

    public static void showLogin() {
        JFrame loginFrame = new JFrame(MainFrame.SYSTEM_NAME);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setMinimumSize(new Dimension(840, 580));
        loginFrame.setSize(940, 660);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setContentPane(new LoginPanel(v -> {
            loginFrame.dispose();
            MainFrame main = new MainFrame();
            main.setVisible(true);
        }));
        loginFrame.setVisible(true);
    }

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
