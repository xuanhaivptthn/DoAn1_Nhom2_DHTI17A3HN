package Utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Tập hợp hằng số màu sắc, font chữ và hằng số phân quyền dùng chung cho giao diện Quản lý sân bóng Swing.
 * <p>
 * Lớp này định nghĩa bảng màu chủ đạo (màu xanh lá sân bóng hiện đại, màu nền, đường viền, trạng thái thành công/cảnh báo/thất bại),
 * bộ font chữ Segoe UI nhất quán và các mảng hằng số ánh xạ vai trò người dùng cũng như trạng thái hoạt động.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public final class UIConstants {

    /**
     * Khởi tạo riêng biệt nhằm ngăn chặn việc khởi tạo đối tượng hằng số {@code UIConstants}.
     */
    private UIConstants() {
    }

    // ===== Màu sắc chủ đạo (Xanh thể thao hiện đại, sạch & sang trọng) =====

    /** Màu xanh đậm sân bóng chính (#1B5E20). */
    public static final Color PRIMARY = new Color(27, 94, 32);

    /** Màu xanh tối thanh Sidebar (#0F3D14). */
    public static final Color PRIMARY_DARK = new Color(15, 61, 20);

    /** Màu xanh lá tươi điểm nhấn (#4CAF50). */
    public static final Color PRIMARY_LIGHT = new Color(76, 175, 80);

    /** Màu vàng hổ phách làm điểm nhấn phụ (#FFC107). */
    public static final Color ACCENT = new Color(255, 193, 7);

    /** Màu nền ứng dụng xám nhạt dịu mắt (#F5F7FA). */
    public static final Color BG = new Color(245, 247, 250);

    /** Nền trắng thẻ Card. */
    public static final Color CARD_BG = Color.WHITE;

    /** Đường viền xám mềm (#E2E8F0). */
    public static final Color BORDER = new Color(226, 232, 240);

    /** Màu chữ chính Slate 800 (#1E293B). */
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);

    /** Màu chữ phụ Slate 500 (#64748B). */
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    // ===== Status Colors (Màu trạng thái) =====

    /** Màu đỏ báo lỗi / nguy hiểm (#E11D48). */
    public static final Color DANGER = new Color(225, 29, 72);

    /** Nền đỏ nhạt cho cảnh báo lỗi. */
    public static final Color DANGER_BG = new Color(255, 228, 230);

    /** Màu đỏ đậm khi rê chuột qua nút xóa. */
    public static final Color DANGER_HOVER = new Color(190, 18, 60);

    /** Màu xanh lá báo thành công (#10B981). */
    public static final Color SUCCESS = new Color(16, 185, 129);

    /** Nền xanh lá nhạt thông báo thành công. */
    public static final Color SUCCESS_BG = new Color(209, 250, 229);

    /** Màu cam cảnh báo Amber (#F59E0B). */
    public static final Color WARNING = new Color(245, 158, 11);

    /** Nền cam nhạt cảnh báo. */
    public static final Color WARNING_BG = new Color(254, 243, 199);

    /** Màu xanh dương thông tin (#0EA5E9). */
    public static final Color INFO = new Color(14, 165, 233);

    /** Nền xanh dương nhạt cho thẻ thông tin. */
    public static final Color INFO_BG = new Color(224, 242, 254);

    // ===== Bảng dữ liệu (Table) =====

    /** Màu nền cho thanh tiêu đề bảng header. */
    public static final Color TABLE_HEADER = new Color(27, 94, 32);

    /** Màu nền cho các dòng chẵn xen kẽ trong bảng. */
    public static final Color TABLE_ROW_ALT = new Color(248, 250, 252);

    /** Màu nền dòng được người dùng click chọn trong bảng. */
    public static final Color TABLE_SELECTION = new Color(220, 252, 231);

    /** Màu nền ô nhập liệu input. */
    public static final Color INPUT_BG = new Color(255, 255, 255);

    /** Đường viền ô nhập liệu input. */
    public static final Color INPUT_BORDER = new Color(203, 213, 225);

    // ===== Font Chữ Hệ Thống =====

    /** Font tiêu đề chính (Segoe UI Bold 22pt). */
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);

    /** Font tiêu đề phụ (Segoe UI Bold 15pt). */
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);

    /** Font nội dung bình thường (Segoe UI Plain 13pt). */
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);

    /** Font chữ in đậm (Segoe UI Bold 13pt). */
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    /** Font chú thích nhỏ (Segoe UI Plain 12pt). */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    /** Font trên các nút bấm action (Segoe UI Bold 13pt). */
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    /** Font trong các ô của bảng dữ liệu (Segoe UI Plain 13pt). */
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);

    /** Font trên tiêu đề cột bảng (Segoe UI Bold 13pt). */
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    // Vai trò & Trạng thái hệ thống (Chỉ 2 vai trò: Quản trị viên và Nhân viên)

    /** Danh sách mã mã hóa vai trò người dùng ("Admin", "NhanVien"). */
    public static final String[] VAI_TRO = {"Admin", "NhanVien"};

    /** Danh sách tên hiển thị vai trò người dùng ("Quản trị viên", "Nhân viên"). */
    public static final String[] VAI_TRO_HIEN_THI = {"Quản trị viên", "Nhân viên"};

    /** Danh sách mã mã hóa trạng thái tài khoản ("HoatDong", "Khoa"). */
    public static final String[] TRANG_THAI = {"HoatDong", "Khoa"};

    /** Danh sách tên hiển thị trạng thái tài khoản ("Hoạt động", "Đã khoá"). */
    public static final String[] TRANG_THAI_HIEN_THI = {"Hoạt động", "Đã khoá"};
}
