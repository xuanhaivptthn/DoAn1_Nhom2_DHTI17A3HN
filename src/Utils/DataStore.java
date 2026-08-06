package Utils;

import Model.BaoTri;
import Model.ChuSan;
import Model.DatLich;
import Model.DichVu;
import Model.HoaDon;
import Model.KhachHang;
import Model.Kho;
import Model.KhuVucSan;
import Model.NhanVien;
import Model.TaiKhoan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lớp Quản lý dữ liệu trung tâm (DataStore) áp dụng thiết kế Singleton pattern.
 * <p>
 * DataStore kết nối với CSDL MySQL thông qua các lớp DAO & DBConnect để tải dữ liệu ban đầu.
 * Nếu không thể kết nối CSDL hoặc khi chế độ demo offline được bật, DataStore sẽ nạp
 * dữ liệu mẫu (mock data) vào bộ nhớ Ram nhằm đảm bảo hệ thống luôn sẵn sàng vận hành.
 * Đồng thời hỗ trợ tra cứu, đồng bộ trạng thái sân bảo trì, lịch đặt sân và tính toán hóa đơn.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public final class DataStore {

    /**
     * Thể hiện duy nhất (Singleton Instance) của lớp DataStore.
     */
    private static DataStore INSTANCE;

    /**
     * Danh sách lưu trữ thông tin các Tài khoản người dùng.
     */
    private final List<TaiKhoan> taiKhoans = new ArrayList<>();

    /**
     * Danh sách lưu trữ thông tin các Chủ sân.
     */
    private final List<ChuSan> chuSans = new ArrayList<>();

    /**
     * Danh sách lưu trữ thông tin các Nhân viên.
     */
    private final List<NhanVien> nhanViens = new ArrayList<>();

    /**
     * Danh sách lưu trữ thông tin các Khu vực sân bóng.
     */
    private final List<KhuVucSan> khuVucs = new ArrayList<>();

    /**
     * Danh sách dịch vụ đi kèm (HLV, trọng tài, giặt sấy,...).
     */
    private final List<DichVu> dichVus = new ArrayList<>();

    /**
     * Danh sách dịch vụ thuộc dạng vật tư kho hàng hóa.
     */
    private final List<DichVu> khoItems = new ArrayList<>();

    /**
     * Danh sách thông tin mặt hàng kho lưu trữ số lượng tồn kho.
     */
    private final List<Kho> khos = new ArrayList<>();

    /**
     * Danh sách tất cả các lượt đặt sân bóng.
     */
    private final List<DatLich> datLichs = new ArrayList<>();

    /**
     * Danh sách tất cả các phiếu bảo trì sân bóng.
     */
    private final List<BaoTri> baoTris = new ArrayList<>();

    /**
     * Danh sách thông tin tất cả khách hàng.
     */
    private final List<KhachHang> khachHangs = new ArrayList<>();

    /**
     * Danh sách tất cả hóa đơn thanh toán đã khởi tạo.
     */
    private final List<HoaDon> hoaDons = new ArrayList<>();

    /**
     * Cờ đánh dấu có sử dụng cơ sở dữ liệu MySQL hay không (Mặc định là {@code true}).
     */
    private static boolean useDatabase = true;

    /**
     * Khởi tạo riêng của DataStore, nạp dữ liệu từ CSDL hoặc nạp dữ liệu mẫu.
     */
    private DataStore() {
        seed();
    }

    /**
     * Lấy thể hiện Singleton duy nhất của lớp DataStore.
     *
     * @return Đối tượng {@link DataStore} duy nhất.
     */
    public static synchronized DataStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DataStore();
        }
        return INSTANCE;
    }

    /**
     * Bật hoặc tắt chế độ sử dụng cơ sở dữ liệu MySQL.
     *
     * @param flag {@code true} để sử dụng MySQL; {@code false} để dùng dữ liệu mẫu offline.
     */
    public static void setUseDatabase(boolean flag) {
        useDatabase = flag;
        if (INSTANCE != null) {
            INSTANCE.reseed();
        }
    }

    /**
     * Kiểm tra xem hệ thống có đang cấu hình sử dụng CSDL MySQL hay không.
     *
     * @return {@code true} nếu đang sử dụng CSDL; {@code false} nếu không.
     */
    public static boolean isUseDatabase() {
        return useDatabase;
    }

    /**
     * Làm sạch dữ liệu hiện tại trong bộ nhớ và thực hiện nạp lại dữ liệu (Reseed).
     */
    public synchronized void reseed() {
        // Xóa sạch bộ nhớ tạm của tất cả danh sách
        taiKhoans.clear();
        chuSans.clear();
        nhanViens.clear();
        khuVucs.clear();
        dichVus.clear();
        khoItems.clear();
        khos.clear();
        datLichs.clear();
        baoTris.clear();
        khachHangs.clear();
        hoaDons.clear();
        // Gọi lại hàm seed để khởi tạo dữ liệu
        seed();
    }

    /**
     * Nạp dữ liệu vào DataStore. Thử kết nối CSDL MySQL trước, nếu gặp lỗi sẽ tự động rơi về dữ liệu mẫu (Fallback).
     */
    private void seed() {
        if (useDatabase) {
            try {
                // Thử nghiệm kết nối CSDL MySQL
                if (!DAO.DBConnect.testConnection()) {
                    throw new java.sql.SQLException("Không thể thiết lập kết nối MySQL.");
                }

                // Nạp bảng TaiKhoan
                List<TaiKhoan> dbTaiKhoans = new DAO.TaiKhoanDAO().getAll();
                if (dbTaiKhoans != null) {
                    taiKhoans.addAll(dbTaiKhoans);
                }

                // Nạp bảng ChuSan
                List<ChuSan> dbChuSans = new DAO.ChuSanDAO().getAll();
                if (dbChuSans != null) {
                    chuSans.addAll(dbChuSans);
                }

                // Nạp bảng NhanVien
                List<NhanVien> dbNhanViens = new DAO.NhanVienDAO().getAll();
                if (dbNhanViens != null) {
                    nhanViens.addAll(dbNhanViens);
                }

                // Nạp bảng KhuVucSan
                List<KhuVucSan> dbKhuVucs = new DAO.KhuVucSanDAO().getAll();
                if (dbKhuVucs != null) {
                    khuVucs.addAll(dbKhuVucs);
                }

                // Nạp bảng DichVu và phân loại mặt hàng Kho
                List<DichVu> dbDichVus = new DAO.DichVuDAO().getAll();
                if (dbDichVus != null) {
                    for (DichVu d : dbDichVus) {
                        if ("Vật tư kho".equalsIgnoreCase(d.getLoaiDichVu()) || d.getSoLuongTon() > 0) {
                            khoItems.add(d);
                            khos.add(new Kho(d.getId(), d.getTenDichVu(), d.getSoLuongTon(), d.getDonGia(), d.getNhaCungCap()));
                        } else {
                            dichVus.add(d);
                        }
                    }
                }

                // Nạp bảng DatLich và điền thông tin tên sân
                List<DatLich> dbDatLichs = new DAO.DatLichDAO().getAll();
                if (dbDatLichs != null) {
                    for (DatLich d : dbDatLichs) {
                        backfillTenSan(d.getMaSan(), d::setTenSan);
                    }
                    datLichs.addAll(dbDatLichs);
                }

                // Nạp bảng BaoTri và điền tên sân tương ứng
                List<BaoTri> dbBaoTris = new DAO.BaoTriDAO().getAll();
                if (dbBaoTris != null) {
                    for (BaoTri b : dbBaoTris) {
                        backfillTenSan(b.getMaSan(), b::setTenSan);
                    }
                    baoTris.addAll(dbBaoTris);
                }

                // Nạp bảng KhachHang
                List<KhachHang> dbKhachHangs = new DAO.KhachHangDAO().getAll();
                if (dbKhachHangs != null) {
                    khachHangs.addAll(dbKhachHangs);
                }

                // Nạp bảng HoaDon
                List<HoaDon> dbHoaDons = new DAO.HoaDonDAO().getAll();
                if (dbHoaDons != null) {
                    hoaDons.addAll(dbHoaDons);
                }

                // Đồng bộ lại trạng thái bảo trì sân bóng và kết thúc quá trình nạp
                syncTrangThaiSanBaoTri();
                return;
            } catch (Exception ex) {
                System.err.println("Thông báo: Lỗi nạp CSDL MySQL (" + ex.getMessage() + "). Sử dụng dữ liệu mẫu DataStore.");
                // Dọn dẹp dữ liệu nạp dở dang để chuyển sang dữ liệu mẫu
                taiKhoans.clear();
                chuSans.clear();
                nhanViens.clear();
                khuVucs.clear();
                dichVus.clear();
                khoItems.clear();
                khos.clear();
                datLichs.clear();
                baoTris.clear();
                khachHangs.clear();
                hoaDons.clear();
            }
        }

        // Nếu không dùng CSDL hoặc kết nối bị lỗi -> nạp dữ liệu mẫu mock
        seedDefaultTaiKhoans();
        seedDefaultChuSans();
        seedDefaultNhanViens();
        seedDefaultKhachHangs();
        seedDefaultDichVus();
        seedDefaultKhuVucs();
        seedDefaultDatLichs();
        seedDefaultBaoTris();

        // Đồng bộ trạng thái sân bảo trì theo dữ liệu mẫu
        syncTrangThaiSanBaoTri();
    }

    /**
     * Tra cứu tenSan từ maSan trong danh sách khuVucs đã nạp để gán lại cho phiếu đặt hoặc bảo trì.
     *
     * @param maSan  Mã sân bóng.
     * @param setter Consumer gán lại tên sân cho đối tượng mô hình.
     */
    private void backfillTenSan(String maSan, java.util.function.Consumer<String> setter) {
        if (maSan == null) return;
        khuVucs.stream()
                .filter(k -> maSan.equals(k.getMaSan()))
                .findFirst()
                .ifPresent(k -> setter.accept(k.getTenSan()));
    }

    /**
     * Nạp các tài khoản mẫu mặc định.
     */
    private void seedDefaultTaiKhoans() {
        taiKhoans.add(new TaiKhoan("TK001", "admin",      "admin123", "ADMIN",    "HOAT_DONG"));
        taiKhoans.add(new TaiKhoan("TK002", "nhanvien01", "nv123456", "NHAN_VIEN", "HOAT_DONG"));
        taiKhoans.add(new TaiKhoan("TK003", "nhanvien02", "nv123456", "NHAN_VIEN", "HOAT_DONG"));
        taiKhoans.add(new TaiKhoan("TK004", "nhanvien03", "nv123456", "NHAN_VIEN", "KHOA"));
    }

    /**
     * Nạp dữ liệu chủ sân mẫu.
     */
    private void seedDefaultChuSans() {
        chuSans.add(new ChuSan("CS001", "TK001", "Chủ Sân Quản Lý", "0988111222"));
    }

    /**
     * Nạp dữ liệu nhân viên mẫu.
     */
    private void seedDefaultNhanViens() {
        nhanViens.add(new NhanVien("NV001", "TK002", "Nguyễn Văn Nam", "0912345678", "Hà Nội"));
        nhanViens.add(new NhanVien("NV002", "TK003", "Trần Thị Hằng", "0987654321", "Hà Nội"));
        nhanViens.add(new NhanVien("NV003", "TK004", "Lê Hoàng Long", "0905123456", "Hà Nội"));
    }

    /**
     * Nạp dữ liệu khách hàng mẫu.
     */
    private void seedDefaultKhachHangs() {
        khachHangs.add(new KhachHang(1, "Anh Đức (FC Anh Em)", "0912345678"));
        khachHangs.add(new KhachHang(2, "Anh Tuấn (FC Thể Công)", "0987654321"));
        khachHangs.add(new KhachHang(3, "Chị Mai (Công ty FPT)", "0905123456"));
    }

    /**
     * Nạp dữ liệu các dịch vụ và mặt hàng kho mẫu.
     */
    private void seedDefaultDichVus() {
        dichVus.add(new DichVu(1, "DV001", "Dịch vụ thuê trọng tài chính", "Nhân sự", 150000, "Trọng tài chuyên nghiệp điều hành 1 trận (90p)"));
        dichVus.add(new DichVu(2, "DV002", "Huấn luyện viên cá nhân 1v1", "HLV cá nhân", 300000, "HLV hướng dẫn kỹ thuật cá nhân theo giờ"));
        dichVus.add(new DichVu(3, "DV003", "Giặt sấy trang phục thi đấu", "Giặt sấy", 30000, "Giặt sấy tiệt trùng bộ quần áo sau trận"));
        dichVus.add(new DichVu(4, "DV004", "Hỗ trợ truyền thông & Quay phim", "Dịch vụ thi đấu", 250000, "Quay video trận đấu & phát lại Highlights"));

        addKhoItem(101, "Nước suối Aquafina 500ml", 120, 10000, "Công ty Nước khoáng Aquafina");
        addKhoItem(102, "Nước điện giải Revive", 85, 15000, "Công ty Pocari Sweat Việt Nam");
        addKhoItem(103, "Áo bít tập luyện (Bộ)", 25, 30000, "Xưởng may Trang phục Thể thao");
        addKhoItem(104, "Bóng thi đấu Động Lực", 15, 50000, "Tập đoàn Thể thao Động Lực");
        addKhoItem(105, "Găng tay thủ môn cao cấp", 8, 40000, "Adidas Việt Nam");
        addKhoItem(106, "Giày đá bóng sân cỏ nhân tạo", 12, 50000, "NCS Sports Việt Nam");
    }

    /**
     * Nạp dữ liệu các sân bóng mẫu.
     */
    private void seedDefaultKhuVucs() {
        khuVucs.add(new KhuVucSan("SAN001", "Sân A1 (Sân 5)", "San5", 250000, "HOAT_DONG"));
        khuVucs.add(new KhuVucSan("SAN002", "Sân A2 (Sân 5)", "San5", 250000, "HOAT_DONG"));
        khuVucs.add(new KhuVucSan("SAN003", "Sân B1 (Sân 7)", "San7", 400000, "HOAT_DONG"));
        khuVucs.add(new KhuVucSan("SAN004", "Sân B2 (Sân 7)", "San7", 400000, "HOAT_DONG"));
        khuVucs.add(new KhuVucSan("SAN005", "Sân C1 (Sân 11)", "San11", 800000, "BAO_TRI"));
    }

    /**
     * Nạp dữ liệu lịch đặt sân mẫu.
     */
    private void seedDefaultDatLichs() {
        String today = java.time.LocalDate.now().toString();
        DatLich dl1 = new DatLich(1, "DL001", 1, "Sân A1 (Sân 5)", "Anh Đức (FC Anh Em)", "0912345678", today, "08:00", "10:00", 425000, "ChoXacNhan", "TK002", "Đặt cọc trước 100k");
        dl1.setMaSan("SAN001");
        dl1.setTienSan(375000);
        dl1.setTienDichVu(50000);
        dl1.setDatCoc(100000);
        dl1.setTrangThaiTT("ThanhToanMotPhan");
        dl1.setDichVuKem("Nước suối Aquafina 500ml (x5): 50,000 VNĐ");
        datLichs.add(dl1);

        DatLich dl2 = new DatLich(2, "DL002", 3, "Sân B1 (Sân 7)", "Anh Tuấn (FC Thể Công)", "0987654321", today, "19:00", "21:00", 680000, "DaXacNhan", "Trần Thị Thu", "Thanh toán cọc qua CK");
        dl2.setMaSan("SAN003");
        dl2.setTienSan(600000);
        dl2.setTienDichVu(80000);
        dl2.setDatCoc(200000);
        dl2.setTrangThaiTT("ThanhToanMotPhan");
        dl2.setDichVuKem("Nước điện giải Revive (x4): 60,000 VNĐ\nÁo bít tập luyện (Bộ) (x1): 20,000 VNĐ");
        datLichs.add(dl2);

        DatLich dl3 = new DatLich(3, "DL003", 2, "Sân A2 (Sân 5)", "Chị Mai (Công ty FPT)", "0905123456", today, "21:00", "23:00", 375000, "HoanThanh", "Chủ Sân Quản Lý", "Đã chuyển khoản đủ 100%");
        dl3.setMaSan("SAN002");
        dl3.setTienSan(375000);
        dl3.setDatCoc(375000);
        dl3.setTrangThaiTT("DaThanhToan");
        datLichs.add(dl3);
    }

    /**
     * Nạp dữ liệu các phiếu bảo trì mẫu.
     */
    private void seedDefaultBaoTris() {
        BaoTri bt1 = new BaoTri(1, "BT001", 5, "Sân C1 (Sân 11)", "Thay lại thảm cỏ nhân tạo vùng cấm địa & kiểm tra hệ thống đèn pha LED", "Lê Minh Tuấn", "2026-07-25", "2026-08-05", 4500000, "DangXuLy");
        bt1.setMaSan("SAN005");
        baoTris.add(bt1);

        BaoTri bt2 = new BaoTri(2, "BT002", 4, "Sân B2 (Sân 7)", "Bảo dưỡng định kỳ lưới chắn bóng xung quanh", "Trần Thị Lan", "2026-07-20", "2026-07-22", 800000, "HoanThanh");
        bt2.setMaSan("SAN004");
        baoTris.add(bt2);
    }

    /**
     * Phương thức bổ trợ thêm một sản phẩm kho vào cả 2 danh sách {@code khoItems} và {@code khos}.
     *
     * @param maHH   Mã hàng hóa.
     * @param tenHH  Tên hàng hóa.
     * @param slTon  Số lượng tồn ban đầu.
     * @param donGia Đơn giá bán lẻ.
     * @param ncc    Nhà cung cấp.
     */
    private void addKhoItem(int maHH, String tenHH, int slTon, double donGia, String ncc) {
        khoItems.add(new DichVu(maHH, tenHH, slTon, donGia, ncc));
        khos.add(new Kho(maHH, tenHH, slTon, donGia, ncc));
    }

    /**
     * Giảm số lượng tồn kho của một mặt hàng dịch vụ trong DataStore.
     *
     * @param dv    Mặt hàng dịch vụ kho {@link DichVu}.
     * @param count Số lượng xuất bán.
     */
    public synchronized void giamKhoStock(DichVu dv, int count) {
        if (dv == null || count <= 0) return;
        // Gọi hàm xuất kho giảm số lượng tồn trên đối tượng DichVu
        dv.xuatKho(count);
        dv.capNhatDuLieu();

        // Cập nhật lại số lượng tồn kho đồng bộ sang đối tượng Kho tương ứng
        for (Kho k : khos) {
            if (dv.getMaDichVu() != null && dv.getMaDichVu().equalsIgnoreCase(k.getMaHangHoa())) {
                k.setSoLuongTon(dv.getSoLuongTon());
                break;
            }
            if (dv.getTenDichVu() != null && dv.getTenDichVu().equalsIgnoreCase(k.getTenHangHoa())) {
                k.setSoLuongTon(dv.getSoLuongTon());
                break;
            }
        }
    }

    /** @return Danh sách tài khoản người dùng hệ thống. */
    public List<TaiKhoan> getTaiKhoans() { return taiKhoans; }

    /** @return Danh sách các chủ sân. */
    public List<ChuSan> getChuSans() { return chuSans; }

    /**
     * Tìm thông tin Chủ sân dựa theo mã tài khoản người dùng.
     *
     * @param maTaiKhoan Mã tài khoản liên kết.
     * @return Đối tượng {@link ChuSan} tương ứng hoặc {@code null}.
     */
    public ChuSan findChuSanByMaTaiKhoan(String maTaiKhoan) {
        if (maTaiKhoan == null) return null;
        return chuSans.stream()
                .filter(cs -> maTaiKhoan.equals(cs.getMaTaiKhoan()))
                .findFirst().orElse(null);
    }

    /**
     * Tạo mới hoặc cập nhật hồ sơ chủ sân gắn với một tài khoản.
     *
     * @param maTaiKhoan         Mã tài khoản liên kết.
     * @param tenChuSan          Họ tên chủ sân.
     * @param soDienThoaiChuSan Số điện thoại chủ sân.
     * @return Đối tượng {@link ChuSan} đã lưu hoặc cập nhật.
     */
    public synchronized ChuSan saveOrUpdateChuSan(String maTaiKhoan, String tenChuSan, String soDienThoaiChuSan) {
        if (maTaiKhoan == null) return null;
        ChuSan existing = findChuSanByMaTaiKhoan(maTaiKhoan);
        if (existing != null) {
            existing.setTenChuSan(tenChuSan);
            existing.setSoDienThoaiChuSan(soDienThoaiChuSan);
            if (isUseDatabase()) {
                new DAO.ChuSanDAO().update(existing);
            }
            return existing;
        } else {
            String ma = CodeGen.next("CS", chuSans.stream().map(ChuSan::getMaChuSan).toList(), 3);
            ChuSan cs = new ChuSan(ma, maTaiKhoan, tenChuSan, soDienThoaiChuSan);
            chuSans.add(cs);
            if (isUseDatabase()) {
                new DAO.ChuSanDAO().insert(cs);
            }
            return cs;
        }
    }

    /** @return Danh sách nhân viên trong hệ thống. */
    public List<NhanVien> getNhanViens() { return nhanViens; }

    /**
     * Tìm nhân viên theo mã tài khoản người dùng.
     *
     * @param maTaiKhoan Mã tài khoản liên kết.
     * @return Đối tượng {@link NhanVien} hoặc {@code null}.
     */
    public NhanVien findNhanVienByMaTaiKhoan(String maTaiKhoan) {
        if (maTaiKhoan == null) return null;
        return nhanViens.stream()
                .filter(nv -> maTaiKhoan.equals(nv.getMaTaiKhoan()))
                .findFirst().orElse(null);
    }

    /**
     * Tạo mới hoặc cập nhật thông tin nhân viên theo mã tài khoản.
     *
     * @param maTaiKhoan Mã tài khoản liên kết.
     * @param hoTen      Họ tên nhân viên.
     * @param sdt        Số điện thoại.
     * @param diaChi     Địa chỉ liên hệ.
     * @return Đối tượng {@link NhanVien} sau khi đã lưu.
     */
    public synchronized NhanVien saveOrUpdateNhanVien(String maTaiKhoan, String hoTen, String sdt, String diaChi) {
        if (maTaiKhoan == null) return null;
        NhanVien existing = findNhanVienByMaTaiKhoan(maTaiKhoan);
        if (existing != null) {
            existing.setHoTenNhanVien(hoTen);
            existing.setSoDienThoaiNhanVien(sdt);
            existing.setDiaChi(diaChi);
            if (isUseDatabase()) {
                new DAO.NhanVienDAO().update(existing);
            }
            return existing;
        } else {
            String ma = CodeGen.next("NV", nhanViens.stream().map(NhanVien::getMaNhanVien).toList(), 3);
            NhanVien nv = new NhanVien(ma, maTaiKhoan, hoTen, sdt, diaChi);
            nhanViens.add(nv);
            if (isUseDatabase()) {
                new DAO.NhanVienDAO().insert(nv);
            }
            return nv;
        }
    }

    /** @return Danh sách các khu vực sân bóng. */
    public List<KhuVucSan> getKhuVucs() { return khuVucs; }

    /**
     * Đồng bộ hóa trạng thái sân bóng dựa trên danh sách các phiếu bảo trì đang hoạt động.
     */
    public synchronized void syncTrangThaiSanBaoTri() {
        for (KhuVucSan san : khuVucs) {
            // Không tự động đổi trạng thái sân nếu sân đã bị ngưng hoạt động thủ công
            if ("NGUNG_HOAT_DONG".equalsIgnoreCase(san.getTrangThai())) {
                continue;
            }
            // Kiểm tra xem sân có phiếu bảo trì đang chạy không
            boolean hasActiveMaint = baoTris.stream().anyMatch(b -> {
                boolean matchCourt = (san.getMaSan() != null && san.getMaSan().equalsIgnoreCase(b.getMaSan()))
                        || (b.getTenSan() != null && b.getTenSan().equalsIgnoreCase(san.getTenSan()));
                if (!matchCourt) return false;
                String tt = b.getTrangThaiPhieu() != null ? b.getTrangThaiPhieu().trim().toUpperCase() : "";
                return "DANG_BAO_TRI".equals(tt) || "DANGXULY".equals(tt);
            });

            // Gán lại trạng thái HOAT_DONG hoặc BAO_TRI tùy thuộc phiếu bảo trì
            if (!hasActiveMaint) {
                san.setTrangThai("HOAT_DONG");
            } else {
                san.setTrangThai("BAO_TRI");
            }
        }
    }

    /**
     * Kiểm tra một sân bóng có đang bị bảo trì hiện tại hay không.
     *
     * @param k Đối tượng sân bóng {@link KhuVucSan}.
     * @return {@code true} nếu đang bảo trì; {@code false} nếu ngược lại.
     */
    public boolean isSanBaoTri(KhuVucSan k) {
        return isSanBaoTriVoiNgay(k, null);
    }

    /**
     * Kiểm tra một sân bóng có thuộc lịch bảo trì vào một ngày cụ thể hay không.
     *
     * @param k          Đối tượng sân bóng {@link KhuVucSan}.
     * @param ngayDatStr Chuỗi ngày kiểm tra (YYYY-MM-DD).
     * @return {@code true} nếu sân bị bảo trì vào ngày đó; {@code false} nếu rảnh.
     */
    public boolean isSanBaoTriVoiNgay(KhuVucSan k, String ngayDatStr) {
        if (k == null) return false;

        // 1. Sân bị ngưng hoạt động hoặc ghi nhận bảo trì thủ công -> chặn đặt
        if ("NGUNG_HOAT_DONG".equalsIgnoreCase(k.getTrangThai()) || "BAO_TRI".equalsIgnoreCase(k.getTrangThai()) || "BaoTri".equalsIgnoreCase(k.getTrangThai())) {
            return true;
        }

        // 2. Lọc các phiếu bảo trì đang hoạt động (chưa hoàn thành & chưa hủy)
        List<BaoTri> activeMaints = baoTris.stream().filter(b -> {
            boolean matchCourt = (k.getMaSan() != null && k.getMaSan().equalsIgnoreCase(b.getMaSan()))
                    || (b.getTenSan() != null && b.getTenSan().equalsIgnoreCase(k.getTenSan()));
            if (!matchCourt) return false;

            String tt = b.getTrangThaiPhieu() != null ? b.getTrangThaiPhieu().trim().toUpperCase() : "";
            return !"HOAN_THANH".equals(tt) && !"HOANTHANH".equals(tt) && !"HUY".equals(tt) && !"DAHUY".equals(tt);
        }).toList();

        if (activeMaints.isEmpty()) {
            return false;
        }

        // 3. Nếu không truyền ngayDatStr -> coi như đang bị bảo trì
        if (ngayDatStr == null || ngayDatStr.isBlank()) {
            return true;
        }

        // 4. Nếu có ngayDatStr -> đối soát ngày nằm trong khoảng [ngayBatDau, ngayKetThuc]
        return activeMaints.stream().anyMatch(b -> isDateInMaintenanceRange(ngayDatStr, b.getNgayBatDau(), b.getNgayKetThuc()));
    }

    /**
     * Kiểm tra xem ngày chỉ định có nằm trong khoảng thời gian bảo trì hay không.
     *
     * @param targetDateStr Ngày kiểm tra.
     * @param startDateStr  Ngày bắt đầu bảo trì.
     * @param endDateStr    Ngày kết thúc bảo trì.
     * @return {@code true} nếu thuộc khoảng bảo trì; {@code false} nếu không.
     */
    private boolean isDateInMaintenanceRange(String targetDateStr, String startDateStr, String endDateStr) {
        if (targetDateStr == null || targetDateStr.isBlank()) return false;
        if (startDateStr == null || startDateStr.isBlank()) return false;
        try {
            java.time.LocalDate targetDate = java.time.LocalDate.parse(targetDateStr.trim());
            java.time.LocalDate startDate = java.time.LocalDate.parse(startDateStr.trim());
            java.time.LocalDate endDate = (endDateStr != null && !endDateStr.isBlank())
                    ? java.time.LocalDate.parse(endDateStr.trim())
                    : startDate;
            return (!targetDate.isBefore(startDate)) && (!targetDate.isAfter(endDate));
        } catch (Exception e) {
            if (targetDateStr.trim().equalsIgnoreCase(startDateStr.trim())) return true;
            return endDateStr != null && targetDateStr.trim().equalsIgnoreCase(endDateStr.trim());
        }
    }

    /**
     * Kiểm tra sân bóng có đang trong thời gian được thuê đá bóng ở thời điểm hiện tại hay không.
     *
     * @param k Đối tượng sân bóng {@link KhuVucSan}.
     * @return {@code true} nếu sân đang có khách thuê đá; {@code false} nếu rảnh.
     */
    public boolean isSanDangThue(KhuVucSan k) {
        if (k == null) return false;
        if (isSanBaoTri(k)) return false;

        String today = java.time.LocalDate.now().toString();
        java.time.LocalTime now = java.time.LocalTime.now();

        return datLichs.stream().anyMatch(d -> {
            if ("DaHuy".equalsIgnoreCase(d.getTrangThai())) return false;
            if (!today.equals(d.getNgayDat())) return false;

            boolean matchCourt = (k.getMaSan() != null && k.getMaSan().equalsIgnoreCase(d.getMaSan()))
                    || (d.getTenSan() != null && d.getTenSan().toLowerCase().contains(k.getMaSan().toLowerCase()));
            if (!matchCourt) return false;

            try {
                String sStart = d.getGioBatDau().trim();
                if (sStart.length() > 5 && sStart.contains(" ")) sStart = sStart.substring(sStart.indexOf(" ") + 1).trim();
                if (sStart.length() > 5) sStart = sStart.substring(0, 5);

                String sEnd = d.getGioKetThuc().trim();
                if (sEnd.length() > 5 && sEnd.contains(" ")) sEnd = sEnd.substring(sEnd.indexOf(" ") + 1).trim();
                if (sEnd.length() > 5) sEnd = sEnd.substring(0, 5);

                java.time.LocalTime start = java.time.LocalTime.parse(sStart);
                java.time.LocalTime end = java.time.LocalTime.parse(sEnd);

                return !now.isBefore(start) && !now.isAfter(end);
            } catch (Exception ex) {
                return false;
            }
        });
    }

    /**
     * Lấy chuỗi mô tả trạng thái hiện tại của sân bóng ("Đang Bảo trì", "Đang thuê", "Sẵn sàng").
     *
     * @param k Đối tượng sân bóng {@link KhuVucSan}.
     * @return Chuỗi mô tả trạng thái.
     */
    public String getTrangThaiSanHienTai(KhuVucSan k) {
        if (k == null) return "";
        if (isSanBaoTri(k)) return "Đang Bảo trì";
        if (isSanDangThue(k)) return "Đang thuê";
        return "Sẵn sàng";
    }

    /**
     * Lấy danh sách tất cả các sân bóng hiện không nằm trong tình trạng bảo trì.
     *
     * @return Danh sách các sân bóng hoạt động bình thường.
     */
    public List<KhuVucSan> getKhuVucsKhongBaoTri() {
        return khuVucs.stream()
                .filter(k -> !isSanBaoTri(k))
                .collect(Collectors.toList());
    }

    /** @return Danh sách dịch vụ đi kèm. */
    public List<DichVu> getDichVus() { return dichVus; }

    /** @return Danh sách mặt hàng kho. */
    public List<DichVu> getKhoItems() { return khoItems; }

    /** @return Danh sách đối tượng Kho. */
    public List<Kho> getKhos() { return khos; }

    /** @return Danh sách tất cả lượt đặt sân. */
    public List<DatLich> getDatLichs() { return datLichs; }

    /** @return Danh sách tất cả phiếu bảo trì. */
    public List<BaoTri> getBaoTris() { return baoTris; }

    /** @return Danh sách tất cả khách hàng. */
    public List<KhachHang> getKhachHangs() { return khachHangs; }

    /** @return Danh sách tất cả hóa đơn. */
    public List<HoaDon> getHoaDons() { return hoaDons; }

    /**
     * Tìm dịch vụ theo ID số nguyên.
     *
     * @param id Mã ID dịch vụ.
     * @return Đối tượng {@link DichVu} hoặc {@code null}.
     */
    public DichVu findDichVuById(int id) {
        for (DichVu d : dichVus) {
            if (d.getId() == id) return d;
        }
        for (DichVu d : khoItems) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    /**
     * Tìm dịch vụ theo mã chuỗi (ví dụ: "DV001").
     *
     * @param ma Mã dịch vụ.
     * @return Đối tượng {@link DichVu} hoặc {@code null}.
     */
    public DichVu findDichVuByMa(String ma) {
        if (ma == null || ma.isBlank()) return null;
        String cleanMa = ma.trim();
        for (DichVu d : dichVus) {
            if (cleanMa.equalsIgnoreCase(d.getMaDichVu())) return d;
        }
        for (DichVu d : khoItems) {
            if (cleanMa.equalsIgnoreCase(d.getMaDichVu())) return d;
        }
        return null;
    }

    /**
     * Lưu mới hoặc cập nhật thông tin khách hàng dựa vào số điện thoại.
     *
     * @param tenKhachHang Họ tên khách hàng.
     * @param sdt          Số điện thoại liên lạc.
     * @return Đối tượng {@link KhachHang} sau khi lưu.
     */
    public synchronized KhachHang saveOrUpdateKhachHang(String tenKhachHang, String sdt) {
        if (sdt == null || sdt.isBlank()) return null;
        String cleanSdt = sdt.trim();
        KhachHang existing = khachHangs.stream()
                .filter(k -> cleanSdt.equalsIgnoreCase(k.getSoDienThoai().trim()))
                .findFirst().orElse(null);

        if (existing != null) {
            if (tenKhachHang != null && !tenKhachHang.isBlank())
                existing.setTenKhachHang(tenKhachHang.trim());
            if (isUseDatabase()) {
                new DAO.KhachHangDAO().update(existing);
            }
            return existing;
        } else {
            int nextId = khachHangs.size() + 1;
            KhachHang newKh = new KhachHang(nextId,
                    tenKhachHang != null ? tenKhachHang.trim() : "", cleanSdt);
            khachHangs.add(newKh);
            if (isUseDatabase()) {
                new DAO.KhachHangDAO().insert(newKh);
            }
            return newKh;
        }
    }

    /**
     * Tìm khách hàng theo số điện thoại.
     *
     * @param sdt Số điện thoại.
     * @return Đối tượng {@link KhachHang} hoặc {@code null}.
     */
    public KhachHang findKhachHangBySoDienThoai(String sdt) {
        if (sdt == null || sdt.isBlank()) return null;
        String cleanSdt = sdt.trim();
        return khachHangs.stream()
                .filter(k -> cleanSdt.equalsIgnoreCase(k.getSoDienThoai().trim()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm khách hàng theo mã khách hàng.
     *
     * @param maKhachHang Mã khách hàng.
     * @return Đối tượng {@link KhachHang} hoặc {@code null}.
     */
    public KhachHang findKhachHangById(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.isBlank()) return null;
        String cleanMa = maKhachHang.trim();
        return khachHangs.stream()
                .filter(k -> cleanMa.equalsIgnoreCase(k.getMaKhachHang()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm sân bóng theo mã sân.
     *
     * @param maSan Mã sân bóng.
     * @return Đối tượng {@link KhuVucSan} hoặc {@code null}.
     */
    public KhuVucSan findKhuVucSanById(String maSan) {
        if (maSan == null || maSan.isBlank()) return null;
        String cleanMa = maSan.trim();
        return khuVucs.stream()
                .filter(k -> cleanMa.equalsIgnoreCase(k.getMaSan()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm chủ sân theo mã chủ sân.
     *
     * @param maChuSan Mã chủ sân.
     * @return Đối tượng {@link ChuSan} hoặc {@code null}.
     */
    public ChuSan findChuSanById(String maChuSan) {
        if (maChuSan == null || maChuSan.isBlank()) return null;
        String cleanMa = maChuSan.trim();
        return chuSans.stream()
                .filter(c -> cleanMa.equalsIgnoreCase(c.getMaChuSan()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm nhân viên theo mã nhân viên.
     *
     * @param maNhanVien Mã nhân viên.
     * @return Đối tượng {@link NhanVien} hoặc {@code null}.
     */
    public NhanVien findNhanVienById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) return null;
        String cleanMa = maNhanVien.trim();
        return nhanViens.stream()
                .filter(n -> cleanMa.equalsIgnoreCase(n.getMaNhanVien()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm tài khoản theo mã tài khoản.
     *
     * @param maTaiKhoan Mã tài khoản.
     * @return Đối tượng {@link TaiKhoan} hoặc {@code null}.
     */
    public TaiKhoan findTaiKhoanByMa(String maTaiKhoan) {
        if (maTaiKhoan == null || maTaiKhoan.isBlank()) return null;
        String cleanMa = maTaiKhoan.trim();
        return taiKhoans.stream()
                .filter(t -> cleanMa.equalsIgnoreCase(t.getMaTaiKhoan()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm phiếu đặt lịch theo mã lịch đặt.
     *
     * @param maLichDat Mã lịch đặt.
     * @return Đối tượng {@link DatLich} hoặc {@code null}.
     */
    public DatLich findDatLichById(String maLichDat) {
        if (maLichDat == null || maLichDat.isBlank()) return null;
        String cleanMa = maLichDat.trim();
        return datLichs.stream()
                .filter(d -> cleanMa.equalsIgnoreCase(d.getMaLichDat()))
                .findFirst().orElse(null);
    }

    /**
     * Tìm hóa đơn theo mã hóa đơn.
     *
     * @param maHoaDon Mã hóa đơn.
     * @return Đối tượng {@link HoaDon} hoặc {@code null}.
     */
    public HoaDon findHoaDonById(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.isBlank()) return null;
        String cleanMa = maHoaDon.trim();
        return hoaDons.stream()
                .filter(h -> cleanMa.equalsIgnoreCase(h.getMaHoaDon()))
                .findFirst().orElse(null);
    }

    /**
     * Tự động sinh hoặc cập nhật hóa đơn thanh toán cho một lượt đặt sân.
     *
     * @param d           Đối tượng {@link DatLich} đặt sân.
     * @param phuongThucTT Phương thức thanh toán áp dụng.
     * @return Đối tượng {@link HoaDon} đã hoàn tất tính toán và lưu trữ.
     */
    public synchronized HoaDon saveOrUpdateHoaDonForBooking(DatLich d, String phuongThucTT) {
        if (d == null || d.getMaLichDat() == null) return null;

        // Tính các chi tiết tiền sân, dịch vụ, kho và tiền giảm giá/đặt cọc
        double tienSvcOnly = d.getTongTienDichVuOnly();
        double tienKhoOnly = d.getTongTienKhoOnly();
        double tienSan = d.getTienSan();
        double giamGia = d.getDatCoc();

        // Tìm xem hóa đơn đã từng được khởi tạo cho mã lịch đặt này chưa
        HoaDon hd = hoaDons.stream()
                .filter(h -> d.getMaLichDat().equalsIgnoreCase(h.getMaLichDat()))
                .findFirst().orElse(null);

        String nowStr = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String maNhanVien = d.getMaTaiKhoan() != null ? d.getMaTaiKhoan() : "Admin";

        // Tổng hợp chi tiết thông tin các dịch vụ đi kèm và vật tư kho thành chuỗi mô tả
        StringBuilder dvStr = new StringBuilder();
        if (d.getSelectedDvMap() != null && !d.getSelectedDvMap().isEmpty()) {
            for (java.util.Map.Entry<Integer, Integer> entry : d.getSelectedDvMap().entrySet()) {
                DichVu dv = findDichVuById(entry.getKey());
                if (dv != null && entry.getValue() > 0) {
                    if (dvStr.length() > 0) dvStr.append("\n");
                    dvStr.append("[Dịch vụ thuê] ").append(dv.getTenDichVu()).append(" (x").append(entry.getValue()).append("): ")
                         .append(String.format("%,.0f VNĐ", dv.getDonGia() * entry.getValue()));
                }
            }
        }
        if (d.getSelectedDoAnMap() != null && !d.getSelectedDoAnMap().isEmpty()) {
            for (java.util.Map.Entry<Integer, Integer> entry : d.getSelectedDoAnMap().entrySet()) {
                DichVu dv = findDichVuById(entry.getKey());
                if (dv != null && entry.getValue() > 0) {
                    if (dvStr.length() > 0) dvStr.append("\n");
                    dvStr.append("[Vật phẩm kho] ").append(dv.getTenDichVu()).append(" (x").append(entry.getValue()).append("): ")
                         .append(String.format("%,.0f VNĐ", dv.getDonGia() * entry.getValue()));
                }
            }
        }
        if (d.getDichVuKem() != null && !d.getDichVuKem().isBlank()) {
            String[] lines = d.getDichVuKem().split("\n");
            for (String line : lines) {
                if (!line.isBlank() && !dvStr.toString().contains(line.trim())) {
                    if (dvStr.length() > 0) dvStr.append("\n");
                    dvStr.append(line.trim());
                }
            }
        }
        String dichVuDetails = dvStr.toString();

        if (hd == null) {
            // Trường hợp hóa đơn chưa tồn tại: Sinh mã hóa đơn mới (VD: "HD001") và khởi tạo
            String maHd = "HD" + d.getMaLichDat().replaceAll("\\D", "");
            if (maHd.equals("HD")) maHd = "HD" + (hoaDons.size() + 1);
            hd = new HoaDon(maHd, d.getMaLichDat(), maNhanVien, nowStr, tienSan, tienSvcOnly, tienKhoOnly, giamGia, 0, phuongThucTT != null ? phuongThucTT : "Tiền mặt");
            hd.setDichVuKem(dichVuDetails);
            hd.tinhTien(); // Tính toán lại tổng tiền cuối cùng
            hoaDons.add(hd);
            if (isUseDatabase()) {
                try { new DAO.HoaDonDAO().insert(hd); } catch (Exception ignored) {}
            }
        } else {
            // Trường hợp hóa đơn đã có: Cập nhật lại các khoản tiền, phương thức thanh toán và thời gian
            hd.setChiPhiSan(tienSan);
            hd.setTongTienDichVu(tienSvcOnly);
            hd.setTongTienKho(tienKhoOnly);
            hd.setGiamGia(giamGia);
            hd.setDichVuKem(dichVuDetails);
            hd.setNgayThanhToan(nowStr);
            if (phuongThucTT != null) hd.setPhuongThucThanhToan(phuongThucTT);
            hd.tinhTien(); // Cập nhật lại tổng tiền
            if (isUseDatabase()) {
                try { new DAO.HoaDonDAO().update(hd); } catch (Exception ignored) {}
            }
        }
        return hd;
    }
}
