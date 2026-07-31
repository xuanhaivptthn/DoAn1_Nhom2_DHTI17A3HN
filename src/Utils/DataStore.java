package Utils;

import Model.BaoTri;
import Model.ChuSan;
import Model.DatLich;
import Model.DichVu;
import Model.KhachHang;
import Model.Kho;
import Model.KhuVucSan;
import Model.PhienLamViec;
import Model.TaiKhoan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DataStore kết nối với CSDL MySQL thông qua lớp DAO & DBConnect.
 */
public final class DataStore {

    private static DataStore INSTANCE;

    private final List<TaiKhoan> taiKhoans = new ArrayList<>();
    private final List<ChuSan> chuSans = new ArrayList<>();
    private final List<KhuVucSan> khuVucs = new ArrayList<>();
    private final List<DichVu> dichVus = new ArrayList<>();
    private final List<DichVu> khoItems = new ArrayList<>();
    private final List<Kho> khos = new ArrayList<>();
    private final List<DatLich> datLichs = new ArrayList<>();
    private final List<BaoTri> baoTris = new ArrayList<>();
    private final List<PhienLamViec> phienHistory = new ArrayList<>();
    private final List<KhachHang> khachHangs = new ArrayList<>();

    private static boolean useDatabase = true;

    private DataStore() {
        seed();
    }

