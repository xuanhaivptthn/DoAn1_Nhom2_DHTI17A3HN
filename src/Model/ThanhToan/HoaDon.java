package Model.ThanhToan;

import java.time.LocalDateTime;

public class HoaDon {
    private String maHD;
    private String maDatSan; // Liên kết với thông tin đặt sân
    private String maNV; // Nhân viên lập hóa đơn
    private LocalDateTime ngayLap;
    private double tongTienSan;
    private double tongTienDichVu;
    private double tongTienThanhToan;
    private String trangThai; // "Chưa thanh toán", "Đã thanh toán"
}
