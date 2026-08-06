package Controller;

import Model.KhuVucSan;
import Utils.DataStore;
import DAO.KhuVucSanDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ danh mục Khu vực Sân bóng.
 * <p>
 * Lớp này xử lý các thao tác liên quan đến danh sách các sân bóng trong hệ thống:
 * lấy danh sách các sân bóng, thêm/chỉnh sửa thông tin sân bóng, xóa sân bóng
 * và đồng bộ trạng thái bảo trì của sân với lịch bảo trì đang diễn ra.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class KhuVucSanController {

    /**
     * Đối tượng DAO truy xuất dữ liệu danh mục Khu vực sân bóng trong CSDL MySQL.
     */
    private final KhuVucSanDAO khuVucSanDAO = new KhuVucSanDAO();

    /**
     * Khởi tạo đối tượng {@code KhuVucSanController} mặc định.
     */
    public KhuVucSanController() {}

    /**
     * Lấy toàn bộ danh sách khu vực sân bóng đang được quản lý.
     *
     * @return Danh sách các đối tượng {@link KhuVucSan}.
     */
    public List<KhuVucSan> getAllCourts() {
        // Lấy danh sách sân bóng từ DataStore
        return DataStore.get().getKhuVucs();
    }

    /**
     * Lưu mới hoặc cập nhật thông tin một sân bóng vào hệ thống.
     *
     * @param san Đối tượng sân bóng {@link KhuVucSan} chứa thông tin cần lưu.
     * @return {@code true} nếu thành công; {@code false} nếu tham số {@code san} bị {@code null}.
     */
    public boolean saveOrUpdateCourt(KhuVucSan san) {
        // Kiểm tra hợp lệ đối tượng sân bóng
        if (san == null) return false;

        // Nếu sân bóng chưa có trong danh sách DataStore thì thêm mới vào bộ nhớ
        if (!DataStore.get().getKhuVucs().contains(san)) {
            DataStore.get().getKhuVucs().add(san);
        }

        // Nếu sử dụng CSDL MySQL, chèn dữ liệu vào bảng sân bóng
        if (DataStore.isUseDatabase()) {
            khuVucSanDAO.insert(san);
        }
        return true;
    }

    /**
     * Xóa một sân bóng khỏi hệ thống dựa trên mã sân bóng.
     *
     * @param maSan Mã sân bóng cần xóa (ví dụ: "SAN001").
     * @return {@code true} nếu xóa thành công; {@code false} nếu mã sân bị {@code null}.
     */
    public boolean deleteCourt(String maSan) {
        // Kiểm tra mã sân bóng truyền vào
        if (maSan == null) return false;

        // Loại bỏ sân bóng khớp mã khỏi danh sách bộ nhớ DataStore
        DataStore.get().getKhuVucs().removeIf(s -> maSan.equalsIgnoreCase(s.getMaSan()));

        // Thực hiện xóa bản ghi tương ứng trong CSDL MySQL nếu đang kết nối
        if (DataStore.isUseDatabase()) {
            khuVucSanDAO.delete(maSan);
        }
        return true;
    }

    /**
     * Đồng bộ lại trạng thái bảo trì của tất cả các sân bóng dựa trên phiếu bảo trì đang hoạt động.
     */
    public void syncTrangThaiSanBaoTri() {
        // Ủy quyền quá trình kiểm tra và cập nhật trạng thái bảo trì cho DataStore
        DataStore.get().syncTrangThaiSanBaoTri();
    }
}
