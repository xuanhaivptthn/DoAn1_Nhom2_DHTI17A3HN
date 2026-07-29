package GiaoDien;

import Model.TaiKhoan;

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
            "STT", "Tên đăng nhập", "Họ tên", "Số điện thoại",
            "Email", "Vai trò", "Trạng thái"
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
                    boolean matchKw = kw.isEmpty()
                            || contains(tk.getTenDangNhap(), kw)
                            || contains(tk.getHoTen(), kw)
                            || contains(tk.getSoDienThoai(), kw)
                            || contains(tk.getEmail(), kw);
                    boolean matchRole = vaiTroFilter == null
                            || vaiTroFilter.isEmpty()
                            || "Tất cả".equalsIgnoreCase(vaiTroFilter)
                            || vaiTroFilter.equalsIgnoreCase(tk.getVaiTroHienThi())
                            || vaiTroFilter.equalsIgnoreCase(tk.getVaiTro())
                            || ("Quản trị viên".equalsIgnoreCase(vaiTroFilter) && ("Admin".equalsIgnoreCase(tk.getVaiTro()) || (tk.getVaiTroHienThi() != null && tk.getVaiTroHienThi().contains("Quản trị"))))
                            || ("Nhân viên".equalsIgnoreCase(vaiTroFilter) && ("NhanVien".equalsIgnoreCase(tk.getVaiTro()) || "Nhân viên".equalsIgnoreCase(tk.getVaiTroHienThi())));
                    boolean matchStatus = trangThaiFilter == null
                            || trangThaiFilter.isEmpty()
                            || "Tất cả".equalsIgnoreCase(trangThaiFilter)
                            || trangThaiFilter.equalsIgnoreCase(tk.getTrangThaiHienThi())
                            || trangThaiFilter.equalsIgnoreCase(tk.getTrangThai())
                            || (("Đã khóa".equalsIgnoreCase(trangThaiFilter) || "Bị khóa".equalsIgnoreCase(trangThaiFilter))
                                && ("Khoa".equalsIgnoreCase(tk.getTrangThai()) || "Đã khóa".equalsIgnoreCase(tk.getTrangThaiHienThi()) || "Bị khóa".equalsIgnoreCase(tk.getTrangThaiHienThi())))
                            || ("Hoạt động".equalsIgnoreCase(trangThaiFilter) && ("HoatDong".equalsIgnoreCase(tk.getTrangThai()) || "Hoạt động".equalsIgnoreCase(tk.getTrangThaiHienThi())));
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
            if (allData.get(i).getId() == tk.getId()) {
                allData.set(i, tk);
                break;
            }
        }
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    public void removeTaiKhoan(int id) {
        allData.removeIf(tk -> tk.getId() == id);
        filteredData = new ArrayList<>(allData);
        fireTableDataChanged();
    }

    public int nextId() {
        return allData.stream().mapToInt(TaiKhoan::getId).max().orElse(0) + 1;
    }

    public boolean existsUsername(String username, int excludeId) {
        return allData.stream()
                .anyMatch(tk -> tk.getTenDangNhap().equalsIgnoreCase(username) && tk.getId() != excludeId);
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
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> tk.getTenDangNhap();
            case 2 -> tk.getHoTen();
            case 3 -> tk.getSoDienThoai();
            case 4 -> tk.getEmail();
            case 5 -> tk.getVaiTroHienThi();
            case 6 -> tk.getTrangThaiHienThi();
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
