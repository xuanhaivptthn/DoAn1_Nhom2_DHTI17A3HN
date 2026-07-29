package Model;

/**
 * Model BaoTri thuộc tính chuẩn theo Biểu đồ lớp lĩnh vực (llv.png).
 */
public class BaoTri {
    private int id;
    private String maBaoTri;
    private String maPhieu;
    private int khuVucId;
    private String tenSan;
    private String ngayBaoTri;
    private String noiDung;
    private String nguoiPhuTrach;
    private String ngayBatDau;
    private String ngayKetThuc;
    private double chiPhi;
    private String trangThai;   // ChoXuLy | DangXuLy | HoanThanh | Huy

    public BaoTri() {
    }

    public BaoTri(int id, String maPhieu, int khuVucId, String tenSan, String noiDung,
                  String nguoiPhuTrach, String ngayBatDau, String ngayKetThuc,
                  double chiPhi, String trangThai) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.maBaoTri = maPhieu;
        this.khuVucId = khuVucId;
        this.tenSan = tenSan;
        this.noiDung = noiDung;
        this.nguoiPhuTrach = nguoiPhuTrach;
        this.ngayBatDau = ngayBatDau;
        this.ngayBaoTri = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.chiPhi = chiPhi;
        this.trangThai = trangThai;
    }

    // GETTERS & SETTERS THEO SƠ ĐỒ LLV.PNG
    public String getMaBaoTri() { return maBaoTri != null ? maBaoTri : maPhieu; }
    public void setMaBaoTri(String maBaoTri) { this.maBaoTri = maBaoTri; this.maPhieu = maBaoTri; }

    public String getNgayBaoTri() { return ngayBaoTri != null ? ngayBaoTri : ngayBatDau; }
    public void setNgayBaoTri(String ngayBaoTri) { this.ngayBaoTri = ngayBaoTri; this.ngayBatDau = ngayBaoTri; }

    // GETTERS & SETTERS HIỆN CÓ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; this.maBaoTri = maPhieu; }
    public int getKhuVucId() { return khuVucId; }
    public void setKhuVucId(int khuVucId) { this.khuVucId = khuVucId; }
    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public String getNguoiPhuTrach() { return nguoiPhuTrach; }
    public void setNguoiPhuTrach(String nguoiPhuTrach) { this.nguoiPhuTrach = nguoiPhuTrach; }
    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; this.ngayBaoTri = ngayBatDau; }
    public String getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public double getChiPhi() { return chiPhi; }
    public void setChiPhi(double chiPhi) { this.chiPhi = chiPhi; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTrangThaiHienThi() {
        return switch (trangThai == null ? "" : trangThai) {
            case "ChoXuLy" -> "Chờ xử lý";
            case "DangXuLy" -> "Đang xử lý";
            case "HoanThanh" -> "Hoàn thành";
            case "Huy" -> "Đã hủy";
            default -> trangThai;
        };
    }
}
