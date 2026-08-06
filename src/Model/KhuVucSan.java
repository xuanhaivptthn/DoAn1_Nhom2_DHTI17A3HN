package Model;

/**
 * Lớp model đại diện cho thông tin danh mục sân bóng (bảng san_bong trong CSDL).
 * <p>
 * Lớp này lưu trữ thông tin của từng sân bóng đơn lẻ nằm trong cụm sân,
 * bao gồm mã sân, tên sân, loại sân (sân 5, 7, 11 người), đơn giá thuê mỗi giờ,
 * trạng thái hoạt động hiện tại và mã chủ sân quản lý.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class KhuVucSan {
    /** 
     * Mã sân bóng (Khoá chính, AUTO_INCREMENT hoặc chuỗi định dạng như SAN001, varchar(20)).
     */
    private String maSan;

    /** 
     * Tên sân bóng hiển thị (Ví dụ: Sân 5A, Sân 7B, NOT NULL).
     */
    private String tenSan;

    /** 
     * Phân loại sân bóng (Ví dụ: San5, San7, San11, NOT NULL).
     */
    private String loaiSan;

    /** 
     * Đơn giá thuê sân bóng cố định theo 1 giờ (DECIMAL(12,2), NOT NULL).
     */
    private double giaThueTheoGio;

    /** 
     * Trạng thái hoạt động của sân (HOAT_DONG | BAO_TRI | NGUNG_HOAT_DONG - DEFAULT 'HOAT_DONG').
     */
    private String trangThai;

    /** 
     * Mã chủ sân phụ trách sân bóng này (Khoá ngoại tham chiếu chu_san.maChuSan).
     */
    private String maChuSan;

    /**
     * Khởi tạo một đối tượng KhuVucSan mới không tham số.
     */
    public KhuVucSan() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo đối tượng KhuVucSan với 5 thuộc tính cơ bản.
     * 
     * @param maSan          Mã sân bóng.
     * @param tenSan         Tên sân bóng.
     * @param loaiSan        Loại sân bóng.
     * @param giaThueTheoGio Đơn giá thuê theo giờ.
     * @param trangThai      Trạng thái hoạt động.
     */
    public KhuVucSan(String maSan, String tenSan, String loaiSan,
                     double giaThueTheoGio, String trangThai) {
        // Gán 5 thuộc tính cơ bản
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
        this.giaThueTheoGio = giaThueTheoGio;
        this.trangThai = trangThai;
    }

    /**
     * Khởi tạo đối tượng KhuVucSan bao gồm cả mã chủ sân.
     * 
     * @param maSan          Mã sân bóng.
     * @param maChuSan       Mã chủ sân quản lý.
     * @param tenSan         Tên sân bóng.
     * @param loaiSan        Loại sân bóng.
     * @param giaThueTheoGio Đơn giá thuê theo giờ.
     * @param trangThai      Trạng thái hoạt động.
     */
    public KhuVucSan(String maSan, String maChuSan, String tenSan, String loaiSan,
                     double giaThueTheoGio, String trangThai) {
        // Gọi constructor 5 tham số và gán maChuSan
        this(maSan, tenSan, loaiSan, giaThueTheoGio, trangThai);
        this.maChuSan = maChuSan;
    }

    /**
     * Constructor tương thích với việc seed dữ liệu mẫu cũ.
     * 
     * @param id             ID dạng số nguyên (bỏ qua do mã đã là chuỗi).
     * @param maSan          Mã sân bóng.
     * @param tenSan         Tên sân bóng.
     * @param loaiSan        Loại sân.
     * @param giaThueTheoGio Đơn giá thuê theo giờ.
     * @param moTa           Mô tả sân (bỏ qua do CSDL cũ đã bỏ ô này).
     * @param trangThai      Trạng thái hoạt động.
     */
    public KhuVucSan(int id, String maSan, String tenSan, String loaiSan,
                     double giaThueTheoGio, String moTa, String trangThai) {
        // Gán các thông tin sân bóng
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
        this.giaThueTheoGio = giaThueTheoGio;
        this.trangThai = trangThai;
        // moTa không còn tồn tại trong CSDL hiện tại nên bỏ qua
    }

    /**
     * Lấy mã sân bóng.
     * 
     * @return Mã sân bóng.
     */
    public String getMaSan() {
        // Trả về mã sân bóng
        return maSan;
    }

    /**
     * Cập nhật mã sân bóng.
     * 
     * @param maSan Mã sân bóng mới.
     */
    public void setMaSan(String maSan) {
        // Gán mã sân bóng
        this.maSan = maSan;
    }

    /**
     * Lấy mã chủ sân sở hữu/quản lý sân này.
     * 
     * @return Mã chủ sân.
     */
    public String getMaChuSan() {
        // Trả về mã chủ sân
        return maChuSan;
    }

    /**
     * Cập nhật mã chủ sân sở hữu/quản lý sân.
     * 
     * @param maChuSan Mã chủ sân mới.
     */
    public void setMaChuSan(String maChuSan) {
        // Gán mã chủ sân
        this.maChuSan = maChuSan;
    }

    /**
     * Lấy tên hiển thị của sân bóng.
     * 
     * @return Tên sân bóng.
     */
    public String getTenSan() {
        // Trả về tên sân bóng
        return tenSan;
    }

    /**
     * Cập nhật tên hiển thị của sân bóng.
     * 
     * @param tenSan Tên sân bóng mới.
     */
    public void setTenSan(String tenSan) {
        // Gán tên sân bóng
        this.tenSan = tenSan;
    }

    /**
     * Lấy mã loại sân bóng.
     * 
     * @return Mã loại sân.
     */
    public String getLoaiSan() {
        // Trả về loại sân
        return loaiSan;
    }

    /**
     * Cập nhật mã loại sân bóng.
     * 
     * @param loaiSan Loại sân bóng mới.
     */
    public void setLoaiSan(String loaiSan) {
        // Gán loại sân
        this.loaiSan = loaiSan;
    }

    /**
     * Lấy giá thuê theo giờ cố định của sân.
     * 
     * @return Đơn giá thuê/giờ.
     */
    public double getGiaThueTheoGio() {
        // Trả về giá thuê theo giờ
        return giaThueTheoGio;
    }

    /**
     * Cập nhật giá thuê theo giờ cố định của sân.
     * 
     * @param giaThueTheoGio Đơn giá thuê/giờ mới.
     */
    public void setGiaThueTheoGio(double giaThueTheoGio) {
        // Gán giá thuê theo giờ
        this.giaThueTheoGio = giaThueTheoGio;
    }

    /**
     * Lấy mã trạng thái hoạt động của sân.
     * 
     * @return Mã trạng thái.
     */
    public String getTrangThai() {
        // Trả về mã trạng thái
        return trangThai;
    }

    /**
     * Cập nhật mã trạng thái hoạt động của sân.
     * 
     * @param trangThai Trạng thái mới.
     */
    public void setTrangThai(String trangThai) {
        // Gán trạng thái
        this.trangThai = trangThai;
    }

    /**
     * Tra cứu đối tượng ChuSan liên kết từ DataStore qua maChuSan.
     * 
     * @return Đối tượng ChuSan hoặc null nếu không tìm thấy.
     */
    public ChuSan getChuSan() {
        // Tra cứu chủ sân từ DataStore nếu maChuSan khác null
        if (maChuSan != null) {
            return Utils.DataStore.get().findChuSanById(maChuSan);
        }
        return null;
    }

    /**
     * Trích xuất số ID định danh dạng số từ chuỗi mã sân (ví dụ "SAN001" -> 1).
     * 
     * @return Giá trị ID kiểu số nguyên.
     */
    public int getId() {
        // Trả về 0 nếu maSan trống
        if (maSan == null || maSan.isBlank()) return 0;
        
        // Lấy tất cả ký tự là chữ số
        String digits = maSan.replaceAll("\\D", "");
        if (digits.isEmpty()) return 0;
        try {
            // Chuyển đổi sang số nguyên
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /**
     * Lấy tên mô tả loại sân bóng thân thiện để hiển thị UI.
     * 
     * @return Chuỗi mô tả loại sân ("Sân 5 người", "Sân 7 người", "Sân 11 người").
     */
    public String getLoaiSanHienThi() {
        // Kiểm tra loại sân null
        if (loaiSan == null) return "";
        // Chuyển mã loại sân sang tên hiển thị tiếng Việt
        return switch (loaiSan) {
            case "San5"  -> "Sân 5 người";
            case "San7"  -> "Sân 7 người";
            case "San11" -> "Sân 11 người";
            default      -> loaiSan;
        };
    }

    /**
     * Lấy tên mô tả trạng thái sân bóng thân thiện để hiển thị UI.
     * 
     * @return Chuỗi mô tả trạng thái ("Sẵn sàng", "Đang thuê", "Đang Bảo trì").
     */
    public String getTrangThaiHienThi() {
        // Kiểm tra trạng thái null
        if (trangThai == null) return "";
        // Map các mã trạng thái cũ và mới sang tiếng Việt
        return switch (trangThai.toUpperCase()) {
            case "HOAT_DONG", "SANSAN", "SANSANG" -> "Sẵn sàng";
            case "DANGTHUE", "DANG_THUE"          -> "Đang thuê";
            case "BAO_TRI", "BAOTRI", "DANG_BAO_TRI" -> "Đang Bảo trì";
            default -> trangThai;
        };
    }

    /**
     * Kiểm tra xem sân bóng có đang ở trạng thái hoạt động / sẵn sàng cho thuê hay không.
     * 
     * @return true nếu sân sẵn sàng, false nếu đang bảo trì hoặc tạm ngưng hoạt động.
     */
    public boolean isHoatDong() {
        // So sánh trạng thái với HOAT_DONG hoặc SanSang
        return trangThai != null && (
            "HOAT_DONG".equalsIgnoreCase(trangThai) || "SanSang".equalsIgnoreCase(trangThai)
        );
    }

    /**
     * Chuyển đổi thông tin sân bóng thành chuỗi hiển thị gọn.
     * 
     * @return Chuỗi định dạng "Mã sân - Tên sân".
     */
    @Override
    public String toString() {
        // Ghép mã sân và tên sân
        return maSan + " - " + tenSan;
    }
}
