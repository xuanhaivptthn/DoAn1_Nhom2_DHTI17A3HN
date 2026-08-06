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
 * <p>
 * Lớp này cung cấp các nghiệp vụ xác thực đăng nhập người dùng (thông qua {@link SessionManager}),
 * truy vấn danh sách tài khoản người dùng, quản lý hồ sơ khách hàng, tìm kiếm khách hàng theo SĐT,
 * cũng như lưu/cập nhật thông tin khách hàng.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class TaiKhoanController {

    /**
     * Đối tượng DAO quản lý truy xuất bảng Tài khoản trong CSDL MySQL.
     */
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    /**
     * Đối tượng DAO quản lý truy xuất bảng Khách hàng trong CSDL MySQL.
     */
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    /**
     * Khởi tạo đối tượng {@code TaiKhoanController} mặc định.
     */
    public TaiKhoanController() {}

    /**
     * Thực hiện xác thực đăng nhập người dùng vào hệ thống.
     *
     * @param username Tên đăng nhập.
     * @param password Mật khẩu đăng nhập.
     * @return {@link Optional} chứa thông báo lỗi nếu đăng nhập thất bại; hoặc {@code Optional.empty()} nếu đăng nhập thành công.
     */
    public Optional<String> login(String username, String password) {
        // Ủy quyền xử lý xác thực và lưu phiên đăng nhập cho SessionManager
        return SessionManager.get().login(username, password);
    }

    /**
     * Lấy toàn bộ danh sách tài khoản người dùng hệ thống.
     *
     * @return Danh sách các đối tượng {@link TaiKhoan}.
     */
    public List<TaiKhoan> getAllTaiKhoan() {
        // Lấy danh sách tài khoản hệ thống từ DataStore
        return DataStore.get().getTaiKhoans();
    }

    /**
     * Lấy toàn bộ danh sách thông tin khách hàng đã đăng ký hoặc đặt sân.
     *
     * @return Danh sách các đối tượng {@link KhachHang}.
     */
    public List<KhachHang> getAllKhachHang() {
        // Trả về danh sách thông tin khách hàng từ DataStore
        return DataStore.get().getKhachHangs();
    }

    /**
     * Tìm kiếm thông tin khách hàng dựa trên số điện thoại liên lạc.
     *
     * @param sdt Chuỗi số điện thoại khách hàng cần tra cứu.
     * @return Đối tượng {@link KhachHang} nếu tìm thấy; {@code null} nếu không tìm thấy.
     */
    public KhachHang findKhachHangBySoDienThoai(String sdt) {
        // Tra cứu khách hàng theo số điện thoại trong bộ nhớ DataStore
        return DataStore.get().findKhachHangBySoDienThoai(sdt);
    }

    /**
     * Lưu mới hoặc cập nhật tên của khách hàng theo số điện thoại.
     *
     * @param tenKhach Tên khách hàng.
     * @param sdt      Số điện thoại của khách hàng.
     * @return Đối tượng {@link KhachHang} đã được tạo hoặc cập nhật thành công.
     */
    public KhachHang saveOrUpdateKhachHang(String tenKhach, String sdt) {
        // Ủy quyền tạo/cập nhật thông tin khách hàng cho DataStore
        return DataStore.get().saveOrUpdateKhachHang(tenKhach, sdt);
    }
}
