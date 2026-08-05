package Controller;

import Model.DatLich;
import Model.HoaDon;
import Utils.DataStore;
import DAO.HoaDonDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ nghiệp vụ Hóa đơn thanh toán.
 */
public class HoaDonController {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    public HoaDonController() {}

    public List<HoaDon> getAllInvoices() {
        return DataStore.get().getHoaDons();
    }

    public HoaDon saveOrUpdateHoaDonForBooking(DatLich d, String phuongThucTT) {
        return DataStore.get().saveOrUpdateHoaDonForBooking(d, phuongThucTT);
    }

    public boolean insertInvoice(HoaDon h) {
        if (h == null) return false;
        if (!DataStore.get().getHoaDons().contains(h)) {
            DataStore.get().getHoaDons().add(h);
        }
        if (DataStore.isUseDatabase()) {
            return hoaDonDAO.insert(h);
        }
        return true;
    }

    public boolean updateInvoice(HoaDon h) {
        if (h == null) return false;
        if (DataStore.isUseDatabase()) {
            return hoaDonDAO.update(h);
        }
        return true;
    }

    public boolean deleteInvoice(String maHoaDon) {
        if (maHoaDon == null) return false;
        DataStore.get().getHoaDons().removeIf(h -> maHoaDon.equalsIgnoreCase(h.getMaHoaDon()));
        if (DataStore.isUseDatabase()) {
            return hoaDonDAO.delete(maHoaDon);
        }
        return true;
    }
}
