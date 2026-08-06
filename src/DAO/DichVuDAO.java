package DAO;

import Model.DichVu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Dịch vụ ({@link DichVu}).
 * <p>
 * Quản lý danh mục các dịch vụ phụ trợ tại sân bóng (nước uống, cho thuê đồ, đồ ăn...)
 * thông qua bảng {@code dich_vu} trong CSDL.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class DichVuDAO {

    /**
     * Khởi tạo mặc định cho DichVuDAO.
     */
    public DichVuDAO() {
    }

    /**
     * Lấy toàn bộ danh sách dịch vụ hiện có trong hệ thống, sắp xếp theo mã dịch vụ.
     *
     * @return Danh sách {@link List} các đối tượng {@link DichVu}.
     */
    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả dịch vụ, sắp xếp tăng dần theo maDichVu
        String sql = "SELECT * FROM dich_vu ORDER BY maDichVu ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt ResultSet và ánh xạ sang đối tượng DichVu
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi truy vấn danh sách dịch vụ
            System.err.println("Lỗi DichVuDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm một dịch vụ mới vào CSDL.
     *
     * @param d Đối tượng {@link DichVu} chứa thông tin dịch vụ cần thêm.
     * @return {@code true} nếu thêm dịch vụ thành công, ngược lại {@code false}.
     */
    public boolean insert(DichVu d) {
        // Câu lệnh SQL insert dịch vụ mới
        String sql = "INSERT INTO dich_vu (maDichVu, tenDichVu, loaiDichVu, gia, moTa, soLuongTon, donVi) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị các tham số cho PreparedStatement
                pstmt.setString(1, d.getMaDichVu());
                pstmt.setString(2, d.getTenDichVu());
                pstmt.setString(3, d.getLoaiDichVu());
                pstmt.setDouble(4, d.getGia());
                pstmt.setString(5, d.getMoTa());
                pstmt.setInt(6, d.getSoLuongTon());
                pstmt.setString(7, d.getDonVi());
                // Thực thi insert và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi insert dịch vụ
            System.err.println("Lỗi DichVuDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin một dịch vụ theo mã dịch vụ.
     *
     * @param d Đối tượng {@link DichVu} chứa thông tin cập nhật mới.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(DichVu d) {
        // Câu lệnh SQL update thông tin dịch vụ
        String sql = "UPDATE dich_vu SET tenDichVu = ?, loaiDichVu = ?, gia = ?, moTa = ?, soLuongTon = ?, donVi = ? WHERE maDichVu = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị thay đổi
                pstmt.setString(1, d.getTenDichVu());
                pstmt.setString(2, d.getLoaiDichVu());
                pstmt.setDouble(3, d.getGia());
                pstmt.setString(4, d.getMoTa());
                pstmt.setInt(5, d.getSoLuongTon());
                pstmt.setString(6, d.getDonVi());
                // Điều kiện khoá chính maDichVu
                pstmt.setString(7, d.getMaDichVu());
                // Thực thi update và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi update dịch vụ
            System.err.println("Lỗi DichVuDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một dịch vụ khỏi CSDL theo mã dịch vụ.
     *
     * @param maDichVu Mã dịch vụ cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maDichVu) {
        // Câu lệnh SQL xóa dịch vụ theo maDichVu
        String sql = "DELETE FROM dich_vu WHERE maDichVu = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số maDichVu
                pstmt.setString(1, maDichVu);
                // Thực thi delete và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi delete dịch vụ
            System.err.println("Lỗi DichVuDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một dòng dữ liệu trong {@link ResultSet} thành đối tượng {@link DichVu}.
     *
     * @param rs Đối tượng kết quả {@link ResultSet} thu được sau câu SQL.
     * @return Đối tượng {@link DichVu} với các trường tương ứng.
     * @throws SQLException Nếu có lỗi khi trích xuất giá trị cột.
     */
    private DichVu mapResultSet(ResultSet rs) throws SQLException {
        // Khởi tạo đối tượng DichVu từ dữ liệu ResultSet
        DichVu d = new DichVu(
                rs.getString("maDichVu"),
                rs.getString("tenDichVu"),
                rs.getString("loaiDichVu"),
                rs.getDouble("gia"),
                rs.getString("moTa")
        );
        // Gán thêm số lượng tồn và đơn vị tính
        d.setSoLuongTon(rs.getInt("soLuongTon"));
        d.setDonVi(rs.getString("donVi"));
        return d;
    }
}
