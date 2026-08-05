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

        UIManager.put("Button.background", java.awt.Color.WHITE);
        UIManager.put("Button.foreground", UIConstants.TEXT_PRIMARY);

        javax.swing.Painter<javax.swing.JComponent> fillPainter = (g, c, w, h) -> {
            java.awt.Color bg = c.getBackground();
            if (bg != null) {
                g.setColor(bg);
                g.fillRect(0, 0, w, h);
            }
        };

        UIManager.put("Button[Enabled].backgroundPainter", fillPainter);
        UIManager.put("Button[Focused].backgroundPainter", fillPainter);
        UIManager.put("Button[Default].backgroundPainter", fillPainter);
        UIManager.put("Button[Default+Focused].backgroundPainter", fillPainter);
        UIManager.put("Button[Pressed].backgroundPainter", (javax.swing.Painter<javax.swing.JComponent>) (g, c, w, h) -> {
            java.awt.Color bg = c.getBackground();
            if (bg != null) {
                g.setColor(bg.darker());
                g.fillRect(0, 0, w, h);
            }
        });
        UIManager.put("Button[MouseOver].backgroundPainter", (javax.swing.Painter<javax.swing.JComponent>) (g, c, w, h) -> {
            java.awt.Color bg = c.getBackground();
            if (bg != null) {
                g.setColor(bg.equals(java.awt.Color.WHITE) ? new java.awt.Color(241, 245, 249) : bg.brighter());
                g.fillRect(0, 0, w, h);
            }
        });

        setUIFont(UIConstants.FONT_NORMAL);

        SwingUtilities.invokeLater(DoAn1_Nhom2_DHTI17A3HN::showLogin);
    }

    public static void showLogin() {
        JFrame loginFrame = new JFrame(MainFrame.SYSTEM_NAME);
        loginFrame.setIconImage(Utils.IconUtils.getBallIcon(32).getImage());
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setMinimumSize(new Dimension(840, 580));
        loginFrame.setSize(940, 660);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setContentPane(new LoginPanel(v -> {
            loginFrame.dispose();
            MainFrame main = new MainFrame();
            main.setIconImage(Utils.IconUtils.getBallIcon(32).getImage());
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
