package DAO;

import Model.Kho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Kho hàng hóa ({@link Kho}).
 * <p>
 * Thực hiện các thao tác thêm, sửa, xóa, tìm kiếm danh mục hàng hóa, dụng cụ, thiết bị
 * tồn kho trong bảng {@code kho} của cơ sở dữ liệu.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class KhoDAO {

    /**
     * Khởi tạo mặc định cho KhoDAO.
     */
    public KhoDAO() {
    }

    /**
     * Lấy toàn bộ danh sách hàng hóa trong kho, sắp xếp tăng dần theo mã hàng hóa.
     *
     * @return Danh sách {@link List} các đối tượng {@link Kho}. Trả về danh sách rỗng nếu gặp lỗi.
     */
    public List<Kho> getAll() {
        List<Kho> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả bản ghi từ bảng kho
        String sql = "SELECT * FROM kho ORDER BY maHangHoa ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt qua dữ liệu trả về và thêm vào danh sách
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi lấy danh sách hàng tồn kho
            System.err.println("Lỗi KhoDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm một hàng hóa mới vào kho.
     *
     * @param k Đối tượng {@link Kho} chứa thông tin hàng hóa mới.
     * @return {@code true} nếu chèn dữ liệu thành công, ngược lại {@code false}.
     */
    public boolean insert(Kho k) {
        // Lệnh SQL chèn hàng hóa mới vào bảng kho
        String sql = "INSERT INTO kho (maHangHoa, tenHangHoa, soLuongTon, donGia, nhaCungCap) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Thiết lập các tham số tương ứng với thông tin hàng hóa
                pstmt.setString(1, k.getMaHangHoa());
                pstmt.setString(2, k.getTenHangHoa());
                pstmt.setInt(3, k.getSoLuongTon());
                pstmt.setDouble(4, k.getDonGia());
                pstmt.setString(5, k.getNhaCungCap());
                // Thực thi lệnh SQL INSERT
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi nếu chèn dữ liệu thất bại
            System.err.println("Lỗi KhoDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin hàng hóa trong kho theo mã hàng hóa.
     *
     * @param k Đối tượng {@link Kho} chứa thông tin sửa đổi.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(Kho k) {
        // Lệnh SQL cập nhật thông tin tên, số lượng tồn, đơn giá, nhà cung cấp
        String sql = "UPDATE kho SET tenHangHoa = ?, soLuongTon = ?, donGia = ?, nhaCungCap = ? WHERE maHangHoa = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số cần thay đổi
                pstmt.setString(1, k.getTenHangHoa());
                pstmt.setInt(2, k.getSoLuongTon());
                pstmt.setDouble(3, k.getDonGia());
                pstmt.setString(4, k.getNhaCungCap());
                // Điều kiện maHangHoa làm khóa chính
                pstmt.setString(5, k.getMaHangHoa());
                // Thực thi lệnh SQL UPDATE
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi nếu cập nhật thất bại
            System.err.println("Lỗi KhoDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một mặt hàng khỏi kho theo mã hàng hóa.
     *
     * @param maHangHoa Mã hàng hóa cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maHangHoa) {
        // Lệnh SQL xóa hàng hóa theo maHangHoa
        String sql = "DELETE FROM kho WHERE maHangHoa = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị mã hàng hóa cần xóa
                pstmt.setString(1, maHangHoa);
                // Thực thi câu lệnh SQL DELETE
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi nếu xóa hàng hóa thất bại
            System.err.println("Lỗi KhoDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một dòng dữ liệu từ {@link ResultSet} sang đối tượng {@link Kho}.
     *
     * @param rs Đối tượng kết quả truy vấn.
     * @return Đối tượng {@link Kho} khởi tạo từ dòng dữ liệu.
     * @throws SQLException Nếu có lỗi khi trích xuất cột dữ liệu.
     */
    private Kho mapResultSet(ResultSet rs) throws SQLException {
        // Trích xuất các thuộc tính maHangHoa, tenHangHoa, soLuongTon, donGia, nhaCungCap
        return new Kho(
                rs.getString("maHangHoa"),
                rs.getString("tenHangHoa"),
                rs.getInt("soLuongTon"),
                rs.getDouble("donGia"),
                rs.getString("nhaCungCap")
        );
    }
}
