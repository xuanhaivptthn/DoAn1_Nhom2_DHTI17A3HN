package Model;

/**
 * Bảng: tai_khoan
 * Lưu thông tin đăng nhập và phân quyền người dùng hệ thống.
 */
public class TaiKhoan {
    /** PK, AUTO_INCREMENT */
    private String maTaiKhoan;
    /** NOT NULL, UNIQUE */
    private String tenDangNhap;
    /** NOT NULL - mật khẩu (đã mã hóa) */
    private String matKhau;
    /** ENUM: ADMIN | CHU_SAN | NHAN_VIEN | KHACH_HANG */
    private String quyenHan;
    /** varchar(20) - trạng thái tài khoản */
    private String trangThai;

    public TaiKhoan() {
    }

    public TaiKhoan(String maTaiKhoan, String tenDangNhap, String matKhau, String quyenHan, String trangThai) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyenHan = quyenHan;
        this.trangThai = trangThai;
    }

    /** Constructor 8-param tương thích TaiKhoanFormDialog cũ: (id, tenDangNhap, matKhau, hoTen, sdt, email, vaiTro, trangThai) */
    public TaiKhoan(int id, String tenDangNhap, String matKhau, String hoTen,
                    String soDienThoai, String email, String vaiTro, String trangThai) {
        this.maTaiKhoan  = String.format("TK%03d", id);
        this.tenDangNhap = tenDangNhap;
        this.matKhau     = matKhau;
        // Map vaiTro cũ sang quyenHan mới
        this.quyenHan = switch (vaiTro == null ? "" : vaiTro) {
            case "Admin"     -> "ADMIN";
            case "NhanVien"  -> "NHAN_VIEN";
            case "KhachHang" -> "KHACH_HANG";
            default -> vaiTro.toUpperCase().replace(" ", "_");
        };
        this.trangThai = switch (trangThai == null ? "" : trangThai) {
            case "HoatDong"  -> "HOAT_DONG";
            case "Khoa"      -> "KHOA";
            default -> trangThai.toUpperCase().replace(" ", "_");
        };
    }

    public String getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getQuyenHan() { return quyenHan; }
    public void setQuyenHan(String quyenHan) { this.quyenHan = quyenHan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // ─── Helper methods ────────────────────────────────────────────────────────

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(quyenHan);
    }

    public boolean isChuSan() {
        return "CHU_SAN".equalsIgnoreCase(quyenHan);
    }

    public boolean isNhanVien() {
        return "NHAN_VIEN".equalsIgnoreCase(quyenHan);
    }

    public boolean isKhachHang() {
        return "KHACH_HANG".equalsIgnoreCase(quyenHan);
    }

    public boolean isHoatDong() {
        return "HOAT_DONG".equalsIgnoreCase(trangThai);
    }

    public String getQuyenHanHienThi() {
        if (quyenHan == null) return "";
        return switch (quyenHan.toUpperCase()) {
            case "ADMIN"      -> "Quản trị viên";
            case "CHU_SAN"    -> "Chủ sân";
            case "NHAN_VIEN"  -> "Nhân viên";
            case "KHACH_HANG" -> "Khách hàng";
            default           -> quyenHan;
        };
    }

    public String getTrangThaiHienThi() {
        if (trangThai == null) return "";
        return switch (trangThai.toUpperCase()) {
            case "HOAT_DONG" -> "Hoạt động";
            case "KHOA"      -> "Đã khóa";
            default          -> trangThai;
        };
    }

    @Override
    public String toString() {
        return tenDangNhap + " [" + getQuyenHanHienThi() + "]";
    }

}
