package Utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Hằng số màu sắc, font chữ và hằng số phân quyền cho giao diện Quản lý sân bóng.
 */
public final class UIConstants {

    private UIConstants() {
    }

    // ===== Màu sắc chủ đạo (Xanh thể thao hiện đại, sạch & sang trọng) =====
    public static final Color PRIMARY = new Color(27, 94, 32);           // Xanh đậm sân bóng (#1B5E20)
    public static final Color PRIMARY_DARK = new Color(15, 61, 20);      // Sidebar dark (#0F3D14)
    public static final Color PRIMARY_LIGHT = new Color(76, 175, 80);    // Accent xanh lá (#4CAF50)
    public static final Color ACCENT = new Color(255, 193, 7);           // Vàng Hổ Phách (#FFC107)

    public static final Color BG = new Color(245, 247, 250);             // Background xám nhạt dịu mắt
    public static final Color CARD_BG = Color.WHITE;                     // Nền Card trắng
    public static final Color BORDER = new Color(226, 232, 240);         // Đường viền xám mềm (#E2E8F0)

    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);       // Chữ chính Slate 800 (#1E293B)
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Chữ phụ Slate 500 (#64748B)

    // ===== Status Colors (Màu trạng thái) =====
    public static final Color DANGER = new Color(225, 29, 72);           // Đỏ Rose (#E11D48)
    public static final Color DANGER_BG = new Color(255, 228, 230);
    public static final Color DANGER_HOVER = new Color(190, 18, 60);

    public static final Color SUCCESS = new Color(16, 185, 129);         // Xanh Emerald (#10B981)
    public static final Color SUCCESS_BG = new Color(209, 250, 229);

    public static final Color WARNING = new Color(245, 158, 11);         // Cam Amber (#F59E0B)
    public static final Color WARNING_BG = new Color(254, 243, 199);

    public static final Color INFO = new Color(14, 165, 233);            // Xanh Sky (#0EA5E9)
    public static final Color INFO_BG = new Color(224, 242, 254);

    // ===== Bảng dữ liệu (Table) =====
    public static final Color TABLE_HEADER = new Color(27, 94, 32);
    public static final Color TABLE_ROW_ALT = new Color(248, 250, 252);
    public static final Color TABLE_SELECTION = new Color(220, 252, 231);

    public static final Color INPUT_BG = new Color(255, 255, 255);
    public static final Color INPUT_BORDER = new Color(203, 213, 225);

    // ===== Font Chữ Hệ Thống =====
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    // Vai trò & Trạng thái hệ thống (Chỉ 2 vai trò: Quản trị viên và Nhân viên)
    public static final String[] VAI_TRO = {"Admin", "NhanVien"};
    public static final String[] VAI_TRO_HIEN_THI = {"Quản trị viên", "Nhân viên"};

    public static final String[] TRANG_THAI = {"HoatDong", "Khoa"};
    public static final String[] TRANG_THAI_HIEN_THI = {"Hoạt động", "Đã khoá"};
}
