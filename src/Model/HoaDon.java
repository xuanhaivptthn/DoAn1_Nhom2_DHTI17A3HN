package Model;

/**
 * Lớp model đại diện cho thông tin hóa đơn thanh toán (bảng hoa_don trong CSDL).
 * <p>
 * Lớp này quản lý việc tính toán và lưu trữ các khoản chi phí thanh toán cho mỗi lượt thuê sân bóng,
 * bao gồm tiền sân, tiền dịch vụ đi kèm, tiền mua đồ ăn/vật tư kho, các khoản giảm giá
 * và phương thức thanh toán của khách hàng.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class HoaDon {
    /** 
     * Mã hóa đơn (Khoá chính, AUTO_INCREMENT trong DB hoặc chuỗi varchar(20)).
     */
    private String maHoaDon;

    /** 
     * Mã lịch đặt sân tương ứng (Khoá ngoại tham chiếu lich_dat_san.maLichDat, NOT NULL, UNIQUE).
     */
    private String maLichDat;

    /** 
     * Mã nhân viên lập hóa đơn thanh toán (Khoá ngoại tham chiếu nhan_vien.maNhanVien, NOT NULL).
     */
    private String maNhanVien;

    /** 
     * Ngày giờ lập và thanh toán hóa đơn (DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP).
     */
    private String ngayThanhToan;

    /** 
     * Chi phí thuê sân bóng (DECIMAL(12,2), NOT NULL).
     */
    private double chiPhiSan;

    /** 
     * Tổng số tiền các dịch vụ phát sinh thêm (DECIMAL(12,2), NOT NULL, DEFAULT 0).
     */
    private double tongTienDichVu;

    /** 
     * Tổng số tiền các vật tư/sản phẩm kho phát sinh (DECIMAL(12,2), NOT NULL, DEFAULT 0).
     */
    private double tongTienKho;

    /** 
     * Chiết khấu hoặc giảm giá áp dụng cho hóa đơn (DECIMAL(12,2), NOT NULL, DEFAULT 0).
     */
    private double giamGia;

    /** 
     * Tổng số tiền thực tế thu của khách hàng (DECIMAL(12,2), NOT NULL).
     */
    private double tongTien;

    /** 
     * Phương thức thanh toán (Ví dụ: "Tiền mặt", "Chuyển khoản QR", "Thẻ", varchar(100), NOT NULL).
     */
    private String phuongThucThanhToan;

    /** 
     * Chuỗi danh sách chi tiết các dịch vụ kèm theo & sản phẩm kho được chọn.
     */
    private String dichVuKem;

    /**
     * Khởi tạo một đối tượng HoaDon mới không tham số.
     */
    public HoaDon() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo đối tượng HoaDon với 9 tham số cơ bản.
     * 
     * @param maHoaDon            Mã hóa đơn.
     * @param maLichDat           Mã lịch đặt sân.
     * @param maNhanVien          Mã nhân viên thanh toán.
     * @param ngayThanhToan       Thời gian thanh toán.
     * @param chiPhiSan           Tiền thuê sân bóng.
     * @param tongTienDichVu      Tổng tiền các dịch vụ.
     * @param giamGia             Số tiền giảm giá.
     * @param tongTien            Tổng số tiền thanh toán thực tế.
     * @param phuongThucThanhToan Phương thức thanh toán.
     */
    public HoaDon(String maHoaDon, String maLichDat, String maNhanVien,
                  String ngayThanhToan, double chiPhiSan, double tongTienDichVu,
                  double giamGia, double tongTien, String phuongThucThanhToan) {
        // Gán thuộc tính chi tiết cho hóa đơn
        this.maHoaDon           = maHoaDon;
        this.maLichDat          = maLichDat;
        this.maNhanVien         = maNhanVien;
        this.ngayThanhToan      = ngayThanhToan;
        this.chiPhiSan          = chiPhiSan;
        this.tongTienDichVu     = tongTienDichVu;
        this.giamGia            = giamGia;
        this.tongTien           = tongTien;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    /**
     * Khởi tạo đối tượng HoaDon với 10 tham số bao gồm thêm tiền hàng hóa kho.
     * 
     * @param maHoaDon            Mã hóa đơn.
     * @param maLichDat           Mã lịch đặt sân.
     * @param maNhanVien          Mã nhân viên thanh toán.
     * @param ngayThanhToan       Thời gian thanh toán.
     * @param chiPhiSan           Tiền thuê sân.
     * @param tongTienDichVu      Tổng tiền các dịch vụ.
     * @param tongTienKho         Tổng tiền hàng hóa kho.
     * @param giamGia             Số tiền giảm giá.
     * @param tongTien            Tổng tiền thực thu.
     * @param phuongThucThanhToan Phương thức thanh toán.
     */
    public HoaDon(String maHoaDon, String maLichDat, String maNhanVien,
                  String ngayThanhToan, double chiPhiSan, double tongTienDichVu,
                  double tongTienKho, double giamGia, double tongTien, String phuongThucThanhToan) {
        // Gọi constructor 9 tham số và gán thêm tổng tiền kho
        this(maHoaDon, maLichDat, maNhanVien, ngayThanhToan, chiPhiSan, tongTienDichVu, giamGia, tongTien, phuongThucThanhToan);
        this.tongTienKho = tongTienKho;
    }

    /**
     * Constructor tương thích với mã UI cũ (không truyền maLichDat và maNhanVien).
     * 
     * @param maHoaDon            Mã hóa đơn.
     * @param ngayThanhToan       Thời gian thanh toán.
     * @param chiPhiSan           Tiền thuê sân.
     * @param tongTienDichVu      Tổng tiền dịch vụ.
     * @param giamGia             Số tiền giảm giá.
     * @param tongTien            Tổng tiền thực thu.
     * @param phuongThucThanhToan Phương thức thanh toán.
     */
    public HoaDon(String maHoaDon, String ngayThanhToan, double chiPhiSan,
                  double tongTienDichVu, double giamGia, double tongTien,
                  String phuongThucThanhToan) {
        // Truyền null cho mã lịch đặt và mã nhân viên
        this(maHoaDon, null, null, ngayThanhToan, chiPhiSan,
             tongTienDichVu, giamGia, tongTien, phuongThucThanhToan);
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────

    /**
     * Lấy mã hóa đơn.
     * 
     * @return Mã hóa đơn.
     */
    public String getMaHoaDon() {
        // Trả về mã hóa đơn
        return maHoaDon;
    }

    /**
     * Cập nhật mã hóa đơn.
     * 
     * @param maHoaDon Mã hóa đơn mới.
     */
    public void setMaHoaDon(String maHoaDon) {
        // Gán mã hóa đơn
        this.maHoaDon = maHoaDon;
    }

    /**
     * Lấy mã lịch đặt sân tương ứng.
     * 
     * @return Mã lịch đặt sân.
     */
    public String getMaLichDat() {
        // Trả về mã lịch đặt
        return maLichDat;
    }

    /**
     * Cập nhật mã lịch đặt sân tương ứng.
     * 
     * @param maLichDat Mã lịch đặt mới.
     */
    public void setMaLichDat(String maLichDat) {
        // Gán mã lịch đặt
        this.maLichDat = maLichDat;
    }

    /**
     * Lấy mã nhân viên thanh toán.
     * 
     * @return Mã nhân viên.
     */
    public String getMaNhanVien() {
        // Trả về mã nhân viên
        return maNhanVien;
    }

    /**
     * Cập nhật mã nhân viên thực hiện thanh toán.
     * 
     * @param maNhanVien Mã nhân viên mới.
     */
    public void setMaNhanVien(String maNhanVien) {
        // Gán mã nhân viên
        this.maNhanVien = maNhanVien;
    }

    /**
     * Lấy thời gian thực hiện thanh toán.
     * 
     * @return Chuỗi thời gian thanh toán.
     */
    public String getNgayThanhToan() {
        // Trả về ngày thanh toán
        return ngayThanhToan;
    }

    /**
     * Cập nhật thời gian thanh toán.
     * 
     * @param ngayThanhToan Thời gian thanh toán mới.
     */
    public void setNgayThanhToan(String ngayThanhToan) {
        // Gán ngày thanh toán
        this.ngayThanhToan = ngayThanhToan;
    }

    /**
     * Lấy chi phí thuê sân bóng.
     * 
     * @return Tiền thuê sân bóng.
     */
    public double getChiPhiSan() {
        // Trả về tiền sân
        return chiPhiSan;
    }

    /**
     * Cập nhật chi phí thuê sân bóng.
     * 
     * @param chiPhiSan Số tiền thuê sân mới.
     */
    public void setChiPhiSan(double chiPhiSan) {
        // Gán chi phí sân
        this.chiPhiSan = chiPhiSan;
    }

    /**
     * Lấy tổng tiền các dịch vụ đi kèm.
     * 
     * @return Tiền dịch vụ.
     */
    public double getTongTienDichVu() {
        // Trả về tiền dịch vụ
        return tongTienDichVu;
    }

    /**
     * Cập nhật tổng tiền dịch vụ.
     * 
     * @param tongTienDichVu Tiền dịch vụ mới.
     */
    public void setTongTienDichVu(double tongTienDichVu) {
        // Gán tiền dịch vụ
        this.tongTienDichVu = tongTienDichVu;
    }

    /**
     * Lấy tổng tiền hàng hóa/vật tư xuất kho.
     * 
     * @return Tiền hàng hóa kho.
     */
    public double getTongTienKho() {
        // Trả về tiền hàng hóa kho
        return tongTienKho;
    }

    /**
     * Cập nhật tổng tiền hàng hóa kho.
     * 
     * @param tongTienKho Tiền kho mới.
     */
    public void setTongTienKho(double tongTienKho) {
        // Gán tiền kho
        this.tongTienKho = tongTienKho;
    }

    /**
     * Lấy số tiền giảm giá chiết khấu.
     * 
     * @return Số tiền giảm giá.
     */
    public double getGiamGia() {
        // Trả về tiền giảm giá
        return giamGia;
    }

    /**
     * Cập nhật số tiền giảm giá.
     * 
     * @param giamGia Tiền giảm giá mới.
     */
    public void setGiamGia(double giamGia) {
        // Gán tiền giảm giá
        this.giamGia = giamGia;
    }

    /**
     * Lấy tổng số tiền thanh toán thực thu.
     * 
     * @return Tổng số tiền.
     */
    public double getTongTien() {
        // Trả về tổng tiền thực thu
        return tongTien;
    }

    /**
     * Cập nhật trực tiếp tổng tiền hóa đơn.
     * 
     * @param tongTien Tổng tiền mới.
     */
    public void setTongTien(double tongTien) {
        // Gán tổng tiền
        this.tongTien = tongTien;
    }

    /**
     * Lấy tên phương thức thanh toán được chọn.
     * 
     * @return Tên phương thức thanh toán.
     */
    public String getPhuongThucThanhToan() {
        // Trả về phương thức thanh toán
        return phuongThucThanhToan;
    }

    /**
     * Cập nhật phương thức thanh toán.
     * 
     * @param phuongThucThanhToan Tên phương thức thanh toán mới.
     */
    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        // Gán phương thức thanh toán
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    /**
     * Lấy danh sách mô tả các dịch vụ kèm theo.
     * 
     * @return Chuỗi mô tả dịch vụ kèm theo.
     */
    public String getDichVuKem() {
        // Trả về dịch vụ kèm theo
        return dichVuKem;
    }

    /**
     * Cập nhật danh sách mô tả dịch vụ kèm theo.
     * 
     * @param dichVuKem Chuỗi dịch vụ kèm mới.
     */
    public void setDichVuKem(String dichVuKem) {
        // Gán dịch vụ kèm theo
        this.dichVuKem = dichVuKem;
    }

    /**
     * Tra cứu đối tượng DatLich tương ứng từ DataStore qua maLichDat.
     * 
     * @return Đối tượng DatLich hoặc null nếu không tìm thấy.
     */
    public DatLich getDatLich() {
        // Tra cứu lịch đặt từ DataStore nếu có mã lịch đặt
        if (maLichDat != null) {
            return Utils.DataStore.get().findDatLichById(maLichDat);
        }
        return null;
    }

    /**
     * Tra cứu đối tượng NhanVien thực hiện thanh toán từ DataStore qua maNhanVien.
     * 
     * @return Đối tượng NhanVien hoặc null nếu không tìm thấy.
     */
    public NhanVien getNhanVien() {
        // Tra cứu nhân viên từ DataStore nếu có mã nhân viên
        if (maNhanVien != null) {
            return Utils.DataStore.get().findNhanVienById(maNhanVien);
        }
        return null;
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /**
     * Tính toán lại tổng tiền thực thu theo công thức:
     * <br>
     * {@code tongTien = (chiPhiSan + tongTienDichVu + tongTienKho) - giamGia}
     * 
     * @return Tổng số tiền thực thu sau khi tính toán.
     */
    public double tinhTien() {
        // Tính tổng cộng tiền sân, tiền dịch vụ, tiền kho rồi trừ chiết khấu giảm giá
        this.tongTien = (this.chiPhiSan + this.tongTienDichVu + this.tongTienKho) - this.giamGia;
        return this.tongTien;
    }
}
