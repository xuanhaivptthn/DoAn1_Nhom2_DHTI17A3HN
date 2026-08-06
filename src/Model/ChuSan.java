package Model;

/**
 * Lớp model đại diện cho thông tin chủ sân bóng (bảng chu_san trong CSDL).
 * <p>
 * Lớp này lưu trữ thông tin cá nhân của chủ sở hữu/quản lý cụm sân bóng,
 * bao gồm mã chủ sân, liên kết tài khoản hệ thống, tên chủ sân và số điện thoại liên lạc.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChuSan {
    /** 
     * Mã chủ sân bóng (Khoá chính, tự động tăng / định danh dạng chuỗi).
     */
    private String maChuSan;

    /** 
     * Mã tài khoản hệ thống của chủ sân (Khoá ngoại tham chiếu tới bảng tai_khoan, NOT NULL, UNIQUE).
     */
    private String maTaiKhoan;

    /** 
     * Họ và tên của chủ sân bóng (NOT NULL).
     */
    private String tenChuSan;

    /** 
     * Số điện thoại liên hệ của chủ sân (NOT NULL, varchar(10)).
     */
    private String soDienThoaiChuSan;

    /**
     * Khởi tạo một đối tượng ChuSan mới không tham số.
     */
    public ChuSan() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo một đối tượng ChuSan với đầy đủ các thuộc tính.
     * 
     * @param maChuSan          Mã chủ sân bóng.
     * @param maTaiKhoan        Mã tài khoản hệ thống liên kết.
     * @param tenChuSan         Họ và tên chủ sân bóng.
     * @param soDienThoaiChuSan Số điện thoại liên lạc.
     */
    public ChuSan(String maChuSan, String maTaiKhoan, String tenChuSan, String soDienThoaiChuSan) {
        // Gán các giá trị tham số vào trường dữ liệu tương ứng
        this.maChuSan = maChuSan;
        this.maTaiKhoan = maTaiKhoan;
        this.tenChuSan = tenChuSan;
        this.soDienThoaiChuSan = soDienThoaiChuSan;
    }

    /**
     * Lấy mã chủ sân bóng.
     * 
     * @return Mã chủ sân.
     */
    public String getMaChuSan() {
        // Trả về mã chủ sân
        return maChuSan;
    }

    /**
     * Cập nhật mã chủ sân bóng.
     * 
     * @param maChuSan Mã chủ sân mới.
     */
    public void setMaChuSan(String maChuSan) {
        // Cập nhật mã chủ sân
        this.maChuSan = maChuSan;
    }

    /**
     * Lấy mã tài khoản liên kết của chủ sân.
     * 
     * @return Mã tài khoản hệ thống.
     */
    public String getMaTaiKhoan() {
        // Trả về mã tài khoản
        return maTaiKhoan;
    }

    /**
     * Cập nhật mã tài khoản liên kết.
     * 
     * @param maTaiKhoan Mã tài khoản hệ thống mới.
     */
    public void setMaTaiKhoan(String maTaiKhoan) {
        // Cập nhật mã tài khoản
        this.maTaiKhoan = maTaiKhoan;
    }

    /**
     * Lấy tên của chủ sân bóng.
     * 
     * @return Tên chủ sân bóng.
     */
    public String getTenChuSan() {
        // Trả về tên chủ sân
        return tenChuSan;
    }

    /**
     * Cập nhật tên chủ sân bóng.
     * 
     * @param tenChuSan Tên chủ sân mới.
     */
    public void setTenChuSan(String tenChuSan) {
        // Cập nhật tên chủ sân
        this.tenChuSan = tenChuSan;
    }

    /**
     * Lấy số điện thoại của chủ sân bóng.
     * 
     * @return Số điện thoại.
     */
    public String getSoDienThoaiChuSan() {
        // Trả về số điện thoại chủ sân
        return soDienThoaiChuSan;
    }

    /**
     * Cập nhật số điện thoại của chủ sân bóng.
     * 
     * @param soDienThoaiChuSan Số điện thoại mới.
     */
    public void setSoDienThoaiChuSan(String soDienThoaiChuSan) {
        // Cập nhật số điện thoại chủ sân
        this.soDienThoaiChuSan = soDienThoaiChuSan;
    }

    /**
     * Tra cứu và truy xuất đối tượng TaiKhoan liên kết tương ứng từ bộ nhớ DataStore.
     * 
     * @return Đối tượng {@link TaiKhoan} nếu tìm thấy, ngược lại trả về null.
     */
    public TaiKhoan getTaiKhoan() {
        // Kiểm tra mã tài khoản khác null
        if (maTaiKhoan != null) {
            // Tra cứu thông tin tài khoản qua DataStore
            return Utils.DataStore.get().findTaiKhoanByMa(maTaiKhoan);
        }
        // Trả về null nếu chưa liên kết mã tài khoản
        return null;
    }

    /**
     * Chuyển đổi thông tin chủ sân thành dạng chuỗi hiển thị gọn trên giao diện.
     * 
     * @return Chuỗi định dạng "Tên chủ sân (Số điện thoại)".
     */
    @Override
    public String toString() {
        // Kết hợp tên và số điện thoại chủ sân
        return tenChuSan + " (" + soDienThoaiChuSan + ")";
    }
}
