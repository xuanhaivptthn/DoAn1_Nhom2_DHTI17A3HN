package DAO;

import Model.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Khách hàng ({@link KhachHang}).
 * <p>
 * Thực hiện các thao tác quản lý dữ liệu thông tin khách hàng trong bảng {@code khach_hang}
 * bao gồm xem danh sách, thêm mới, sửa và xóa thông tin khách hàng.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class KhachHangDAO {

    /**
     * Khởi tạo mặc định cho KhachHangDAO.
     */
    public KhachHangDAO() {
    }

    /**
     * Lấy toàn bộ danh sách khách hàng, sắp xếp tăng dần theo mã khách hàng.
     *
     * @return Danh sách {@link List} các đối tượng {@link KhachHang}.
     */
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        // Câu lệnh SQL truy vấn danh sách khách hàng
        String sql = "SELECT * FROM khach_hang ORDER BY maKhachHang ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt ResultSet và ánh xạ sang đối tượng KhachHang
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi lấy toàn bộ khách hàng
            System.err.println("Lỗi KhachHangDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm thông tin một khách hàng mới vào CSDL.
     *
     * @param kh Đối tượng {@link KhachHang} chứa thông tin cần thêm.
     * @return {@code true} nếu thêm thành công, ngược lại {@code false}.
     */
    public boolean insert(KhachHang kh) {
        // Câu lệnh SQL insert khách hàng mới
        String sql = "INSERT INTO khach_hang (maKhachHang, tenKhachHang, soDienThoai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số maKhachHang, tenKhachHang, soDienThoai
                pstmt.setString(1, kh.getMaKhachHang());
                pstmt.setString(2, kh.getTenKhachHang());
                pstmt.setString(3, kh.getSoDienThoai());
                // Thực thi insert và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi insert khách hàng
            System.err.println("Lỗi KhachHangDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin tên và số điện thoại của khách hàng theo mã khách hàng.
     *
     * @param kh Đối tượng {@link KhachHang} chứa thông tin cập nhật mới.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(KhachHang kh) {
        // Câu lệnh SQL update thông tin khách hàng
        String sql = "UPDATE khach_hang SET tenKhachHang = ?, soDienThoai = ? WHERE maKhachHang = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các giá trị cần thay đổi
                pstmt.setString(1, kh.getTenKhachHang());
                pstmt.setString(2, kh.getSoDienThoai());
                // Điều kiện maKhachHang
                pstmt.setString(3, kh.getMaKhachHang());
                // Thực thi update và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi update khách hàng
            System.err.println("Lỗi KhachHangDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một khách hàng khỏi CSDL theo mã khách hàng.
     *
     * @param maKhachHang Mã khách hàng cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maKhachHang) {
        // Câu lệnh SQL xóa khách hàng theo maKhachHang
        String sql = "DELETE FROM khach_hang WHERE maKhachHang = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số maKhachHang
                pstmt.setString(1, maKhachHang);
                // Thực thi delete và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi delete khách hàng
            System.err.println("Lỗi KhachHangDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một bản ghi từ {@link ResultSet} sang đối tượng {@link KhachHang}.
     *
     * @param rs Kết quả truy vấn từ CSDL.
     * @return Đối tượng {@link KhachHang} chứa dữ liệu ánh xạ.
     * @throws SQLException Nếu xảy ra lỗi đọc trường dữ liệu.
     */
    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        // Khởi tạo và trả về đối tượng KhachHang từ ResultSet
        return new KhachHang(
                rs.getString("maKhachHang"),
                rs.getString("tenKhachHang"),
                rs.getString("soDienThoai")
        );
    }
}
