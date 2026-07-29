package Model;

/**
 * Model Khách hàng đặt sân bóng.
 * Hệ thống tự động lưu thông tin khách hàng khi tạo đơn đặt sân
 * để hỗ trợ gợi ý / điền nhanh cho các lần đặt sân tiếp theo.
 */
public class KhachHang {
    private int id;
    private String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String ghiChu;
    private int soLanDatSan;

    public KhachHang() {
    }

    public KhachHang(int id, String hoTen, String soDienThoai, String email, String ghiChu) {
        this(id, hoTen, soDienThoai, email, ghiChu, 1);
    }

    public KhachHang(int id, String hoTen, String soDienThoai, String email, String ghiChu, int soLanDatSan) {
        this.id = id;
        this.maKhachHang = String.format("KH%03d", id);
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ghiChu = ghiChu;
        this.soLanDatSan = soLanDatSan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaKhachHang() { return maKhachHang != null ? maKhachHang : String.format("KH%03d", id); }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public int getSoLanDatSan() { return soLanDatSan; }
    public void setSoLanDatSan(int soLanDatSan) { this.soLanDatSan = soLanDatSan; }
    public void tangSoLanDat() { this.soLanDatSan++; }

    @Override
    public String toString() {
        return hoTen + " (" + soDienThoai + ")";
    }
}
