package DAO;

import Model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Tài khoản người dùng ({@link TaiKhoan}).
 * <p>
 * Phụ trách quản lý thông tin đăng nhập, phân quyền (chủ sân, nhân viên, v.v.) và trạng thái
 * hoạt động của các tài khoản trong bảng {@code tai_khoan}.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class TaiKhoanDAO {

    /**
     * Khởi tạo mặc định cho TaiKhoanDAO.
     */
    public TaiKhoanDAO() {
    }

    /**
     * Lấy toàn bộ danh sách tài khoản trong hệ thống, sắp xếp tăng dần theo mã tài khoản.
     *
     * @return Danh sách {@link List} các đối tượng {@link TaiKhoan}.
     */
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        // Câu lệnh SQL truy vấn tất cả tài khoản
        String sql = "SELECT * FROM tai_khoan ORDER BY maTaiKhoan ASC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt ResultSet và ánh xạ sang đối tượng TaiKhoan
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi lấy tất cả tài khoản
            System.err.println("Lỗi TaiKhoanDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Kiểm tra thông tin đăng nhập bằng tên đăng nhập và mật khẩu.
     *
     * @param username Tên đăng nhập của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Đối tượng {@link TaiKhoan} nếu khớp thông tin, ngược lại trả về {@code null}.
     */
    public TaiKhoan findByUsernameAndPassword(String username, String password) {
        // Câu lệnh SQL tìm tài khoản theo tenDangNhap và matKhau
        String sql = "SELECT * FROM tai_khoan WHERE tenDangNhap = ? AND matKhau = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán thông tin tenDangNhap và matKhau vào câu lệnh
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Nếu tìm thấy tài khoản hợp lệ
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi xác thực đăng nhập
            System.err.println("Lỗi TaiKhoanDAO.findByUsernameAndPassword(): " + ex.getMessage());
        }
        return null;
    }

    /**
     * Tìm kiếm thông tin tài khoản theo tên đăng nhập.
     *
     * @param username Tên đăng nhập cần kiểm tra.
     * @return Đối tượng {@link TaiKhoan} nếu tồn tại, ngược lại trả về {@code null}.
     */
    public TaiKhoan findByUsername(String username) {
        // Câu lệnh SQL tìm tài khoản theo tenDangNhap
        String sql = "SELECT * FROM tai_khoan WHERE tenDangNhap = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị tham số username
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Nếu tìm thấy bản ghi phù hợp
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi tìm tài khoản theo username
            System.err.println("Lỗi TaiKhoanDAO.findByUsername(): " + ex.getMessage());
        }
        return null;
    }

    /**
     * Thêm mới một tài khoản vào hệ thống.
     *
     * @param tk Đối tượng {@link TaiKhoan} chứa thông tin tài khoản mới.
     * @return {@code true} nếu thêm thành công, ngược lại {@code false}.
     */
    public boolean insert(TaiKhoan tk) {
        // Câu lệnh SQL chèn tài khoản mới vào bảng tai_khoan
        String sql = "INSERT INTO tai_khoan (maTaiKhoan, tenDangNhap, matKhau, quyenHan, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán các tham số tương ứng từ TaiKhoan
                pstmt.setString(1, tk.getMaTaiKhoan());
                pstmt.setString(2, tk.getTenDangNhap());
                pstmt.setString(3, tk.getMatKhau());
                pstmt.setString(4, tk.getQuyenHan());
                pstmt.setString(5, tk.getTrangThai());
                // Thực thi lệnh insert
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi insert tài khoản
            System.err.println("Lỗi TaiKhoanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin tên đăng nhập, mật khẩu, quyền hạn và trạng thái của tài khoản.
     *
     * @param tk Đối tượng {@link TaiKhoan} chứa thông tin sửa đổi.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(TaiKhoan tk) {
        // Câu lệnh SQL cập nhật thông tin tài khoản theo maTaiKhoan
        String sql = "UPDATE tai_khoan SET tenDangNhap = ?, matKhau = ?, quyenHan = ?, trangThai = ? WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị các cột thay đổi
                pstmt.setString(1, tk.getTenDangNhap());
                pstmt.setString(2, tk.getMatKhau());
                pstmt.setString(3, tk.getQuyenHan());
                pstmt.setString(4, tk.getTrangThai());
                // Điều kiện khoá chính maTaiKhoan
                pstmt.setString(5, tk.getMaTaiKhoan());
                // Thực thi lệnh update
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi update tài khoản
            System.err.println("Lỗi TaiKhoanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một tài khoản khỏi CSDL theo mã tài khoản.
     *
     * @param maTaiKhoan Mã tài khoản cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maTaiKhoan) {
        // Câu lệnh SQL xóa tài khoản theo maTaiKhoan
        String sql = "DELETE FROM tai_khoan WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán giá trị khoá chính maTaiKhoan
                pstmt.setString(1, maTaiKhoan);
                // Thực thi lệnh delete
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // In thông báo lỗi khi delete tài khoản
            System.err.println("Lỗi TaiKhoanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ một bản ghi từ {@link ResultSet} sang đối tượng {@link TaiKhoan}.
     *
     * @param rs Kết quả trả về từ câu truy vấn SQL.
     * @return Đối tượng {@link TaiKhoan} đã bọc dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi đọc giá trị cột trong ResultSet.
     */
    private TaiKhoan mapResultSet(ResultSet rs) throws SQLException {
        // Khởi tạo và trả về đối tượng TaiKhoan
        return new TaiKhoan(
                rs.getString("maTaiKhoan"),
                rs.getString("tenDangNhap"),
                rs.getString("matKhau"),
                rs.getString("quyenHan"),
                rs.getString("trangThai")
        );
    }
}
