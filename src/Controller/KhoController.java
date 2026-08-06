package Controller;

import Model.DichVu;
import Model.Kho;
import Utils.DataStore;
import DAO.KhoDAO;
import DAO.DichVuDAO;

import java.util.List;

/**
 * Lớp Điều khiển (Controller) quản lý toàn bộ kho hàng hóa, tồn kho và dịch vụ đi kèm.
 * <p>
 * Lớp này xử lý các thao tác truy vấn danh sách vật tư kho, danh sách dịch vụ cho thuê/bán lẻ,
 * cập nhật giảm số lượng tồn kho khi xuất bán, nhập kho bổ sung hàng hóa,
 * thêm/sửa dịch vụ và xóa dịch vụ khỏi hệ thống.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class KhoController {

    /**
     * Đối tượng DAO thực hiện các thao tác quản lý Kho trong CSDL MySQL.
     */
    private final KhoDAO khoDAO = new KhoDAO();

    /**
     * Đối tượng DAO thực hiện thao tác quản lý danh mục Dịch vụ trong CSDL MySQL.
     */
    private final DichVuDAO dichVuDAO = new DichVuDAO();

    /**
     * Khởi tạo đối tượng {@code KhoController} mặc định.
     */
    public KhoController() {}

    /**
     * Lấy toàn bộ danh sách mặt hàng dịch vụ thuộc phân loại kho vật tư.
     *
     * @return Danh sách các đối tượng {@link DichVu} trong kho.
     */
    public List<DichVu> getAllKhoItems() {
        // Lấy danh sách các mặt hàng thuộc dạng vật tư kho
        return DataStore.get().getKhoItems();
    }

    /**
     * Lấy danh sách đối tượng tồn kho chi tiết từ bộ nhớ DataStore.
     *
     * @return Danh sách các đối tượng {@link Kho}.
     */
    public List<Kho> getAllKhos() {
        // Lấy danh sách đối tượng Kho lưu giữ thông tin số lượng tồn
        return DataStore.get().getKhos();
    }

    /**
     * Lấy toàn bộ danh sách các dịch vụ đi kèm (cho thuê sân bóng, HLV, trọng tài,...).
     *
     * @return Danh sách các đối tượng {@link DichVu}.
     */
    public List<DichVu> getAllServices() {
        // Trả về danh sách dịch vụ không thuộc vật tư tồn kho thuần túy
        return DataStore.get().getDichVus();
    }

    /**
     * Trừ bớt số lượng tồn kho của một mặt hàng dịch vụ khi xuất bán hoặc cho thuê.
     * <p>
     * Phương thức được đồng bộ hóa (synchronized) để đảm bảo an toàn luồng khi xử lý giao dịch đồng thời.
     * </p>
     *
     * @param dv    Đối tượng {@link DichVu} cần giảm số lượng tồn.
     * @param count Số lượng xuất bán/sử dụng.
     */
    public synchronized void giamStock(DichVu dv, int count) {
        // Ủy quyền giảm số lượng tồn kho cho DataStore
        DataStore.get().giamKhoStock(dv, count);
    }

    /**
     * Nhập thêm hàng hóa bổ sung vào kho.
     * <p>
     * Cập nhật số lượng tồn cho đối tượng dịch vụ và đồng bộ thông tin sang danh sách Kho tương ứng.
     * </p>
     *
     * @param dv    Mặt hàng dịch vụ/vật tư {@link DichVu} cần nhập thêm.
     * @param count Số lượng nhập thêm (phải > 0).
     */
    public synchronized void nhapKho(DichVu dv, int count) {
        // Kiểm tra đối tượng hợp lệ và số lượng nhập phải lớn hơn 0
        if (dv == null || count <= 0) return;

        // Gọi phương thức nhập kho của đối tượng DichVu để tăng số lượng tồn
        dv.nhapKho(count);
        // Cập nhật lại chuỗi thông tin mô tả/dữ liệu bổ trợ
        dv.capNhatDuLieu();

        // Tìm và cập nhật lại số lượng tồn kho trong danh sách Kho
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

    /**
     * Lưu mới hoặc cập nhật thông tin một dịch vụ vào bộ nhớ và cơ sở dữ liệu.
     *
     * @param dv Đối tượng {@link DichVu} chứa thông tin cần lưu hoặc sửa đổi.
     * @return {@code true} nếu thành công; {@code false} nếu tham số {@code dv} bị {@code null}.
     */
    public boolean saveOrUpdateDichVu(DichVu dv) {
        // Kiểm tra dữ liệu dịch vụ đầu vào
        if (dv == null) return false;

        // Thêm vào danh sách bộ nhớ DataStore nếu chưa có
        if (!DataStore.get().getDichVus().contains(dv)) {
            DataStore.get().getDichVus().add(dv);
        }

        // Nếu hệ thống bật CSDL MySQL, chèn dữ liệu vào CSDL
        if (DataStore.isUseDatabase()) {
            dichVuDAO.insert(dv);
        }
        return true;
    }

    /**
     * Xóa một dịch vụ khỏi hệ thống dựa trên mã dịch vụ.
     *
     * @param maDichVu Mã dịch vụ cần xóa (ví dụ: "DV001").
     * @return {@code true} nếu xóa thành công; {@code false} nếu mã dịch vụ bị {@code null}.
     */
    public boolean deleteDichVu(String maDichVu) {
        // Kiểm tra tham số mã dịch vụ
        if (maDichVu == null) return false;

        // Loại bỏ dịch vụ khỏi danh sách lưu trữ DataStore
        DataStore.get().getDichVus().removeIf(d -> maDichVu.equalsIgnoreCase(d.getMaDichVu()));

        // Xóa dòng dịch vụ tương ứng khỏi MySQL CSDL nếu đang kết nối
        if (DataStore.isUseDatabase()) {
            dichVuDAO.delete(maDichVu);
        }
        return true;
    }
}
