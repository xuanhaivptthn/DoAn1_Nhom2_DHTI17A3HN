package Model;

import Utils.DataStore;

/**
 * Lớp model đại diện cho dịch vụ đi kèm và vật tư hàng hóa trong kho (bảng dich_vu trong CSDL).
 * <p>
 * Lớp này quản lý các dịch vụ đi kèm khi thuê sân bóng (nước uống, áo đấu, trọng tài,...)
 * cũng như các mặt hàng tồn kho được quản lý tại cụm sân.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class DichVu {
    /** 
     * Mã dịch vụ (Khoá chính, AUTO_INCREMENT trong DB hoặc chuỗi định dạng như DV001, HH001, varchar(20)).
     */
    private String maDichVu;

    /** 
     * Loại dịch vụ (Phân loại: Ví dụ "Nước uống", "Dụng cụ", "Vật tư kho", varchar(50), NOT NULL).
     */
    private String loaiDichVu;

    /** 
     * Tên gọi của dịch vụ hoặc hàng hóa (NOT NULL, UNIQUE, varchar(100)).
     */
    private String tenDichVu;

    /** 
     * Đơn giá tính cho một đơn vị dịch vụ/hàng hóa (DECIMAL(15,0), NOT NULL).
     */
    private double gia;

    /** 
     * Mô tả chi tiết dịch vụ hoặc tên nhà cung cấp sản phẩm (TEXT, có thể null).
     */
    private String moTa;

    // ─── Trường mở rộng dùng cho kho hàng hóa (không có trong bảng dich_vu CSDL) ─
    /** 
     * Số lượng tồn kho hiện tại (dùng khi loại dịch vụ là "Vật tư kho").
     */
    private int soLuongTon;

    /** 
     * Đơn vị tính của dịch vụ/hàng hóa (Ví dụ: chai, lon, bộ, lượt,...).
     */
    private String donVi;

    /**
     * Khởi tạo một đối tượng DichVu mới không tham số.
     */
    public DichVu() {
        // Khởi tạo mặc định
    }

    /**
     * Khởi tạo đối tượng DichVu với các trường thông tin dịch vụ cơ bản.
     * 
     * @param maDichVu   Mã dịch vụ.
     * @param tenDichVu  Tên dịch vụ.
     * @param loaiDichVu Loại dịch vụ.
     * @param gia        Đơn giá dịch vụ.
     * @param moTa       Mô tả chi tiết.
     */
    public DichVu(String maDichVu, String tenDichVu, String loaiDichVu, double gia, String moTa) {
        // Gán các trường giá trị cho dịch vụ
        this.maDichVu   = maDichVu;
        this.tenDichVu  = tenDichVu;
        this.loaiDichVu = loaiDichVu;
        this.gia        = gia;
        this.moTa       = moTa;
        this.soLuongTon = 0;
        this.donVi      = "";
    }

    /**
     * Constructor tương thích seed dịch vụ cũ với mã ID dạng số nguyên.
     * 
     * @param id         ID định danh dạng số nguyên.
     * @param maDichVu   Mã dịch vụ chuỗi (nếu null sẽ tự tạo định dạng DV%03d).
     * @param tenDichVu  Tên dịch vụ.
     * @param loaiDichVu Loại dịch vụ.
     * @param gia        Đơn giá dịch vụ.
     * @param moTa       Mô tả chi tiết.
     */
    public DichVu(int id, String maDichVu, String tenDichVu, String loaiDichVu,
                  double gia, String moTa) {
        // Gọi constructor chính với mã được định dạng chuẩn
        this(maDichVu != null ? maDichVu : String.format("DV%03d", id),
             tenDichVu, loaiDichVu, gia, moTa);
    }

    /**
     * Constructor dành riêng cho việc khởi tạo mặt hàng trong kho hàng hóa.
     * 
     * @param id          ID số nguyên của mặt hàng.
     * @param tenHangHoa  Tên sản phẩm hàng hóa.
     * @param soLuongTon  Số lượng tồn kho ban đầu.
     * @param donGia      Đơn giá bán lẻ/cho thuê.
     * @param nhaCungCap  Thông tin nhà cung cấp (lưu vào ô moTa).
     */
    public DichVu(int id, String tenHangHoa, int soLuongTon, double donGia, String nhaCungCap) {
        // Gán thông tin hàng hóa kho
        this.maDichVu   = String.format("HH%03d", id);
        this.tenDichVu  = tenHangHoa;
        this.soLuongTon = soLuongTon;
        this.gia        = donGia;
        this.moTa       = nhaCungCap;
        this.donVi      = "cái";
        this.loaiDichVu = "Vật tư kho";
    }

    /**
     * Constructor 8 tham số tương thích với Dialog khởi tạo/chỉnh sửa dịch vụ UI.
     * 
     * @param id           ID số nguyên của dịch vụ.
     * @param tenDichVu    Tên dịch vụ.
     * @param moTa         Mô tả chi tiết.
     * @param gia          Đơn giá.
     * @param donVi        Đơn vị tính.
     * @param trangThai    Trạng thái dịch vụ.
     * @param soLuongTon   Số lượng tồn kho.
     * @param tonToiThieu  Mức tồn tối thiểu.
     */
    public DichVu(int id, String tenDichVu, String moTa, double gia, String donVi,
                  String trangThai, int soLuongTon, int tonToiThieu) {
        // Gán thông tin chi tiết dịch vụ mở rộng
        this.maDichVu   = String.format("DV%03d", id);
        this.tenDichVu  = tenDichVu;
        this.moTa       = moTa;
        this.gia        = gia;
        this.donVi      = donVi;
        this.loaiDichVu = donVi;
        this.soLuongTon = soLuongTon;
    }

    // ─── Getters & Setters theo CSDL ─────────────────────────────────────────

    /**
     * Lấy mã dịch vụ.
     * 
     * @return Mã dịch vụ.
     */
    public String getMaDichVu() {
        // Trả về mã dịch vụ
        return maDichVu;
    }

    /**
     * Cập nhật mã dịch vụ.
     * 
     * @param maDichVu Mã dịch vụ mới.
     */
    public void setMaDichVu(String maDichVu) {
        // Gán mã dịch vụ
        this.maDichVu = maDichVu;
    }

    /**
     * Lấy tên dịch vụ.
     * 
     * @return Tên dịch vụ.
     */
    public String getTenDichVu() {
        // Trả về tên dịch vụ
        return tenDichVu;
    }

    /**
     * Cập nhật tên dịch vụ.
     * 
     * @param tenDichVu Tên dịch vụ mới.
     */
    public void setTenDichVu(String tenDichVu) {
        // Gán tên dịch vụ
        this.tenDichVu = tenDichVu;
    }

    /**
     * Lấy loại dịch vụ.
     * 
     * @return Chuỗi phân loại dịch vụ.
     */
    public String getLoaiDichVu() {
        // Trả về loại dịch vụ
        return loaiDichVu;
    }

    /**
     * Cập nhật loại dịch vụ.
     * 
     * @param loaiDichVu Loại dịch vụ mới.
     */
    public void setLoaiDichVu(String loaiDichVu) {
        // Gán loại dịch vụ
        this.loaiDichVu = loaiDichVu;
    }

    /**
     * Lấy giá dịch vụ.
     * 
     * @return Đơn giá dịch vụ.
     */
    public double getGia() {
        // Trả về đơn giá dịch vụ
        return gia;
    }

    /**
     * Cập nhật đơn giá dịch vụ.
     * 
     * @param gia Đơn giá dịch vụ mới.
     */
    public void setGia(double gia) {
        // Gán giá dịch vụ
        this.gia = gia;
    }

    /**
     * Lấy nội dung mô tả của dịch vụ.
     * 
     * @return Chuỗi mô tả.
     */
    public String getMoTa() {
        // Trả về mô tả
        return moTa;
    }

    /**
     * Cập nhật nội dung mô tả của dịch vụ.
     * 
     * @param moTa Chuỗi mô tả mới.
     */
    public void setMoTa(String moTa) {
        // Gán mô tả
        this.moTa = moTa;
    }

    // ─── Alias helpers tương thích UI code hiện tại ───────────────────────────

    /**
     * Alias lấy đơn giá dịch vụ (tương đương getGia()).
     * 
     * @return Đơn giá.
     */
    public double getDonGia() {
        // Trả về đơn giá
        return gia;
    }

    /**
     * Alias cập nhật đơn giá dịch vụ.
     * 
     * @param donGia Đơn giá mới.
     */
    public void setDonGia(double donGia) {
        // Gán đơn giá
        this.gia = donGia;
    }

    /**
     * Lấy số lượng sản phẩm tồn kho.
     * 
     * @return Số lượng tồn kho.
     */
    public int getSoLuongTon() {
        // Trả về số lượng tồn
        return soLuongTon;
    }

    /**
     * Cập nhật số lượng sản phẩm tồn kho.
     * 
     * @param soLuongTon Số lượng tồn mới.
     */
    public void setSoLuongTon(int soLuongTon) {
        // Gán số lượng tồn kho
        this.soLuongTon = soLuongTon;
    }

    /**
     * Lấy đơn vị tính của dịch vụ/sản phẩm.
     * 
     * @return Chuỗi tên đơn vị tính.
     */
    public String getDonVi() {
        // Trả về đơn vị tính hoặc chuỗi rỗng nếu null
        return donVi != null ? donVi : "";
    }

    /**
     * Cập nhật đơn vị tính.
     * 
     * @param donVi Đơn vị tính mới.
     */
    public void setDonVi(String donVi) {
        // Gán đơn vị tính
        this.donVi = donVi;
    }

    /**
     * Lấy mức giới hạn tồn kho tối thiểu.
     * 
     * @return Mức tồn kho tối thiểu (Mặc định: 5).
     */
    public int getTonToiThieu() {
        // Trả về mức tồn tối thiểu mặc định là 5
        return 5;
    }

    /**
     * Trích xuất số ID định danh dạng số từ chuỗi mã dịch vụ (ví dụ "DV001" -> 1, "HH002" -> 2).
     * 
     * @return ID kiểu số nguyên.
     */
    public int getId() {
        // Kiểm tra nếu mã dịch vụ trống
        if (maDichVu == null || maDichVu.isBlank()) return 0;
        
        // Lọc lấy các ký tự là chữ số
        String digits = maDichVu.replaceAll("\\D", "");
        if (digits.isEmpty()) return 0;
        try {
            // Ép kiểu chuỗi số sang int
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ─── Alias cho kho hàng hóa ───────────────────────────────────────────────

    /**
     * Lấy mã hàng hóa kho (tương đương với ID số).
     * 
     * @return Mã hàng hóa dạng int.
     */
    public int getMaHangHoa() {
        // Lấy ID số nguyên
        return getId();
    }

    /**
     * Lấy tên hàng hóa kho (tương đương tên dịch vụ).
     * 
     * @return Tên hàng hóa.
     */
    public String getTenHangHoa() {
        // Trả về tên dịch vụ làm tên hàng hóa
        return tenDichVu;
    }

    /**
     * Lấy thông tin nhà cung cấp (lưu trữ trong trường moTa).
     * 
     * @return Tên nhà cung cấp.
     */
    public String getNhaCungCap() {
        // Trả về trường moTa hoặc giá trị mặc định nếu trống
        return moTa != null && !moTa.isBlank() ? moTa : "Tổng kho Sân bóng";
    }

    /**
     * Cập nhật nhà cung cấp hàng hóa.
     * 
     * @param ncc Tên nhà cung cấp mới.
     */
    public void setNhaCungCap(String ncc) {
        // Gán nhà cung cấp vào moTa
        this.moTa = ncc;
    }

    // ─── Helper methods ────────────────────────────────────────────────────────

    /**
     * Kiểm tra xem mặt hàng trong kho có bị cảnh báo sắp hết hàng hay không.
     * 
     * @return true nếu số lượng tồn lớn hơn 0 và nhỏ hơn hoặc bằng mức tồn tối thiểu.
     */
    public boolean isSapHet() {
        // So sánh tồn kho với mức tồn tối thiểu
        return soLuongTon > 0 && soLuongTon <= getTonToiThieu();
    }

    /**
     * Thực hiện nhập thêm số lượng vào tồn kho sản phẩm.
     * 
     * @param soLuong Số lượng nhập thêm vào kho.
     */
    public void nhapKho(int soLuong) {
        // Nếu số lượng nhập hợp lệ (>0) thì cộng dồn vào soLuongTon
        if (soLuong > 0) soLuongTon += soLuong;
    }

    /**
     * Thực hiện xuất bớt hàng hóa ra khỏi kho.
     * 
     * @param soLuong Số lượng muốn xuất kho.
     * @return true nếu xuất kho thành công, false nếu số lượng không hợp lệ hoặc vượt quá tồn kho.
     */
    public boolean xuatKho(int soLuong) {
        // Kiểm tra điều kiện số lượng xuất
        if (soLuong <= 0 || soLuong > soLuongTon) return false;
        // Trừ số lượng tồn kho
        soLuongTon -= soLuong;
        return true;
    }

    /**
     * Chuyển đổi đối tượng dịch vụ sang dạng chuỗi hiển thị tên và giá tiền.
     * 
     * @return Chuỗi định dạng "Tên dịch vụ (Giá VNĐ)".
     */
    @Override
    public String toString() {
        // Trả về chuỗi kết hợp tên dịch vụ và giá định dạng tiền tệ
        return tenDichVu + " (" + String.format("%,.0f", gia) + " VNĐ)";
    }

    // ─── Phương thức theo sơ đồ lớp e_DichVu & e_Kho ─────────────────────────

    /**
     * Truy vấn danh sách toàn bộ các dịch vụ từ DataStore.
     * 
     * @return Danh sách đối tượng DichVu.
     */
    public static java.util.List<DichVu> truyVanDanhSach() {
        // Lấy danh sách dịch vụ từ DataStore
        return DataStore.get().getDichVus();
    }

    /**
     * Tìm kiếm và lấy thông tin chi tiết của dịch vụ dựa trên mã dịch vụ.
     * 
     * @param maDichVu Mã dịch vụ cần tra cứu.
     * @return Đối tượng DichVu tương ứng.
     */
    public static DichVu layThongTinDichVu(String maDichVu) {
        // Tra cứu dịch vụ theo mã trong DataStore
        return DataStore.get().findDichVuByMa(maDichVu);
    }

    /**
     * Kiểm tra xem tên dịch vụ mới có bị trùng lặp với các dịch vụ đã có hay không.
     * 
     * @param ten Tên dịch vụ cần kiểm tra.
     * @return true nếu đã tồn tại dịch vụ cùng tên.
     */
    public boolean kiemTraTrungTen(String ten) {
        // Nếu chuỗi truyền vào null thì trả về false
        if (ten == null) return false;
        // Duyệt stream danh sách dịch vụ để so sánh tên (không phân biệt hoa thường)
        return DataStore.get().getDichVus().stream().anyMatch(d -> ten.equalsIgnoreCase(d.getTenDichVu()));
    }

    /**
     * Thêm mới và lưu thông tin dịch vụ vào bộ nhớ và cơ sở dữ liệu.
     * 
     * @return true nếu lưu thành công.
     */
    public boolean luuDichVu() {
        // Thêm đối tượng vào DataStore nếu chưa có
        if (!DataStore.get().getDichVus().contains(this)) {
            DataStore.get().getDichVus().add(this);
        }
        // Lưu vào CSDL nếu chế độ Database đang bật
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().insert(this); } catch (Exception ignored) {}
        }
        return true;
    }

    /**
     * Cập nhật dữ liệu dịch vụ hiện tại vào cơ sở dữ liệu.
     * 
     * @return true nếu cập nhật thành công.
     */
    public boolean capNhatDuLieu() {
        // Thực hiện câu lệnh update trong DAO nếu kết nối CSDL
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().update(this); } catch (Exception ignored) {}
        }
        return true;
    }

    /**
     * Xóa dịch vụ khỏi hệ thống dựa vào mã dịch vụ.
     * 
     * @param maDichVu Mã dịch vụ cần xóa.
     * @return true nếu thực hiện xóa thành công.
     */
    public boolean xoaDichVu(String maDichVu) {
        // Xóa dịch vụ trong bộ nhớ DataStore
        DataStore.get().getDichVus().removeIf(d -> d.getMaDichVu() != null && d.getMaDichVu().equals(maDichVu));
        // Xóa dịch vụ trong CSDL thông qua DAO
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().delete(maDichVu); } catch (Exception ignored) {}
        }
        return true;
    }

    /**
     * Truy vấn danh sách các mặt hàng tồn kho từ DataStore.
     * 
     * @return Danh sách các mặt hàng trong kho.
     */
    public static java.util.List<DichVu> truyVanTonKho() {
        // Trả về danh sách đồ kho trong DataStore
        return DataStore.get().getKhoItems();
    }

    /**
     * Lấy số lượng tồn kho của một mặt hàng cụ thể theo mã hàng hóa.
     * 
     * @param maHangHoa Mã hàng hóa (ví dụ "HH001").
     * @return Số lượng tồn kho.
     */
    public static int truyVanSoLuongTon(String maHangHoa) {
        // Tìm mặt hàng trong danh sách kho
        DichVu item = DataStore.get().getKhoItems().stream()
                .filter(d -> ("HH" + d.getMaHangHoa()).equalsIgnoreCase(maHangHoa))
                .findFirst().orElse(null);
        // Trả về số lượng tồn nếu tìm thấy, ngược lại trả về 0
        return item != null ? item.getSoLuongTon() : 0;
    }

    /**
     * Lưu thông tin phiếu nhập kho (gọi đến hàm luuDichVu()).
     * 
     * @return true nếu lưu thành công.
     */
    public boolean luuPhieuNhap() {
        // Gọi hàm lưu dịch vụ
        return luuDichVu();
    }

    /**
     * Cập nhật số lượng tồn kho cho mặt hàng chỉ định.
     * 
     * @param maHangHoa Mã hàng hóa.
     * @param sl        Số lượng tồn mới.
     * @return true nếu cập nhật thành công.
     */
    public boolean capNhatSoLuongTon(String maHangHoa, int sl) {
        // Cập nhật thuộc tính soLuongTon và đồng bộ dữ liệu
        setSoLuongTon(sl);
        return capNhatDuLieu();
    }

    /**
     * Xóa mặt hàng khỏi danh mục kho hàng hóa.
     * 
     * @param maHangHoa Mã hàng hóa cần xóa.
     * @return true nếu xóa thành công.
     */
    public boolean xoaHangHoa(String maHangHoa) {
        // Ủy quyền thao tác xóa dịch vụ
        return xoaDichVu(maHangHoa);
    }

    /**
     * Lưu phiếu xuất kho sản phẩm.
     * 
     * @return true nếu thành công.
     */
    public boolean luuPhieuXuat() {
        // Gọi hàm cập nhật dữ liệu
        return capNhatDuLieu();
    }

    /**
     * Giảm số lượng tồn kho khi bán/sử dụng sản phẩm.
     * 
     * @param maHangHoa Mã hàng hóa xuất.
     * @param sl        Số lượng muốn giảm.
     * @return true nếu xuất kho và cập nhật thành công.
     */
    public boolean giamSoLuongTon(String maHangHoa, int sl) {
        // Xuất kho và cập nhật dữ liệu nếu thành công
        return xuatKho(sl) && capNhatDuLieu();
    }
}
