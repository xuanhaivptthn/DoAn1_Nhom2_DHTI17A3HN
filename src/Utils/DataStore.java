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

/**
 * DataStore đóng vai trò CSDL lưu trữ bộ nhớ mẫu cho toàn bộ hệ thống sân bóng.
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

    private DataStore() {
        seed();
    }

    public static synchronized DataStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DataStore();
        }
        return INSTANCE;
    }

    private void seed() {
        // TÀI KHOẢN TRUY CẬP HỆ THỐNG (Khách hàng không cấp tài khoản đăng nhập)
        taiKhoans.add(new TaiKhoan(1, "admin", "admin123", "Nguyễn Văn Admin",
                "0901234567", "admin@sanbong.vn", "Admin", "HoatDong"));
        taiKhoans.add(new TaiKhoan(2, "nhanvien01", "nv123456", "Trần Thị Lan",
                "0912345678", "lan.tran@sanbong.vn", "NhanVien", "HoatDong"));
        taiKhoans.add(new TaiKhoan(3, "nhanvien02", "nv123456", "Lê Minh Tuấn",
                "0923456789", "tuan.le@sanbong.vn", "NhanVien", "HoatDong"));
        taiKhoans.add(new TaiKhoan(4, "nhanvien03", "nv123456", "Đỗ Thị Hương",
                "0967890123", "huong.do@sanbong.vn", "NhanVien", "Khoa"));

        // DANH SÁCH KHÁCH HÀNG (Lưu thông tin CSDL khi đặt sân để hỗ trợ tự động gợi ý)
        khachHangs.add(new KhachHang(1, "Anh Tú", "0903111222", "anhtu@gmail.com", "Khách quen đặt sân 5"));
        khachHangs.add(new KhachHang(2, "Cty FPT", "0988333444", "fpt@fpt.com.vn", "Đơn vị giải đấu doanh nghiệp"));
        khachHangs.add(new KhachHang(3, "Anh Minh", "0901234567", "minh.nguyen@yahoo.com", "Đội bóng Minh Vương FC"));
        khachHangs.add(new KhachHang(4, "Anh Long", "0912345678", "long.hoang@gmail.com", "Thường đặt sân tối thứ 6"));
        khachHangs.add(new KhachHang(5, "Phạm Quốc Hùng", "0934567890", "hung.pham@gmail.com", "Khách cá nhân"));
        khachHangs.add(new KhachHang(6, "Hoàng Thị Mai", "0945678901", "mai.hoang@gmail.com", "Đội nữ Mai Hoa"));

        // 1. DANH SÁCH DỊCH VỤ RIÊNG (Nhân sự, Phi vật thể, Tiện ích sân bóng)
        dichVus.add(new DichVu(1, "DV001", "Dịch vụ thuê trọng tài chính", "Nhân sự", 150000, "Trọng tài chuyên nghiệp điều hành 1 trận (90p)"));
        dichVus.add(new DichVu(2, "DV002", "Huấn luyện viên cá nhân 1v1", "HLV cá nhân", 300000, "HLV hướng dẫn kỹ thuật cá nhân theo giờ"));
        dichVus.add(new DichVu(3, "DV003", "Giặt sấy trang phục thi đấu", "Giặt sấy", 30000, "Giặt sấy tiệt trùng bộ quần áo sau trận"));
        dichVus.add(new DichVu(4, "DV004", "Hỗ trợ truyền thông & Quay phim", "Dịch vụ thi đấu", 250000, "Quay video trận đấu & phát lại Highlights"));

        // 2. DANH SÁCH MẶT HÀNG KHO HÀNG (Dùng để Bán / Cho thuê vật phẩm qua Quản lý dịch vụ)
        addKhoItem(101, "Bóng đá FIFA Động Lực size 5", 15, 30000, "Tập đoàn Thể thao Động Lực");
        addKhoItem(102, "Áo lưới tập bib phân đội", 60, 20000, "Xưởng may Trang phục Thể thao");
        addKhoItem(103, "Nước suối Aquafina 500ml", 150, 10000, "Công ty Nước khoáng Aquafina");
        addKhoItem(104, "Nước bù điện giải Pocari 500ml", 120, 15000, "Công ty Pocari Sweat Việt Nam");
        addKhoItem(105, "Găng tay thủ môn Adidas", 10, 50000, "Adidas Việt Nam");
        addKhoItem(106, "Lưới bóng đá S7", 12, 180000, "NCS Sports Việt Nam");
        addKhoItem(107, "Băng gối & khuỷu tay bảo vệ", 25, 45000, "Y tế Thể thao Chấn thương");

        khuVucs.add(new KhuVucSan(1, "Sân 1", "Sân 1 (S5)", "San5", 200000, "Có mái che", "SanSang"));
        khuVucs.add(new KhuVucSan(2, "Sân 2", "Sân 2 (S7)", "San7", 300000, "Cỏ FIFA", "DangThue"));
        khuVucs.add(new KhuVucSan(3, "Sân 3", "Sân 3 (S7)", "San7", 300000, "Ngoài trời", "SanSang"));
        khuVucs.add(new KhuVucSan(4, "Sân 4", "Sân 4 (S5)", "San5", 180000, "Cỏ nhân tạo", "SanSang"));
        khuVucs.add(new KhuVucSan(5, "Sân lớn", "Sân 11A Sân lớn", "San11", 800000, "Đèn pha LED", "BaoTri"));

        // Lịch đặt mẫu
        String dDate1 = "2026-07-11";
        String dDate2 = java.time.LocalDate.now().toString();

        for (String dateStr : new String[]{dDate1, dDate2}) {
            DatLich dl1 = new DatLich(datLichs.size() + 1, "DL" + (datLichs.size() + 1), 1, "Sân 1 (S5)", "Anh Tú",
                    "0903111222", dateStr, "17:00", "18:00", 200000, "HoanThanh", "Admin", "");
            dl1.setTrangThaiTT("DaThanhToan");
            datLichs.add(dl1);

            DatLich dl2 = new DatLich(datLichs.size() + 1, "DL" + (datLichs.size() + 1), 1, "Sân 1 (S5)", "Cty FPT",
                    "0988333444", dateStr, "19:00", "20:00", 200000, "HoanThanh", "Admin", "");
            dl2.setTrangThaiTT("DaThanhToan");
            datLichs.add(dl2);

            DatLich dl3 = new DatLich(datLichs.size() + 1, "DL" + (datLichs.size() + 1), 2, "Sân 2 (S7)", "Anh Minh",
                    "0901234567", dateStr, "16:00", "17:00", 300000, "DaXacNhan", "Trần Thị Lan", "");
            dl3.setTrangThaiTT("ChuaThanhToan");
            datLichs.add(dl3);

            DatLich dl4 = new DatLich(datLichs.size() + 1, "DL" + (datLichs.size() + 1), 2, "Sân 2 (S7)", "Anh Long",
                    "0912345678", dateStr, "18:00", "19:00", 360000, "DaXacNhan", "Admin", "Đã cọc 100k");
            dl4.setTienSan(300000);
            dl4.addDichVuKem("Nước suối 500ml", 6, 60000);
            dl4.setDatCoc(100000);
            dl4.setTrangThaiTT("ChuaThanhToan");
            datLichs.add(dl4);

            DatLich dl5 = new DatLich(datLichs.size() + 1, "DL" + (datLichs.size() + 1), 3, "Sân 3 (S7)", "Chị Hoa",
                    "0908765432", dateStr, "18:00", "19:00", 300000, "DaXacNhan", "Lê Minh Tuấn", "");
            dl5.setTrangThaiTT("ChuaThanhToan");
            datLichs.add(dl5);
        }

        baoTris.add(new BaoTri(1, "BT001", 3, "Sân 3", "Bảo trì - Thay lưới",
                "Đỗ Thị Hương", "2026-07-11", "2026-07-28", 500000, "DangXuLy"));
        baoTris.add(new BaoTri(2, "BT002", 5, "Sân lớn", "Thay thảm cỏ + kiểm tra đèn pha",
                "Đỗ Thị Hương", "2026-07-20", "2026-07-28", 15000000, "DangXuLy"));
        baoTris.add(new BaoTri(3, "BT003", 2, "Sân 2", "Sửa khung thành góc phải",
                "Lê Minh Tuấn", "2026-07-18", "2026-07-28", 500000, "DangXuLy"));
        baoTris.add(new BaoTri(4, "BT004", 4, "Sân 4", "Bảo dưỡng hệ thống tưới",
                "Trần Thị Lan", "2026-07-26", "2026-07-28", 300000, "ChoXuLy"));
    }

    private void addKhoItem(int maHH, String tenHH, int slTon, double donGia, String ncc) {
        khoItems.add(new DichVu(maHH, tenHH, slTon, donGia, ncc));
        khos.add(new Kho(maHH, tenHH, slTon, donGia, ncc));
    }

    public List<TaiKhoan> getTaiKhoans() { return taiKhoans; }
    public List<KhuVucSan> getKhuVucs() { return khuVucs; }
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
            return existing;
        } else {
            int nextId = khachHangs.stream().mapToInt(KhachHang::getId).max().orElse(0) + 1;
            KhachHang newKh = new KhachHang(nextId, hoTen != null ? hoTen.trim() : "", cleanSdt, email, ghiChu);
            khachHangs.add(newKh);
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
