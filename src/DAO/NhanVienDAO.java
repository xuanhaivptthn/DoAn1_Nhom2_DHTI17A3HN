package DAO;

import Model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Nhân viên ({@link NhanVien}).
 * <p>
 * Quản lý thông tin hồ sơ nhân viên trong bảng {@code nhan_vien} bao gồm lấy danh sách,
 * tìm kiếm theo tài khoản, thêm mới, sửa và xóa thông tin nhân viên.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class NhanVienDAO {

    /**
     * Khởi tạo mặc định cho NhanVienDAO.
     */
    public NhanVienDAO() {
    }

    /**
     * Lấy toàn bộ danh sách nhân viên trong CSDL, sắp xếp tăng dần theo mã nhân viên.
     *
     * @return Danh sách {@link List} các đối tượng {@link NhanVien}.
     */
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả nhân viên, sắp xếp tăng dần theo maNhanVien
        String sql = "SELECT * FROM nhan_vien ORDER BY maNhanVien ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt ResultSet và ánh xạ sang đối tượng NhanVien
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi lấy toàn bộ nhân viên
            System.err.println("Lỗi NhanVienDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Tìm kiếm thông tin nhân viên theo mã tài khoản liên kết.
     *
     * @param maTaiKhoan Mã tài khoản của nhân viên cần tìm.
     * @return Đối tượng {@link NhanVien} nếu tìm thấy, ngược lại trả về {@code null}.
     */
    public NhanVien findByMaTaiKhoan(String maTaiKhoan) {
        // Câu lệnh SQL tìm nhân viên theo maTaiKhoan
        String sql = "SELECT * FROM nhan_vien WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số maTaiKhoan
                pstmt.setString(1, maTaiKhoan);
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Nếu có bản ghi thì ánh xạ và trả về NhanVien
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi tìm nhân viên theo tài khoản
            System.err.println("Lỗi NhanVienDAO.findByMaTaiKhoan(): " + ex.getMessage());
        }
        return null;
    }

    /**
     * Thêm thông tin một nhân viên mới vào CSDL.
     *
     * @param nv Đối tượng {@link NhanVien} chứa thông tin cần thêm.
     * @return {@code true} nếu thêm thành công, ngược lại {@code false}.
     */
    public boolean insert(NhanVien nv) {
        // Câu lệnh SQL insert nhân viên mới
        String sql = "INSERT INTO nhan_vien (maNhanVien, maTaiKhoan, hoTenNhanVien, soDienThoaiNhanVien, diaChi) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các giá trị tham số cho PreparedStatement
                pstmt.setString(1, nv.getMaNhanVien());
                pstmt.setString(2, nv.getMaTaiKhoan());
                pstmt.setString(3, nv.getHoTenNhanVien());
                pstmt.setString(4, nv.getSoDienThoaiNhanVien());
                pstmt.setString(5, nv.getDiaChi());
                // Thực thi insert và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi insert nhân viên
            System.err.println("Lỗi NhanVienDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin họ tên, số điện thoại và địa chỉ của nhân viên theo mã nhân viên.
     *
     * @param nv Đối tượng {@link NhanVien} chứa thông tin cập nhật mới.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(NhanVien nv) {
        // Câu lệnh SQL update thông tin nhân viên
        String sql = "UPDATE nhan_vien SET hoTenNhanVien = ?, soDienThoaiNhanVien = ?, diaChi = ? WHERE maNhanVien = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị các trường thay đổi
                pstmt.setString(1, nv.getHoTenNhanVien());
                pstmt.setString(2, nv.getSoDienThoaiNhanVien());
                pstmt.setString(3, nv.getDiaChi());
                // Điều kiện khoá chính maNhanVien
                pstmt.setString(4, nv.getMaNhanVien());
                // Thực thi update và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi update nhân viên
            System.err.println("Lỗi NhanVienDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một nhân viên khỏi CSDL theo mã nhân viên.
     *
     * @param maNhanVien Mã nhân viên cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maNhanVien) {
        // Câu lệnh SQL xóa nhân viên theo maNhanVien
        String sql = "DELETE FROM nhan_vien WHERE maNhanVien = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số maNhanVien
                pstmt.setString(1, maNhanVien);
                // Thực thi delete và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi delete nhân viên
            System.err.println("Lỗi NhanVienDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một dòng bản ghi trong {@link ResultSet} sang đối tượng {@link NhanVien}.
     *
     * @param rs Đối tượng {@link ResultSet} chứa kết quả truy vấn.
     * @return Đối tượng {@link NhanVien} đã qua ánh xạ.
     * @throws SQLException Nếu có lỗi khi trích xuất thông tin các cột.
     */
    private NhanVien mapResultSet(ResultSet rs) throws SQLException {
        // Đọc các cột và khởi tạo đối tượng NhanVien
        return new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("maTaiKhoan"),
                rs.getString("hoTenNhanVien"),
                rs.getString("soDienThoaiNhanVien"),
                rs.getString("diaChi")
        );
    }
}
