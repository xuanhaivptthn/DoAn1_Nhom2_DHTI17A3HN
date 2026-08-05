package Model;

/**
 * Bảng: lich_dat_san
 * Lưu trữ các lịch đặt sân bóng của khách hàng.
 * FK: maKhachHang → khach_hang(maKhachHang)
 *     maSan       → san_bong(maSan)
 *     maTaiKhoan  → tai_khoan(maTaiKhoan)  (nhân viên xử lý)
 */
public class DatLich {
    /** PK, varchar(20) */
    private String maLichDat;
    /** FK → san_bong.maSan, NOT NULL */
    private String maSan;
    /** FK → tai_khoan.maTaiKhoan (nhân viên xử lý), varchar(50) */
    private String maTaiKhoan;
    /** FK → khach_hang.maKhachHang, NOT NULL */
    private String maKhachHang;
    /** NOT NULL, varchar(100) - tên khách hàng (denormalized để tiện hiển thị) */
    private String tenKhach;
    /** NOT NULL, varchar(15) */
    private String soDienThoaiKhach;
    /** NOT NULL, DATE (yyyy-MM-dd) */
    private String ngayDat;
    /** NOT NULL, DATETIME (HH:mm hoặc yyyy-MM-dd HH:mm:ss) */
    private String gioBatDau;
    /** NOT NULL, DATETIME */
    private String gioKetThuc;
    /** NOT NULL, varchar(20) - trạng thái lịch đặt */
    private String trangThai;
    /** Ghi chú cho lần đặt này (không lưu vào hồ sơ khách hàng) */
    private String ghiChu;

    // ─── Trường UI bổ sung (không có trong DB, tính toán lúc runtime) ─────────
    private String tenSan;
    private double tienSan;
    private double tienDichVu;
    private double tongTien;
    private double datCoc;
    private String trangThaiTT;  // ChuaThanhToan | ThanhToanMotPhan | DaThanhToan
    private String dichVuKem;

    /** Map {maDichVu → soLuong} dịch vụ đi kèm */
    private java.util.Map<Integer, Integer> selectedDvMap  = new java.util.HashMap<>();
    /** Map {maHangHoa → soLuong} vật phẩm kho/đồ ăn */
    private java.util.Map<Integer, Integer> selectedDoAnMap = new java.util.HashMap<>();

    public DatLich() {
        this.trangThaiTT = "ChuaThanhToan";
        this.dichVuKem   = "";
        this.datCoc      = 0;
    }

    public DatLich(int id, String maLichDat, int khuVucId, String tenSan, String tenKhach,
                   String soDienThoaiKhach, String ngayDat, String gioBatDau, String gioKetThuc,
                   double tongTien, String trangThai, String maTaiKhoan, String ghiChu) {
        this.maLichDat       = maLichDat;
        this.tenSan          = tenSan;
        this.tenKhach        = tenKhach;
        this.soDienThoaiKhach = soDienThoaiKhach;
        this.ngayDat         = ngayDat;
        this.gioBatDau       = gioBatDau;
        this.gioKetThuc      = gioKetThuc;
        this.tienSan         = tongTien;
        this.tienDichVu      = 0;
        this.tongTien        = tongTien;
        this.trangThai       = trangThai;
        this.trangThaiTT     = "HoanThanh".equals(trangThai) ? "DaThanhToan" : "ChuaThanhToan";
        this.maTaiKhoan      = maTaiKhoan;
        this.ghiChu          = ghiChu;
        this.dichVuKem       = "";
        this.datCoc          = 0;
    }

    // ─── Getters & Setters theo CSDL ─────────────────────────────────────────

    public String getMaLichDat() { return maLichDat; }
    public void setMaLichDat(String maLichDat) { this.maLichDat = maLichDat; }

    public String getMaSan() { return maSan; }
    public void setMaSan(String maSan) { this.maSan = maSan; }

    public String getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getTenKhach() {
        if (tenKhach != null && !tenKhach.isBlank()) return tenKhach;
        if (maKhachHang != null) {
            KhachHang kh = Utils.DataStore.get().findKhachHangById(maKhachHang);
            if (kh != null && kh.getTenKhachHang() != null) return kh.getTenKhachHang();
        }
        return "";
    }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }

    public String getSoDienThoaiKhach() {
        if (soDienThoaiKhach != null && !soDienThoaiKhach.isBlank()) return soDienThoaiKhach;
        if (maKhachHang != null) {
            KhachHang kh = Utils.DataStore.get().findKhachHangById(maKhachHang);
            if (kh != null && kh.getSoDienThoai() != null) return kh.getSoDienThoai();
        }
        return "";
    }
    public void setSoDienThoaiKhach(String soDienThoaiKhach) { this.soDienThoaiKhach = soDienThoaiKhach; }

    public String getNgayDat() { return ngayDat; }
    public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }

    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }

    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    // ─── Trường UI runtime ────────────────────────────────────────────────────

    public String getTenSan() { return tenSan; }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }

    public double getTienSan() { return tienSan; }
    public void setTienSan(double tienSan) { this.tienSan = tienSan; recalc(); }

    public double getTienDichVu() { return tienDichVu; }
    public void setTienDichVu(double tienDichVu) { this.tienDichVu = tienDichVu; recalc(); }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getDatCoc() { return datCoc; }
    public void setDatCoc(double datCoc) { this.datCoc = datCoc; }

    public double getConLai() {
        if ("DaThanhToan".equalsIgnoreCase(trangThaiTT)) return 0;
        return Math.max(0, tongTien - datCoc);
    }

    public String getTrangThaiTT() { return trangThaiTT; }
    public void setTrangThaiTT(String trangThaiTT) { this.trangThaiTT = trangThaiTT; }

    public String getDichVuKem() { return dichVuKem; }
    public void setDichVuKem(String dichVuKem) { this.dichVuKem = dichVuKem; }

    public java.util.Map<Integer, Integer> getSelectedDvMap() { return selectedDvMap; }
    public void setSelectedDvMap(java.util.Map<Integer, Integer> m) {
        this.selectedDvMap = m != null ? m : new java.util.HashMap<>();
    }

    public java.util.Map<Integer, Integer> getSelectedDoAnMap() { return selectedDoAnMap; }
    public void setSelectedDoAnMap(java.util.Map<Integer, Integer> m) {
        this.selectedDoAnMap = m != null ? m : new java.util.HashMap<>();
    }

    // ─── Helper methods ───────────────────────────────────────────────────────

    public String getKhungGio() {
        return gioBatDau + " - " + gioKetThuc;
    }

    public String getTrangThaiHienThi() {
        return switch (trangThai == null ? "" : trangThai) {
            case "ChoXacNhan" -> "Chờ xác nhận";
            case "DaXacNhan"  -> "Đã xác nhận";
            case "HoanThanh"  -> "Hoàn thành";
            case "DaHuy"      -> "Đã hủy";
            default           -> trangThai;
        };
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
