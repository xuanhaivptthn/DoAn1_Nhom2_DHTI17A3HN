package Model;

/**
 * Bảng: kho
 * Lưu trữ thông tin hàng hóa trong kho sân bóng.
 */
public class Kho {
    /** PK, AUTO_INCREMENT, varchar(20) */
    private String maHangHoa;
    /** NOT NULL, UNIQUE, varchar(100) */
    private String tenHangHoa;
    /** NOT NULL, DEFAULT 0 */
    private int soLuongTon;
    /** NOT NULL, DECIMAL(15,0) */
    private double donGia;
    /** NOT NULL, varchar(100) */
    private String nhaCungCap;

    public Kho() {
    }

    public Kho(String maHangHoa, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        this.maHangHoa  = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.soLuongTon = soLuongTon;
        this.donGia     = donGia;
        this.nhaCungCap = nhaCungCap;
    }

    /** Constructor tương thích seed dữ liệu mẫu cũ (id int) */
    public Kho(int id, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        this(String.format("HH%03d", id), tenHangHoa, soLuongTon, donGia, nhaCungCap);
    }

    public String getMaHangHoa() { return maHangHoa; }
    public void setMaHangHoa(String maHangHoa) { this.maHangHoa = maHangHoa; }

    public String getTenHangHoa() { return tenHangHoa; }
    public void setTenHangHoa(String tenHangHoa) { this.tenHangHoa = tenHangHoa; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(String nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    // ─── Helper methods ────────────────────────────────────────────────────────

    public int truyVanTonKho() { return soLuongTon; }

    public void nhapKho(int soLuong) {
        if (soLuong > 0) soLuongTon += soLuong;
    }

    public boolean xuatKho(int soLuong) {
        if (soLuong <= 0 || soLuong > soLuongTon) return false;
        soLuongTon -= soLuong;
        return true;
    }

    @Override
    public String toString() {
        return tenHangHoa + " (Tồn: " + soLuongTon + ", " + String.format("%,.0f", donGia) + " VNĐ)";
    }
}
