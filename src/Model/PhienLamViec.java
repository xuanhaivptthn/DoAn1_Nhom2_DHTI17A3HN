package Model;

/**
 * Phiên làm việc (session) sau khi đăng nhập.
 */
public class PhienLamViec {
    private int id;
    private String sessionId;
    private String tenDangNhap;
    private String hoTen;
    private String vaiTro;
    private String thoiGianDangNhap;
    private String thoiGianDangXuat;
    private String trangThai;   // DangHoatDong | DaDangXuat | HetHan
    private String diaChiIp;
    private String thietBi;

    public PhienLamViec() {
    }

    public PhienLamViec(String sessionId, String tenDangNhap, String hoTen, String vaiTro,
                        String thoiGianDangNhap, String thoiGianDangXuat,
                        String trangThai) {
        this(sessionId, tenDangNhap, hoTen, vaiTro, thoiGianDangNhap, thoiGianDangXuat, trangThai, "127.0.0.1", "Desktop App (Java Swing)");
    }

    public PhienLamViec(String sessionId, String tenDangNhap, String hoTen, String vaiTro,
                        String thoiGianDangNhap, String thoiGianDangXuat,
                        String trangThai, String diaChiIp, String thietBi) {
        this.sessionId = sessionId;
        this.tenDangNhap = tenDangNhap;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
        this.thoiGianDangNhap = thoiGianDangNhap;
        this.thoiGianDangXuat = thoiGianDangXuat;
        this.trangThai = trangThai;
        this.diaChiIp = diaChiIp;
        this.thietBi = thietBi;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public String getThoiGianDangNhap() { return thoiGianDangNhap; }
    public void setThoiGianDangNhap(String thoiGianDangNhap) { this.thoiGianDangNhap = thoiGianDangNhap; }
    public String getThoiGianDangXuat() { return thoiGianDangXuat; }
    public void setThoiGianDangXuat(String thoiGianDangXuat) { this.thoiGianDangXuat = thoiGianDangXuat; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getDiaChiIp() { return diaChiIp; }
    public void setDiaChiIp(String diaChiIp) { this.diaChiIp = diaChiIp; }
    public String getThietBi() { return thietBi; }
    public void setThietBi(String thietBi) { this.thietBi = thietBi; }

    public String getTrangThaiHienThi() {
        return switch (trangThai == null ? "" : trangThai) {
            case "DangHoatDong" -> "Đang hoạt động";
            case "DaDangXuat" -> "Đã đăng xuất";
            case "HetHan" -> "Hết hạn";
            default -> trangThai;
        };
    }

    public String getVaiTroHienThi() {
        return switch (vaiTro == null ? "" : vaiTro) {
            case "Admin" -> "Chủ sân / Quản trị";
            case "NhanVien" -> "Nhân viên";
            case "KhachHang" -> "Khách hàng";
            default -> vaiTro;
        };
    }
}
