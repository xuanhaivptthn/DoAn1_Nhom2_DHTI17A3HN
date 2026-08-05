package Controller;

import Model.KhuVucSan;
import Utils.DataStore;
import DAO.KhuVucSanDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ danh mục Khu vực Sân bóng.
 */
public class KhuVucSanController {

    private final KhuVucSanDAO khuVucSanDAO = new KhuVucSanDAO();

    public KhuVucSanController() {}

    public List<KhuVucSan> getAllCourts() {
        return DataStore.get().getKhuVucs();
    }

    public boolean saveOrUpdateCourt(KhuVucSan san) {
        if (san == null) return false;
        if (!DataStore.get().getKhuVucs().contains(san)) {
            DataStore.get().getKhuVucs().add(san);
        }
        if (DataStore.isUseDatabase()) {
            khuVucSanDAO.insert(san);
        }
        return true;
    }

    public boolean deleteCourt(String maSan) {
        if (maSan == null) return false;
        DataStore.get().getKhuVucs().removeIf(s -> maSan.equalsIgnoreCase(s.getMaSan()));
        if (DataStore.isUseDatabase()) {
            khuVucSanDAO.delete(maSan);
        }
        return true;
    }

    public void syncTrangThaiSanBaoTri() {
        DataStore.get().syncTrangThaiSanBaoTri();
    }
}
