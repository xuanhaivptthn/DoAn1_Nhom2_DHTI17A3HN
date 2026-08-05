package Test.Utils;

import Model.KhuVucSan;
import Model.BaoTri;
import Utils.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.parallel.ResourceLock;

@ResourceLock("SessionManager")
public class DataStoreTest {

    @BeforeEach
    public void setUp() {
        DataStore.setUseDatabase(false);
        DataStore.get().reseed();
    }

    @Test
    public void testDataStoreInitialization() {
        assertNotNull(DataStore.get());
        assertTrue(DataStore.get().getTaiKhoans().size() > 0);
        assertTrue(DataStore.get().getKhuVucs().size() > 0);
    }

    @Test
    public void testFindKhachHangBySoDienThoai() {
        // We know seedDefaultKhachHangs adds Anh Duc with 0912345678
        var kh = DataStore.get().findKhachHangBySoDienThoai("0912345678");
        assertNotNull(kh);
        assertEquals("Anh Đức (FC Anh Em)", kh.getTenKhachHang());
        
        var notFound = DataStore.get().findKhachHangBySoDienThoai("0000000000");
        assertNull(notFound);
    }

    @Test
    public void testIsSanBaoTriVoiNgay() {
        KhuVucSan san = DataStore.get().getKhuVucs().stream()
                .filter(k -> "SAN001".equals(k.getMaSan()))
                .findFirst().orElse(null);
        
        assertNotNull(san);
        
        // Let's add a maintenance for SAN001 from today to tomorrow
        String today = java.time.LocalDate.now().toString();
        String tomorrow = java.time.LocalDate.now().plusDays(1).toString();
        
        BaoTri bt = new BaoTri("BT999", "SAN001", "Bảo dưỡng cỏ nhân tạo", today, tomorrow, "DANG_BAO_TRI");
        DataStore.get().getBaoTris().add(bt);
        
        assertTrue(DataStore.get().isSanBaoTriVoiNgay(san, today));
        assertTrue(DataStore.get().isSanBaoTriVoiNgay(san, tomorrow));
        assertFalse(DataStore.get().isSanBaoTriVoiNgay(san, java.time.LocalDate.now().minusDays(2).toString()));
    }
}
