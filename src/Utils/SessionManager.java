package Utils;

import Model.TaiKhoan;

import java.util.List;
import java.util.Optional;

/**
 * Lớp Quản lý phiên làm việc (SessionManager) người dùng theo mô hình Singleton.
 * <p>
 * Lớp này lưu giữ đối tượng {@link TaiKhoan} hiện đang đăng nhập vào phần mềm,
 * xử lý xác thực thông tin tài khoản (kiểm tra rỗng, kiểm tra CSDL / danh sách DataStore, kiểm tra khóa tài khoản),
 * cũng như hỗ trợ kiểm tra vai trò phân quyền (Admin / Nhân viên).
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public final class SessionManager {

    /**
     * Thể hiện duy nhất (Singleton Instance) của SessionManager.
     */
    private static final SessionManager INSTANCE = new SessionManager();

    /**
     * Tài khoản người dùng hiện tại đang đăng nhập trong ứng dụng.
     */
    private TaiKhoan currentUser;

    /**
     * Khởi tạo riêng biệt ngăn ngừa việc tạo đối tượng ngoài lớp.
     */
    private SessionManager() {
    }

    /**
     * Lấy thể hiện duy nhất của SessionManager.
     *
     * @return Đối tượng {@link SessionManager} duy nhất.
     */
    public static SessionManager get() {
        return INSTANCE;
    }

    /**
     * Thực hiện xác thực đăng nhập người dùng với tên đăng nhập và mật khẩu.
     *
     * @param username Tên đăng nhập nhập từ màn hình Login.
     * @param password Mật khẩu nhập từ màn hình Login.
     * @return {@link Optional} chứa thông báo lỗi tiếng Việt nếu đăng nhập thất bại; hoặc {@code Optional.empty()} nếu thành công.
     */
    public Optional<String> login(String username, String password) {
        // Kiểm tra hợp lệ dữ liệu tên đăng nhập
        if (username == null || username.isBlank()) {
            return Optional.of("Vui lòng nhập tên đăng nhập.");
        }
        // Kiểm tra hợp lệ dữ liệu mật khẩu
        if (password == null || password.isBlank()) {
            return Optional.of("Vui lòng nhập mật khẩu.");
        }

        // Nếu chế độ CSDL MySQL đang kích hoạt
        if (DataStore.isUseDatabase()) {
            if (!DAO.DBConnect.testConnection()) {
                return Optional.of("Không thể kết nối MySQL! Vui lòng mở XAMPP hoặc chọn 'Dữ liệu mẫu'.");
            }
            try {
                // Tra cứu tài khoản trong MySQL theo username
                TaiKhoan dbUser = new DAO.TaiKhoanDAO().findByUsername(username.trim());
                if (dbUser != null) {
                    // Kiểm tra tài khoản có bị khóa không
                    if (!dbUser.isHoatDong()) {
                        return Optional.of("Tài khoản đã bị vô hiệu hóa.");
                    }
                    // Kiểm tra khớp mật khẩu
                    if (dbUser.getMatKhau().equals(password)) {
                        this.currentUser = dbUser; // Lưu thông tin người dùng đang đăng nhập
                        return Optional.empty(); // Thành công
                    }
                }
                return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
            } catch (Exception ex) {
                return Optional.of("Lỗi kết nối CSDL MySQL: " + ex.getMessage());
            }
        }

        // Nếu sử dụng bộ lưu trữ bộ nhớ DataStore (Offline / Demo mode)
        List<TaiKhoan> list = DataStore.get().getTaiKhoans();
        Optional<TaiKhoan> opt = list.stream()
                .filter(u -> u.getTenDangNhap().equalsIgnoreCase(username.trim()))
                .findFirst();

        if (opt.isEmpty()) {
            return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        TaiKhoan u = opt.get();
        // Kiểm tra xem tài khoản có ở trạng thái HOAT_DONG không
        if (!u.isHoatDong()) {
            return Optional.of("Tài khoản đã bị vô hiệu hóa.");
        }

        // Kiểm tra tính chính xác của mật khẩu
        if (!u.getMatKhau().equals(password)) {
            return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        // Đăng nhập thành công -> thiết lập người dùng hiện tại
        this.currentUser = u;
        return Optional.empty();
    }

    /**
     * Đăng xuất khỏi phiên làm việc hiện tại.
     */
    public void logout() {
        // Xóa thông tin người dùng đang đăng nhập
        this.currentUser = null;
    }

    /**
     * Lấy thông tin tài khoản người dùng đang đăng nhập hệ thống.
     *
     * @return Đối tượng {@link TaiKhoan} hiện tại hoặc {@code null} nếu chưa đăng nhập.
     */
    public TaiKhoan getCurrentUser() {
        return currentUser;
    }

    /**
     * Kiểm tra xem hiện tại đã có người dùng đăng nhập vào ứng dụng chưa.
     *
     * @return {@code true} nếu đã đăng nhập; {@code false} nếu chưa.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Kiểm tra xem người dùng hiện tại có vai trò Quản trị viên (Admin) hay không.
     *
     * @return {@code true} nếu là Admin; {@code false} nếu không phải.
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Kiểm tra người dùng hiện tại có vai trò Nhân viên hay không.
     *
     * @return {@code true} nếu là Nhân viên; {@code false} nếu không phải.
     */
    public boolean isNhanVienOnly() {
        return currentUser != null && currentUser.isNhanVien();
    }

    /**
     * Kiểm tra người dùng hiện tại có vai trò Nhân viên hay không.
     *
     * @return {@code true} nếu là Nhân viên; {@code false} nếu không phải.
     */
    public boolean isNhanVien() {
        return currentUser != null && currentUser.isNhanVien();
    }
}
