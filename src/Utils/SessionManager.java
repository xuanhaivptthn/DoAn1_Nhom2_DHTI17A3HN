package Utils;

import Model.TaiKhoan;

import java.util.List;
import java.util.Optional;

/**
 * Quản lý phiên làm việc của người dùng hiện tại trong ứng dụng.
 */
public final class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    private TaiKhoan currentUser;

    private SessionManager() {
    }

    public static SessionManager get() {
        return INSTANCE;
    }

    public Optional<String> login(String username, String password) {
        if (username == null || username.isBlank()) {
            return Optional.of("Vui lòng nhập tên đăng nhập.");
        }
        if (password == null || password.isBlank()) {
            return Optional.of("Vui lòng nhập mật khẩu.");
        }

        if (DataStore.isUseDatabase()) {
            if (!DAO.DBConnect.testConnection()) {
                return Optional.of("Không thể kết nối MySQL! Vui lòng mở XAMPP hoặc chọn 'Dữ liệu mẫu'.");
            }
            try {
                // Kiểm tra tài khoản tồn tại theo username trước để kiểm tra trạng thái hoạt động
                TaiKhoan dbUser = new DAO.TaiKhoanDAO().findByUsername(username.trim());
                if (dbUser != null) {
                    if (!dbUser.isHoatDong()) {
                        return Optional.of("Tài khoản đã bị vô hiệu hóa.");
                    }
                    // Nếu tài khoản hoạt động, kiểm tra mật khẩu
                    if (dbUser.getMatKhau().equals(password)) {
                        this.currentUser = dbUser;
                        return Optional.empty();
                    }
                }
                return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
            } catch (Exception ex) {
                return Optional.of("Lỗi kết nối CSDL MySQL: " + ex.getMessage());
            }
        }

        List<TaiKhoan> list = DataStore.get().getTaiKhoans();
        Optional<TaiKhoan> opt = list.stream()
                .filter(u -> u.getTenDangNhap().equalsIgnoreCase(username.trim()))
                .findFirst();

        if (opt.isEmpty()) {
            return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        TaiKhoan u = opt.get();
        if (!u.isHoatDong()) {
            return Optional.of("Tài khoản đã bị vô hiệu hóa.");
        }

        if (!u.getMatKhau().equals(password)) {
            return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        this.currentUser = u;
        return Optional.empty();
    }

    public void logout() {
        this.currentUser = null;
    }

    public TaiKhoan getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public boolean isNhanVienOnly() {
        return currentUser != null && currentUser.isNhanVien();
    }

    public boolean isNhanVien() {
        return currentUser != null && currentUser.isNhanVien();
    }
}
