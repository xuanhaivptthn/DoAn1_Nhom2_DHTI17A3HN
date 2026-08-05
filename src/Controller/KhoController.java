package Controller;

import Model.DichVu;
import Model.Kho;
import Utils.DataStore;
import DAO.KhoDAO;
import DAO.DichVuDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ kho hàng hóa, tồn kho và dịch vụ đi kèm.
 */
public class KhoController {

    private final KhoDAO khoDAO = new KhoDAO();
    private final DichVuDAO dichVuDAO = new DichVuDAO();

    public KhoController() {}

    public List<DichVu> getAllKhoItems() {
        return DataStore.get().getKhoItems();
    }

    public List<Kho> getAllKhos() {
        return DataStore.get().getKhos();
    }

    public List<DichVu> getAllServices() {
        return DataStore.get().getDichVus();
    }

    public synchronized void giamStock(DichVu dv, int count) {
        DataStore.get().giamKhoStock(dv, count);
    }

    public synchronized void nhapKho(DichVu dv, int count) {
        if (dv == null || count <= 0) return;
        dv.nhapKho(count);
        dv.capNhatDuLieu();
        for (Kho k : DataStore.get().getKhos()) {
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

    public boolean saveOrUpdateDichVu(DichVu dv) {
        if (dv == null) return false;
        if (!DataStore.get().getDichVus().contains(dv)) {
            DataStore.get().getDichVus().add(dv);
        }
        if (DataStore.isUseDatabase()) {
            dichVuDAO.insert(dv);
        }
        return true;
    }

    public boolean deleteDichVu(String maDichVu) {
        if (maDichVu == null) return false;
        DataStore.get().getDichVus().removeIf(d -> maDichVu.equalsIgnoreCase(d.getMaDichVu()));
        if (DataStore.isUseDatabase()) {
            dichVuDAO.delete(maDichVu);
        }
        return true;
    }
}
