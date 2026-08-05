package Controller;

import Model.TaiKhoan;
import Model.KhachHang;
import Utils.DataStore;
import Utils.SessionManager;
import DAO.TaiKhoanDAO;
import DAO.KhachHangDAO;

import java.util.List;
import java.util.Optional;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ Tài khoản người dùng & Khách hàng.
 */
public class TaiKhoanController {

    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public TaiKhoanController() {}

    public Optional<String> login(String username, String password) {
        return SessionManager.get().login(username, password);
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        return DataStore.get().getTaiKhoans();
    }

    public List<KhachHang> getAllKhachHang() {
        return DataStore.get().getKhachHangs();
    }

    public KhachHang findKhachHangBySoDienThoai(String sdt) {
        return DataStore.get().findKhachHangBySoDienThoai(sdt);
    }

    public KhachHang saveOrUpdateKhachHang(String tenKhach, String sdt) {
        return DataStore.get().saveOrUpdateKhachHang(tenKhach, sdt);
    }
}
