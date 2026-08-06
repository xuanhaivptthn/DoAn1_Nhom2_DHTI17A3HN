package GiaoDien.Panels;

import Model.TaiKhoan;
import Utils.CodeGen;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Lớp TableModel tùy chỉnh quản lý hiển thị danh sách tài khoản hệ thống (TaiKhoanTableModel).
 * <p>
 * Mở rộng từ {@link AbstractTableModel} để cung cấp mô hình dữ liệu cho {@link javax.swing.JTable},
 * hỗ trợ các tính năng như hiển thị thông tin hồ sơ đi kèm (Họ tên, SĐT Chủ sân / Nhân viên),
 * lọc theo từ khóa, phân loại theo vai trò và trạng thái tài khoản.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class TaiKhoanTableModel extends AbstractTableModel {

    /**
     * Danh sách tên tiêu đề các cột của bảng tài khoản.
     */
    private static final String[] COLUMNS = {
            "STT", "Tên đăng nhập", "Họ và tên", "Số điện thoại", "Vai trò", "Trạng thái"
    };

    /**
     * Danh sách toàn bộ tài khoản ban đầu thu thập từ DataStore.
     */
    private final List<TaiKhoan> allData = new ArrayList<>();

    /**
     * Danh sách tài khoản đã qua xử lý lọc theo điều kiện hiển thị trên bảng.
     */
    private List<TaiKhoan> filteredData = new ArrayList<>();

    /**
     * Cập nhật danh sách dữ liệu tài khoản mới và thông báo làm mới giao diện bảng.
     * 
     * @param data Danh sách tài khoản mới cần thiết lập
     */
    public void setData(List<TaiKhoan> data) {
        allData.clear();
        if (data != null) {
            allData.addAll(data);
        }
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    /**
     * Thực hiện lọc dữ liệu danh sách tài khoản theo từ khóa tìm kiếm, vai trò người dùng và trạng thái tài khoản.
     * 
     * @param keyword         Từ khóa tìm kiếm theo tên đăng nhập, họ tên hoặc SĐT
     * @param vaiTroFilter    Bộ lọc vai trò (Quản trị viên, Nhân viên, Tất cả)
     * @param trangThaiFilter Bộ lọc trạng thái (Hoạt động, Đã khóa, Tất cả)
     */
    public void filter(String keyword, String vaiTroFilter, String trangThaiFilter) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        filteredData = allData.stream()
                .filter(tk -> {
                    String hoTen = "";
                    String sdt = "";
                    // Tra cứu họ tên và số điện thoại tương ứng với tài khoản từ Chủ sân hoặc Nhân viên
                    if (tk.isChuSan() || tk.isAdmin()) {
                        Model.ChuSan cs = Utils.DataStore.get().findChuSanByMaTaiKhoan(tk.getMaTaiKhoan());
                        if (cs != null) { hoTen = cs.getTenChuSan(); sdt = cs.getSoDienThoaiChuSan(); }
                    } else if (tk.isNhanVien()) {
                        Model.NhanVien nv = Utils.DataStore.get().findNhanVienByMaTaiKhoan(tk.getMaTaiKhoan());
                        if (nv != null) { hoTen = nv.getHoTenNhanVien(); sdt = nv.getSoDienThoaiNhanVien(); }
                    }

                    // Kiểm tra khớp từ khóa
                    boolean matchKw = kw.isEmpty()
                            || contains(tk.getTenDangNhap(), kw)
                            || contains(hoTen, kw)
                            || contains(sdt, kw);
                    // Kiểm tra khớp vai trò
                    boolean matchRole = vaiTroFilter == null
                            || vaiTroFilter.isEmpty()
                            || "Tất cả".equalsIgnoreCase(vaiTroFilter)
                            || vaiTroFilter.equalsIgnoreCase(tk.getQuyenHanHienThi())
                            || vaiTroFilter.equalsIgnoreCase(tk.getQuyenHan())
                            || ("Quản trị viên".equalsIgnoreCase(vaiTroFilter) && ("ADMIN".equalsIgnoreCase(tk.getQuyenHan()) || (tk.getQuyenHanHienThi() != null && tk.getQuyenHanHienThi().contains("Quản trị"))))
                            || ("Nhân viên".equalsIgnoreCase(vaiTroFilter) && ("NHAN_VIEN".equalsIgnoreCase(tk.getQuyenHan()) || "Nhân viên".equalsIgnoreCase(tk.getQuyenHanHienThi())));
                    // Kiểm tra khớp trạng thái
                    boolean matchStatus = trangThaiFilter == null
                            || trangThaiFilter.isEmpty()
                            || "Tất cả".equalsIgnoreCase(trangThaiFilter)
                            || trangThaiFilter.equalsIgnoreCase(tk.getTrangThaiHienThi())
                            || trangThaiFilter.equalsIgnoreCase(tk.getTrangThai())
                            || (("Đã khóa".equalsIgnoreCase(trangThaiFilter) || "Đã khoá".equalsIgnoreCase(trangThaiFilter) || "Bị khóa".equalsIgnoreCase(trangThaiFilter))
                                && ("KHOA".equalsIgnoreCase(tk.getTrangThai()) || "Đã khóa".equalsIgnoreCase(tk.getTrangThaiHienThi()) || "Đã khoá".equalsIgnoreCase(tk.getTrangThaiHienThi()) || "Bị khóa".equalsIgnoreCase(tk.getTrangThaiHienThi())))
                            || ("Hoạt động".equalsIgnoreCase(trangThaiFilter) && ("HOAT_DONG".equalsIgnoreCase(tk.getTrangThai()) || "Hoạt động".equalsIgnoreCase(tk.getTrangThaiHienThi())));
                    return matchKw && matchRole && matchStatus;
                })
                .collect(Collectors.toList());
        fireTableDataChanged();
    }

    /**
     * Kiểm tra xem chuỗi nguồn có chứa từ khóa (không phân biệt chữ hoa/thường).
     */
    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

    /**
     * Lấy đối tượng tài khoản tại vị trí chỉ số hàng trên bảng dữ liệu đã lọc.
     * 
     * @param row Chỉ số dòng (0-indexed)
     * @return TaiKhoan hoặc null nếu chỉ số vượt ngoài phạm vi
     */
    public TaiKhoan getAt(int row) {
        if (row < 0 || row >= filteredData.size()) {
            return null;
        }
        return filteredData.get(row);
    }

    /**
     * Lấy danh sách bản sao của toàn bộ tài khoản trong dữ liệu gốc.
     * 
     * @return List&lt;TaiKhoan&gt;
     */
    public List<TaiKhoan> getAllData() {
        return new ArrayList<>(allData);
    }

    /**
     * Thêm một tài khoản mới vào model dữ liệu và cập nhật bảng.
     * 
     * @param tk Tài khoản mới cần thêm
     */
    public void addTaiKhoan(TaiKhoan tk) {
        allData.add(tk);
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    /**
     * Cập nhật thông tin của tài khoản đã tồn tại trong model.
     * 
     * @param tk Đối tượng TaiKhoan mang thông tin mới cần cập nhật
     */
    public void updateTaiKhoan(TaiKhoan tk) {
        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).getMaTaiKhoan().equals(tk.getMaTaiKhoan())) {
                allData.set(i, tk);
                break;
            }
        }
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    /**
     * Xóa tài khoản ra khỏi model dữ liệu dựa trên mã tài khoản.
     * 
     * @param maTaiKhoan Mã định danh của tài khoản cần xóa
     */
    public void removeTaiKhoan(String maTaiKhoan) {
        allData.removeIf(tk -> tk.getMaTaiKhoan().equals(maTaiKhoan));
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    /**
     * Tự động sinh mã tài khoản tiếp theo có tiền tố "TK" (ví dụ: TK001, TK002).
     * 
     * @return Chuỗi mã tài khoản duy nhất tiếp theo
     */
    public String nextMaTaiKhoan() {
        List<String> allCodes = Utils.DataStore.get().getTaiKhoans().stream().map(TaiKhoan::getMaTaiKhoan).toList();
        return CodeGen.next("TK", allCodes, 3);
    }

    /**
     * Kiểm tra xem tên đăng nhập đã tồn tại trong hệ thống chưa.
     * 
     * @param username           Tên đăng nhập cần kiểm tra
     * @param excludeMaTaiKhoan Mã tài khoản loại trừ khi cập nhật (null nếu kiểm tra thêm mới)
     * @return True nếu tên đăng nhập đã được sử dụng bởi tài khoản khác, ngược lại False
     */
    public boolean existsUsername(String username, String excludeMaTaiKhoan) {
        return allData.stream()
                .anyMatch(tk -> tk.getTenDangNhap().equalsIgnoreCase(username) && !tk.getMaTaiKhoan().equals(excludeMaTaiKhoan));
    }

    @Override
    public int getRowCount() {
        return filteredData.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TaiKhoan tk = filteredData.get(rowIndex);
        String hoTen = "";
        String sdt = "";
        // Tra cứu bổ sung tên và số điện thoại thông qua thông tin hồ sơ
        if (tk.isChuSan() || tk.isAdmin()) {
            Model.ChuSan cs = Utils.DataStore.get().findChuSanByMaTaiKhoan(tk.getMaTaiKhoan());
            if (cs != null) { hoTen = cs.getTenChuSan(); sdt = cs.getSoDienThoaiChuSan(); }
        } else if (tk.isNhanVien()) {
            Model.NhanVien nv = Utils.DataStore.get().findNhanVienByMaTaiKhoan(tk.getMaTaiKhoan());
            if (nv != null) { hoTen = nv.getHoTenNhanVien(); sdt = nv.getSoDienThoaiNhanVien(); }
        }

        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> tk.getTenDangNhap();
            case 2 -> hoTen;
            case 3 -> sdt;
            case 4 -> tk.getQuyenHanHienThi();
            case 5 -> tk.getTrangThaiHienThi();
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
