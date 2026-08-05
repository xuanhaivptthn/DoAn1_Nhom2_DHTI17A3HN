package Model;

/**
 * Bảng: bao_tri
 * Lưu trữ phiếu bảo trì sân bóng.
 * FK: maSan → san_bong(maSan)
 */
public class BaoTri {
    /** PK, NOT NULL, varchar(20) */
    private String maPhieuBaoTri;
    /** FK → san_bong.maSan, NOT NULL, varchar(20) */
    private String maSan;
    /** NOT NULL, TEXT - chi tiết sự cố/hạng mục hỏng */
    private String noiDung;
    /** NOT NULL, DATETIME */
    private String ngayBatDau;
    /** NULL, DATETIME - rỗng nếu chưa xong */
    private String ngayKetThuc;
    /** NOT NULL, DEFAULT 'DANG_BAO_TRI' - DANG_BAO_TRI | HOAN_THANH | HUY */
    private String trangThaiPhieu;

    // ─── Trường UI bổ sung (không có trong DB, tính toán lúc runtime) ─────────
    /** Tên sân hiển thị (denormalized từ join) */
    private String tenSan;

    public BaoTri() {
        this.trangThaiPhieu = "DANG_BAO_TRI";
    }

    public BaoTri(String maPhieuBaoTri, String maSan, String noiDung,
                  String ngayBatDau, String ngayKetThuc, String trangThaiPhieu) {
        this.maPhieuBaoTri  = maPhieuBaoTri;
        this.maSan          = maSan;
        this.noiDung        = noiDung;
        this.ngayBatDau     = ngayBatDau;
        this.ngayKetThuc    = ngayKetThuc;
        this.trangThaiPhieu = trangThaiPhieu;
    }

    /** Constructor tương thích seed dữ liệu mẫu cũ */
    public BaoTri(int id, String maPhieu, int khuVucId, String tenSan, String noiDung,
                  String nguoiPhuTrach, String ngayBatDau, String ngayKetThuc,
                  double chiPhi, String trangThai) {
        this.maPhieuBaoTri  = maPhieu;
        this.noiDung        = noiDung;
        this.ngayBatDau     = ngayBatDau;
        this.ngayKetThuc    = ngayKetThuc;
        this.tenSan         = tenSan;
        // Chuẩn hóa trạng thái về ENUM mới
        this.trangThaiPhieu = switch (trangThai == null ? "" : trangThai) {
            case "DangXuLy", "DANG_BAO_TRI" -> "DANG_BAO_TRI";
            case "HoanThanh", "HOAN_THANH"  -> "HOAN_THANH";
            case "Huy", "HUY"               -> "HUY";
            default -> "DANG_BAO_TRI";
        };
        // nguoiPhuTrach, chiPhi không còn trong CSDL - bỏ qua
    }

    public String getMaPhieuBaoTri() { return maPhieuBaoTri; }
    public void setMaPhieuBaoTri(String maPhieuBaoTri) { this.maPhieuBaoTri = maPhieuBaoTri; }

    public String getMaSan() { return maSan; }
    public void setMaSan(String maSan) { this.maSan = maSan; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public String getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getTrangThaiPhieu() { return trangThaiPhieu; }
    public void setTrangThaiPhieu(String trangThaiPhieu) { this.trangThaiPhieu = trangThaiPhieu; }

    public String getTenSan() {
        if (tenSan != null && !tenSan.isBlank()) return tenSan;
        if (maSan != null) {
            KhuVucSan kv = Utils.DataStore.get().findKhuVucSanById(maSan);
            if (kv != null && kv.getTenSan() != null) return kv.getTenSan();
        }
        return "";
    }
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }

    // ─── Helper methods ────────────────────────────────────────────────────────

    public String getTrangThaiHienThi() {
        if (trangThaiPhieu == null) return "";
        return switch (trangThaiPhieu.toUpperCase()) {
            case "DANG_BAO_TRI" -> "Đang bảo trì";
            case "HOAN_THANH"   -> "Hoàn thành";
            case "HUY"          -> "Đã hủy";
            default             -> trangThaiPhieu;
        };
    }

    public boolean isDangBaoTri() {
        return "DANG_BAO_TRI".equalsIgnoreCase(trangThaiPhieu);
    }

}
