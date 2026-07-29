package Utils;

import Model.PhienLamViec;
import Model.TaiKhoan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Quản lý phiên làm việc của người dùng hiện tại trong ứng dụng.
 */
public final class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TaiKhoan currentUser;
    private PhienLamViec currentSession;

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
                return Optional.of("Lỗi kết nối CSDL MySQL! Vui lòng bật MySQL trên XAMPP hoặc chọn 'Dữ liệu mẫu'.");
            }
            try {
                TaiKhoan dbUser = new DAO.TaiKhoanDAO().findByUsernameAndPassword(username.trim(), password);
                if (dbUser != null) {
                    if (!dbUser.isHoatDong()) {
                        return Optional.of("Tài khoản của bạn hiện đang bị khóa.");
                    }
                    this.currentUser = dbUser;
                    int nextId = DataStore.get().getPhienHistory().size() + 1;
                    String sid = String.format("SES-%04d", nextId);
                    String now = LocalDateTime.now().format(FMT);
                    this.currentSession = new PhienLamViec(sid, dbUser.getTenDangNhap(), dbUser.getHoTen(), dbUser.getVaiTro(),
                            now, null, "DangHoatDong", "127.0.0.1", "Desktop App (Java Swing)");
                    DataStore.get().getPhienHistory().add(0, currentSession);
                    try { new DAO.PhienLamViecDAO().insert(currentSession); } catch (Exception ignored) {}
                    return Optional.empty();
                } else {
                    return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
                }
            } catch (Exception ex) {
                return Optional.of("Lỗi kết nối CSDL MySQL: " + ex.getMessage());
            }
        }

        List<TaiKhoan> list = DataStore.get().getTaiKhoans();
        Optional<TaiKhoan> opt = list.stream()
                .filter(u -> u.getTenDangNhap().equalsIgnoreCase(username.trim()) && u.getMatKhau().equals(password))
                .findFirst();

        if (opt.isEmpty()) {
            return Optional.of("Tên đăng nhập hoặc mật khẩu không đúng.");
        }

        TaiKhoan u = opt.get();
        if (!u.isHoatDong()) {
            return Optional.of("Tài khoản của bạn hiện đang bị khóa.");
        }

        this.currentUser = u;

        int nextId = DataStore.get().getPhienHistory().size() + 1;
        String sid = String.format("SES-%04d", nextId);
        String now = LocalDateTime.now().format(FMT);

        this.currentSession = new PhienLamViec(sid, u.getTenDangNhap(), u.getHoTen(), u.getVaiTro(),
                now, null, "DangHoatDong", "127.0.0.1", "Desktop App (Java Swing)");

        DataStore.get().getPhienHistory().add(0, currentSession);

        return Optional.empty();
    }

    public void logout() {
        if (currentSession != null) {
            currentSession.setThoiGianDangXuat(LocalDateTime.now().format(FMT));
            currentSession.setTrangThai("DaDangXuat");
        }
        this.currentUser = null;
        this.currentSession = null;
    }

    public void forceEndSession(String sessionId) {
        if (sessionId == null) return;
        DataStore.get().getPhienHistory().stream()
                .filter(p -> p.getSessionId().equals(sessionId) && "DangHoatDong".equals(p.getTrangThai()))
                .findFirst()
                .ifPresent(p -> {
                    p.setThoiGianDangXuat(LocalDateTime.now().format(FMT));
                    p.setTrangThai("DaDangXuat");
                });
    }

    public TaiKhoan getCurrentUser() {
        return currentUser;
    }

    public PhienLamViec getCurrentSession() {
        return currentSession;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public boolean isNhanVienOnly() {
        return currentUser != null && currentUser.isNhanVienOnly();
    }

    public boolean isNhanVien() {
        return currentUser != null && currentUser.isNhanVien();
    }
}
