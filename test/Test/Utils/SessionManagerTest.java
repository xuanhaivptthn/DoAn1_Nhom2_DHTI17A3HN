package Test.Utils;

import Model.TaiKhoan;
import Utils.DataStore;
import Utils.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.parallel.ResourceLock;

@ResourceLock("SessionManager")
public class SessionManagerTest {

    @BeforeEach
    public void setUp() {
        DataStore.setUseDatabase(false);
        DataStore.get().reseed();
        SessionManager.get().logout();
    }

    @Test
    public void testLoginEmptyCredentials() {
        Optional<String> errorOpt = SessionManager.get().login("", "admin123");
        assertTrue(errorOpt.isPresent());
        assertEquals("Vui lòng nhập tên đăng nhập.", errorOpt.get());

        Optional<String> errorOpt2 = SessionManager.get().login("admin", "");
        assertTrue(errorOpt2.isPresent());
        assertEquals("Vui lòng nhập mật khẩu.", errorOpt2.get());
    }

    @Test
    public void testLoginInvalidCredentials() {
        Optional<String> errorOpt = SessionManager.get().login("non_existent_user", "password");
        assertTrue(errorOpt.isPresent());
        assertEquals("Tên đăng nhập hoặc mật khẩu không đúng.", errorOpt.get());

        Optional<String> errorOpt2 = SessionManager.get().login("admin", "wrong_password");
        assertTrue(errorOpt2.isPresent());
        assertEquals("Tên đăng nhập hoặc mật khẩu không đúng.", errorOpt2.get());
    }

    @Test
    public void testLoginSuccessAdmin() {
        // admin / admin123 is a seeded admin account
        Optional<String> errorOpt = SessionManager.get().login("admin", "admin123");
        assertFalse(errorOpt.isPresent());
        
        assertTrue(SessionManager.get().isLoggedIn());
        assertTrue(SessionManager.get().isAdmin());
        assertFalse(SessionManager.get().isNhanVienOnly());
        
        TaiKhoan current = SessionManager.get().getCurrentUser();
        assertNotNull(current);
        assertEquals("admin", current.getTenDangNhap());
        assertEquals("ADMIN", current.getQuyenHan());
    }

    @Test
    public void testLoginSuccessNhanVien() {
        // nhanvien01 / nv123456 is a seeded employee account
        Optional<String> errorOpt = SessionManager.get().login("nhanvien01", "nv123456");
        assertFalse(errorOpt.isPresent());
        
        assertTrue(SessionManager.get().isLoggedIn());
        assertFalse(SessionManager.get().isAdmin());
        assertTrue(SessionManager.get().isNhanVien());
        assertTrue(SessionManager.get().isNhanVienOnly());
    }

    @Test
    public void testLogout() {
        SessionManager.get().login("admin", "admin123");
        assertTrue(SessionManager.get().isLoggedIn());

        SessionManager.get().logout();
        assertFalse(SessionManager.get().isLoggedIn());
        assertNull(SessionManager.get().getCurrentUser());
        assertNull(SessionManager.get().getCurrentSession());
    }
}
