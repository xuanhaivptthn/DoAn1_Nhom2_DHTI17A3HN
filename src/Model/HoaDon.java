package Model;

/**
 * Bảng: hoa_don
 * Lưu thông tin hóa đơn thanh toán cho lịch đặt sân.
 * FK: maLichDat  → lich_dat_san(maLichDat)
 *     maNhanVien → nhan_vien(maNhanVien)
 */
public class HoaDon {
    /** PK, AUTO_INCREMENT, varchar(20) */
    private String maHoaDon;
    /** FK → lich_dat_san.maLichDat, NOT NULL, UNIQUE */
    private String maLichDat;
    /** FK → nhan_vien.maNhanVien, NOT NULL */
    private String maNhanVien;
    /** NOT NULL, DEFAULT CURRENT_TIMESTAMP */
    private String ngayThanhToan;
    /** NOT NULL, DECIMAL(12,2) - tiền thuê sân bóng */
    private double chiPhiSan;
    /** NOT NULL, DEFAULT 0, DECIMAL(12,2) - tiền các dịch vụ phát sinh */
    private double tongTienDichVu;
    /** NOT NULL, DEFAULT 0, DECIMAL(12,2) - tiền giảm giá/khuyến mãi */
    private double giamGia;
    /** NOT NULL, DECIMAL(12,2) - số tiền thực thu của khách */
    private double tongTien;
    /** NOT NULL, varchar(100) */
    private String phuongThucThanhToan;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, String maLichDat, String maNhanVien,
                  String ngayThanhToan, double chiPhiSan, double tongTienDichVu,
                  double giamGia, double tongTien, String phuongThucThanhToan) {
        this.maHoaDon           = maHoaDon;
        this.maLichDat          = maLichDat;
        this.maNhanVien         = maNhanVien;
        this.ngayThanhToan      = ngayThanhToan;
        this.chiPhiSan          = chiPhiSan;
        this.tongTienDichVu     = tongTienDichVu;
        this.giamGia            = giamGia;
        this.tongTien           = tongTien;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    /** Constructor tương thích với code UI cũ (không có maLichDat, maNhanVien) */
    public HoaDon(String maHoaDon, String ngayThanhToan, double chiPhiSan,
                  double tongTienDichVu, double giamGia, double tongTien,
                  String phuongThucThanhToan) {
        this(maHoaDon, null, null, ngayThanhToan, chiPhiSan,
             tongTienDichVu, giamGia, tongTien, phuongThucThanhToan);
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaLichDat() { return maLichDat; }
    public void setMaLichDat(String maLichDat) { this.maLichDat = maLichDat; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(String ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public double getChiPhiSan() { return chiPhiSan; }
    public void setChiPhiSan(double chiPhiSan) { this.chiPhiSan = chiPhiSan; }

    public double getTongTienDichVu() { return tongTienDichVu; }
    public void setTongTienDichVu(double tongTienDichVu) { this.tongTienDichVu = tongTienDichVu; }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /** Tính lại tongTien = chiPhiSan + tongTienDichVu - giamGia */
    public double tinhTien() {
        this.tongTien = (this.chiPhiSan + this.tongTienDichVu) - this.giamGia;
        return this.tongTien;
    }
}
