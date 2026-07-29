package Model;


public class DatLich {
    private int id;
    private String maLichDat;
    private String maPhieu;
    private int khuVucId;
    private String tenSan;
    private String tenKhach;
    private String soDienThoaiKhach;
    private String soDienThoai;
    private String ngayDat;     // yyyy-MM-dd
    private String gioBatDau;   // HH:mm
    private String gioKetThuc;
    private double tienSan;
    private double tienDichVu;
    private double tongTien;
    private String trangThai;       // ChoXacNhan | DaXacNhan | DaHuy | HoanThanh
    private String trangThaiTT;     // ChuaThanhToan | DaThanhToan | ThanhToanMotPhan
    private String nhanVienLap;
    private String ghiChu;
    private String dichVuKem;
    private double datCoc;

    public DatLich() {
        this.trangThaiTT = "ChuaThanhToan";
        this.dichVuKem = "";
        this.datCoc = 0;
    }

    public DatLich(int id, String maPhieu, int khuVucId, String tenSan, String tenKhach,
                   String soDienThoai, String ngayDat, String gioBatDau, String gioKetThuc,
                   double tongTien, String trangThai, String nhanVienLap, String ghiChu) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.maLichDat = maPhieu;
        this.khuVucId = khuVucId;
        this.tenSan = tenSan;
        this.tenKhach = tenKhach;
        this.soDienThoai = soDienThoai;
        this.soDienThoaiKhach = soDienThoai;
        this.ngayDat = ngayDat;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.tienSan = tongTien;
        this.tienDichVu = 0;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.trangThaiTT = "HoanThanh".equals(trangThai) ? "DaThanhToan" : "ChuaThanhToan";
        this.nhanVienLap = nhanVienLap;
        this.ghiChu = ghiChu;
        this.dichVuKem = "";
        this.datCoc = 0;
    }

    public String getMaLichDat() { return maLichDat != null ? maLichDat : maPhieu; }
    public void setMaLichDat(String maLichDat) { this.maLichDat = maLichDat; this.maPhieu = maLichDat; }

    public String getSoDienThoaiKhach() { return soDienThoaiKhach != null ? soDienThoaiKhach : soDienThoai; }
    public void setSoDienThoaiKhach(String soDienThoaiKhach) { this.soDienThoaiKhach = soDienThoaiKhach; this.soDienThoai = soDienThoaiKhach; }

    // GETTERS & SETTERS HIỆN CÓ
    public double getDatCoc() { return datCoc; }
    public void setDatCoc(double datCoc) { this.datCoc = datCoc; }
    public double getConLai() {
        if ("DaThanhToan".equalsIgnoreCase(trangThaiTT)) return 0;
        return Math.max(0, tongTien - datCoc);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; this.maLichDat = maPhieu; }
    public int getKhuVucId() { return khuVucId; }
    public void setKhuVucId(int khuVucId) { this.khuVucId = khuVucId; }
    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }
    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; this.soDienThoaiKhach = soDienThoai; }
    public String getNgayDat() { return ngayDat; }
    public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }
    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }
    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }
    public double getTienSan() { return tienSan; }
    public void setTienSan(double tienSan) { this.tienSan = tienSan; recalc(); }
    public double getTienDichVu() { return tienDichVu; }
    public void setTienDichVu(double tienDichVu) { this.tienDichVu = tienDichVu; recalc(); }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getTrangThaiTT() { return trangThaiTT; }
    public void setTrangThaiTT(String trangThaiTT) { this.trangThaiTT = trangThaiTT; }
    public String getNhanVienLap() { return nhanVienLap; }
    public void setNhanVienLap(String nhanVienLap) { this.nhanVienLap = nhanVienLap; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public String getDichVuKem() { return dichVuKem; }
    public void setDichVuKem(String dichVuKem) { this.dichVuKem = dichVuKem; }

    public String getKhungGio() {
        return gioBatDau + " - " + gioKetThuc;
    }

    public String getTrangThaiHienThi() {
        return switch (trangThai == null ? "" : trangThai) {
            case "ChoXacNhan" -> "Chờ xác nhận";
            case "DaXacNhan" -> "Đã xác nhận";
            case "HoanThanh" -> "Hoàn thành";
            case "DaHuy" -> "Đã hủy";
            default -> trangThai;
        };
    }

    private java.util.Map<Integer, Integer> selectedDvMap = new java.util.HashMap<>();
    private java.util.Map<Integer, Integer> selectedDoAnMap = new java.util.HashMap<>();

    public java.util.Map<Integer, Integer> getSelectedDvMap() { return selectedDvMap; }
    public void setSelectedDvMap(java.util.Map<Integer, Integer> selectedDvMap) {
        this.selectedDvMap = selectedDvMap != null ? selectedDvMap : new java.util.HashMap<>();
    }

    public java.util.Map<Integer, Integer> getSelectedDoAnMap() { return selectedDoAnMap; }
    public void setSelectedDoAnMap(java.util.Map<Integer, Integer> selectedDoAnMap) {
        this.selectedDoAnMap = selectedDoAnMap != null ? selectedDoAnMap : new java.util.HashMap<>();
    }

    public void addDichVuKem(String name, int qty, double cost) {
        if (dichVuKem == null || dichVuKem.isBlank()) {
            dichVuKem = String.format("%s (x%d): %,.0f VNĐ", name, qty, cost);
        } else {
            dichVuKem += String.format("\n%s (x%d): %,.0f VNĐ", name, qty, cost);
        }
        this.tienDichVu += cost;
        recalc();
    }

    public void recalc() {
        this.tongTien = this.tienSan + this.tienDichVu;
    }
}
