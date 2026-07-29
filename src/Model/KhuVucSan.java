package Model;

/**
 * Model KhuVucSan (SanBong) thuộc tính chuẩn theo Biểu đồ lớp lĩnh vực (llv.png).
 */
public class KhuVucSan {
    private int id;
    private String maSan;
    private String tenSan;
    private String loaiSan;         // San5 | San7 | San11
    private double giaTheoGio;
    private float giaThueTheoGio;   // float giaThueTheoGio theo llv.png
    private String moTa;
    private String trangThai;       // SanSang | DangThue | BaoTri

    public KhuVucSan() {
    }

    public KhuVucSan(int id, String maSan, String tenSan, String loaiSan,
                     double giaTheoGio, String moTa, String trangThai) {
        this.id = id;
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.loaiSan = loaiSan;
        this.giaTheoGio = giaTheoGio;
        this.giaThueTheoGio = (float) giaTheoGio;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSan() { return maSan; }
    public void setMaSan(String maSan) { this.maSan = maSan; }
    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }
    public String getLoaiSan() { return loaiSan; }
    public void setLoaiSan(String loaiSan) { this.loaiSan = loaiSan; }
    public double getGiaTheoGio() { return giaTheoGio; }
    public void setGiaTheoGio(double giaTheoGio) { this.giaTheoGio = giaTheoGio; this.giaThueTheoGio = (float) giaTheoGio; }

    public float getGiaThueTheoGio() { return giaThueTheoGio > 0 ? giaThueTheoGio : (float) giaTheoGio; }
    public void setGiaThueTheoGio(float giaThueTheoGio) { this.giaThueTheoGio = giaThueTheoGio; this.giaTheoGio = giaThueTheoGio; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getLoaiSanHienThi() {
        return switch (loaiSan == null ? "" : loaiSan) {
            case "San5" -> "Sân 5 người";
            case "San7" -> "Sân 7 người";
            case "San11" -> "Sân 11 người";
            default -> loaiSan;
        };
    }

    public String getTrangThaiHienThi() {
        return switch (trangThai == null ? "" : trangThai) {
            case "SanSang" -> "Sẵn sàng";
            case "DangThue" -> "Đang thuê";
            case "BaoTri" -> "Bảo trì";
            default -> trangThai;
        };
    }

    @Override
    public String toString() {
        return maSan + " - " + tenSan;
    }
}
