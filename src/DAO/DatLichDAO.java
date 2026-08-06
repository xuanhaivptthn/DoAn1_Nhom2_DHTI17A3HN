package DAO;

import Model.DatLich;
import Model.DichVu;
import Utils.DataStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Đặt lịch sân ({@link DatLich}).
 * <p>
 * Thực hiện lưu trữ, truy vấn, cập nhật và xóa thông tin phiếu đặt sân bóng
 * trong bảng {@code lich_dat_san} của cơ sở dữ liệu.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class DatLichDAO {

    /**
     * Khởi tạo mặc định cho DatLichDAO.
     */
    public DatLichDAO() {
    }

    /**
     * Lấy toàn bộ danh sách lịch đặt sân, sắp xếp giảm dần theo ngày đặt.
     *
     * @return Danh sách {@link List} các đối tượng {@link DatLich}.
     */
    public List<DatLich> getAll() {
        List<DatLich> list = new ArrayList<>();
        // Truy vấn tất cả thông tin lịch đặt sân, sắp xếp ngày đặt mới nhất lên đầu
        String sql = "SELECT * FROM lich_dat_san ORDER BY ngayDat DESC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Đọc qua kết quả truy vấn và ánh xạ sang đối tượng DatLich
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi lấy danh sách lịch đặt
            System.err.println("Lỗi DatLichDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm một thông tin đặt lịch mới vào CSDL.
     * <p>
     * Nếu mã khách hàng chưa được thiết lập, hệ thống sẽ tự động tạo mới hoặc cập nhật
     * khách hàng thông qua {@link DataStore#saveOrUpdateKhachHang(String, String)}.
     * </p>
     *
     * @param d Đối tượng {@link DatLich} chứa thông tin đặt sân.
     * @return {@code true} nếu thêm lịch đặt thành công, ngược lại {@code false}.
     */
    public boolean insert(DatLich d) {
        // Nếu mã khách hàng trống, thực hiện tự động tạo/lấy mã khách hàng dựa trên tên và số điện thoại
        if (d.getMaKhachHang() == null || d.getMaKhachHang().isBlank()) {
            Model.KhachHang kh = Utils.DataStore.get().saveOrUpdateKhachHang(d.getTenKhach(), d.getSoDienThoaiKhach());
            if (kh != null) {
                d.setMaKhachHang(kh.getMaKhachHang());
            }
        }
        // Lệnh SQL chèn bản ghi lịch đặt sân mới
        String sql = "INSERT INTO lich_dat_san (maLichDat, maSan, maTaiKhoan, maKhachHang, ngayDat, gioBatDau, gioKetThuc, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các thuộc tính của đối tượng đặt lịch vào câu lệnh SQL
                pstmt.setString(1, d.getMaLichDat());
                pstmt.setString(2, d.getMaSan());
                pstmt.setString(3, d.getMaTaiKhoan());
                pstmt.setString(4, d.getMaKhachHang());
                pstmt.setString(5, d.getNgayDat());
                pstmt.setString(6, d.getGioBatDau());
                pstmt.setString(7, d.getGioKetThuc());
                pstmt.setString(8, d.getTrangThai());
                pstmt.setString(9, d.getGhiChu());
                // Thực thi lệnh chèn và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi nếu thêm lịch đặt thất bại
            System.err.println("Lỗi DatLichDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin phiếu đặt sân theo mã lịch đặt.
     * <p>
     * Nếu mã khách hàng trống, tự động cập nhật/tạo mới khách hàng tương ứng.
     * </p>
     *
     * @param d Đối tượng {@link DatLich} chứa thông tin sửa đổi.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(DatLich d) {
        // Tự động xử lý lưu/cập nhật mã khách hàng nếu mã bị trống
        if (d.getMaKhachHang() == null || d.getMaKhachHang().isBlank()) {
            Model.KhachHang kh = Utils.DataStore.get().saveOrUpdateKhachHang(d.getTenKhach(), d.getSoDienThoaiKhach());
            if (kh != null) {
                d.setMaKhachHang(kh.getMaKhachHang());
            }
        }
        // Lệnh SQL cập nhật thông tin lịch đặt sân
        String sql = "UPDATE lich_dat_san SET maSan = ?, maTaiKhoan = ?, maKhachHang = ?, ngayDat = ?, gioBatDau = ?, gioKetThuc = ?, trangThai = ?, ghiChu = ? WHERE maLichDat = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các thông tin thay đổi vào PreparedStatement
                pstmt.setString(1, d.getMaSan());
                pstmt.setString(2, d.getMaTaiKhoan());
                pstmt.setString(3, d.getMaKhachHang());
                pstmt.setString(4, d.getNgayDat());
                pstmt.setString(5, d.getGioBatDau());
                pstmt.setString(6, d.getGioKetThuc());
                pstmt.setString(7, d.getTrangThai());
                pstmt.setString(8, d.getGhiChu());
                // Điều kiện mã lịch đặt cần cập nhật
                pstmt.setString(9, d.getMaLichDat());
                // Thực thi câu lệnh SQL UPDATE
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi cập nhật lịch đặt
            System.err.println("Lỗi DatLichDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa thông tin đặt lịch theo mã lịch đặt.
     *
     * @param maLichDat Mã lịch đặt sân cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maLichDat) {
        // Lệnh SQL xóa lịch đặt sân
        String sql = "DELETE FROM lich_dat_san WHERE maLichDat = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán mã lịch đặt cần xóa
                pstmt.setString(1, maLichDat);
                // Thực thi câu lệnh xóa bản ghi
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi nếu xóa thất bại
            System.err.println("Lỗi DatLichDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một hàng trong {@link ResultSet} sang đối tượng model {@link DatLich}.
     *
     * @param rs Đối tượng {@link ResultSet} nhận được từ câu truy vấn SQL.
     * @return Đối tượng {@link DatLich} chứa thông tin vừa ánh xạ.
     * @throws SQLException Nếu có lỗi khi lấy dữ liệu từ cột ResultSet.
     */
    private DatLich mapResultSet(ResultSet rs) throws SQLException {
        // Tạo đối tượng DatLich và gán các trường giá trị lấy từ ResultSet
        DatLich d = new DatLich();
        d.setMaLichDat(rs.getString("maLichDat"));
        d.setMaSan(rs.getString("maSan"));
        d.setMaTaiKhoan(rs.getString("maTaiKhoan"));
        d.setMaKhachHang(rs.getString("maKhachHang"));
        d.setNgayDat(rs.getString("ngayDat"));
        d.setGioBatDau(rs.getString("gioBatDau"));
        d.setGioKetThuc(rs.getString("gioKetThuc"));
        d.setTrangThai(rs.getString("trangThai"));
        d.setGhiChu(rs.getString("ghiChu"));

        return d;
    }
}
