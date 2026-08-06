package Model;

/**
 * Lớp model đại diện cho thông tin nhân viên (bảng nhan_vien trong CSDL).
 * <p>
 * Lớp này lưu trữ chi tiết hồ sơ nhân viên phục vụ, lễ tân và quản lý tại sân bóng,
 * bao gồm mã nhân viên, mã tài khoản hệ thống liên kết, họ tên, số điện thoại và địa chỉ.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class NhanVien {
    /** 
     * Mã nhân viên (Khoá chính, AUTO_INCREMENT trong DB hoặc chuỗi định dạng NV001).
     */
    private String maNhanVien;

    /** 
     * Mã tài khoản hệ thống của nhân viên (Khoá ngoại tham chiếu tai_khoan.maTaiKhoan, NOT NULL, UNIQUE).
     */
    private String maTaiKhoan;

    /** 
     * Họ tên đầy đủ của nhân viên (NOT NULL).
     */
    private String hoTenNhanVien;

    /** 
     * Số điện thoại của nhân viên (NOT NULL, varchar(10)).
     */
    private String soDienThoaiNhanVien;

    /** 
     * Địa chỉ nơi ở/thường trú của nhân viên (NOT NULL).
     */
    private String diaChi;

    /**
     * Khởi tạo một đối tượng NhanVien mới không tham số.
     */
    public NhanVien() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo đối tượng NhanVien với đầy đủ các thuộc tính.
     * 
     * @param maNhanVien          Mã nhân viên.
     * @param maTaiKhoan        Mã tài khoản hệ thống liên kết.
     * @param hoTenNhanVien       Họ tên đầy đủ.
     * @param soDienThoaiNhanVien Số điện thoại liên hệ.
     * @param diaChi              Địa chỉ liên lạc.
     */
    public NhanVien(String maNhanVien, String maTaiKhoan, String hoTenNhanVien,
                    String soDienThoaiNhanVien, String diaChi) {
        // Gán các thuộc tính nhân viên
        this.maNhanVien = maNhanVien;
        this.maTaiKhoan = maTaiKhoan;
        this.hoTenNhanVien = hoTenNhanVien;
        this.soDienThoaiNhanVien = soDienThoaiNhanVien;
        this.diaChi = diaChi;
    }

    /**
     * Lấy mã nhân viên.
     * 
     * @return Mã nhân viên.
     */
    public String getMaNhanVien() {
        // Trả về mã nhân viên
        return maNhanVien;
    }

    /**
     * Cập nhật mã nhân viên.
     * 
     * @param maNhanVien Mã nhân viên mới.
     */
    public void setMaNhanVien(String maNhanVien) {
        // Gán mã nhân viên
        this.maNhanVien = maNhanVien;
    }

    /**
     * Lấy mã tài khoản liên kết của nhân viên.
     * 
     * @return Mã tài khoản hệ thống.
     */
    public String getMaTaiKhoan() {
        // Trả về mã tài khoản
        return maTaiKhoan;
    }

    /**
     * Cập nhật mã tài khoản liên kết của nhân viên.
     * 
     * @param maTaiKhoan Mã tài khoản mới.
     */
    public void setMaTaiKhoan(String maTaiKhoan) {
        // Gán mã tài khoản
        this.maTaiKhoan = maTaiKhoan;
    }

    /**
     * Lấy họ tên nhân viên.
     * 
     * @return Họ tên nhân viên.
     */
    public String getHoTenNhanVien() {
        // Trả về họ tên nhân viên
        return hoTenNhanVien;
    }

    /**
     * Cập nhật họ tên nhân viên.
     * 
     * @param hoTenNhanVien Họ tên nhân viên mới.
     */
    public void setHoTenNhanVien(String hoTenNhanVien) {
        // Gán họ tên nhân viên
        this.hoTenNhanVien = hoTenNhanVien;
    }

    /**
     * Lấy số điện thoại của nhân viên.
     * 
     * @return Số điện thoại.
     */
    public String getSoDienThoaiNhanVien() {
        // Trả về số điện thoại nhân viên
        return soDienThoaiNhanVien;
    }

    /**
     * Cập nhật số điện thoại của nhân viên.
     * 
     * @param soDienThoaiNhanVien Số điện thoại mới.
     */
    public void setSoDienThoaiNhanVien(String soDienThoaiNhanVien) {
        // Gán số điện thoại nhân viên
        this.soDienThoaiNhanVien = soDienThoaiNhanVien;
    }

    /**
     * Lấy địa chỉ liên lạc của nhân viên.
     * 
     * @return Địa chỉ.
     */
    public String getDiaChi() {
        // Trả về địa chỉ nhân viên
        return diaChi;
    }

    /**
     * Cập nhật địa chỉ liên lạc của nhân viên.
     * 
     * @param diaChi Địa chỉ mới.
     */
    public void setDiaChi(String diaChi) {
        // Gán địa chỉ nhân viên
        this.diaChi = diaChi;
    }

    /**
     * Tra cứu đối tượng TaiKhoan liên kết từ bộ nhớ DataStore qua maTaiKhoan.
     * 
     * @return Đối tượng TaiKhoan hoặc null nếu chưa gắn mã tài khoản.
     */
    public TaiKhoan getTaiKhoan() {
        // Tra cứu thông tin tài khoản qua DataStore nếu maTaiKhoan không null
        if (maTaiKhoan != null) {
            return Utils.DataStore.get().findTaiKhoanByMa(maTaiKhoan);
        }
        return null;
    }

    /**
     * Chuyển đổi thông tin nhân viên thành dạng chuỗi hiển thị rút gọn.
     * 
     * @return Chuỗi định dạng "Họ tên (Số điện thoại)".
     */
    @Override
    public String toString() {
        // Ghép họ tên và số điện thoại nhân viên
        return hoTenNhanVien + " (" + soDienThoaiNhanVien + ")";
    }
}
