package Model;

/**
 * Model Tài khoản thuộc tính chuẩn theo Biểu đồ lớp lĩnh vực (llv.png).
 */
public class TaiKhoan {
    private int id;
    private String maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private String quyenHan;   // Admin | NhanVien | KhachHang
    private String trangThai;  // HoatDong | Khoa

    // Thuộc tính bổ sung theo sơ đồ llv.png
    private String maNhanVien;
    private String hoTenNhanVien;
    private String soDienThoaiNhanVien;
    private String diaChi;
    private String chucVu;
    private String maChuSan;
    private String hoTenChuSan;
    private String soDienThoaiChuSan;

    // Các thuộc tính kế thừa UI
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String vaiTro;

    public TaiKhoan() {
    }

    public TaiKhoan(int id, String tenDangNhap, String matKhau, String hoTen,
                    String soDienThoai, String email, String vaiTro, String trangThai) {
        this.id = id;
        this.maTaiKhoan = "TK" + id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.vaiTro = vaiTro;
        this.quyenHan = vaiTro;
        this.trangThai = trangThai;

        if ("Admin".equalsIgnoreCase(vaiTro)) {
            this.maChuSan = "CS" + id;
            this.hoTenChuSan = hoTen;
            this.soDienThoaiChuSan = soDienThoai;
        } else {
            this.maNhanVien = "NV" + id;
            this.hoTenNhanVien = hoTen;
            this.soDienThoaiNhanVien = soDienThoai;
            this.chucVu = "Nhân viên quản lý sân bóng";
        }
    }

    // GETTERS & SETTERS THEO SƠ ĐỒ LLV.PNG
    public String getMaTaiKhoan() { return maTaiKhoan != null ? maTaiKhoan : "TK" + id; }
    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getQuyenHan() { return quyenHan != null ? quyenHan : vaiTro; }
    public void setQuyenHan(String quyenHan) { this.quyenHan = quyenHan; this.vaiTro = quyenHan; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTenNhanVien() { return hoTenNhanVien != null ? hoTenNhanVien : hoTen; }
    public void setHoTenNhanVien(String hoTenNhanVien) { this.hoTenNhanVien = hoTenNhanVien; this.hoTen = hoTenNhanVien; }

    public String getSoDienThoaiNhanVien() { return soDienThoaiNhanVien != null ? soDienThoaiNhanVien : soDienThoai; }
    public void setSoDienThoaiNhanVien(String soDienThoaiNhanVien) { this.soDienThoaiNhanVien = soDienThoaiNhanVien; this.soDienThoai = soDienThoaiNhanVien; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public String getMaChuSan() { return maChuSan; }
    public void setMaChuSan(String maChuSan) { this.maChuSan = maChuSan; }

    public String getHoTenChuSan() { return hoTenChuSan; }
    public void setHoTenChuSan(String hoTenChuSan) { this.hoTenChuSan = hoTenChuSan; }

    public String getSoDienThoaiChuSan() { return soDienThoaiChuSan; }
    public void setSoDienThoaiChuSan(String soDienThoaiChuSan) { this.soDienThoaiChuSan = soDienThoaiChuSan; }

    // GETTERS & SETTERS HIỆN CÓ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; this.hoTenNhanVien = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; this.soDienThoaiNhanVien = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; this.quyenHan = vaiTro; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getVaiTroHienThi() {
        if (vaiTro == null) return "";
        return switch (vaiTro) {
            case "Admin" -> "Quản trị viên";
            case "NhanVien" -> "Nhân viên";
            default -> vaiTro;
        };
    }

    public String getTrangThaiHienThi() {
        if (trangThai == null) return "";
        return switch (trangThai) {
            case "HoatDong" -> "Hoạt động";
            case "Khoa" -> "Đã khóa";
            default -> trangThai;
        };
    }

    public boolean isHoatDong() {
        return "HoatDong".equals(trangThai);
    }

    public boolean isAdmin() {
        return "Admin".equals(vaiTro);
    }

    public boolean isNhanVien() {
        return "NhanVien".equals(vaiTro) || "Admin".equals(vaiTro);
    }

    public boolean isNhanVienOnly() {
        return "NhanVien".equals(vaiTro);
    }
}
