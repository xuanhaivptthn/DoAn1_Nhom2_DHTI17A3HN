package Controller;

import Model.DatLich;
import Model.KhuVucSan;
import Utils.DataStore;
import DAO.DatLichDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ nghiệp vụ Đặt sân bóng.
 */
public class DatLichController {

    private final DatLichDAO datLichDAO = new DatLichDAO();
    private final HoaDonController hoaDonController = new HoaDonController();

    public DatLichController() {}

    public List<DatLich> getAllBookings() {
        return DataStore.get().getDatLichs();
    }

    public boolean createBooking(DatLich d) {
        if (d == null) return false;

        // Auto-resolve customer if missing
        if (d.getMaKhachHang() == null || d.getMaKhachHang().isBlank()) {
            Model.KhachHang kh = DataStore.get().saveOrUpdateKhachHang(d.getTenKhach(), d.getSoDienThoaiKhach());
            if (kh != null) {
                d.setMaKhachHang(kh.getMaKhachHang());
            }
        }

        DataStore.get().getDatLichs().add(d);
        if (DataStore.isUseDatabase()) {
            datLichDAO.insert(d);
        }
        return true;
    }

    public boolean updateBooking(DatLich d) {
        if (d == null) return false;

        if (DataStore.isUseDatabase()) {
            datLichDAO.update(d);
        }
        hoaDonController.saveOrUpdateHoaDonForBooking(d, "Tiền mặt");
        return true;
    }

    public boolean updateBookingStatus(DatLich d, String newStatus, String phuongThucTT) {
        if (d == null || newStatus == null) return false;

        switch (newStatus) {
            case "ChoXacNhan" -> {
                d.setTrangThai("ChoXacNhan");
                d.setTrangThaiTT("ChuaThanhToan");
            }
            case "DaXacNhan" -> {
                d.setTrangThai("DaXacNhan");
                d.setTrangThaiTT("ChuaThanhToan");
            }
            case "HoanThanh" -> {
                d.setTrangThai("HoanThanh");
                d.setTrangThaiTT("DaThanhToan");
                hoaDonController.saveOrUpdateHoaDonForBooking(d, phuongThucTT != null ? phuongThucTT : "Tiền mặt");
            }
            case "DaHuy" -> {
                d.setTrangThai("DaHuy");
            }
        }

        if (DataStore.isUseDatabase()) {
            datLichDAO.update(d);
        }
        return true;
    }

    public DatLich findOverlapBooking(String maSan, String ngayDat, String gioBD, String gioKT, String excludeMaLichDat) {
        int newStart = toMinutes(gioBD);
        int newEnd = toMinutes(gioKT);

        if (newStart >= newEnd) return null;

        for (DatLich existing : DataStore.get().getDatLichs()) {
            if (excludeMaLichDat != null && excludeMaLichDat.equalsIgnoreCase(existing.getMaLichDat())) continue;
            if (maSan != null && !maSan.equalsIgnoreCase(existing.getMaSan())) continue;
            if (ngayDat != null && !ngayDat.trim().equalsIgnoreCase(existing.getNgayDat().trim())) continue;
            if ("DaHuy".equalsIgnoreCase(existing.getTrangThai())) continue;

            int exStart = toMinutes(existing.getGioBatDau());
            int exEnd = toMinutes(existing.getGioKetThuc());

            if (newStart < exEnd && newEnd > exStart) {
                return existing;
            }
        }
        return null;
    }

    public boolean isSanBaoTriVoiNgay(KhuVucSan san, String dateStr) {
        return DataStore.get().isSanBaoTriVoiNgay(san, dateStr);
    }

    private static int toMinutes(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        try {
            String[] parts = timeStr.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }
}
