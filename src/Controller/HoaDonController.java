package Controller;

import Model.DatLich;
import Model.HoaDon;
import Utils.DataStore;
import DAO.HoaDonDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ nghiệp vụ Hóa đơn thanh toán.
 * <p>
 * Lớp này thực hiện vai trò cầu nối giữa tầng giao diện hiển thị hóa đơn,
 * thống kê doanh thu và bộ lưu trữ CSDL MySQL / bộ nhớ DataStore. Quản lý
 * các thao tác lấy danh sách hóa đơn, tự động tạo/cập nhật hóa đơn từ phiếu đặt sân,
 * thêm mới, chỉnh sửa và xóa hóa đơn thanh toán.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class HoaDonController {

    /**
     * Đối tượng DAO hỗ trợ truy xuất và thao tác bảng hóa đơn trong CSDL MySQL.
     */
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    /**
     * Khởi tạo đối tượng {@code HoaDonController} mặc định.
     */
    public HoaDonController() {}

    /**
     * Lấy toàn bộ danh sách hóa đơn thanh toán hiện có từ bộ nhớ DataStore.
     *
     * @return Danh sách các đối tượng {@link HoaDon}.
     */
    public List<HoaDon> getAllInvoices() {
        // Trả về danh sách hóa đơn được lưu trữ trong DataStore
        return DataStore.get().getHoaDons();
    }

    /**
     * Tự động tính toán, tạo mới hoặc cập nhật hóa đơn tương ứng với thông tin đặt sân.
     *
     * @param d           Đối tượng thông tin đặt sân {@link DatLich}.
     * @param phuongThucTT Phương thức thanh toán áp dụng (ví dụ: "Tiền mặt", "Chuyển khoản").
     * @return Đối tượng {@link HoaDon} sau khi đã được lưu hoặc cập nhật thành công.
     */
    public HoaDon saveOrUpdateHoaDonForBooking(DatLich d, String phuongThucTT) {
        // Ủy quyền việc sinh hoặc cập nhật hóa đơn đặt sân cho DataStore
        return DataStore.get().saveOrUpdateHoaDonForBooking(d, phuongThucTT);
    }

    /**
     * Thêm mới một hóa đơn thanh toán vào bộ nhớ và cơ sở dữ liệu.
     *
     * @param h Đối tượng {@link HoaDon} cần thêm vào hệ thống.
     * @return {@code true} nếu thêm hóa đơn thành công; {@code false} nếu dữ liệu truyền vào bị {@code null} hoặc ghi CSDL thất bại.
     */
    public boolean insertInvoice(HoaDon h) {
        // Kiểm tra hợp lệ đối tượng hóa đơn
        if (h == null) return false;

        // Nếu hóa đơn chưa tồn tại trong DataStore thì thêm mới
        if (!DataStore.get().getHoaDons().contains(h)) {
            DataStore.get().getHoaDons().add(h);
        }

        // Nếu chế độ CSDL MySQL đang kích hoạt thì ghi dữ liệu hóa đơn xuống CSDL
        if (DataStore.isUseDatabase()) {
            return hoaDonDAO.insert(h);
        }
        return true;
    }

    /**
     * Cập nhật thông tin chi tiết của một hóa đơn thanh toán.
     *
     * @param h Đối tượng {@link HoaDon} mang thông tin sửa đổi.
     * @return {@code true} nếu cập nhật thành công; {@code false} nếu tham số {@code h} bị {@code null} hoặc cập nhật CSDL lỗi.
     */
    public boolean updateInvoice(HoaDon h) {
        // Kiểm tra đối tượng tham số
        if (h == null) return false;

        // Nếu sử dụng CSDL MySQL, thực hiện gọi DAO cập nhật dữ liệu hóa đơn
        if (DataStore.isUseDatabase()) {
            return hoaDonDAO.update(h);
        }
        return true;
    }

    /**
     * Xóa một hóa đơn khỏi hệ thống theo mã hóa đơn.
     *
     * @param maHoaDon Mã định danh hóa đơn cần xóa (ví dụ: "HD001").
     * @return {@code true} nếu xóa thành công; {@code false} nếu mã hóa đơn bị {@code null} hoặc thao tác CSDL thất bại.
     */
    public boolean deleteInvoice(String maHoaDon) {
        // Kiểm tra mã hóa đơn đầu vào
        if (maHoaDon == null) return false;

        // Xóa hóa đơn có mã phù hợp khỏi bộ nhớ DataStore
        DataStore.get().getHoaDons().removeIf(h -> maHoaDon.equalsIgnoreCase(h.getMaHoaDon()));

        // Thực hiện xóa dòng tương ứng trong bảng CSDL MySQL nếu đang dùng CSDL
        if (DataStore.isUseDatabase()) {
            return hoaDonDAO.delete(maHoaDon);
        }
        return true;
    }
}
