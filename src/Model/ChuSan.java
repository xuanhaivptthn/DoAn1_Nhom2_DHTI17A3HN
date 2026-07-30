package Model;

/**
 * Bảng: chu_san
 * Lưu thông tin chủ sân bóng.
 * FK: maTaiKhoan → tai_khoan(maTaiKhoan)
 */
public class ChuSan {
    /** PK, AUTO_INCREMENT */
    private String maChuSan;
    /** FK → tai_khoan.maTaiKhoan, NOT NULL, UNIQUE */
    private String maTaiKhoan;
    /** NOT NULL */
    private String tenChuSan;
    /** NOT NULL, varchar(10) */
    private String soDienThoaiChuSan;

    public ChuSan() {
    }

    public ChuSan(String maChuSan, String maTaiKhoan, String tenChuSan, String soDienThoaiChuSan) {
        this.maChuSan = maChuSan;
        this.maTaiKhoan = maTaiKhoan;
        this.tenChuSan = tenChuSan;
        this.soDienThoaiChuSan = soDienThoaiChuSan;
    }

    public String getMaChuSan() { return maChuSan; }
    public void setMaChuSan(String maChuSan) { this.maChuSan = maChuSan; }

    public String getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getTenChuSan() { return tenChuSan; }
    public void setTenChuSan(String tenChuSan) { this.tenChuSan = tenChuSan; }

    public String getSoDienThoaiChuSan() { return soDienThoaiChuSan; }
    public void setSoDienThoaiChuSan(String soDienThoaiChuSan) { this.soDienThoaiChuSan = soDienThoaiChuSan; }

    @Override
    public String toString() {
        return tenChuSan + " (" + soDienThoaiChuSan + ")";
    }
}
