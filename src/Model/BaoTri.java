package Model;

/**
 * Lớp model đại diện cho thông tin bảo trì sân bóng (bảng bao_tri trong CSDL).
 * <p>
 * Lớp này quản lý các thông tin về phiếu bảo trì sân bóng bao gồm mã phiếu, sân được bảo trì,
 * nội dung công việc bảo trì, thời gian bắt đầu, thời gian kết thúc và trạng thái phiếu.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class BaoTri {
    /** 
     * Mã phiếu bảo trì (Khoá chính, không được null, varchar(20)).
     */
    private String maPhieuBaoTri;

    /** 
     * Mã sân bóng cần bảo trì (Khoá ngoại liên kết tới bảng san_bong, không được null, varchar(20)).
     */
    private String maSan;

    /** 
     * Chi tiết sự cố hoặc các hạng mục hư hỏng cần bảo trì (TEXT, không được null).
     */
    private String noiDung;

    /** 
     * Thời điểm bắt đầu thực hiện bảo trì (DATETIME, không được null).
     */
    private String ngayBatDau;

    /** 
     * Thời điểm hoàn thành bảo trì (DATETIME, có thể null hoặc để rỗng nếu chưa bảo trì xong).
     */
    private String ngayKetThuc;

    /** 
     * Trạng thái phiếu bảo trì: DANG_BAO_TRI, HOAN_THANH hoặc HUY (Mặc định là 'DANG_BAO_TRI').
     */
    private String trangThaiPhieu;

    // ─── Trường UI bổ sung (không có trong DB, tính toán lúc runtime) ─────────
    /** 
     * Tên sân hiển thị trên giao diện (Denormalized từ thao tác join dữ liệu).
     */
    private String tenSan;

    /**
     * Khởi tạo một đối tượng BaoTri mới với các giá trị mặc định.
     * Trạng thái phiếu được tự động gán là "DANG_BAO_TRI".
     */
    public BaoTri() {
        // Mặc định khởi tạo phiếu ở trạng thái đang bảo trì
        this.trangThaiPhieu = "DANG_BAO_TRI";
    }

    /**
     * Khởi tạo một đối tượng BaoTri với đầy đủ các thông tin lưu trữ trong cơ sở dữ liệu.
     * 
     * @@param maPhieuBaoTri  Mã phiếu bảo trì duy nhất.
     * @param maSan          Mã sân bóng tiến hành bảo trì.
     * @param noiDung        Nội dung, chi tiết công việc bảo trì.
     * @param ngayBatDau     Thời gian bắt đầu bảo trì.
     * @param ngayKetThuc    Thời gian kết thúc bảo trì.
     * @param trangThaiPhieu Trạng thái của phiếu bảo trì.
     */
    public BaoTri(String maPhieuBaoTri, String maSan, String noiDung,
                  String ngayBatDau, String ngayKetThuc, String trangThaiPhieu) {
        // Gán các trường dữ liệu từ tham số truyền vào
        this.maPhieuBaoTri  = maPhieuBaoTri;
        this.maSan          = maSan;
        this.noiDung        = noiDung;
        this.ngayBatDau     = ngayBatDau;
        this.ngayKetThuc    = ngayKetThuc;
        this.trangThaiPhieu = trangThaiPhieu;
    }

    /**
     * Constructor tương thích seed dữ liệu mẫu cũ và hỗ trợ chuyển đổi dữ liệu.
     * 
     * @param id             ID số nguyên (dữ liệu cũ).
     * @param maPhieu        Mã phiếu bảo trì.
     * @param khuVucId       Mã khu vực sân (dữ liệu cũ).
     * @param tenSan         Tên sân hiển thị.
     * @param noiDung        Nội dung bảo trì.
     * @param nguoiPhuTrach  Người phụ trách bảo trì (bỏ qua do CSDL không dùng).
     * @param ngayBatDau     Thời gian bắt đầu bảo trì.
     * @param ngayKetThuc    Thời gian kết thúc bảo trì.
     * @param chiPhi         Chi phí bảo trì (bỏ qua do CSDL không dùng).
     * @param trangThai      Trạng thái bảo trì cũ cần chuẩn hóa.
     */
    public BaoTri(int id, String maPhieu, int khuVucId, String tenSan, String noiDung,
                  String nguoiPhuTrach, String ngayBatDau, String ngayKetThuc,
                  double chiPhi, String trangThai) {
        // Gán mã phiếu và các thông tin cơ bản
        this.maPhieuBaoTri  = maPhieu;
        this.noiDung        = noiDung;
        this.ngayBatDau     = ngayBatDau;
        this.ngayKetThuc    = ngayKetThuc;
        this.tenSan         = tenSan;
        
        // Chuẩn hóa chuỗi trạng thái cũ về giá trị quy định mới
        this.trangThaiPhieu = switch (trangThai == null ? "" : trangThai) {
            case "DangXuLy", "DANG_BAO_TRI" -> "DANG_BAO_TRI";
            case "HoanThanh", "HOAN_THANH"  -> "HOAN_THANH";
            case "Huy", "HUY"               -> "HUY";
            default -> "DANG_BAO_TRI";
        };
        // Lưu ý: nguoiPhuTrach và chiPhi không còn có trong CSDL nên bỏ qua không lưu
    }

    /**
     * Lấy mã phiếu bảo trì.
     * 
     * @return Chuỗi mã phiếu bảo trì.
     */
    public String getMaPhieuBaoTri() {
        // Trả về mã phiếu bảo trì
        return maPhieuBaoTri;
    }

    /**
     * Cập nhật mã phiếu bảo trì.
     * 
     * @param maPhieuBaoTri Mã phiếu bảo trì mới.
     */
    public void setMaPhieuBaoTri(String maPhieuBaoTri) {
        // Cập nhật giá trị mã phiếu bảo trì
        this.maPhieuBaoTri = maPhieuBaoTri;
    }

    /**
     * Lấy mã sân bóng cần bảo trì.
     * 
     * @return Chuỗi mã sân bóng.
     */
    public String getMaSan() {
        // Trả về mã sân
        return maSan;
    }

    /**
     * Cập nhật mã sân bóng cần bảo trì.
     * 
     * @param maSan Mã sân bóng mới.
     */
    public void setMaSan(String maSan) {
        // Cập nhật giá trị mã sân
        this.maSan = maSan;
    }

    /**
     * Lấy nội dung chi tiết công việc bảo trì.
     * 
     * @return Chuỗi mô tả nội dung bảo trì.
     */
    public String getNoiDung() {
        // Trả về nội dung sự cố/bảo trì
        return noiDung;
    }

    /**
     * Cập nhật nội dung chi tiết công việc bảo trì.
     * 
     * @param noiDung Nội dung bảo trì mới.
     */
    public void setNoiDung(String noiDung) {
        // Cập nhật giá trị nội dung
        this.noiDung = noiDung;
    }

    /**
     * Lấy thời gian bắt đầu bảo trì.
     * 
     * @return Chuỗi thời gian bắt đầu.
     */
    public String getNgayBatDau() {
        // Trả về ngày bắt đầu bảo trì
        return ngayBatDau;
    }

    /**
     * Cập nhật thời gian bắt đầu bảo trì.
     * 
     * @param ngayBatDau Thời gian bắt đầu mới.
     */
    public void setNgayBatDau(String ngayBatDau) {
        // Cập nhật giá trị ngày bắt đầu
        this.ngayBatDau = ngayBatDau;
    }

    /**
     * Lấy thời gian kết thúc bảo trì.
     * 
     * @return Chuỗi thời gian kết thúc.
     */
    public String getNgayKetThuc() {
        // Trả về ngày kết thúc bảo trì
        return ngayKetThuc;
    }

    /**
     * Cập nhật thời gian kết thúc bảo trì.
     * 
     * @param ngayKetThuc Thời gian kết thúc mới.
     */
    public void setNgayKetThuc(String ngayKetThuc) {
        // Cập nhật giá trị ngày kết thúc
        this.ngayKetThuc = ngayKetThuc;
    }

    /**
     * Lấy mã trạng thái của phiếu bảo trì.
     * 
     * @return Chuỗi trạng thái phiếu (DANG_BAO_TRI, HOAN_THANH, HUY).
     */
    public String getTrangThaiPhieu() {
        // Trả về mã trạng thái phiếu
        return trangThaiPhieu;
    }

    /**
     * Cập nhật mã trạng thái của phiếu bảo trì.
     * 
     * @param trangThaiPhieu Mã trạng thái mới.
     */
    public void setTrangThaiPhieu(String trangThaiPhieu) {
        // Cập nhật giá trị mã trạng thái
        this.trangThaiPhieu = trangThaiPhieu;
    }

    /**
     * Lấy tên sân bóng hiển thị. Nếu chưa có tên sân, tự động tra cứu trong DataStore qua maSan.
     * 
     * @return Tên sân bóng tương ứng.
     */
    public String getTenSan() {
        // Kiểm tra nếu tên sân đã được gán trực tiếp
        if (tenSan != null && !tenSan.isBlank()) {
            return tenSan;
        }
        // Nếu chưa có tenSan nhưng có maSan, thực hiện tìm kiếm trong bộ nhớ DataStore
        if (maSan != null) {
            KhuVucSan kv = Utils.DataStore.get().findKhuVucSanById(maSan);
            if (kv != null && kv.getTenSan() != null) {
                return kv.getTenSan();
            }
        }
        // Trả về chuỗi rỗng nếu không tìm thấy
        return "";
    }

    /**
     * Cập nhật tên sân hiển thị.
     * 
     * @param tenSan Tên sân bóng mới.
     */
    public void setTenSan(String tenSan) {
        // Cập nhật giá trị tên sân
        this.tenSan = tenSan;
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /**
     * Lấy tên trạng thái thân thiện với người dùng để hiển thị trên bảng/giao diện.
     * 
     * @return Chuỗi mô tả trạng thái tiếng Việt ("Đang bảo trì", "Hoàn thành", "Đã hủy").
     */
    public String getTrangThaiHienThi() {
        // Kiểm tra trường hợp trạng thái null
        if (trangThaiPhieu == null) return "";
        // Chuyển đổi mã trạng thái sang tên tiếng Việt hiển thị UI
        return switch (trangThaiPhieu.toUpperCase()) {
            case "DANG_BAO_TRI" -> "Đang bảo trì";
            case "HOAN_THANH"   -> "Hoàn thành";
            case "HUY"          -> "Đã hủy";
            default             -> trangThaiPhieu;
        };
    }

    /**
     * Kiểm tra xem sân bóng có đang ở trạng thái đang bảo trì hay không.
     * 
     * @return true nếu sân đang bảo trì, false nếu ngược lại.
     */
    public boolean isDangBaoTri() {
        // So sánh không phân biệt hoa thường với mã trạng thái "DANG_BAO_TRI"
        return "DANG_BAO_TRI".equalsIgnoreCase(trangThaiPhieu);
    }
}
