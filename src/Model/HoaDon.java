package Model;

/**
 * Model HoaDon thuộc tính chuẩn theo Biểu đồ lớp lĩnh vực (llv.png).
 */
public class HoaDon {
    private String maHoaDon;
    private String ngayThanhToan;
    private float chiPhiSan;
    private float tongTienDichVu;
    private float giamGia;
    private float tongTien;
    private String phuongThucThanhToan;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, String ngayThanhToan, float chiPhiSan,
                  float tongTienDichVu, float giamGia, float tongTien, String phuongThucThanhToan) {
        this.maHoaDon = maHoaDon;
        this.ngayThanhToan = ngayThanhToan;
        this.chiPhiSan = chiPhiSan;
        this.tongTienDichVu = tongTienDichVu;
        this.giamGia = giamGia;
        this.tongTien = tongTien;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(String ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public float getChiPhiSan() { return chiPhiSan; }
    public void setChiPhiSan(float chiPhiSan) { this.chiPhiSan = chiPhiSan; }

    public float getTongTienDichVu() { return tongTienDichVu; }
    public void setTongTienDichVu(float tongTienDichVu) { this.tongTienDichVu = tongTienDichVu; }

    public float getGiamGia() { return giamGia; }
    public void setGiamGia(float giamGia) { this.giamGia = giamGia; }

    public float getTongTien() { return tongTien; }
    public void setTongTien(float tongTien) { this.tongTien = tongTien; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public float tinhTien() {
        this.tongTien = (this.chiPhiSan + this.tongTienDichVu) - this.giamGia;
        return this.tongTien;
    }

    public void xuatHoaDon() {
    }
}
