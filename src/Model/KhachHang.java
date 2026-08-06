package Model;

/**
 * Lớp model đại diện cho thông tin khách hàng (bảng khach_hang trong CSDL).
 * <p>
 * Lớp này lưu trữ các thông tin liên hệ cá nhân của khách hàng đăng ký hoặc đặt sân bóng,
 * bao gồm mã khách hàng, tên khách hàng và số điện thoại liên lạc.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class KhachHang {
    /** 
     * Mã khách hàng (Khoá chính, varchar(20)).
     */
    private String maKhachHang;

    /** 
     * Họ tên của khách hàng (NOT NULL, varchar(100)).
     */
    private String tenKhachHang;

    /** 
     * Số điện thoại liên hệ của khách hàng (NOT NULL, UNIQUE, varchar(15)).
     */
    private String soDienThoai;

    /**
     * Khởi tạo một đối tượng KhachHang mới không tham số.
     */
    public KhachHang() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo đối tượng KhachHang với đầy đủ các trường thuộc tính.
     * 
     * @param maKhachHang  Mã khách hàng.
     * @param tenKhachHang Tên đầy đủ của khách hàng.
     * @param soDienThoai  Số điện thoại liên lạc.
     */
    public KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai) {
        // Gán các thuộc tính truyền vào
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
    }

    /**
     * Constructor tương thích với việc seed dữ liệu mẫu cũ (tự động tạo mã chuỗi từ ID).
     * 
     * @param id           ID dạng số nguyên.
     * @param tenKhachHang Tên khách hàng.
     * @param soDienThoai  Số điện thoại.
     */
    public KhachHang(int id, String tenKhachHang, String soDienThoai) {
        // Tự động định dạng mã KH dạng "KH001" từ id
        this.maKhachHang = String.format("KH%03d", id);
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
    }

    /**
     * Lấy mã khách hàng.
     * 
     * @return Mã khách hàng.
     */
    public String getMaKhachHang() {
        // Trả về mã khách hàng
        return maKhachHang;
    }

    /**
     * Cập nhật mã khách hàng.
     * 
     * @param maKhachHang Mã khách hàng mới.
     */
    public void setMaKhachHang(String maKhachHang) {
        // Gán mã khách hàng
        this.maKhachHang = maKhachHang;
    }

    /**
     * Lấy tên khách hàng.
     * 
     * @return Tên khách hàng.
     */
    public String getTenKhachHang() {
        // Trả về tên khách hàng
        return tenKhachHang;
    }

    /**
     * Cập nhật tên khách hàng.
     * 
     * @param tenKhachHang Tên khách hàng mới.
     */
    public void setTenKhachHang(String tenKhachHang) {
        // Gán tên khách hàng
        this.tenKhachHang = tenKhachHang;
    }

    /**
     * Lấy số điện thoại khách hàng.
     * 
     * @return Số điện thoại.
     */
    public String getSoDienThoai() {
        // Trả về số điện thoại
        return soDienThoai;
    }

    /**
     * Cập nhật số điện thoại khách hàng.
     * 
     * @param soDienThoai Số điện thoại mới.
     */
    public void setSoDienThoai(String soDienThoai) {
        // Gán số điện thoại
        this.soDienThoai = soDienThoai;
    }

    // ─── Alias helpers tương thích với DatLich ───────────────────────────────

    /**
     * Alias lấy tên khách hàng (tương đương getTenKhachHang()).
     * 
     * @return Tên khách hàng.
     */
    public String getTenKhach() {
        // Trả về tên khách hàng
        return tenKhachHang;
    }

    /**
     * Alias cập nhật tên khách hàng (tương đương setTenKhachHang()).
     * 
     * @param tenKhach Tên khách mới.
     */
    public void setTenKhach(String tenKhach) {
        // Gán tên khách hàng
        this.tenKhachHang = tenKhach;
    }

    /**
     * Alias lấy số điện thoại khách hàng (tương đương getSoDienThoai()).
     * 
     * @return Số điện thoại.
     */
    public String getSoDienThoaiKhach() {
        // Trả về số điện thoại
        return soDienThoai;
    }

    /**
     * Alias cập nhật số điện thoại khách hàng (tương đương setSoDienThoai()).
     * 
     * @param soDienThoaiKhach Số điện thoại mới.
     */
    public void setSoDienThoaiKhach(String soDienThoaiKhach) {
        // Gán số điện thoại
        this.soDienThoai = soDienThoaiKhach;
    }

    /**
     * Chuyển đổi thông tin khách hàng thành dạng chuỗi hiển thị gọn.
     * 
     * @return Chuỗi định dạng "Tên khách hàng (Số điện thoại)".
     */
    @Override
    public String toString() {
        // Trả về tên khách hàng kèm số điện thoại trong ngoặc
        return tenKhachHang + " (" + soDienThoai + ")";
    }
}
