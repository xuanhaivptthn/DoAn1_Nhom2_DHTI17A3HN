package GiaoDien.Panels;

import Model.TaiKhoan;
import Utils.CodeGen;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * TableModel hiển thị danh sách tài khoản.
 */
public class TaiKhoanTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
            "STT", "Tên đăng nhập", "Họ và tên", "Số điện thoại", "Vai trò", "Trạng thái"
    };

    private final List<TaiKhoan> allData = new ArrayList<>();
    private List<TaiKhoan> filteredData = new ArrayList<>();

    public void setData(List<TaiKhoan> data) {
        allData.clear();
        if (data != null) {
            allData.addAll(data);
        }
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    public void filter(String keyword, String vaiTroFilter, String trangThaiFilter) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        filteredData = allData.stream()
                .filter(tk -> {
                    String hoTen = "";
                    String sdt = "";
                    if (tk.isChuSan() || tk.isAdmin()) {
                        Model.ChuSan cs = Utils.DataStore.get().findChuSanByMaTaiKhoan(tk.getMaTaiKhoan());
                        if (cs != null) { hoTen = cs.getTenChuSan(); sdt = cs.getSoDienThoaiChuSan(); }
                    } else if (tk.isNhanVien()) {
                        Model.NhanVien nv = Utils.DataStore.get().findNhanVienByMaTaiKhoan(tk.getMaTaiKhoan());
                        if (nv != null) { hoTen = nv.getHoTenNhanVien(); sdt = nv.getSoDienThoaiNhanVien(); }
                    }

                    boolean matchKw = kw.isEmpty()
                            || contains(tk.getTenDangNhap(), kw)
                            || contains(hoTen, kw)
                            || contains(sdt, kw);
                    boolean matchRole = vaiTroFilter == null
                            || vaiTroFilter.isEmpty()
                            || "Tất cả".equalsIgnoreCase(vaiTroFilter)
                            || vaiTroFilter.equalsIgnoreCase(tk.getQuyenHanHienThi())
                            || vaiTroFilter.equalsIgnoreCase(tk.getQuyenHan())
                            || ("Quản trị viên".equalsIgnoreCase(vaiTroFilter) && ("ADMIN".equalsIgnoreCase(tk.getQuyenHan()) || (tk.getQuyenHanHienThi() != null && tk.getQuyenHanHienThi().contains("Quản trị"))))
                            || ("Nhân viên".equalsIgnoreCase(vaiTroFilter) && ("NHAN_VIEN".equalsIgnoreCase(tk.getQuyenHan()) || "Nhân viên".equalsIgnoreCase(tk.getQuyenHanHienThi())));
                    boolean matchStatus = trangThaiFilter == null
                            || trangThaiFilter.isEmpty()
                            || "Tất cả".equalsIgnoreCase(trangThaiFilter)
                            || trangThaiFilter.equalsIgnoreCase(tk.getTrangThaiHienThi())
                            || trangThaiFilter.equalsIgnoreCase(tk.getTrangThai())
                            || (("Đã khóa".equalsIgnoreCase(trangThaiFilter) || "Bị khóa".equalsIgnoreCase(trangThaiFilter))
                                && ("KHOA".equalsIgnoreCase(tk.getTrangThai()) || "Đã khóa".equalsIgnoreCase(tk.getTrangThaiHienThi()) || "Bị khóa".equalsIgnoreCase(tk.getTrangThaiHienThi())))
                            || ("Hoạt động".equalsIgnoreCase(trangThaiFilter) && ("HOAT_DONG".equalsIgnoreCase(tk.getTrangThai()) || "Hoạt động".equalsIgnoreCase(tk.getTrangThaiHienThi())));
                    return matchKw && matchRole && matchStatus;
                })
                .collect(Collectors.toList());
        fireTableDataChanged();
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

    public TaiKhoan getAt(int row) {
        if (row < 0 || row >= filteredData.size()) {
            return null;
        }
        return filteredData.get(row);
    }

    public List<TaiKhoan> getAllData() {
        return new ArrayList<>(allData);
    }

    public void addTaiKhoan(TaiKhoan tk) {
        allData.add(tk);
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

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

    public void removeTaiKhoan(String maTaiKhoan) {
        allData.removeIf(tk -> tk.getMaTaiKhoan().equals(maTaiKhoan));
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    public String nextMaTaiKhoan() {
        return CodeGen.next("TK", allData.stream().map(TaiKhoan::getMaTaiKhoan).toList(), 3);
    }

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
