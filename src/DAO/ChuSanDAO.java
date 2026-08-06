package DAO;

import Model.ChuSan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Chủ sân ({@link ChuSan}).
 * <p>
 * Đảm nhận việc thực hiện các thao tác thêm, sửa, xóa, tìm kiếm thông tin
 * chủ sân bóng trong bảng {@code chu_san} của cơ sở dữ liệu.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class ChuSanDAO {

    /**
     * Khởi tạo mặc định cho ChuSanDAO.
     */
    public ChuSanDAO() {
    }

    /**
     * Lấy toàn bộ danh sách chủ sân từ CSDL, sắp xếp tăng dần theo mã chủ sân.
     *
     * @return Danh sách {@link List} các đối tượng {@link ChuSan}.
     */
    public List<ChuSan> getAll() {
        List<ChuSan> list = new ArrayList<>();
        // Truy vấn tất cả thông tin từ bảng chu_san, sắp xếp tăng dần theo maChuSan
        String sql = "SELECT * FROM chu_san ORDER BY maChuSan ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt qua ResultSet và ánh xạ từng dòng dữ liệu thành đối tượng ChuSan
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi nếu truy vấn thất bại
            System.err.println("Lỗi ChuSanDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Tìm kiếm thông tin chủ sân theo mã tài khoản liên kết.
     *
     * @param maTaiKhoan Mã tài khoản cần tìm.
     * @return Đối tượng {@link ChuSan} nếu tìm thấy, ngược lại trả về {@code null}.
     */
    public ChuSan findByMaTaiKhoan(String maTaiKhoan) {
        // Truy vấn thông tin chủ sân dựa vào mã tài khoản
        String sql = "SELECT * FROM chu_san WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Thiết lập giá trị tham số maTaiKhoan
                pstmt.setString(1, maTaiKhoan);
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Nếu tìm thấy bản ghi phù hợp thì ánh xạ và trả về
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi tìm chủ sân theo mã tài khoản
            System.err.println("Lỗi ChuSanDAO.findByMaTaiKhoan(): " + ex.getMessage());
        }
        return null;
    }

    /**
     * Thêm một thông tin chủ sân mới vào CSDL.
     *
     * @param cs Đối tượng {@link ChuSan} chứa thông tin cần thêm.
     * @return {@code true} nếu thêm thành công, ngược lại {@code false}.
     */
    public boolean insert(ChuSan cs) {
        // Câu lệnh INSERT bản ghi mới vào bảng chu_san
        String sql = "INSERT INTO chu_san (maChuSan, maTaiKhoan, tenChuSan, soDienThoaiChuSan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Thiết lập các tham số tương ứng với thuộc tính chủ sân
                pstmt.setString(1, cs.getMaChuSan());
                pstmt.setString(2, cs.getMaTaiKhoan());
                pstmt.setString(3, cs.getTenChuSan());
                pstmt.setString(4, cs.getSoDienThoaiChuSan());
                // Thực thi câu lệnh SQL INSERT
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi chèn bản ghi mới
            System.err.println("Lỗi ChuSanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật tên và số điện thoại chủ sân theo mã chủ sân.
     *
     * @param cs Đối tượng {@link ChuSan} chứa thông tin cập nhật mới.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(ChuSan cs) {
        // Câu lệnh UPDATE cập nhật tên và số điện thoại chủ sân
        String sql = "UPDATE chu_san SET tenChuSan = ?, soDienThoaiChuSan = ? WHERE maChuSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các thông tin thay đổi
                pstmt.setString(1, cs.getTenChuSan());
                pstmt.setString(2, cs.getSoDienThoaiChuSan());
                // Gán mã chủ sân làm điều kiện lọc
                pstmt.setString(3, cs.getMaChuSan());
                // Thực thi lệnh cập nhật dữ liệu
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi cập nhật chủ sân
            System.err.println("Lỗi ChuSanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một chủ sân khỏi hệ thống theo mã chủ sân.
     *
     * @param maChuSan Mã chủ sân cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maChuSan) {
        // Lệnh SQL xóa chủ sân theo maChuSan
        String sql = "DELETE FROM chu_san WHERE maChuSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị mã chủ sân
                pstmt.setString(1, maChuSan);
                // Thực thi lệnh xóa
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi xóa chủ sân
            System.err.println("Lỗi ChuSanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một bản ghi từ {@link ResultSet} sang đối tượng {@link ChuSan}.
     *
     * @param rs Kết quả truy vấn từ CSDL.
     * @return Đối tượng {@link ChuSan} tương ứng.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu cột.
     */
    private ChuSan mapResultSet(ResultSet rs) throws SQLException {
        // Khởi tạo đối tượng ChuSan từ các giá trị trong dòng ResultSet hiện tại
        return new ChuSan(
                rs.getString("maChuSan"),
                rs.getString("maTaiKhoan"),
                rs.getString("tenChuSan"),
                rs.getString("soDienThoaiChuSan")
        );
    }
}
