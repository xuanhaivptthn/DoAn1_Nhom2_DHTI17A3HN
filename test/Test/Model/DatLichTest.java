package Test.Model;

import Model.DatLich;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatLichTest {

    @Test
    public void testDatLichInitialization() {
        DatLich dl = new DatLich();
        assertEquals("ChuaThanhToan", dl.getTrangThaiTT());
        assertEquals("", dl.getDichVuKem());
        assertEquals(0.0, dl.getDatCoc());
        assertNotNull(dl.getSelectedDvMap());
        assertNotNull(dl.getSelectedDoAnMap());
    }

    @Test
    public void testDatLichParameterizedConstructor() {
        DatLich dl = new DatLich(1, "DL001", 10, "Sân 5A", "Nguyễn Văn A", "0987654321", "2026-08-05", "17:00", "18:30", 300000.0, "DaXacNhan", "TK002", "Ghi chú test");
        assertEquals("DL001", dl.getMaLichDat());
        assertEquals("Sân 5A", dl.getTenSan());
        assertEquals("Nguyễn Văn A", dl.getTenKhach());
        assertEquals("0987654321", dl.getSoDienThoaiKhach());
        assertEquals("2026-08-05", dl.getNgayDat());
        assertEquals("17:00", dl.getGioBatDau());
        assertEquals("18:30", dl.getGioKetThuc());
        assertEquals(300000.0, dl.getTienSan());
        assertEquals(300000.0, dl.getTongTien());
        assertEquals("DaXacNhan", dl.getTrangThai());
        assertEquals("TK002", dl.getMaTaiKhoan());
        assertEquals("Ghi chú test", dl.getGhiChu());
    }

    @Test
    public void testGettersAndSetters() {
        DatLich dl = new DatLich();
        dl.setMaLichDat("DL002");
        dl.setMaSan("SAN002");
        dl.setMaKhachHang("KH002");
        dl.setTenKhach("Trần Văn B");
        dl.setSoDienThoaiKhach("0912345678");
        dl.setNgayDat("2026-08-06");
        dl.setGioBatDau("18:00");
        dl.setGioKetThuc("19:00");
        dl.setTrangThai("DaThanhToan");
        dl.setGhiChu("Ghi chú 2");
        dl.setTienSan(400000.0);
        dl.setTienDichVu(50000.0);
        dl.setTongTien(450000.0);
        dl.setDatCoc(100000.0);
        dl.setTrangThaiTT("ThanhToanMotPhan");

        assertEquals("DL002", dl.getMaLichDat());
        assertEquals("SAN002", dl.getMaSan());
        assertEquals("KH002", dl.getMaKhachHang());
        assertEquals("Trần Văn B", dl.getTenKhach());
        assertEquals("0912345678", dl.getSoDienThoaiKhach());
        assertEquals("2026-08-06", dl.getNgayDat());
        assertEquals("18:00", dl.getGioBatDau());
        assertEquals("19:00", dl.getGioKetThuc());
        assertEquals("DaThanhToan", dl.getTrangThai());
        assertEquals("Ghi chú 2", dl.getGhiChu());
        assertEquals(400000.0, dl.getTienSan());
        assertEquals(50000.0, dl.getTienDichVu());
        assertEquals(450000.0, dl.getTongTien());
        assertEquals(100000.0, dl.getDatCoc());
        assertEquals("ThanhToanMotPhan", dl.getTrangThaiTT());
    }

    @Test
    public void testMapSelections() {
        DatLich dl = new DatLich();
        dl.getSelectedDvMap().put(1, 2);
        dl.getSelectedDoAnMap().put(101, 5);

        assertEquals(2, dl.getSelectedDvMap().get(1));
        assertEquals(5, dl.getSelectedDoAnMap().get(101));
    }
}
