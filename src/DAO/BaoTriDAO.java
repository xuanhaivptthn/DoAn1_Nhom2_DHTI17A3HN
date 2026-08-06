package DAO;

import Model.BaoTri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Bảo trì ({@link BaoTri}).
 * <p>
 * Đảm nhận nhiệm vụ thực hiện các thao tác CRUD (Thêm, Sửa, Xóa, Truy vấn)
 * dữ liệu bảo trì sân bóng từ bảng {@code bao_tri} trong cơ sở dữ liệu.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class BaoTriDAO {

    /**
     * Khởi tạo mặc định cho BaoTriDAO.
     */
    public BaoTriDAO() {
    }

    /**
     * Lấy toàn bộ danh sách phiếu bảo trì từ CSDL, sắp xếp giảm dần theo ngày bắt đầu.
     *
     * @return Danh sách {@link List} các đối tượng {@link BaoTri}. Trả về danh sách rỗng nếu không có dữ liệu hoặc gặp lỗi.
     */
    public List<BaoTri> getAll() {
        List<BaoTri> list = new ArrayList<>();
        // Câu lệnh SQL truy vấn tất cả phiếu bảo trì, sắp xếp theo ngayBatDau giảm dần
        String sql = "SELECT * FROM bao_tri ORDER BY ngayBatDau DESC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt qua từng dòng kết quả và ánh xạ vào danh sách
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi truy vấn CSDL
            System.err.println("Lỗi BaoTriDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm một phiếu bảo trì mới vào bảng {@code bao_tri}.
     *
     * @param b Đối tượng {@link BaoTri} chứa thông tin cần thêm.
     * @return {@code true} nếu thêm thành công (ít nhất 1 bản ghi bị tác động), ngược lại {@code false}.
     */
    public boolean insert(BaoTri b) {
        // Câu lệnh SQL thêm phiếu bảo trì mới
        String sql = "INSERT INTO bao_tri (maPhieuBaoTri, maSan, noiDung, ngayBatDau, ngayKetThuc, trangThaiPhieu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các tham số cho PreparedStatement
                pstmt.setString(1, b.getMaPhieuBaoTri());
                pstmt.setString(2, b.getMaSan());
                pstmt.setString(3, b.getNoiDung());
                pstmt.setString(4, b.getNgayBatDau());
                pstmt.setString(5, b.getNgayKetThuc());
                pstmt.setString(6, b.getTrangThaiPhieu());
                // Thực thi câu lệnh INSERT và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi insert phiếu bảo trì
            System.err.println("Lỗi BaoTriDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin phiếu bảo trì đã tồn tại theo mã phiếu bảo trì.
     *
     * @param b Đối tượng {@link BaoTri} chứa thông tin cập nhật mới.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(BaoTri b) {
        // Câu lệnh SQL cập nhật thông tin phiếu bảo trì
        String sql = "UPDATE bao_tri SET maSan = ?, noiDung = ?, ngayBatDau = ?, ngayKetThuc = ?, trangThaiPhieu = ? WHERE maPhieuBaoTri = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các giá trị thay đổi
                pstmt.setString(1, b.getMaSan());
                pstmt.setString(2, b.getNoiDung());
                pstmt.setString(3, b.getNgayBatDau());
                pstmt.setString(4, b.getNgayKetThuc());
                pstmt.setString(5, b.getTrangThaiPhieu());
                // Điều kiện khoá chính maPhieuBaoTri
                pstmt.setString(6, b.getMaPhieuBaoTri());
                // Thực thi câu lệnh UPDATE và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi update phiếu bảo trì
            System.err.println("Lỗi BaoTriDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một phiếu bảo trì dựa trên mã phiếu bảo trì.
     *
     * @param maPhieuBaoTri Mã phiếu bảo trì cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maPhieuBaoTri) {
        // Câu lệnh SQL xóa phiếu bảo trì theo maPhieuBaoTri
        String sql = "DELETE FROM bao_tri WHERE maPhieuBaoTri = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán tham số khóa chính maPhieuBaoTri
                pstmt.setString(1, maPhieuBaoTri);
                // Thực thi câu lệnh DELETE và trả về kết quả
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi delete phiếu bảo trì
            System.err.println("Lỗi BaoTriDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một dòng dữ liệu từ {@link ResultSet} sang đối tượng model {@link BaoTri}.
     *
     * @param rs Đối tượng {@link ResultSet} chứa kết quả truy vấn.
     * @return Đối tượng {@link BaoTri} được khởi tạo từ dòng dữ liệu hiện tại.
     * @throws SQLException Nếu có lỗi khi đọc các cột từ ResultSet.
     */
    private BaoTri mapResultSet(ResultSet rs) throws SQLException {
        // Đọc từng trường dữ liệu từ ResultSet và khởi tạo đối tượng BaoTri
        BaoTri b = new BaoTri(
                rs.getString("maPhieuBaoTri"),
                rs.getString("maSan"),
                rs.getString("noiDung"),
                rs.getString("ngayBatDau"),
                rs.getString("ngayKetThuc"),
                rs.getString("trangThaiPhieu")
        );
        return b;
    }
}
