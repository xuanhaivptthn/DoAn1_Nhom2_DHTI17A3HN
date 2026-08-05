package Model;

/**
 * Bảng: san_bong
 * Lưu trữ thông tin danh mục các sân bóng thuộc cụm sân.
 */
public class KhuVucSan {
    /** PK, AUTO_INCREMENT, varchar(20) */
    private String maSan;
    /** NOT NULL - tên hiển thị sân (VD: Sân 5A, Sân 7B) */
    private String tenSan;
    /** NOT NULL - Sân 5 người | Sân 7 người | ... */
    private String loaiSan;
    /** NOT NULL, DECIMAL(12,2) - giá thuê cố định theo khung giờ (/giờ) */
    private double giaThueTheoGio;
    /** ENUM: HOAT_DONG | BAO_TRI | NGUNG_HOAT_DONG  — DEFAULT 'HOAT_DONG' */
    private String trangThai;
    /** FK → chu_san.maChuSan */
    private String maChuSan;

    public KhuVucSan() {
    }

    public KhuVucSan(String maSan, String tenSan, String loaiSan,
                     double giaThueTheoGio, String trangThai) {
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
        this.giaThueTheoGio = giaThueTheoGio;
        this.trangThai = trangThai;
    }

    public KhuVucSan(String maSan, String maChuSan, String tenSan, String loaiSan,
                     double giaThueTheoGio, String trangThai) {
        this(maSan, tenSan, loaiSan, giaThueTheoGio, trangThai);
        this.maChuSan = maChuSan;
    }

    /** Constructor tương thích seed dữ liệu mẫu (id tự sinh mã) */
    public KhuVucSan(int id, String maSan, String tenSan, String loaiSan,
                     double giaThueTheoGio, String moTa, String trangThai) {
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
        this.giaThueTheoGio = giaThueTheoGio;
        this.trangThai = trangThai;
        // moTa không còn trong CSDL - bỏ qua
    }

    public String getMaSan() { return maSan; }
    public void setMaSan(String maSan) { this.maSan = maSan; }

    public String getMaChuSan() { return maChuSan; }
    public void setMaChuSan(String maChuSan) { this.maChuSan = maChuSan; }

    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }

    public String getLoaiSan() { return loaiSan; }
    public void setLoaiSan(String loaiSan) { this.loaiSan = loaiSan; }

    public double getGiaThueTheoGio() { return giaThueTheoGio; }
    public void setGiaThueTheoGio(double giaThueTheoGio) { this.giaThueTheoGio = giaThueTheoGio; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public ChuSan getChuSan() {
        if (maChuSan != null) {
            return Utils.DataStore.get().findChuSanById(maChuSan);
        }
        return null;
    }

    /** ID số cơ bản từ mã sân (ví dụ "SAN001" -> 1) */
    public int getId() {
        if (maSan == null || maSan.isBlank()) return 0;
        String digits = maSan.replaceAll("\\D", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    public String getLoaiSanHienThi() {
        if (loaiSan == null) return "";
        return switch (loaiSan) {
            case "San5"  -> "Sân 5 người";
            case "San7"  -> "Sân 7 người";
            case "San11" -> "Sân 11 người";
            default      -> loaiSan;
        };
    }

    public String getTrangThaiHienThi() {
        if (trangThai == null) return "";
        return switch (trangThai.toUpperCase()) {
            case "HOAT_DONG", "SANSAN", "SANSANG" -> "Sẵn sàng";
            case "DANGTHUE", "DANG_THUE"          -> "Đang thuê";
            case "BAO_TRI", "BAOTRI", "DANG_BAO_TRI" -> "Đang Bảo trì";
            default -> trangThai;
        };
    }

    public boolean isHoatDong() {
        return trangThai != null && (
            "HOAT_DONG".equalsIgnoreCase(trangThai) || "SanSang".equalsIgnoreCase(trangThai)
        );
    }

    @Override
    public String toString() {
        return maSan + " - " + tenSan;
    }

}
