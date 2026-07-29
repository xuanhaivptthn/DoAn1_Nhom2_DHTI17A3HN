package Utils;

import Model.BaoTri;
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
                    datLichs.addAll(dbDatLichs);
                }

                List<BaoTri> dbBaoTris = new DAO.BaoTriDAO().getAll();
                if (dbBaoTris != null && !dbBaoTris.isEmpty()) {
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
        if (khachHangs.isEmpty()) seedDefaultKhachHangs();
        if (dichVus.isEmpty() && khoItems.isEmpty()) seedDefaultDichVus();
        if (khuVucs.isEmpty()) seedDefaultKhuVucs();
        if (datLichs.isEmpty()) seedDefaultDatLichs();
        if (baoTris.isEmpty()) seedDefaultBaoTris();
    }

    private void seedDefaultTaiKhoans() {
        taiKhoans.add(new TaiKhoan(1, "admin", "admin123", "Chủ Sân Quản Lý",
                "0988111222", "admin@sanbong.vn", "Admin", "HoatDong"));
        taiKhoans.add(new TaiKhoan(2, "nhanvien01", "nv123456", "Nguyễn Văn Nhân",
                "0977222333", "nv01@sanbong.vn", "NhanVien", "HoatDong"));
        taiKhoans.add(new TaiKhoan(3, "nhanvien02", "nv123456", "Trần Thị Thu",
                "0966333444", "nv02@sanbong.vn", "NhanVien", "HoatDong"));
        taiKhoans.add(new TaiKhoan(4, "nhanvien03", "nv123456", "Lê Hoàng Nam",
                "0955444555", "nv03@sanbong.vn", "NhanVien", "Khoa"));
    }

    private void seedDefaultKhachHangs() {
        khachHangs.add(new KhachHang(1, "Anh Đức (FC Anh Em)", "0912345678", "duc.fc@gmail.com", "Khách quen đặt cố định thứ 3 & thứ 5", 8));
        khachHangs.add(new KhachHang(2, "Anh Tuấn (FC Thể Công)", "0987654321", "tuan.tc@gmail.com", "Khách hay đá khung 19h - 20h30", 5));
        khachHangs.add(new KhachHang(3, "Chị Mai (Công ty FPT)", "0905123456", "mai.fpt@gmail.com", "Đặt sân cố định cuối tuần cho công ty", 12));
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
        khuVucs.add(new KhuVucSan(1, "A1", "Sân A1 (Sân 5)", "San5", 250000, "Sân cỏ nhân tạo tiêu chuẩn FIFA 5 người, có đèn thắp sáng", "SanSang"));
        khuVucs.add(new KhuVucSan(2, "A2", "Sân A2 (Sân 5)", "San5", 250000, "Sân cỏ nhân tạo 5 người, thoáng mát có lưới chắn bóng mới", "SanSang"));
        khuVucs.add(new KhuVucSan(3, "B1", "Sân B1 (Sân 7)", "San7", 400000, "Sân 7 người cỏ chất lượng cao, thoát nước tốt", "SanSang"));
        khuVucs.add(new KhuVucSan(4, "B2", "Sân B2 (Sân 7)", "San7", 400000, "Sân 7 người trang bị hệ thống chiếu sáng LED hiện đại", "SanSang"));
        khuVucs.add(new KhuVucSan(5, "C1", "Sân C1 (Sân 11)", "San11", 800000, "Sân 11 người đạt tiêu chuẩn thi đấu giải giao hữu chuyên nghiệp", "BaoTri"));
    }

    private void seedDefaultDatLichs() {
        String today = java.time.LocalDate.now().toString();
        DatLich dl1 = new DatLich(1, "DL001", 1, "Sân A1 (Sân 5)", "Anh Đức (FC Anh Em)", "0912345678", today, "17:30", "19:00", 425000, "DaXacNhan", "Nguyễn Văn Nhân", "Đặt cọc trước 100k");
        dl1.setTienSan(375000);
        dl1.setTienDichVu(50000);
        dl1.setDatCoc(100000);
        dl1.setTrangThaiTT("ThanhToanMotPhan");
        dl1.setDichVuKem("Nước suối Aquafina 500ml (x5): 50,000 VNĐ");
        datLichs.add(dl1);

        DatLich dl2 = new DatLich(2, "DL002", 3, "Sân B1 (Sân 7)", "Anh Tuấn (FC Thể Công)", "0987654321", today, "19:00", "20:30", 680000, "DaXacNhan", "Trần Thị Thu", "Thanh toán cọc qua CK");
        dl2.setTienSan(600000);
        dl2.setTienDichVu(80000);
        dl2.setDatCoc(200000);
        dl2.setTrangThaiTT("ThanhToanMotPhan");
        dl2.setDichVuKem("Nước điện giải Revive (x4): 60,000 VNĐ\nÁo bít tập luyện (Bộ) (x1): 20,000 VNĐ");
        datLichs.add(dl2);

        DatLich dl3 = new DatLich(3, "DL003", 2, "Sân A2 (Sân 5)", "Chị Mai (Công ty FPT)", "0905123456", today, "20:30", "22:00", 375000, "HoanThanh", "Chủ Sân Quản Lý", "Đã chuyển khoản đủ 100%");
        dl3.setTienSan(375000);
        dl3.setDatCoc(375000);
        dl3.setTrangThaiTT("DaThanhToan");
        datLichs.add(dl3);
    }

    private void seedDefaultBaoTris() {
        baoTris.add(new BaoTri(1, "BT001", 5, "Sân C1 (Sân 11)", "Thay lại thảm cỏ nhân tạo vùng cấm địa & kiểm tra hệ thống đèn pha LED", "Lê Minh Tuấn", "2026-07-25", "2026-08-05", 4500000, "DangXuLy"));
        baoTris.add(new BaoTri(2, "BT002", 4, "Sân B2 (Sân 7)", "Bảo dưỡng định kỳ lưới chắn bóng xung quanh", "Trần Thị Lan", "2026-07-20", "2026-07-22", 800000, "HoanThanh"));
    }

    private void addKhoItem(int maHH, String tenHH, int slTon, double donGia, String ncc) {
        khoItems.add(new DichVu(maHH, tenHH, slTon, donGia, ncc));
        khos.add(new Kho(maHH, tenHH, slTon, donGia, ncc));
    }

    public List<TaiKhoan> getTaiKhoans() { return taiKhoans; }
    public List<KhuVucSan> getKhuVucs() { return khuVucs; }
    public boolean isSanBaoTri(KhuVucSan k) {
        if (k == null) return false;
        if ("BaoTri".equalsIgnoreCase(k.getTrangThai()) || "Bảo trì".equalsIgnoreCase(k.getTrangThai())) {
            return true;
        }
        return baoTris.stream().anyMatch(b ->
            !"DaHuy".equalsIgnoreCase(b.getTrangThai()) &&
            !"HoanThanh".equalsIgnoreCase(b.getTrangThai()) &&
            (b.getKhuVucId() == k.getId() || (b.getTenSan() != null && b.getTenSan().equalsIgnoreCase(k.getTenSan())))
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

    public synchronized KhachHang saveOrUpdateKhachHang(String hoTen, String sdt, String email, String ghiChu) {
        if (sdt == null || sdt.isBlank()) return null;
        String cleanSdt = sdt.trim();
        KhachHang existing = khachHangs.stream()
                .filter(k -> cleanSdt.equalsIgnoreCase(k.getSoDienThoai().trim()))
                .findFirst().orElse(null);

        if (existing != null) {
            if (hoTen != null && !hoTen.isBlank()) existing.setHoTen(hoTen.trim());
            if (email != null && !email.isBlank()) existing.setEmail(email.trim());
            if (ghiChu != null && !ghiChu.isBlank()) existing.setGhiChu(ghiChu.trim());
            existing.tangSoLanDat();
            new DAO.KhachHangDAO().update(existing);
            return existing;
        } else {
            int nextId = khachHangs.stream().mapToInt(KhachHang::getId).max().orElse(0) + 1;
            KhachHang newKh = new KhachHang(nextId, hoTen != null ? hoTen.trim() : "", cleanSdt, email, ghiChu);
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
