package Model;

/**
 * Bảng: khach_hang
 * Lưu thông tin khách hàng đặt sân bóng.
 */
public class KhachHang {
    /** PK, varchar(20) */
    private String maKhachHang;
    /** NOT NULL, varchar(100) */
    private String tenKhachHang;
    /** NOT NULL, UNIQUE, varchar(15) */
    private String soDienThoai;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
    }

    /** Constructor tương thích seed dữ liệu mẫu (id tự sinh mã) */
    public KhachHang(int id, String tenKhachHang, String soDienThoai) {
        this.maKhachHang = String.format("KH%03d", id);
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
    }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    // ─── Alias helpers tương thích với DatLich ───────────────────────────────
    public String getTenKhach() { return tenKhachHang; }
    public void setTenKhach(String tenKhach) { this.tenKhachHang = tenKhach; }

    public String getSoDienThoaiKhach() { return soDienThoai; }
    public void setSoDienThoaiKhach(String soDienThoaiKhach) { this.soDienThoai = soDienThoaiKhach; }

    @Override
    public String toString() {
        return tenKhachHang + " (" + soDienThoai + ")";
    }
}
