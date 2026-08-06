package Model;

/**
 * Lớp model đại diện cho thông tin tài khoản người dùng (bảng tai_khoan trong CSDL).
 * <p>
 * Lớp này quản lý thông tin xác thực đăng nhập, phân quyền truy cập hệ thống
 * (ADMIN, CHU_SAN, NHAN_VIEN, KHACH_HANG) và trạng thái hoạt động của tài khoản.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class TaiKhoan {
    /** 
     * Mã tài khoản (Khoá chính, AUTO_INCREMENT trong CSDL hoặc mã dạng chuỗi TK001).
     */
    private String maTaiKhoan;

    /** 
     * Tên đăng nhập hệ thống (NOT NULL, UNIQUE).
     */
    private String tenDangNhap;

    /** 
     * Mật khẩu đăng nhập đã mã hóa hoặc chuỗi gốc (NOT NULL).
     */
    private String matKhau;

    /** 
     * Quyền hạn phân quyền sử dụng hệ thống (ENUM: ADMIN | CHU_SAN | NHAN_VIEN | KHACH_HANG).
     */
    private String quyenHan;

    /** 
     * Trạng thái hoạt động của tài khoản (Ví dụ: HOAT_DONG, KHOA, varchar(20)).
     */
    private String trangThai;

    /**
     * Khởi tạo một đối tượng TaiKhoan mới không tham số.
     */
    public TaiKhoan() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo đối tượng TaiKhoan đầy đủ 5 thuộc tính chuẩn CSDL.
     * 
     * @param maTaiKhoan  Mã tài khoản.
     * @param tenDangNhap Tên đăng nhập.
     * @param matKhau     Mật khẩu.
     * @param quyenHan    Mã quyền hạn/vai trò.
     * @param trangThai   Trạng thái tài khoản.
     */
    public TaiKhoan(String maTaiKhoan, String tenDangNhap, String matKhau, String quyenHan, String trangThai) {
        // Gán các giá trị tham số vào trường dữ liệu
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyenHan = quyenHan;
        this.trangThai = trangThai;
    }

    /**
     * Constructor 8 tham số tương thích với Dialog khởi tạo/sửa tài khoản UI cũ.
     * 
     * @param id          ID số nguyên của tài khoản.
     * @param tenDangNhap Tên đăng nhập.
     * @param matKhau     Mật khẩu.
     * @param hoTen       Họ tên người dùng (bỏ qua do lưu ở NhanVien/ChuSan).
     * @param soDienThoai Số điện thoại (bỏ qua).
     * @param email       Email (bỏ qua).
     * @param vaiTro      Chuỗi vai trò cũ cần chuyển đổi sang quyenHan mới.
     * @param trangThai   Chuỗi trạng thái cũ cần chuyển đổi sang trangThai mới.
     */
    public TaiKhoan(int id, String tenDangNhap, String matKhau, String hoTen,
                    String soDienThoai, String email, String vaiTro, String trangThai) {
        // Định dạng mã tài khoản chuỗi TK001 từ id số
        this.maTaiKhoan  = String.format("TK%03d", id);
        this.tenDangNhap = tenDangNhap;
        this.matKhau     = matKhau;
        
        // Chuẩn hóa tên vai trò cũ sang mã quyền hạn quy định chuẩn trong CSDL mới
        this.quyenHan = switch (vaiTro == null ? "" : vaiTro) {
            case "Admin"     -> "ADMIN";
            case "NhanVien"  -> "NHAN_VIEN";
            case "KhachHang" -> "KHACH_HANG";
            default -> vaiTro.toUpperCase().replace(" ", "_");
        };
        
        // Chuẩn hóa chuỗi trạng thái cũ về mã HOAT_DONG / KHOA
        this.trangThai = switch (trangThai == null ? "" : trangThai) {
            case "HoatDong"  -> "HOAT_DONG";
            case "Khoa"      -> "KHOA";
            default -> trangThai.toUpperCase().replace(" ", "_");
        };
    }

    /**
     * Lấy mã tài khoản.
     * 
     * @return Mã tài khoản.
     */
    public String getMaTaiKhoan() {
        // Trả về mã tài khoản
        return maTaiKhoan;
    }

    /**
     * Cập nhật mã tài khoản.
     * 
     * @param maTaiKhoan Mã tài khoản mới.
     */
    public void setMaTaiKhoan(String maTaiKhoan) {
        // Gán mã tài khoản
        this.maTaiKhoan = maTaiKhoan;
    }

    /**
     * Lấy tên đăng nhập.
     * 
     * @return Tên đăng nhập.
     */
    public String getTenDangNhap() {
        // Trả về tên đăng nhập
        return tenDangNhap;
    }

    /**
     * Cập nhật tên đăng nhập.
     * 
     * @param tenDangNhap Tên đăng nhập mới.
     */
    public void setTenDangNhap(String tenDangNhap) {
        // Gán tên đăng nhập
        this.tenDangNhap = tenDangNhap;
    }

    /**
     * Lấy mật khẩu tài khoản.
     * 
     * @return Chuỗi mật khẩu.
     */
    public String getMatKhau() {
        // Trả về mật khẩu
        return matKhau;
    }

    /**
     * Cập nhật mật khẩu tài khoản.
     * 
     * @param matKhau Mật khẩu mới.
     */
    public void setMatKhau(String matKhau) {
        // Gán mật khẩu
        this.matKhau = matKhau;
    }

    /**
     * Lấy mã quyền hạn của tài khoản.
     * 
     * @return Chuỗi mã quyền hạn.
     */
    public String getQuyenHan() {
        // Trả về mã quyền hạn
        return quyenHan;
    }

    /**
     * Cập nhật mã quyền hạn của tài khoản.
     * 
     * @param quyenHan Mã quyền hạn mới.
     */
    public void setQuyenHan(String quyenHan) {
        // Gán mã quyền hạn
        this.quyenHan = quyenHan;
    }

    /**
     * Lấy mã trạng thái của tài khoản.
     * 
     * @return Chuỗi trạng thái.
     */
    public String getTrangThai() {
        // Trả về trạng thái
        return trangThai;
    }

    /**
     * Cập nhật mã trạng thái của tài khoản.
     * 
     * @param trangThai Trạng thái mới.
     */
    public void setTrangThai(String trangThai) {
        // Gán trạng thái
        this.trangThai = trangThai;
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /**
     * Kiểm tra xem tài khoản có vai trò Quản trị viên (ADMIN) hay không.
     * 
     * @return true nếu là ADMIN.
     */
    public boolean isAdmin() {
        // So sánh quyền hạn với "ADMIN"
        return "ADMIN".equalsIgnoreCase(quyenHan);
    }

    /**
     * Kiểm tra xem tài khoản có vai trò Chủ sân (CHU_SAN) hay không.
     * 
     * @return true nếu là CHU_SAN.
     */
    public boolean isChuSan() {
        // So sánh quyền hạn với "CHU_SAN"
        return "CHU_SAN".equalsIgnoreCase(quyenHan);
    }

    /**
     * Kiểm tra xem tài khoản có vai trò Nhân viên (NHAN_VIEN) hay không.
     * 
     * @return true nếu là NHAN_VIEN.
     */
    public boolean isNhanVien() {
        // So sánh quyền hạn với "NHAN_VIEN"
        return "NHAN_VIEN".equalsIgnoreCase(quyenHan);
    }

    /**
     * Kiểm tra xem tài khoản có vai trò Khách hàng (KHACH_HANG) hay không.
     * 
     * @return true nếu là KHACH_HANG.
     */
    public boolean isKhachHang() {
        // So sánh quyền hạn với "KHACH_HANG"
        return "KHACH_HANG".equalsIgnoreCase(quyenHan);
    }

    /**
     * Kiểm tra xem tài khoản có đang ở trạng thái hoạt động hay không.
     * 
     * @return true nếu trangThai bằng "HOAT_DONG".
     */
    public boolean isHoatDong() {
        // So sánh trạng thái với "HOAT_DONG"
        return "HOAT_DONG".equalsIgnoreCase(trangThai);
    }

    /**
     * Lấy tên mô tả quyền hạn thân thiện hiển thị trên giao diện.
     * 
     * @return Chuỗi mô tả tiếng Việt ("Quản trị viên", "Chủ sân", "Nhân viên", "Khách hàng").
     */
    public String getQuyenHanHienThi() {
        // Kiểm tra quyền hạn null
        if (quyenHan == null) return "";
        // Chuyển đổi mã quyền hạn sang chuỗi tiếng Việt hiển thị
        return switch (quyenHan.toUpperCase()) {
            case "ADMIN"      -> "Quản trị viên";
            case "CHU_SAN"    -> "Chủ sân";
            case "NHAN_VIEN"  -> "Nhân viên";
            case "KHACH_HANG" -> "Khách hàng";
            default           -> quyenHan;
        };
    }

    /**
     * Lấy tên mô tả trạng thái thân thiện hiển thị trên giao diện.
     * 
     * @return Chuỗi mô tả tiếng Việt ("Hoạt động", "Đã khoá").
     */
    public String getTrangThaiHienThi() {
        // Kiểm tra trạng thái null
        if (trangThai == null) return "";
        // Chuyển đổi mã trạng thái sang chuỗi tiếng Việt
        return switch (trangThai.toUpperCase()) {
            case "HOAT_DONG" -> "Hoạt động";
            case "KHOA"      -> "Đã khoá";
            default          -> trangThai;
        };
    }

    /**
     * Chuyển đổi đối tượng TaiKhoan thành dạng chuỗi mô tả ngắn gọn.
     * 
     * @return Chuỗi định dạng "Tên đăng nhập [Tên quyền hạn hiển thị]".
     */
    @Override
    public String toString() {
        // Ghép tên đăng nhập và quyền hạn hiển thị
        return tenDangNhap + " [" + getQuyenHanHienThi() + "]";
    }
}
