package Model;

/**
 * Model Kho thuộc tính chuẩn theo Biểu đồ lớp lĩnh vực (llv.png).
 */
public class Kho {
    private int maHangHoa;
    private String tenHangHoa;
    private int soLuongTon;
    private double donGia;
    private String nhaCungCap;

    public Kho() {
    }

    public Kho(int maHangHoa, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        this.maHangHoa = maHangHoa;
        this.tenHangHoa = tenHangHoa;
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
        this.nhaCungCap = nhaCungCap;
    }

    public int getMaHangHoa() { return maHangHoa; }
    public void setMaHangHoa(int maHangHoa) { this.maHangHoa = maHangHoa; }

    public String getTenHangHoa() { return tenHangHoa; }
    public void setTenHangHoa(String tenHangHoa) { this.tenHangHoa = tenHangHoa; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(String nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public int truyVanTonKho() { return soLuongTon; }
    public int truyVanSoLuongTon(int maHH) { return this.maHangHoa == maHH ? soLuongTon : 0; }
    public void luuPhieuNhap() {}
    public void capNhatSoLuongTon(int maHH, int sl) { if (this.maHangHoa == maHH) this.soLuongTon = sl; }
    public void luuPhieuXuat() {}
    public void giamSoLuongTon(int maHH, int sl) { if (this.maHangHoa == maHH && sl <= soLuongTon) this.soLuongTon -= sl; }
}
