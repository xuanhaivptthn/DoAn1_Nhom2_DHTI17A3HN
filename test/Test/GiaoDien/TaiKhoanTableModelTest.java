package Test.GiaoDien;

import GiaoDien.Panels.TaiKhoanTableModel;
import Model.TaiKhoan;
import Utils.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaiKhoanTableModelTest {
    private TaiKhoanTableModel model;

    @BeforeEach
    public void setUp() {
        DataStore.setUseDatabase(false);
        DataStore.get().reseed();
        model = new TaiKhoanTableModel();
        
        List<TaiKhoan> list = new ArrayList<>();
        list.add(new TaiKhoan("TK001", "admin", "admin123", "ADMIN", "HOAT_DONG"));
        list.add(new TaiKhoan("TK002", "staff1", "nv123", "NHAN_VIEN", "HOAT_DONG"));
        list.add(new TaiKhoan("TK003", "staff2", "nv456", "NHAN_VIEN", "KHOA"));
        
        model.setData(list);
    }

    @Test
    public void testSetDataAndCounts() {
        assertEquals(3, model.getRowCount());
        assertEquals(6, model.getColumnCount());
        assertEquals("Tên đăng nhập", model.getColumnName(1));
    }

    @Test
    public void testGetAt() {
        TaiKhoan tk = model.getAt(0);
        assertNotNull(tk);
        assertEquals("admin", tk.getTenDangNhap());
        
        assertNull(model.getAt(5));
    }

    @Test
    public void testAddUpdateAndRemove() {
        TaiKhoan newTk = new TaiKhoan("TK004", "staff3", "nv789", "NHAN_VIEN", "HOAT_DONG");
        model.addTaiKhoan(newTk);
        assertEquals(4, model.getRowCount());
        assertEquals("staff3", model.getAt(3).getTenDangNhap());

        // Update
        TaiKhoan updatedTk = new TaiKhoan("TK004", "staff3_updated", "nv789", "NHAN_VIEN", "HOAT_DONG");
        model.updateTaiKhoan(updatedTk);
        assertEquals("staff3_updated", model.getAt(3).getTenDangNhap());

        // Remove
        model.removeTaiKhoan("TK004");
        assertEquals(3, model.getRowCount());
    }

    @Test
    public void testExistsUsername() {
        assertTrue(model.existsUsername("admin", "exclude_dummy"));
        assertFalse(model.existsUsername("admin", "TK001"));
        assertFalse(model.existsUsername("non_existent", "exclude_dummy"));
    }

    @Test
    public void testFilter() {
        // Filter by keyword
        model.filter("staff", "Tất cả", "Tất cả");
        assertEquals(2, model.getRowCount()); // staff1 and staff2 match

        // Filter by role
        model.filter("", "Quản trị viên", "Tất cả");
        assertEquals(1, model.getRowCount()); // only admin matches

        // Filter by status
        model.filter("", "Tất cả", "Đã khoá");
        assertEquals(1, model.getRowCount()); // only staff2 is locked

        // Match none
        model.filter("non_existent", "Nhân viên", "Đã khoá");
        assertEquals(0, model.getRowCount());
    }
}
