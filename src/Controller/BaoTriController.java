package Controller;

import Model.BaoTri;
import Utils.DataStore;
import DAO.BaoTriDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ công tác Bảo trì Sân bóng.
 * <p>
 * Lớp này chịu trách nhiệm trung gian điều phối giữa giao diện người dùng (UI)
 * và tầng truy xuất dữ liệu (DataStore/DAO) để thực hiện các thao tác:
 * lấy danh sách bảo trì, tạo mới phiếu bảo trì, cập nhật thông tin bảo trì
 * và xóa phiếu bảo trì, đồng thời đồng bộ trạng thái sân bóng.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class BaoTriController {

    /**
     * Đối tượng DAO truy xuất và thao tác dữ liệu Bảo trì trong CSDL MySQL.
     */
    private final BaoTriDAO baoTriDAO = new BaoTriDAO();

    /**
     * Khởi tạo một đối tượng {@code BaoTriController} mặc định.
     */
    public BaoTriController() {}

    /**
     * Lấy toàn bộ danh sách phiếu bảo trì sân bóng từ bộ nhớ DataStore.
     *
     * @return Danh sách các đối tượng {@link BaoTri} hiện có.
     */
    public List<BaoTri> getAllBaoTri() {
        // Trả về danh sách phiếu bảo trì từ bộ lưu trữ tạm thời DataStore
        return DataStore.get().getBaoTris();
    }

    /**
     * Thêm mới một phiếu bảo trì sân bóng vào hệ thống.
     * <p>
     * Phương thức sẽ lưu thông tin phiếu bảo trì vào DataStore, đồng thời lưu vào CSDL
     * nếu chế độ sử dụng CSDL đang bật, sau đó tiến hành đồng bộ lại trạng thái bảo trì của các sân bóng.
     * </p>
     *
     * @param bt Đối tượng {@link BaoTri} chứa thông tin phiếu bảo trì cần tạo mới.
     * @return {@code true} nếu thêm mới thành công; {@code false} nếu tham số đầu vào bị {@code null}.
     */
    public boolean createBaoTri(BaoTri bt) {
        // Kiểm tra đối tượng truyền vào có hợp lệ hay không
        if (bt == null) return false;

        // Thêm phiếu bảo trì vào danh sách bộ nhớ DataStore
        DataStore.get().getBaoTris().add(bt);

        // Nếu hệ thống cấu hình sử dụng CSDL MySQL thì ghi xuống cơ sở dữ liệu
        if (DataStore.isUseDatabase()) {
            baoTriDAO.insert(bt);
        }

        // Cập nhật lại trạng thái bảo trì của sân tương ứng (HOAT_DONG -> BAO_TRI)
        DataStore.get().syncTrangThaiSanBaoTri();
        return true;
    }

    /**
     * Cập nhật thông tin của một phiếu bảo trì đã tồn tại.
     * <p>
     * Cập nhật CSDL (nếu sử dụng CSDL) và thực hiện đồng bộ lại trạng thái của các sân bóng.
     * </p>
     *
     * @param bt Đối tượng {@link BaoTri} chứa thông tin bảo trì đã sửa đổi.
     * @return {@code true} nếu cập nhật thành công; {@code false} nếu tham số đầu vào bị {@code null}.
     */
    public boolean updateBaoTri(BaoTri bt) {
        // Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (bt == null) return false;

        // Nếu bật kết nối CSDL, thực hiện gọi DAO cập nhật thông tin trong MySQL
        if (DataStore.isUseDatabase()) {
            baoTriDAO.update(bt);
        }

        // Đồng bộ lại trạng thái các sân bóng sau khi thay đổi thông tin bảo trì
        DataStore.get().syncTrangThaiSanBaoTri();
        return true;
    }

    /**
     * Xóa phiếu bảo trì sân bóng theo mã phiếu bảo trì.
     * <p>
     * Loại bỏ phiếu khỏi danh sách trong DataStore, xóa khỏi CSDL (nếu dùng CSDL)
     * và đồng bộ lại trạng thái của sân bóng.
     * </p>
     *
     * @param maPhieuBaoTri Mã phiếu bảo trì cần xóa (ví dụ: "BT001").
     * @return {@code true} nếu xóa thành công; {@code false} nếu mã phiếu bị {@code null}.
     */
    public boolean deleteBaoTri(String maPhieuBaoTri) {
        // Kiểm tra mã phiếu bảo trì truyền vào
        if (maPhieuBaoTri == null) return false;

        // Loại bỏ phiếu bảo trì khớp mã khỏi danh sách lưu trữ bộ nhớ
        DataStore.get().getBaoTris().removeIf(b -> maPhieuBaoTri.equalsIgnoreCase(b.getMaPhieuBaoTri()));

        // Xóa dữ liệu tương ứng trong CSDL nếu đang kết nối MySQL
        if (DataStore.isUseDatabase()) {
            baoTriDAO.delete(maPhieuBaoTri);
        }

        // Cập nhật và đồng bộ lại trạng thái sân bóng sau khi xóa phiếu bảo trì
        DataStore.get().syncTrangThaiSanBaoTri();
        return true;
    }
}
