package Model;

/**
 * Lớp model đại diện cho lịch đặt sân bóng (bảng lich_dat_san trong CSDL).
 * <p>
 * Lớp này lưu trữ chi tiết việc đặt sân của khách hàng, bao gồm thông tin sân bóng,
 * thông tin khách hàng, khung thời gian sử dụng, tiền đặt cọc, trạng thái thanh toán,
 * cũng như danh sách các dịch vụ và đồ ăn đi kèm được chọn.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class DatLich {
    /** 
     * Mã lịch đặt sân (Khoá chính, varchar(20)).
     */
    private String maLichDat;

    /** 
     * Mã sân bóng được đặt (Khoá ngoại tham chiếu san_bong.maSan, NOT NULL).
     */
    private String maSan;

    /** 
     * Mã tài khoản của nhân viên xử lý lịch đặt (Khoá ngoại tham chiếu tai_khoan.maTaiKhoan, varchar(50)).
     */
    private String maTaiKhoan;

    /** 
     * Mã khách hàng thực hiện đặt sân (Khoá ngoại tham chiếu khach_hang.maKhachHang, NOT NULL).
     */
    private String maKhachHang;

    /** 
     * Tên khách hàng (Truy vấn denormalized từ bảng khách hàng để hiển thị nhanh trên UI, varchar(100)).
     */
    private String tenKhach;

    /** 
     * Số điện thoại liên hệ của khách hàng (Denormalized, varchar(15)).
     */
    private String soDienThoaiKhach;

    /** 
     * Ngày đặt sân (Định dạng yyyy-MM-dd, NOT NULL).
     */
    private String ngayDat;

    /** 
     * Giờ bắt đầu đá (Định dạng HH:mm hoặc yyyy-MM-dd HH:mm:ss, NOT NULL).
     */
    private String gioBatDau;

    /** 
     * Giờ kết thúc đá (Định dạng HH:mm hoặc yyyy-MM-dd HH:mm:ss, NOT NULL).
     */
    private String gioKetThuc;

    /** 
     * Trạng thái phiếu đặt lịch (Ví dụ: ChoXacNhan, DaXacNhan, HoanThanh, DaHuy).
     */
    private String trangThai;

    /** 
     * Ghi chú thông tin riêng cho lượt đặt sân này.
     */
    private String ghiChu;

    // ─── Trường UI bổ sung (không có trong DB, tính toán lúc runtime) ─────────
    /** 
     * Tên sân bóng tương ứng (tính toán runtime cho UI).
     */
    private String tenSan;

    /** 
     * Tổng tiền thuê sân bóng (không bao gồm dịch vụ).
     */
    private double tienSan;

    /** 
     * Tổng tiền sử dụng dịch vụ và mặt hàng kho đi kèm.
     */
    private double tienDichVu;

    /** 
     * Tổng tiền phải trả (tiền sân + tiền dịch vụ).
     */
    private double tongTien;

    /** 
     * Số tiền khách đã trả trước để đặt cọc.
     */
    private double datCoc;

    /** 
     * Trạng thái thanh toán (ChuaThanhToan, ThanhToanMotPhan, DaThanhToan).
     */
    private String trangThaiTT;

    /** 
     * Chuỗi mô tả các dịch vụ đi kèm đã đặt (dùng hiển thị hóa đơn/bảng).
     */
    private String dichVuKem;

    /** 
     * Bản đồ lưu danh sách dịch vụ chọn thêm {maDichVu -> soLuong}.
     */
    private java.util.Map<Integer, Integer> selectedDvMap  = new java.util.HashMap<>();

    /** 
     * Bản đồ lưu danh sách đồ ăn/vật phẩm kho chọn thêm {maHangHoa -> soLuong}.
     */
    private java.util.Map<Integer, Integer> selectedDoAnMap = new java.util.HashMap<>();

    /**
     * Khởi tạo một đối tượng DatLich mới với các giá trị mặc định cho UI.
     * Mặc định trạng thái thanh toán là "ChuaThanhToan" và tiền đặt cọc bằng 0.
     */
    public DatLich() {
        // Thiết lập trạng thái thanh toán mặc định và tiền đặt cọc ban đầu
        this.trangThaiTT = "ChuaThanhToan";
        this.dichVuKem   = "";
        this.datCoc      = 0;
    }

    /**
     * Khởi tạo một đối tượng DatLich đầy đủ tham số tương thích với dữ liệu mẫu cũ.
     * 
     * @param id               ID số nguyên của lịch đặt.
     * @param maLichDat        Mã lịch đặt chuỗi.
     * @param khuVucId         Mã khu vực sân.
     * @param tenSan           Tên sân bóng.
     * @param tenKhach         Tên khách hàng đặt.
     * @param soDienThoaiKhach Số điện thoại khách hàng.
     * @param ngayDat          Ngày đặt sân.
     * @param gioBatDau        Giờ bắt đầu sử dụng sân.
     * @param gioKetThuc       Giờ kết thúc sử dụng sân.
     * @param tongTien         Tổng tiền dự tính.
     * @param trangThai        Trạng thái lịch đặt.
     * @param maTaiKhoan       Mã tài khoản nhân viên tiếp nhận.
     * @param ghiChu           Ghi chú thêm.
     */
    public DatLich(int id, String maLichDat, int khuVucId, String tenSan, String tenKhach,
                   String soDienThoaiKhach, String ngayDat, String gioBatDau, String gioKetThuc,
                   double tongTien, String trangThai, String maTaiKhoan, String ghiChu) {
        // Gán các thuộc tính cơ bản
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
        
        // Tự động xác định trạng thái thanh toán dựa theo trạng thái lịch
        this.trangThaiTT     = "HoanThanh".equals(trangThai) ? "DaThanhToan" : "ChuaThanhToan";
        this.maTaiKhoan      = maTaiKhoan;
        this.ghiChu          = ghiChu;
        this.dichVuKem       = "";
        this.datCoc          = 0;
    }

    // ─── Getters & Setters theo CSDL ─────────────────────────────────────────

    /**
     * Lấy mã lịch đặt sân.
     * 
     * @return Mã lịch đặt.
     */
    public String getMaLichDat() {
        // Trả về mã lịch đặt
        return maLichDat;
    }

    /**
     * Cập nhật mã lịch đặt sân.
     * 
     * @param maLichDat Mã lịch đặt mới.
     */
    public void setMaLichDat(String maLichDat) {
        // Gán giá trị mã lịch đặt
        this.maLichDat = maLichDat;
    }

    /**
     * Lấy mã sân bóng được đặt.
     * 
     * @return Mã sân bóng.
     */
    public String getMaSan() {
        // Trả về mã sân
        return maSan;
    }

    /**
     * Cập nhật mã sân bóng được đặt.
     * 
     * @param maSan Mã sân bóng mới.
     */
    public void setMaSan(String maSan) {
        // Gán giá trị mã sân
        this.maSan = maSan;
    }

    /**
     * Lấy mã tài khoản nhân viên lập lịch đặt.
     * 
     * @return Mã tài khoản nhân viên.
     */
    public String getMaTaiKhoan() {
        // Trả về mã tài khoản xử lý
        return maTaiKhoan;
    }

    /**
     * Cập nhật mã tài khoản nhân viên lập lịch.
     * 
     * @param maTaiKhoan Mã tài khoản mới.
     */
    public void setMaTaiKhoan(String maTaiKhoan) {
        // Gán giá trị mã tài khoản
        this.maTaiKhoan = maTaiKhoan;
    }

    /**
     * Lấy mã khách hàng thực hiện đặt sân.
     * 
     * @return Mã khách hàng.
     */
    public String getMaKhachHang() {
        // Trả về mã khách hàng
        return maKhachHang;
    }

    /**
     * Cập nhật mã khách hàng thực hiện đặt sân.
     * 
     * @param maKhachHang Mã khách hàng mới.
     */
    public void setMaKhachHang(String maKhachHang) {
        // Gán giá trị mã khách hàng
        this.maKhachHang = maKhachHang;
    }

    /**
     * Lấy tên khách hàng. Nếu tên khách trống, tự động tra cứu từ đối tượng KhachHang trong DataStore.
     * 
     * @return Tên khách hàng.
     */
    public String getTenKhach() {
        // Kiểm tra xem trường tên khách đã có dữ liệu chưa
        if (tenKhach != null && !tenKhach.isBlank()) return tenKhach;
        
        // Tra cứu qua DataStore nếu có maKhachHang
        if (maKhachHang != null) {
            KhachHang kh = Utils.DataStore.get().findKhachHangById(maKhachHang);
            if (kh != null && kh.getTenKhachHang() != null) return kh.getTenKhachHang();
        }
        return "";
    }

    /**
     * Cập nhật tên khách hàng đặt sân.
     * 
     * @param tenKhach Tên khách hàng mới.
     */
    public void setTenKhach(String tenKhach) {
        // Gán tên khách hàng
        this.tenKhach = tenKhach;
    }

    /**
     * Lấy số điện thoại khách hàng. Tra cứu tự động nếu thông tin chưa có sẵn.
     * 
     * @return Số điện thoại khách hàng.
     */
    public String getSoDienThoaiKhach() {
        // Kiểm tra nếu số điện thoại đã tồn tại
        if (soDienThoaiKhach != null && !soDienThoaiKhach.isBlank()) return soDienThoaiKhach;
        
        // Tra cứu từ DataStore nếu có maKhachHang
        if (maKhachHang != null) {
            KhachHang kh = Utils.DataStore.get().findKhachHangById(maKhachHang);
            if (kh != null && kh.getSoDienThoai() != null) return kh.getSoDienThoai();
        }
        return "";
    }

    /**
     * Cập nhật số điện thoại của khách hàng.
     * 
     * @param soDienThoaiKhach Số điện thoại mới.
     */
    public void setSoDienThoaiKhach(String soDienThoaiKhach) {
        // Gán số điện thoại khách hàng
        this.soDienThoaiKhach = soDienThoaiKhach;
    }

    /**
     * Lấy ngày đặt sân bóng.
     * 
     * @return Chuỗi ngày đặt (yyyy-MM-dd).
     */
    public String getNgayDat() {
        // Trả về ngày đặt
        return ngayDat;
    }

    /**
     * Cập nhật ngày đặt sân bóng.
     * 
     * @param ngayDat Ngày đặt mới.
     */
    public void setNgayDat(String ngayDat) {
        // Gán giá trị ngày đặt
        this.ngayDat = ngayDat;
    }

    /**
     * Lấy thời điểm bắt đầu đá sân.
     * 
     * @return Chuỗi thời gian bắt đầu.
     */
    public String getGioBatDau() {
        // Trả về giờ bắt đầu
        return gioBatDau;
    }

    /**
     * Cập nhật thời điểm bắt đầu đá sân.
     * 
     * @param gioBatDau Thời gian bắt đầu mới.
     */
    public void setGioBatDau(String gioBatDau) {
        // Gán giá trị giờ bắt đầu
        this.gioBatDau = gioBatDau;
    }

    /**
     * Lấy thời điểm kết thúc đá sân.
     * 
     * @return Chuỗi thời gian kết thúc.
     */
    public String getGioKetThuc() {
        // Trả về giờ kết thúc
        return gioKetThuc;
    }

    /**
     * Cập nhật thời điểm kết thúc đá sân.
     * 
     * @param gioKetThuc Thời gian kết thúc mới.
     */
    public void setGioKetThuc(String gioKetThuc) {
        // Gán giá trị giờ kết thúc
        this.gioKetThuc = gioKetThuc;
    }

    /**
     * Lấy trạng thái của lịch đặt sân.
     * 
     * @return Chuỗi trạng thái lịch.
     */
    public String getTrangThai() {
        // Trả về trạng thái
        return trangThai;
    }

    /**
     * Cập nhật trạng thái lịch đặt sân.
     * 
     * @param trangThai Trạng thái mới.
     */
    public void setTrangThai(String trangThai) {
        // Gán giá trị trạng thái
        this.trangThai = trangThai;
    }

    /**
     * Lấy thông tin ghi chú cho lượt đặt sân.
     * 
     * @return Chuỗi ghi chú.
     */
    public String getGhiChu() {
        // Trả về ghi chú
        return ghiChu;
    }

    /**
     * Cập nhật thông tin ghi chú cho lượt đặt sân.
     * 
     * @param ghiChu Ghi chú mới.
     */
    public void setGhiChu(String ghiChu) {
        // Gán giá trị ghi chú
        this.ghiChu = ghiChu;
    }

    // ─── Trường UI runtime ────────────────────────────────────────────────────

    /**
     * Lấy tên sân bóng hiển thị UI. Tra cứu từ DataStore qua mã sân nếu cần.
     * 
     * @return Tên sân bóng.
     */
    public String getTenSan() {
        // Trả về tenSan nếu có sẵn
        if (tenSan != null && !tenSan.isBlank()) return tenSan;
        
        // Tra cứu theo maSan từ DataStore
        if (maSan != null) {
            KhuVucSan kv = Utils.DataStore.get().findKhuVucSanById(maSan);
            if (kv != null && kv.getTenSan() != null) return kv.getTenSan();
        }
        return "";
    }

    /**
     * Cập nhật tên sân hiển thị.
     * 
     * @param tenSan Tên sân bóng mới.
     */
    public void setTenSan(String tenSan) {
        // Gán tên sân bóng
        this.tenSan = tenSan;
    }

    /**
     * Lấy số tiền thuê sân bóng độc lập.
     * 
     * @return Số tiền thuê sân.
     */
    public double getTienSan() {
        // Trả về tiền sân
        return tienSan;
    }

    /**
     * Cập nhật tiền thuê sân và tính lại tổng tiền.
     * 
     * @param tienSan Số tiền thuê sân mới.
     */
    public void setTienSan(double tienSan) {
        // Cập nhật giá tiền sân và gọi recalc() để cập nhật tongTien
        this.tienSan = tienSan;
        recalc();
    }

    /**
     * Tính tổng tiền dịch vụ phát sinh từ danh sách dịch vụ và đồ ăn kho đã chọn,
     * hoặc từ hóa đơn tương ứng nếu có.
     * 
     * @return Tổng chi phí dịch vụ đi kèm.
     */
    public double getTienDichVu() {
        // Ưu tiên tính tiền từ bản đồ dịch vụ và kho đang chọn
        double calc = getTongTienDichVuOnly() + getTongTienKhoOnly();
        if (calc > 0) return calc;
        
        // Nếu không có trong bản đồ, kiểm tra biến tienDichVu hiện tại
        if (this.tienDichVu > 0) return this.tienDichVu;
        
        // Cuối cùng kiểm tra thông tin dịch vụ trong HoaDon nếu đã lập hóa đơn
        if (maLichDat != null) {
            Model.HoaDon hd = Utils.DataStore.get().getHoaDons().stream()
                    .filter(h -> maLichDat.equalsIgnoreCase(h.getMaLichDat()))
                    .findFirst().orElse(null);
            if (hd != null) {
                return hd.getTongTienDichVu() + hd.getTongTienKho();
            }
        }
        return 0;
    }

    /**
     * Cập nhật giá trị tổng tiền dịch vụ và tự động tính lại tổng tiền thanh toán.
     * 
     * @param tienDichVu Số tiền dịch vụ mới.
     */
    public void setTienDichVu(double tienDichVu) {
        // Cập nhật tiền dịch vụ và gọi lại hàm tính tổng
        this.tienDichVu = tienDichVu;
        recalc();
    }

    /**
     * Lấy tổng tiền cần chi trả (tiền sân + tiền dịch vụ).
     * 
     * @return Tổng số tiền.
     */
    public double getTongTien() {
        // Tính tổng tiền bằng tiền sân cộng với tiền dịch vụ
        return getTienSan() + getTienDichVu();
    }

    /**
     * Cập nhật trực tiếp giá trị tổng tiền.
     * 
     * @param tongTien Giá trị tổng tiền mới.
     */
    public void setTongTien(double tongTien) {
        // Gán tổng tiền
        this.tongTien = tongTien;
    }

    /**
     * Lấy số tiền khách hàng đã đặt cọc trước.
     * 
     * @return Số tiền đặt cọc.
     */
    public double getDatCoc() {
        // Trả về tiền đặt cọc
        return datCoc;
    }

    /**
     * Cập nhật số tiền khách hàng đặt cọc.
     * 
     * @param datCoc Số tiền cọc mới.
     */
    public void setDatCoc(double datCoc) {
        // Gán số tiền đặt cọc
        this.datCoc = datCoc;
    }

    /**
     * Tính số tiền còn lại khách hàng cần thanh toán sau khi trừ tiền cọc.
     * 
     * @return Số tiền chưa thanh toán (tối thiểu là 0).
     */
    public double getConLai() {
        // Nếu đã thanh toán đủ thì số tiền còn lại là 0
        if ("DaThanhToan".equalsIgnoreCase(trangThaiTT)) return 0;
        
        // Trả về tổng tiền trừ tiền cọc, không âm
        return Math.max(0, getTongTien() - datCoc);
    }

    /**
     * Lấy mã trạng thái thanh toán.
     * 
     * @return Chuỗi trạng thái thanh toán (ChuaThanhToan, ThanhToanMotPhan, DaThanhToan).
     */
    public String getTrangThaiTT() {
        // Trả về trạng thái thanh toán
        return trangThaiTT;
    }

    /**
     * Cập nhật mã trạng thái thanh toán.
     * 
     * @param trangThaiTT Trạng thái thanh toán mới.
     */
    public void setTrangThaiTT(String trangThaiTT) {
        // Gán trạng thái thanh toán
        this.trangThaiTT = trangThaiTT;
    }

    /**
     * Lấy chuỗi thông tin mô tả chi tiết các dịch vụ đi kèm.
     * 
     * @return Chuỗi thông tin dịch vụ kèm theo.
     */
    public String getDichVuKem() {
        // Kiểm tra xem đã có thông tin chuỗi dịch vụ kèm chưa
        if (dichVuKem != null && !dichVuKem.isBlank()) return dichVuKem;
        
        // Tra cứu thêm từ hóa đơn liên kết nếu có
        if (maLichDat != null) {
            Model.HoaDon hd = Utils.DataStore.get().getHoaDons().stream()
                    .filter(h -> maLichDat.equalsIgnoreCase(h.getMaLichDat()))
                    .findFirst().orElse(null);
            if (hd != null && hd.getDichVuKem() != null && !hd.getDichVuKem().isBlank()) {
                return hd.getDichVuKem();
            }
        }
        return dichVuKem;
    }

    /**
     * Cập nhật chuỗi mô tả dịch vụ đi kèm.
     * 
     * @param dichVuKem Chuỗi mô tả dịch vụ mới.
     */
    public void setDichVuKem(String dichVuKem) {
        // Gán chuỗi dịch vụ kèm
        this.dichVuKem = dichVuKem;
    }

    /**
     * Lấy danh sách các dịch vụ chọn kèm và số lượng tương ứng.
     * 
     * @return Map với key là mã dịch vụ (int) và value là số lượng (int).
     */
    public java.util.Map<Integer, Integer> getSelectedDvMap() {
        // Trả về map dịch vụ được chọn
        return selectedDvMap;
    }

    /**
     * Cập nhật danh sách các dịch vụ chọn kèm.
     * 
     * @param m Map danh sách dịch vụ mới.
     */
    public void setSelectedDvMap(java.util.Map<Integer, Integer> m) {
        // Gán map dịch vụ (đảm bảo không null)
        this.selectedDvMap = m != null ? m : new java.util.HashMap<>();
    }

    /**
     * Lấy danh sách đồ ăn/vật tư kho chọn kèm và số lượng tương ứng.
     * 
     * @return Map với key là mã hàng hóa (int) và value là số lượng (int).
     */
    public java.util.Map<Integer, Integer> getSelectedDoAnMap() {
        // Trả về map đồ ăn/vật phẩm kho
        return selectedDoAnMap;
    }

    /**
     * Cập nhật danh sách đồ ăn/vật tư kho chọn kèm.
     * 
     * @param m Map danh sách đồ ăn/kho mới.
     */
    public void setSelectedDoAnMap(java.util.Map<Integer, Integer> m) {
        // Gán map đồ ăn (đảm bảo không null)
        this.selectedDoAnMap = m != null ? m : new java.util.HashMap<>();
    }

    // ─── Helper methods ───────────────────────────────────────────────────────

    /**
     * Lấy chuỗi định dạng biểu diễn khung giờ đá (GioBatDau - GioKetThuc).
     * 
     * @return Chuỗi dạng "18:00 - 19:30".
     */
    public String getKhungGio() {
        // Ghép giờ bắt đầu và giờ kết thúc thành chuỗi khung giờ
        return gioBatDau + " - " + gioKetThuc;
    }

    /**
     * Chuyển đổi mã trạng thái lịch đặt sang tên tiếng Việt hiển thị UI.
     * 
     * @return Chuỗi biểu diễn trạng thái thân thiện.
     */
    public String getTrangThaiHienThi() {
        // Map các mã trạng thái sang tiếng Việt tương ứng
        return switch (trangThai == null ? "" : trangThai) {
            case "ChoXacNhan" -> "Chờ xác nhận";
            case "DaXacNhan"  -> "Đã xác nhận";
            case "HoanThanh"  -> "Hoàn thành";
            case "DaHuy"      -> "Đã hủy";
            default           -> trangThai;
        };
    }

    /**
     * Thêm thông tin một dịch vụ đi kèm vào danh sách hiển thị và cộng tiền dịch vụ.
     * 
     * @param name Tên dịch vụ/sản phẩm.
     * @param qty  Số lượng.
     * @param cost Tổng chi phí của dịch vụ đó.
     */
    public void addDichVuKem(String name, int qty, double cost) {
        // Thêm dòng thông tin vào chuỗi dichVuKem
        if (dichVuKem == null || dichVuKem.isBlank()) {
            dichVuKem = String.format("%s (x%d): %,.0f VNĐ", name, qty, cost);
        } else {
            dichVuKem += String.format("\n%s (x%d): %,.0f VNĐ", name, qty, cost);
        }
        // Cộng dồn vào tiền dịch vụ và tính lại tổng tiền lịch đặt
        this.tienDichVu += cost;
        recalc();
    }

    /**
     * Tính tổng chi phí chỉ riêng các dịch vụ trong selectedDvMap (không tính hàng hóa kho).
     * 
     * @return Tổng số tiền các dịch vụ.
     */
    public double getTongTienDichVuOnly() {
        double total = 0;
        if (selectedDvMap != null) {
            // Duyệt qua từng dịch vụ được chọn trong Map
            for (java.util.Map.Entry<Integer, Integer> entry : selectedDvMap.entrySet()) {
                int id = entry.getKey();
                int qty = entry.getValue();
                if (qty <= 0) continue;
                
                // Tra cứu dịch vụ từ DataStore
                DichVu dv = Utils.DataStore.get().findDichVuById(id);
                // Nếu dịch vụ hợp lệ và không thuộc loại Vật tư kho thì tính tiền
                if (dv != null && !"Vật tư kho".equalsIgnoreCase(dv.getLoaiDichVu())) {
                    total += dv.getDonGia() * qty;
                }
            }
        }
        return total;
    }

    /**
     * Tính tổng chi phí chỉ riêng các sản phẩm từ kho (đồ ăn, nước uống, vật tư) được chọn.
     * 
     * @return Tổng số tiền hàng hóa kho.
     */
    public double getTongTienKhoOnly() {
        double total = 0;
        // Duyệt danh sách đồ ăn chọn trong selectedDoAnMap
        if (selectedDoAnMap != null) {
            for (java.util.Map.Entry<Integer, Integer> entry : selectedDoAnMap.entrySet()) {
                int id = entry.getKey();
                int qty = entry.getValue();
                if (qty <= 0) continue;
                
                DichVu dv = Utils.DataStore.get().findDichVuById(id);
                if (dv != null) {
                    total += dv.getDonGia() * qty;
                }
            }
        }
        // Duyệt các vật tư kho nằm trong selectedDvMap có loaiDichVu là "Vật tư kho"
        if (selectedDvMap != null) {
            for (java.util.Map.Entry<Integer, Integer> entry : selectedDvMap.entrySet()) {
                int id = entry.getKey();
                int qty = entry.getValue();
                if (qty <= 0) continue;
                
                DichVu dv = Utils.DataStore.get().findDichVuById(id);
                if (dv != null && "Vật tư kho".equalsIgnoreCase(dv.getLoaiDichVu())) {
                    total += dv.getDonGia() * qty;
                }
            }
        }
        return total;
    }

    /**
     * Tính toán lại tổng tiền của lịch đặt sân (Tổng tiền = Tiền thuê sân + Tiền dịch vụ).
     */
    public void recalc() {
        // Cập nhật lại thuộc tính tongTien
        this.tongTien = this.tienSan + this.tienDichVu;
    }
}
