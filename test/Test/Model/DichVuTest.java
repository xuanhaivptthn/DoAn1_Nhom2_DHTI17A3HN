package Test.Model;

import Model.DichVu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DichVuTest {

    @Test
    public void testDichVuInitialization() {
        DichVu dv = new DichVu("DV001", "Trọng tài chính", "Nhân sự", 150000.0, "Mô tả trọng tài");
        assertEquals("DV001", dv.getMaDichVu());
        assertEquals("Trọng tài chính", dv.getTenDichVu());
        assertEquals("Nhân sự", dv.getLoaiDichVu());
        assertEquals(150000.0, dv.getGia());
        assertEquals("Mô tả trọng tài", dv.getMoTa());
        assertEquals(0, dv.getSoLuongTon());
        assertEquals("", dv.getDonVi());
    }

    @Test
    public void testDichVuInventoryManagement() {
        DichVu dv = new DichVu(101, "Bóng thi đấu", 10, 80000.0, "Nhà cung cấp Động Lực");
        assertEquals("HH101", dv.getMaDichVu());
        assertEquals("Bóng thi đấu", dv.getTenHangHoa());
        assertEquals(10, dv.getSoLuongTon());
        assertEquals(80000.0, dv.getDonGia());
        assertEquals("Nhà cung cấp Động Lực", dv.getNhaCungCap());
        assertEquals("cái", dv.getDonVi());
        assertEquals("Vật tư kho", dv.getLoaiDichVu());
        assertFalse(dv.isSapHet());

        // Test nhapKho
        dv.nhapKho(5);
        assertEquals(15, dv.getSoLuongTon());

        // Test xuatKho success
        boolean res = dv.xuatKho(12);
        assertTrue(res);
        assertEquals(3, dv.getSoLuongTon());
        
        // Test isSapHet trigger
        assertTrue(dv.isSapHet());

        // Test xuatKho failure (exceed stock)
        boolean resFail = dv.xuatKho(5);
        assertFalse(resFail);
        assertEquals(3, dv.getSoLuongTon());
    }

    @Test
    public void testDichVuMultiParamConstructor() {
        DichVu dv = new DichVu(202, "Nước suối", "Thức uống giải khát", 10000.0, "Chai", "Còn hàng", 4, 2);
        assertEquals("DV202", dv.getMaDichVu());
        assertEquals("Nước suối", dv.getTenDichVu());
        assertEquals("Thức uống giải khát", dv.getMoTa());
        assertEquals(10000.0, dv.getGia());
        assertEquals("Chai", dv.getDonVi());
        assertEquals("Chai", dv.getLoaiDichVu());
        assertEquals(4, dv.getSoLuongTon());
        assertTrue(dv.isSapHet()); // 4 is <= tonToiThieu (which is 5 by default)
    }
}
