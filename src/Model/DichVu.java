package Model;

/**
 * Bảng: dich_vu
 * Lưu trữ các dịch vụ đi kèm khi đặt sân bóng.
 */
public class DichVu {
    /** PK, AUTO_INCREMENT, varchar(20) */
    private String maDichVu;
    /** NOT NULL, varchar(50) */
    private String loaiDichVu;
    /** NOT NULL, UNIQUE, varchar(100) */
    private String tenDichVu;
    /** NOT NULL, DECIMAL(15,0) - đơn giá dịch vụ */
    private double gia;
    /** TEXT, NULL */
    private String moTa;

    // ─── Trường mở rộng dùng cho kho hàng hóa (không có trong bảng dich_vu CSDL) ─
    /** Số lượng tồn kho (chỉ dùng khi loaiDichVu = "Vật tư kho") */
    private int soLuongTon;
    /** Đơn vị tính (cái, bộ, chai...) - hiển thị UI */
    private String donVi;

    public DichVu() {
    }

    public DichVu(String maDichVu, String tenDichVu, String loaiDichVu, double gia, String moTa) {
        this.maDichVu   = maDichVu;
        this.tenDichVu  = tenDichVu;
        this.loaiDichVu = loaiDichVu;
        this.gia        = gia;
        this.moTa       = moTa;
        this.soLuongTon = 0;
        this.donVi      = "";
    }

    /** Constructor tương thích seed dịch vụ cũ */
    public DichVu(int id, String maDichVu, String tenDichVu, String loaiDichVu,
                  double gia, String moTa) {
        this(maDichVu != null ? maDichVu : String.format("DV%03d", id),
             tenDichVu, loaiDichVu, gia, moTa);
    }

    /** Constructor dùng cho kho hàng hóa */
    public DichVu(int id, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        this.maDichVu   = String.format("HH%03d", id);
        this.tenDichVu  = tenHangHoa;
        this.soLuongTon = soLuongTon;
        this.gia        = donGia;
        this.moTa       = nhaCungCap;
        this.donVi      = "cái";
        this.loaiDichVu = "Vật tư kho";
    }

    /** Constructor 8-param tương thích DichVuFormDialog: (id, ten, moTa, gia, donVi, trangThai, soLuongTon, tonToiThieu) */
    public DichVu(int id, String tenDichVu, String moTa, double gia, String donVi,
                  String trangThai, int soLuongTon, int tonToiThieu) {
        this.maDichVu   = String.format("DV%03d", id);
        this.tenDichVu  = tenDichVu;
        this.moTa       = moTa;
        this.gia        = gia;
        this.donVi      = donVi;
        this.loaiDichVu = donVi;
        this.soLuongTon = soLuongTon;
    }

    // ─── Getters & Setters theo CSDL ─────────────────────────────────────────

    public String getMaDichVu() { return maDichVu; }
    public void setMaDichVu(String maDichVu) { this.maDichVu = maDichVu; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public String getLoaiDichVu() { return loaiDichVu; }
    public void setLoaiDichVu(String loaiDichVu) { this.loaiDichVu = loaiDichVu; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    // ─── Alias helpers tương thích UI code hiện tại ───────────────────────────

    /** Alias getDonGia() → getGia() */
    public double getDonGia() { return gia; }
    public void setDonGia(double donGia) { this.gia = donGia; }

    /** Số lượng tồn kho (dùng cho kho hàng hóa) */
    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    /** Đơn vị tính */
    public String getDonVi() { return donVi != null ? donVi : ""; }
    public void setDonVi(String donVi) { this.donVi = donVi; }

    /** Mức tồn tối thiểu - mặc định 5 */
    public int getTonToiThieu() { return 5; }

    /** id nội bộ cho UI (dùng hashCode của maDichVu) */
    public int getId() { return maDichVu != null ? maDichVu.hashCode() & 0x7FFFFFFF : 0; }

    // ─── Alias cho kho hàng hóa ───────────────────────────────────────────────
    public int getMaHangHoa() { return getId(); }
    public String getTenHangHoa() { return tenDichVu; }
    public String getNhaCungCap() { return moTa != null && !moTa.isBlank() ? moTa : "Tổng kho Sân bóng"; }
    public void setNhaCungCap(String ncc) { this.moTa = ncc; }

    // ─── Helper methods ────────────────────────────────────────────────────────

    public boolean isSapHet() {
        return soLuongTon > 0 && soLuongTon <= getTonToiThieu();
    }

    public void nhapKho(int soLuong) {
        if (soLuong > 0) soLuongTon += soLuong;
    }

    public boolean xuatKho(int soLuong) {
        if (soLuong <= 0 || soLuong > soLuongTon) return false;
        soLuongTon -= soLuong;
        return true;
    }

    /** @deprecated id nội bộ UI - dùng getMaDichVu() */
    public void setId(int id) {
        if (maDichVu == null || maDichVu.isBlank()) {
            maDichVu = String.format("DV%03d", id);
        }
    }

    /** @deprecated Không còn trong CSDL - trả về "DangBan" mặc định */
    public String getTrangThai() { return "DangBan"; }
    /** @deprecated Không còn trong CSDL */
    public void setTrangThai(String trangThai) { /* no-op */ }

    @Override
    public String toString() {
        return tenDichVu + " (" + String.format("%,.0f", gia) + " VNĐ)";
    }
}
