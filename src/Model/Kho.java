package Model;

/**
 * Lớp model đại diện cho thông tin vật tư, sản phẩm trong kho (bảng kho trong CSDL).
 * <p>
 * Lớp này quản lý chi tiết các mặt hàng bán lẻ hoặc dụng cụ thể thao lưu trữ tại kho sân bóng,
 * bao gồm mã hàng hóa, tên hàng hóa, số lượng tồn kho, đơn giá bán và nhà cung cấp.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class Kho {
    /** 
     * Mã hàng hóa (Khoá chính, AUTO_INCREMENT trong DB hoặc chuỗi định dạng HH001, varchar(20)).
     */
    private String maHangHoa;

    /** 
     * Tên sản phẩm, mặt hàng tồn kho (NOT NULL, UNIQUE, varchar(100)).
     */
    private String tenHangHoa;

    /** 
     * Số lượng hàng hóa hiện còn trong kho (NOT NULL, DEFAULT 0).
     */
    private int soLuongTon;

    /** 
     * Đơn giá bán lẻ/cho thuê của một đơn vị hàng hóa (NOT NULL, DECIMAL(15,0)).
     */
    private double donGia;

    /** 
     * Tên nhà cung cấp mặt hàng (NOT NULL, varchar(100)).
     */
    private String nhaCungCap;

    /**
     * Khởi tạo một đối tượng Kho mới không tham số.
     */
    public Kho() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo một đối tượng Kho với đầy đủ các thuộc tính lưu trữ CSDL.
     * 
     * @param maHangHoa  Mã hàng hóa chuỗi.
     * @param tenHangHoa Tên mặt hàng.
     * @param soLuongTon Số lượng tồn kho ban đầu.
     * @param donGia     Đơn giá của hàng hóa.
     * @param nhaCungCap Tên nhà cung cấp.
     */
    public Kho(String maHangHoa, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        // Gán các thuộc tính truyền vào
        this.maHangHoa  = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.soLuongTon = soLuongTon;
        this.donGia     = donGia;
        this.nhaCungCap = nhaCungCap;
    }

    /**
     * Constructor tương thích seed dữ liệu mẫu cũ (tự tạo mã dạng HH001 từ ID dạng int).
     * 
     * @param id         ID số nguyên của mặt hàng.
     * @param tenHangHoa Tên mặt hàng.
     * @param soLuongTon Số lượng tồn kho ban đầu.
     * @param donGia     Đơn giá.
     * @param nhaCungCap Tên nhà cung cấp.
     */
    public Kho(int id, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        // Gọi constructor chính với mã hàng hóa tự động định dạng
        this(String.format("HH%03d", id), tenHangHoa, soLuongTon, donGia, nhaCungCap);
    }

    /**
     * Lấy mã hàng hóa.
     * 
     * @return Mã hàng hóa.
     */
    public String getMaHangHoa() {
        // Trả về mã hàng hóa
        return maHangHoa;
    }

    /**
     * Cập nhật mã hàng hóa.
     * 
     * @param maHangHoa Mã hàng hóa mới.
     */
    public void setMaHangHoa(String maHangHoa) {
        // Gán mã hàng hóa
        this.maHangHoa = maHangHoa;
    }

    /**
     * Lấy tên mặt hàng kho.
     * 
     * @return Tên hàng hóa.
     */
    public String getTenHangHoa() {
        // Trả về tên hàng hóa
        return tenHangHoa;
    }

    /**
     * Cập nhật tên mặt hàng kho.
     * 
     * @param tenHangHoa Tên hàng hóa mới.
     */
    public void setTenHangHoa(String tenHangHoa) {
        // Gán tên hàng hóa
        this.tenHangHoa = tenHangHoa;
    }

    /**
     * Lấy số lượng hàng hóa còn trong kho.
     * 
     * @return Số lượng tồn kho.
     */
    public int getSoLuongTon() {
        // Trả về số lượng tồn
        return soLuongTon;
    }

    /**
     * Cập nhật số lượng hàng hóa trong kho.
     * 
     * @param soLuongTon Số lượng tồn mới.
     */
    public void setSoLuongTon(int soLuongTon) {
        // Gán số lượng tồn
        this.soLuongTon = soLuongTon;
    }

    /**
     * Lấy đơn giá bán của mặt hàng.
     * 
     * @return Đơn giá.
     */
    public double getDonGia() {
        // Trả về đơn giá
        return donGia;
    }

    /**
     * Cập nhật đơn giá bán của mặt hàng.
     * 
     * @param donGia Đơn giá mới.
     */
    public void setDonGia(double donGia) {
        // Gán đơn giá
        this.donGia = donGia;
    }

    /**
     * Lấy tên nhà cung cấp.
     * 
     * @return Tên nhà cung cấp.
     */
    public String getNhaCungCap() {
        // Trả về tên nhà cung cấp
        return nhaCungCap;
    }

    /**
     * Cập nhật tên nhà cung cấp.
     * 
     * @param nhaCungCap Tên nhà cung cấp mới.
     */
    public void setNhaCungCap(String nhaCungCap) {
        // Gán tên nhà cung cấp
        this.nhaCungCap = nhaCungCap;
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /**
     * Truy vấn số lượng tồn kho của mặt hàng hiện tại.
     * 
     * @return Số lượng tồn kho.
     */
    public int truyVanTonKho() {
        // Trả về thuộc tính soLuongTon
        return soLuongTon;
    }

    /**
     * Nhập thêm một số lượng hàng hóa vào kho.
     * 
     * @param soLuong Số lượng sản phẩm nhập thêm (> 0).
     */
    public void nhapKho(int soLuong) {
        // Kiểm tra điều kiện và cộng dồn vào số lượng tồn
        if (soLuong > 0) soLuongTon += soLuong;
    }

    /**
     * Xuất bớt số lượng hàng hóa khỏi kho khi bán hoặc sử dụng.
     * 
     * @param soLuong Số lượng sản phẩm muốn xuất (> 0 và <= soLuongTon).
     * @return true nếu xuất thành công, false nếu số lượng không đủ hoặc không hợp lệ.
     */
    public boolean xuatKho(int soLuong) {
        // Kiểm tra tính hợp lệ của số lượng xuất
        if (soLuong <= 0 || soLuong > soLuongTon) return false;
        // Trừ bớt số lượng tồn kho
        soLuongTon -= soLuong;
        return true;
    }

    /**
     * Chuyển đổi thông tin hàng hóa kho thành dạng chuỗi mô tả ngắn gọn.
     * 
     * @return Chuỗi định dạng "Tên hàng hóa (Tồn: X, Y VNĐ)".
     */
    @Override
    public String toString() {
        // Kết hợp tên, số lượng tồn và đơn giá định dạng tiền tệ
        return tenHangHoa + " (Tồn: " + soLuongTon + ", " + String.format("%,.0f", donGia) + " VNĐ)";
    }
}
