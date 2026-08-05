package Controller;

import Model.BaoTri;
import Utils.DataStore;
import DAO.BaoTriDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ công tác Bảo trì Sân bóng.
 */
public class BaoTriController {

    private final BaoTriDAO baoTriDAO = new BaoTriDAO();

    public BaoTriController() {}

    public List<BaoTri> getAllBaoTri() {
        return DataStore.get().getBaoTris();
    }

    public boolean createBaoTri(BaoTri bt) {
        if (bt == null) return false;
        DataStore.get().getBaoTris().add(bt);
        if (DataStore.isUseDatabase()) {
            baoTriDAO.insert(bt);
        }
        DataStore.get().syncTrangThaiSanBaoTri();
        return true;
    }

    public boolean updateBaoTri(BaoTri bt) {
        if (bt == null) return false;
        if (DataStore.isUseDatabase()) {
            baoTriDAO.update(bt);
        }
        DataStore.get().syncTrangThaiSanBaoTri();
        return true;
    }

    public boolean deleteBaoTri(String maPhieuBaoTri) {
        if (maPhieuBaoTri == null) return false;
        DataStore.get().getBaoTris().removeIf(b -> maPhieuBaoTri.equalsIgnoreCase(b.getMaPhieuBaoTri()));
        if (DataStore.isUseDatabase()) {
            baoTriDAO.delete(maPhieuBaoTri);
        }
        DataStore.get().syncTrangThaiSanBaoTri();
        return true;
    }
}
