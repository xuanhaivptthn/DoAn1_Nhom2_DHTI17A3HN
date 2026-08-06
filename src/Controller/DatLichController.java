package Controller;

import Model.DatLich;
import Model.KhuVucSan;
import Utils.DataStore;
import DAO.DatLichDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ nghiệp vụ Đặt sân bóng.
 * <p>
 * Lớp này xử lý các thao tác liên quan đến đặt lịch sân bóng bao gồm:
 * xem danh sách đặt sân, tạo mới phiếu đặt, cập nhật thông tin đặt sân,
 * cập nhật trạng thái phiếu và thanh toán, kiểm tra trùng lịch đặt sân,
 * kiểm tra lịch bảo trì trùng với ngày đặt.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class DatLichController {

    /**
     * Đối tượng DAO thực hiện các thao tác CRUD dữ liệu đặt lịch với CSDL MySQL.
     */
    private final DatLichDAO datLichDAO = new DatLichDAO();

    /**
     * Bộ điều khiển hóa đơn dùng để tự động tạo/cập nhật hóa đơn khi đặt sân hoàn thành.
     */
    private final HoaDonController hoaDonController = new HoaDonController();

    /**
     * Khởi tạo đối tượng {@code DatLichController} mặc định.
     */
    public DatLichController() {}

    /**
     * Lấy danh sách tất cả các phiếu đặt sân từ bộ nhớ hệ thống.
     *
     * @return Danh sách các đối tượng {@link DatLich}.
     */
    public List<DatLich> getAllBookings() {
        // Lấy danh sách phiếu đặt lịch từ DataStore
        return DataStore.get().getDatLichs();
    }

    /**
     * Tạo mới một lượt đặt sân bóng.
     * <p>
     * Nếu mã khách hàng bị khuyết, phương thức sẽ tự động tra cứu/tạo thông tin khách hàng dựa trên tên và SĐT.
     * Sau đó lưu lượt đặt lịch vào DataStore và CSDL (nếu dùng CSDL).
     * </p>
     *
     * @param d Đối tượng {@link DatLich} thông tin lịch đặt sân.
     * @return {@code true} nếu tạo lịch đặt thành công; {@code false} nếu tham số {@code d} bị {@code null}.
     */
    public boolean createBooking(DatLich d) {
        // Kiểm tra đối tượng lịch đặt truyền vào
        if (d == null) return false;

        // Tự động tìm hoặc lưu thông tin khách hàng nếu chưa có mã khách hàng
        if (d.getMaKhachHang() == null || d.getMaKhachHang().isBlank()) {
            Model.KhachHang kh = DataStore.get().saveOrUpdateKhachHang(d.getTenKhach(), d.getSoDienThoaiKhach());
            if (kh != null) {
                d.setMaKhachHang(kh.getMaKhachHang());
            }
        }

        // Thêm phiếu đặt lịch mới vào DataStore
        DataStore.get().getDatLichs().add(d);

        // Lưu thông tin lượt đặt lịch vào cơ sở dữ liệu nếu kết nối CSDL bật
        if (DataStore.isUseDatabase()) {
            datLichDAO.insert(d);
        }
        return true;
    }

    /**
     * Cập nhật thông tin phiếu đặt sân hiện có và đồng bộ hóa đơn tương ứng.
     *
     * @param d Đối tượng {@link DatLich} chứa thông tin sửa đổi.
     * @return {@code true} nếu cập nhật thành công; {@code false} nếu {@code d} bị {@code null}.
     */
    public boolean updateBooking(DatLich d) {
        // Kiểm tra đối tượng hợp lệ
        if (d == null) return false;

        // Cập nhật CSDL MySQL nếu chế độ CSDL đang hoạt động
        if (DataStore.isUseDatabase()) {
            datLichDAO.update(d);
        }

        // Tự động lưu hoặc cập nhật hóa đơn thanh toán cho lượt đặt này
        hoaDonController.saveOrUpdateHoaDonForBooking(d, "Tiền mặt");
        return true;
    }

    /**
     * Cập nhật trạng thái của phiếu đặt sân và hình thức thanh toán.
     * <p>
     * Các trạng thái bao gồm: ChoXacNhan, DaXacNhan, HoanThanh (kèm cập nhật hóa đơn), DaHuy.
     * </p>
     *
     * @param d           Đối tượng {@link DatLich} cần cập nhật.
     * @param newStatus   Trạng thái mới ("ChoXacNhan", "DaXacNhan", "HoanThanh", "DaHuy").
     * @param phuongThucTT Phương thức thanh toán (ví dụ: "Tiền mặt", "Chuyển khoản").
     * @return {@code true} nếu đổi trạng thái thành công; {@code false} nếu dữ liệu không hợp lệ.
     */
    public boolean updateBookingStatus(DatLich d, String newStatus, String phuongThucTT) {
        // Kiểm tra đối tượng và trạng thái mới truyền vào
        if (d == null || newStatus == null) return false;

        // Xử lý chuyển đổi trạng thái đặt sân và trạng thái thanh toán tương ứng
        switch (newStatus) {
            case "ChoXacNhan" -> {
                d.setTrangThai("ChoXacNhan");
                d.setTrangThaiTT("ChuaThanhToan");
            }
            case "DaXacNhan" -> {
                d.setTrangThai("DaXacNhan");
                d.setTrangThaiTT("ChuaThanhToan");
            }
            case "HoanThanh" -> {
                d.setTrangThai("HoanThanh");
                d.setTrangThaiTT("DaThanhToan");
                // Khi giao dịch hoàn thành, lưu hoặc cập nhật lại hóa đơn thanh toán
                hoaDonController.saveOrUpdateHoaDonForBooking(d, phuongThucTT != null ? phuongThucTT : "Tiền mặt");
            }
            case "DaHuy" -> {
                d.setTrangThai("DaHuy");
            }
        }

        // Cập nhật trạng thái xuống CSDL nếu bật CSDL MySQL
        if (DataStore.isUseDatabase()) {
            datLichDAO.update(d);
        }
        return true;
    }

    /**
     * Kiểm tra xem khung giờ đặt sân dự kiến có bị trùng (overlap) với bất kỳ lịch đặt sân hợp lệ nào khác không.
     *
     * @param maSan           Mã sân bóng muốn kiểm tra.
     * @param ngayDat         Ngày đặt sân (định dạng YYYY-MM-DD).
     * @param gioBD           Giờ bắt đầu đặt sân (định dạng HH:mm).
     * @param gioKT           Giờ kết thúc đặt sân (định dạng HH:mm).
     * @param excludeMaLichDat Mã lịch đặt muốn loại trừ (dùng khi cập nhật lại lịch đặt đang có).
     * @return Đối tượng {@link DatLich} bị trùng giờ nếu phát hiện xung đột; {@code null} nếu khung giờ trống.
     */
    public DatLich findOverlapBooking(String maSan, String ngayDat, String gioBD, String gioKT, String excludeMaLichDat) {
        // Chuyển đổi chuỗi giờ bắt đầu và kết thúc thành tổng số phút trong ngày
        int newStart = toMinutes(gioBD);
        int newEnd = toMinutes(gioKT);

        // Khung giờ không hợp lệ (giờ bắt đầu lớn hơn hoặc bằng giờ kết thúc)
        if (newStart >= newEnd) return null;

        // Duyệt qua tất cả lịch đặt trong bộ nhớ để đối soát khung giờ
        for (DatLich existing : DataStore.get().getDatLichs()) {
            // Bỏ qua chính mã lịch đặt đang chỉnh sửa (nếu có)
            if (excludeMaLichDat != null && excludeMaLichDat.equalsIgnoreCase(existing.getMaLichDat())) continue;
            // Bỏ qua nếu mã sân không trùng khớp
            if (maSan != null && !maSan.equalsIgnoreCase(existing.getMaSan())) continue;
            // Bỏ qua nếu ngày đặt khác nhau
            if (ngayDat != null && !ngayDat.trim().equalsIgnoreCase(existing.getNgayDat().trim())) continue;
            // Bỏ qua các lịch đặt đã bị hủy
            if ("DaHuy".equalsIgnoreCase(existing.getTrangThai())) continue;

            // Đổi khung giờ của lịch đã tồn tại ra tổng số phút
            int exStart = toMinutes(existing.getGioBatDau());
            int exEnd = toMinutes(existing.getGioKetThuc());

            // Thuật toán kiểm tra xung đột giao nhau của 2 khoảng thời gian [start1, end1] và [start2, end2]
            if (newStart < exEnd && newEnd > exStart) {
                return existing; // Trả về phiếu bị xung đột
            }
        }
        return null; // Không có trùng lịch
    }

    /**
     * Kiểm tra xem sân bóng có đang thuộc thời gian bảo trì vào ngày được chỉ định hay không.
     *
     * @param san     Đối tượng sân bóng {@link KhuVucSan}.
     * @param dateStr Chuỗi ngày muốn kiểm tra (YYYY-MM-DD).
     * @return {@code true} nếu sân đang bảo trì vào ngày đó; {@code false} nếu ngược lại.
     */
    public boolean isSanBaoTriVoiNgay(KhuVucSan san, String dateStr) {
        // Gọi hàm kiểm tra lịch bảo trì theo ngày từ DataStore
        return DataStore.get().isSanBaoTriVoiNgay(san, dateStr);
    }

    /**
     * Hàm tiện ích nội bộ chuyển đổi chuỗi giờ "HH:mm" thành tổng số phút tính từ đầu ngày (00:00).
     *
     * @param timeStr Chuỗi thời gian dạng "HH:mm" (ví dụ: "08:30" -> 510 phút).
     * @return Tổng số phút; trả về 0 nếu định dạng sai hoặc bị lỗi.
     */
    private static int toMinutes(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        try {
            String[] parts = timeStr.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0; // Trả về 0 nếu bắt gặp lỗi parse số
        }
    }
}
