package DAO;

import Model.KhuVucSan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Khu vực sân bóng ({@link KhuVucSan}).
 * <p>
 * Đảm nhận các thao tác CRUD danh sách sân bóng (sân 5, sân 7, sân 11...)
 * trong bảng {@code san_bong} của cơ sở dữ liệu.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class KhuVucSanDAO {

    /**
     * Khởi tạo mặc định cho KhuVucSanDAO.
     */
    public KhuVucSanDAO() {
    }

    /**
     * Lấy toàn bộ danh sách các sân bóng trong CSDL, sắp xếp tăng dần theo mã sân.
     *
     * @return Danh sách {@link List} các đối tượng {@link KhuVucSan}.
     */
    public List<KhuVucSan> getAll() {
        List<KhuVucSan> list = new ArrayList<>();
        // Câu lệnh SQL lấy danh sách sân bóng từ bảng san_bong
        String sql = "SELECT * FROM san_bong ORDER BY maSan ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Đọc từng kết quả ResultSet và ánh xạ sang KhuVucSan
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi nếu truy vấn thất bại
            System.err.println("Lỗi KhuVucSanDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm một khu vực sân bóng mới vào CSDL.
     *
     * @param k Đối tượng {@link KhuVucSan} chứa thông tin sân mới.
     * @return {@code true} nếu chèn dữ liệu thành công, ngược lại {@code false}.
     */
    public boolean insert(KhuVucSan k) {
        // Lệnh SQL chèn thông tin sân bóng mới
        String sql = "INSERT INTO san_bong (maSan, tenSan, loaiSan, giaThueTheoGio, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Thiết lập các tham số cho lệnh INSERT
                pstmt.setString(1, k.getMaSan());
                pstmt.setString(2, k.getTenSan());
                pstmt.setString(3, k.getLoaiSan());
                pstmt.setDouble(4, k.getGiaThueTheoGio());
                pstmt.setString(5, k.getTrangThai());
                // Thực thi lệnh chèn và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi insert khu vực sân
            System.err.println("Lỗi KhuVucSanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin sân bóng (tên sân, loại sân, giá thuê, trạng thái) theo mã sân.
     *
     * @param k Đối tượng {@link KhuVucSan} chứa thông tin cập nhật mới.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(KhuVucSan k) {
        // Lệnh SQL cập nhật thông tin sân bóng theo mã sân
        String sql = "UPDATE san_bong SET tenSan = ?, loaiSan = ?, giaThueTheoGio = ?, trangThai = ? WHERE maSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Thiết lập thông tin mới cho sân bóng
                pstmt.setString(1, k.getTenSan());
                pstmt.setString(2, k.getLoaiSan());
                pstmt.setDouble(3, k.getGiaThueTheoGio());
                pstmt.setString(4, k.getTrangThai());
                // Điều kiện maSan cần cập nhật
                pstmt.setString(5, k.getMaSan());
                // Thực thi câu lệnh UPDATE
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi cập nhật sân bóng
            System.err.println("Lỗi KhuVucSanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một khu vực sân bóng khỏi CSDL theo mã sân.
     *
     * @param maSan Mã sân bóng cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maSan) {
        // Lệnh SQL xóa sân bóng theo maSan
        String sql = "DELETE FROM san_bong WHERE maSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán tham số maSan
                pstmt.setString(1, maSan);
                // Thực thi câu lệnh DELETE
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi xóa sân bóng
            System.err.println("Lỗi KhuVucSanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ kết quả truy vấn từ {@link ResultSet} sang đối tượng {@link KhuVucSan}.
     *
     * @param rs Đối tượng {@link ResultSet} chứa dòng bản ghi hiện tại.
     * @return Đối tượng {@link KhuVucSan} tương ứng.
     * @throws SQLException Nếu có lỗi xảy ra khi truy xuất dữ liệu từ các cột.
     */
    private KhuVucSan mapResultSet(ResultSet rs) throws SQLException {
        // Khởi tạo đối tượng KhuVucSan từ dòng dữ liệu ResultSet
        return new KhuVucSan(
                rs.getString("maSan"),
                rs.getString("tenSan"),
                rs.getString("loaiSan"),
                rs.getDouble("giaThueTheoGio"),
                rs.getString("trangThai")
        );
    }
}
