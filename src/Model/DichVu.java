package Model;

public class DichVu {
    private int id;
    private String maDichVu;
    private String tenDichVu;
    private String loaiDichVu;
    private double donGia;
    private float donGiaFloat;
    private String donVi;
    private String trangThai;
    private int soLuongTon;
    private int tonToiThieu;
    private String moTa;

    public DichVu() {
    }

    public DichVu(int id, String maDichVu, String tenDichVu, String loaiDichVu, double donGia, String moTa) {
        this.id = id;
        this.maDichVu = maDichVu != null ? maDichVu : String.format("DV%03d", id);
        this.tenDichVu = tenDichVu;
        this.loaiDichVu = loaiDichVu;
        this.donVi = loaiDichVu;
        this.donGia = donGia;
        this.donGiaFloat = (float) donGia;
        this.moTa = moTa;
        this.trangThai = "DangBan";
        this.soLuongTon = 100;
        this.tonToiThieu = 5;
    }

    public DichVu(int id, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        this.id = id;
        this.maDichVu = String.format("HH%03d", id);
        this.tenDichVu = tenHangHoa;
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
        this.donGiaFloat = (float) donGia;
        this.moTa = nhaCungCap;
        this.donVi = "cái";
        this.loaiDichVu = "Vật tư kho";
        this.trangThai = "DangBan";
        this.tonToiThieu = 5;
    }

    public DichVu(int id, String tenDichVu, String moTa, double donGia, String donVi, String trangThai) {
        this(id, tenDichVu, moTa, donGia, donVi, trangThai, 0, 5);
    }

    public DichVu(int id, String tenDichVu, String moTa, double donGia, String donVi,
                  String trangThai, int soLuongTon, int tonToiThieu) {
        this.id = id;
        this.maDichVu = String.format("DV%03d", id);
        this.tenDichVu = tenDichVu;
        this.moTa = moTa;
        this.donGia = donGia;
        this.donGiaFloat = (float) donGia;
        this.donVi = donVi;
        this.loaiDichVu = donVi;
        this.trangThai = trangThai;
        this.soLuongTon = soLuongTon;
        this.tonToiThieu = tonToiThieu;
    }

    public String getMaDichVu() { return maDichVu != null ? maDichVu : "DV" + id; }
    public void setMaDichVu(String maDichVu) { this.maDichVu = maDichVu; }

    public String getLoaiDichVu() { return loaiDichVu != null ? loaiDichVu : donVi; }
    public void setLoaiDichVu(String loaiDichVu) { this.loaiDichVu = loaiDichVu; this.donVi = loaiDichVu; }

    public float getDonGiaFloat() { return (float) donGia; }
    public void setDonGiaFloat(float donGiaFloat) { this.donGiaFloat = donGiaFloat; this.donGia = donGiaFloat; }

    // GETTERS & SETTERS KHO HÀNG HÓA
    public int getMaHangHoa() { return id; }
    public String getTenHangHoa() { return tenDichVu; }
    public String getNhaCungCap() { return moTa != null && !moTa.isBlank() ? moTa : "Tổng kho Sân bóng"; }
    public void setNhaCungCap(String ncc) { this.moTa = ncc; }

    // GETTERS & SETTERS HIỆN CÓ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; this.donGiaFloat = (float) donGia; }
    public String getDonVi() { return donVi; }
    public void setDonVi(String donVi) { this.donVi = donVi; this.loaiDichVu = donVi; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }
    public int getTonToiThieu() { return tonToiThieu; }
    public void setTonToiThieu(int tonToiThieu) { this.tonToiThieu = tonToiThieu; }

    public String getTrangThaiHienThi() {
        return "DangBan".equals(trangThai) ? "Đang bán" : "Ngừng bán";
    }

    public boolean isSapHet() {
        return soLuongTon <= tonToiThieu;
    }

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
        return tenDichVu + " (" + String.format("%,.0f", donGia) + "/" + donVi + ", tồn " + soLuongTon + ")";
    }
}