    public static synchronized DataStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DataStore();
        }
        return INSTANCE;
    }

    public static void setUseDatabase(boolean flag) {
        useDatabase = flag;
        if (INSTANCE != null) {
            INSTANCE.reseed();
        }
    }

    public static boolean isUseDatabase() {
        return useDatabase;
    }

    public synchronized void reseed() {
        taiKhoans.clear();
        chuSans.clear();
        khuVucs.clear();
        dichVus.clear();
        khoItems.clear();
        khos.clear();
        datLichs.clear();
        baoTris.clear();
        phienHistory.clear();
        khachHangs.clear();
        seed();
    }

    private void seed() {
        if (useDatabase) {
            try {
                List<TaiKhoan> dbTaiKhoans = new DAO.TaiKhoanDAO().getAll();
                if (dbTaiKhoans != null && !dbTaiKhoans.isEmpty()) {
                    taiKhoans.addAll(dbTaiKhoans);
                }

                List<ChuSan> dbChuSans = new DAO.ChuSanDAO().getAll();
                if (dbChuSans != null && !dbChuSans.isEmpty()) {
                    chuSans.addAll(dbChuSans);
                }

                List<KhuVucSan> dbKhuVucs = new DAO.KhuVucSanDAO().getAll();
                if (dbKhuVucs != null && !dbKhuVucs.isEmpty()) {
                    khuVucs.addAll(dbKhuVucs);
                }

                List<DichVu> dbDichVus = new DAO.DichVuDAO().getAll();
                if (dbDichVus != null && !dbDichVus.isEmpty()) {
                    for (DichVu d : dbDichVus) {
                        if ("Vật tư kho".equalsIgnoreCase(d.getLoaiDichVu()) || d.getSoLuongTon() > 0) {
                            khoItems.add(d);
                            khos.add(new Kho(d.getId(), d.getTenDichVu(), d.getSoLuongTon(), d.getDonGia(), d.getNhaCungCap()));
                        } else {
                            dichVus.add(d);
                        }
                    }
                }

                List<DatLich> dbDatLichs = new DAO.DatLichDAO().getAll();
                if (dbDatLichs != null && !dbDatLichs.isEmpty()) {
                    for (DatLich d : dbDatLichs) {
                        backfillTenSan(d.getMaSan(), d::setTenSan);
                    }
                    datLichs.addAll(dbDatLichs);
                }

                List<BaoTri> dbBaoTris = new DAO.BaoTriDAO().getAll();
                if (dbBaoTris != null && !dbBaoTris.isEmpty()) {
                    for (BaoTri b : dbBaoTris) {
                        backfillTenSan(b.getMaSan(), b::setTenSan);
                    }
                    baoTris.addAll(dbBaoTris);
                }

                List<KhachHang> dbKhachHangs = new DAO.KhachHangDAO().getAll();
                if (dbKhachHangs != null && !dbKhachHangs.isEmpty()) {
                    khachHangs.addAll(dbKhachHangs);
                }

                List<PhienLamViec> dbPhien = new DAO.PhienLamViecDAO().getAll();
                if (dbPhien != null && !dbPhien.isEmpty()) {
                    phienHistory.addAll(dbPhien);
                }
            } catch (Exception ex) {
                System.err.println("Thông báo: Không thể kết nối CSDL MySQL qua DBConnect. Sử dụng dữ liệu mẫu DataStore.");
            }
        }

        if (taiKhoans.isEmpty()) seedDefaultTaiKhoans();
        if (chuSans.isEmpty()) seedDefaultChuSans();
        if (khachHangs.isEmpty()) seedDefaultKhachHangs();
        if (dichVus.isEmpty() && khoItems.isEmpty()) seedDefaultDichVus();
        if (khuVucs.isEmpty()) seedDefaultKhuVucs();
        if (datLichs.isEmpty()) seedDefaultDatLichs();
        if (baoTris.isEmpty()) seedDefaultBaoTris();
    }

    /** Tra cứu tenSan (denormalized) từ maSan trong danh sách khuVucs đã nạp, dùng khi backfill dữ liệu đọc từ CSDL. */
    private void backfillTenSan(String maSan, java.util.function.Consumer<String> setter) {
        if (maSan == null) return;
        khuVucs.stream()
                .filter(k -> maSan.equals(k.getMaSan()))
                .findFirst()
                .ifPresent(k -> setter.accept(k.getTenSan()));
    }

    private void seedDefaultTaiKhoans() {
        taiKhoans.add(new TaiKhoan("TK001", "admin",      "admin123", "ADMIN",    "HOAT_DONG"));
        taiKhoans.add(new TaiKhoan("TK002", "nhanvien01", "nv123456", "NHAN_VIEN", "HOAT_DONG"));
        taiKhoans.add(new TaiKhoan("TK003", "nhanvien02", "nv123456", "NHAN_VIEN", "HOAT_DONG"));
        taiKhoans.add(new TaiKhoan("TK004", "nhanvien03", "nv123456", "NHAN_VIEN", "KHOA"));
    }

    private void seedDefaultChuSans() {
        chuSans.add(new ChuSan("CS001", "TK001", "Chủ Sân Quản Lý", "0988111222"));
    }

    private void seedDefaultKhachHangs() {
        khachHangs.add(new KhachHang(1, "Anh Đức (FC Anh Em)", "0912345678"));
        khachHangs.add(new KhachHang(2, "Anh Tuấn (FC Thể Công)", "0987654321"));
        khachHangs.add(new KhachHang(3, "Chị Mai (Công ty FPT)", "0905123456"));
    }

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

    private void seedDefaultKhuVucs() {
        khuVucs.add(new KhuVucSan("A1", "Sân A1 (Sân 5)", "San5", 250000, "SanSang"));
        khuVucs.add(new KhuVucSan("A2", "Sân A2 (Sân 5)", "San5", 250000, "SanSang"));
        khuVucs.add(new KhuVucSan("B1", "Sân B1 (Sân 7)", "San7", 400000, "SanSang"));
        khuVucs.add(new KhuVucSan("B2", "Sân B2 (Sân 7)", "San7", 400000, "SanSang"));
        khuVucs.add(new KhuVucSan("C1", "Sân C1 (Sân 11)", "San11", 800000, "BaoTri"));
    }

    private void seedDefaultDatLichs() {
        String today = java.time.LocalDate.now().toString();
        DatLich dl1 = new DatLich(1, "DL001", 1, "Sân A1 (Sân 5)", "Anh Đức (FC Anh Em)", "0912345678", today, "17:30", "19:00", 425000, "DaXacNhan", "Nguyễn Văn Nhân", "Đặt cọc trước 100k");
        dl1.setMaSan("A1");
        dl1.setTienSan(375000);
        dl1.setTienDichVu(50000);
        dl1.setDatCoc(100000);
        dl1.setTrangThaiTT("ThanhToanMotPhan");
        dl1.setDichVuKem("Nước suối Aquafina 500ml (x5): 50,000 VNĐ");
        datLichs.add(dl1);

        DatLich dl2 = new DatLich(2, "DL002", 3, "Sân B1 (Sân 7)", "Anh Tuấn (FC Thể Công)", "0987654321", today, "19:00", "20:30", 680000, "DaXacNhan", "Trần Thị Thu", "Thanh toán cọc qua CK");
        dl2.setMaSan("B1");
        dl2.setTienSan(600000);
        dl2.setTienDichVu(80000);
        dl2.setDatCoc(200000);
        dl2.setTrangThaiTT("ThanhToanMotPhan");
        dl2.setDichVuKem("Nước điện giải Revive (x4): 60,000 VNĐ\nÁo bít tập luyện (Bộ) (x1): 20,000 VNĐ");
        datLichs.add(dl2);

        DatLich dl3 = new DatLich(3, "DL003", 2, "Sân A2 (Sân 5)", "Chị Mai (Công ty FPT)", "0905123456", today, "20:30", "22:00", 375000, "HoanThanh", "Chủ Sân Quản Lý", "Đã chuyển khoản đủ 100%");
        dl3.setMaSan("A2");
        dl3.setTienSan(375000);
        dl3.setDatCoc(375000);
        dl3.setTrangThaiTT("DaThanhToan");
        datLichs.add(dl3);
    }

    private void seedDefaultBaoTris() {
        BaoTri bt1 = new BaoTri(1, "BT001", 5, "Sân C1 (Sân 11)", "Thay lại thảm cỏ nhân tạo vùng cấm địa & kiểm tra hệ thống đèn pha LED", "Lê Minh Tuấn", "2026-07-25", "2026-08-05", 4500000, "DangXuLy");
        bt1.setMaSan("C1");
        baoTris.add(bt1);

        BaoTri bt2 = new BaoTri(2, "BT002", 4, "Sân B2 (Sân 7)", "Bảo dưỡng định kỳ lưới chắn bóng xung quanh", "Trần Thị Lan", "2026-07-20", "2026-07-22", 800000, "HoanThanh");
        bt2.setMaSan("B2");
        baoTris.add(bt2);
    }

    private void addKhoItem(int maHH, String tenHH, int slTon, double donGia, String ncc) {
        khoItems.add(new DichVu(maHH, tenHH, slTon, donGia, ncc));
        khos.add(new Kho(maHH, tenHH, slTon, donGia, ncc));
    }

    public List<TaiKhoan> getTaiKhoans() { return taiKhoans; }
    public List<ChuSan> getChuSans() { return chuSans; }

    public ChuSan findChuSanByMaTaiKhoan(String maTaiKhoan) {
        if (maTaiKhoan == null) return null;
        return chuSans.stream()
                .filter(cs -> maTaiKhoan.equals(cs.getMaTaiKhoan()))
                .findFirst().orElse(null);
    }

    /** Tạo mới hoặc cập nhật hồ sơ chủ sân gắn với một tài khoản (dùng trong màn hình Quản lý tài khoản). */
    public synchronized ChuSan saveOrUpdateChuSan(String maTaiKhoan, String tenChuSan, String soDienThoaiChuSan) {
        if (maTaiKhoan == null) return null;
        ChuSan existing = findChuSanByMaTaiKhoan(maTaiKhoan);
        if (existing != null) {
            existing.setTenChuSan(tenChuSan);
            existing.setSoDienThoaiChuSan(soDienThoaiChuSan);
            new DAO.ChuSanDAO().update(existing);
            return existing;
        } else {
            String ma = CodeGen.next("CS", chuSans.stream().map(ChuSan::getMaChuSan).toList(), 3);
            ChuSan cs = new ChuSan(ma, maTaiKhoan, tenChuSan, soDienThoaiChuSan);
            chuSans.add(cs);
            new DAO.ChuSanDAO().insert(cs);
            return cs;
        }
    }

    public List<KhuVucSan> getKhuVucs() { return khuVucs; }
    public boolean isSanBaoTri(KhuVucSan k) {
        if (k == null) return false;
        if ("BaoTri".equalsIgnoreCase(k.getTrangThai()) || "Bảo trì".equalsIgnoreCase(k.getTrangThai())) {
            return true;
        }
        return baoTris.stream().anyMatch(b ->
            !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu()) &&
            !"HUY".equalsIgnoreCase(b.getTrangThaiPhieu()) &&
            !"HoanThanh".equalsIgnoreCase(b.getTrangThaiPhieu()) &&
            !"HOAN_THANH".equalsIgnoreCase(b.getTrangThaiPhieu()) &&
            ((k.getMaSan() != null && k.getMaSan().equals(b.getMaSan())) || (b.getTenSan() != null && b.getTenSan().equalsIgnoreCase(k.getTenSan())))
        );
    }

    public List<KhuVucSan> getKhuVucsKhongBaoTri() {
        return khuVucs.stream()
                .filter(k -> !isSanBaoTri(k))
                .collect(Collectors.toList());
    }

    public List<DichVu> getDichVus() { return dichVus; }
    public List<DichVu> getKhoItems() { return khoItems; }
    public List<Kho> getKhos() { return khos; }
    public List<DatLich> getDatLichs() { return datLichs; }
    public List<BaoTri> getBaoTris() { return baoTris; }
    public List<PhienLamViec> getPhienHistory() { return phienHistory; }
    public List<KhachHang> getKhachHangs() { return khachHangs; }

    public synchronized KhachHang saveOrUpdateKhachHang(String tenKhachHang, String sdt) {
        if (sdt == null || sdt.isBlank()) return null;
        String cleanSdt = sdt.trim();
        KhachHang existing = khachHangs.stream()
                .filter(k -> cleanSdt.equalsIgnoreCase(k.getSoDienThoai().trim()))
                .findFirst().orElse(null);

        if (existing != null) {
            if (tenKhachHang != null && !tenKhachHang.isBlank())
                existing.setTenKhachHang(tenKhachHang.trim());
            new DAO.KhachHangDAO().update(existing);
            return existing;
        } else {
            int nextId = khachHangs.size() + 1;
            KhachHang newKh = new KhachHang(nextId,
                    tenKhachHang != null ? tenKhachHang.trim() : "", cleanSdt);
            khachHangs.add(newKh);
            new DAO.KhachHangDAO().insert(newKh);
            return newKh;
        }
    }

    public KhachHang findKhachHangBySoDienThoai(String sdt) {
        if (sdt == null || sdt.isBlank()) return null;
        String cleanSdt = sdt.trim();
        return khachHangs.stream()
                .filter(k -> cleanSdt.equalsIgnoreCase(k.getSoDienThoai().trim()))
                .findFirst().orElse(null);
    }
}
