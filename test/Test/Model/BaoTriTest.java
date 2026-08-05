package Test.Model;

import Model.BaoTri;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BaoTriTest {

    @Test
    public void testBaoTriDefaultConstructor() {
        BaoTri bt = new BaoTri();
        assertEquals("DANG_BAO_TRI", bt.getTrangThaiPhieu());
        assertTrue(bt.isDangBaoTri());
        assertEquals("Đang bảo trì", bt.getTrangThaiHienThi());
    }

    @Test
    public void testBaoTriParameterizedConstructor() {
        BaoTri bt = new BaoTri("BT001", "SAN001", "Hỏng lưới", "2026-08-05", "2026-08-06", "HOAN_THANH");
        assertEquals("BT001", bt.getMaPhieuBaoTri());
        assertEquals("SAN001", bt.getMaSan());
        assertEquals("Hỏng lưới", bt.getNoiDung());
        assertEquals("2026-08-05", bt.getNgayBatDau());
        assertEquals("2026-08-06", bt.getNgayKetThuc());
        assertEquals("HOAN_THANH", bt.getTrangThaiPhieu());
        assertFalse(bt.isDangBaoTri());
        assertEquals("Hoàn thành", bt.getTrangThaiHienThi());
    }

    @Test
    public void testBaoTriLegacyConstructor() {
        BaoTri bt = new BaoTri(1, "BT002", 5, "Sân A2", "Sơn lại vạch kẻ", "Anh Nam", "2026-08-05", "2026-08-06", 50000.0, "DangXuLy");
        assertEquals("BT002", bt.getMaPhieuBaoTri());
        assertEquals("Sân A2", bt.getTenSan());
        assertEquals("Sơn lại vạch kẻ", bt.getNoiDung());
        assertEquals("2026-08-05", bt.getNgayBatDau());
        assertEquals("2026-08-06", bt.getNgayKetThuc());
        assertEquals("DANG_BAO_TRI", bt.getTrangThaiPhieu());
        assertTrue(bt.isDangBaoTri());
        assertEquals("Đang bảo trì", bt.getTrangThaiHienThi());
        
        BaoTri bt2 = new BaoTri(2, "BT003", 5, "Sân A2", "Bảo dưỡng đèn", "Anh Nam", "2026-08-05", "2026-08-06", 50000.0, "Huy");
        assertEquals("HUY", bt2.getTrangThaiPhieu());
        assertEquals("Đã hủy", bt2.getTrangThaiHienThi());
    }
}
