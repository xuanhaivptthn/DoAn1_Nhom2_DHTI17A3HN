package Model.DatSan;

import java.time.LocalDateTime;

public class ThongTinDatSan {
    private String maDatSan;
    private String maKH;
    private String maSan;
    private String maNV; // Nhân viên thực hiện đặt
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private double tienCoc;
    private String trangThai; // "Đã cọc", "Đã hủy", "Hoàn thành"
}
