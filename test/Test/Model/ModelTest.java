package Test.Model;

import Model.KhuVucSan;
import Model.TaiKhoan;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testKhuVucSanDisplayLogic() {
        KhuVucSan san1 = new KhuVucSan("SAN001", "Sân 5 số 1", "San5", 200000, "HOAT_DONG");
        assertEquals("Sẵn sàng", san1.getTrangThaiHienThi());

        KhuVucSan san2 = new KhuVucSan("SAN002", "Sân 5 số 2", "San5", 200000, "BAO_TRI");
        assertEquals("Đang Bảo trì", san2.getTrangThaiHienThi());

        KhuVucSan san3 = new KhuVucSan("SAN003", "Sân 7 số 1", "San7", 350000, "DANG_THUE");
        assertEquals("Đang thuê", san3.getTrangThaiHienThi());
    }

    @Test
    public void testTaiKhoanDisplayLogic() {
        TaiKhoan tk1 = new TaiKhoan("TK001", "admin", "admin123", "ADMIN", "HOAT_DONG");
        assertEquals("Quản trị viên", tk1.getQuyenHanHienThi());
        assertEquals("Hoạt động", tk1.getTrangThaiHienThi());
        assertTrue(tk1.isAdmin());
        assertFalse(tk1.isNhanVien());
        assertTrue(tk1.isHoatDong());

        TaiKhoan tk2 = new TaiKhoan("TK002", "staff", "staff123", "NHAN_VIEN", "KHOA");
        assertEquals("Nhân viên", tk2.getQuyenHanHienThi());
        assertEquals("Đã khoá", tk2.getTrangThaiHienThi());
        assertFalse(tk2.isAdmin());
        assertTrue(tk2.isNhanVien());
        assertFalse(tk2.isHoatDong());
    }
}
