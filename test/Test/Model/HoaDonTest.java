package Test.Model;

import Model.HoaDon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HoaDonTest {

    @Test
    public void testHoaDonDefaultConstructor() {
        HoaDon hd = new HoaDon();
        assertNull(hd.getMaHoaDon());
        assertEquals(0.0, hd.getTongTien());
    }

    @Test
    public void testHoaDonParameterizedConstructor() {
        HoaDon hd = new HoaDon("HD001", "DL001", "NV001", "2026-08-05 18:30:00", 300000.0, 50000.0, 10000.0, 340000.0, "Tiền mặt");
        assertEquals("HD001", hd.getMaHoaDon());
        assertEquals("DL001", hd.getMaLichDat());
        assertEquals("NV001", hd.getMaNhanVien());
        assertEquals("2026-08-05 18:30:00", hd.getNgayThanhToan());
        assertEquals(300000.0, hd.getChiPhiSan());
        assertEquals(50000.0, hd.getTongTienDichVu());
        assertEquals(10000.0, hd.getGiamGia());
        assertEquals(340000.0, hd.getTongTien());
        assertEquals("Tiền mặt", hd.getPhuongThucThanhToan());
    }

    @Test
    public void testHoaDonCalculation() {
        HoaDon hd = new HoaDon();
        hd.setChiPhiSan(400000.0);
        hd.setTongTienDichVu(120000.0);
        hd.setGiamGia(50000.0);
        
        double total = hd.tinhTien();
        assertEquals(470000.0, total);
        assertEquals(470000.0, hd.getTongTien());
    }
}
