package Model;

/**
 * Bảng: nhan_vien
 * Lưu thông tin nhân viên quản lý sân bóng.
 * FK: maTaiKhoan → tai_khoan(maTaiKhoan)
 */
public class NhanVien {
    /** PK, AUTO_INCREMENT */
    private String maNhanVien;
    /** FK → tai_khoan.maTaiKhoan, NOT NULL, UNIQUE */
    private String maTaiKhoan;
    /** NOT NULL */
    private String hoTenNhanVien;
    /** NOT NULL, varchar(10) */
    private String soDienThoaiNhanVien;
    /** NOT NULL */
    private String diaChi;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String maTaiKhoan, String hoTenNhanVien,
                    String soDienThoaiNhanVien, String diaChi) {
        this.maNhanVien = maNhanVien;
        this.maTaiKhoan = maTaiKhoan;
        this.hoTenNhanVien = hoTenNhanVien;
        this.soDienThoaiNhanVien = soDienThoaiNhanVien;
        this.diaChi = diaChi;
    }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getHoTenNhanVien() { return hoTenNhanVien; }
    public void setHoTenNhanVien(String hoTenNhanVien) { this.hoTenNhanVien = hoTenNhanVien; }

    public String getSoDienThoaiNhanVien() { return soDienThoaiNhanVien; }
    public void setSoDienThoaiNhanVien(String soDienThoaiNhanVien) { this.soDienThoaiNhanVien = soDienThoaiNhanVien; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    @Override
    public String toString() {
        return hoTenNhanVien + " (" + soDienThoaiNhanVien + ")";
    }
}
