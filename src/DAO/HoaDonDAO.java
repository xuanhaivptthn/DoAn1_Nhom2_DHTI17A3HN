package DAO;

import Model.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu (DAO) cho đối tượng Hóa đơn ({@link HoaDon}).
 * <p>
 * Đảm nhận việc lưu trữ, cập nhật, truy vấn và xóa dữ liệu hóa đơn thanh toán
 * từ bảng {@code hoa_don} trong cơ sở dữ liệu.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class HoaDonDAO {

    /** Đánh dấu xem cột dichVuKem đã được kiểm tra/thêm vào bảng hoa_don hay chưa */
    private static boolean columnChecked = false;

    /**
     * Khởi tạo mặc định cho HoaDonDAO.
     */
    public HoaDonDAO() {
    }

    /**
     * Kiểm tra và tự động thêm cột {@code dichVuKem} vào bảng {@code hoa_don} nếu chưa tồn tại.
     *
     * @param conn Đối tượng kết nối {@link Connection} đến CSDL.
     */
    private static synchronized void ensureColumnExists(Connection conn) {
        // Nếu đã kiểm tra hoặc connection null thì bỏ qua
        if (columnChecked || conn == null) return;
        try (Statement stmt = conn.createStatement()) {
            // Thêm cột dichVuKem dạng TEXT nếu bảng chưa có cột này
            stmt.executeUpdate("ALTER TABLE hoa_don ADD COLUMN dichVuKem TEXT DEFAULT NULL");
        } catch (SQLException ignored) {
            // Bỏ qua ngoại lệ nếu cột đã tồn tại trong CSDL
        }
        // Đánh dấu đã thực hiện kiểm tra cột
        columnChecked = true;
    }

    /**
     * Lấy toàn bộ danh sách hóa đơn từ CSDL, sắp xếp giảm dần theo mã hóa đơn.
     *
     * @return Danh sách {@link List} các đối tượng {@link HoaDon}.
     */
    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả hóa đơn, giảm dần theo mã hóa đơn
        String sql = "SELECT * FROM hoa_don ORDER BY maHoaDon DESC";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return list;
            // Đảm bảo cấu trúc cột dichVuKem trong DB
            ensureColumnExists(conn);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Duyệt qua từng dòng bản ghi và chuyển đổi thành đối tượng HoaDon
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            // Ghi nhận thông báo lỗi nếu truy vấn thất bại
            System.err.println("Lỗi HoaDonDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    /**
     * Thêm một hóa đơn mới vào bảng {@code hoa_don}.
     *
     * @param h Đối tượng {@link HoaDon} chứa thông tin cần chèn.
     * @return {@code true} nếu thêm thành công, ngược lại {@code false}.
     */
    public boolean insert(HoaDon h) {
        // Lệnh SQL chèn thông tin hóa đơn mới bao gồm cả chi phí sân, dịch vụ, kho, giảm giá, v.v.
        String sql = "INSERT INTO hoa_don (maHoaDon, maLichDat, maNhanVien, ngayThanhToan, chiPhiSan, tongTienDichVu, tongTienKho, giamGia, tongTien, phuongThucThanhToan, dichVuKem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            // Đảm bảo cấu trúc cột dichVuKem tồn tại
            ensureColumnExists(conn);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Thiết lập các tham số cho lệnh INSERT
                pstmt.setString(1, h.getMaHoaDon());
                pstmt.setString(2, h.getMaLichDat());
                pstmt.setString(3, h.getMaNhanVien());
                pstmt.setString(4, h.getNgayThanhToan());
                pstmt.setDouble(5, h.getChiPhiSan());
                pstmt.setDouble(6, h.getTongTienDichVu());
                pstmt.setDouble(7, h.getTongTienKho());
                pstmt.setDouble(8, h.getGiamGia());
                pstmt.setDouble(9, h.getTongTien());
                pstmt.setString(10, h.getPhuongThucThanhToan());
                pstmt.setString(11, h.getDichVuKem());
                // Thực thi câu lệnh SQL INSERT
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi chèn hóa đơn
            System.err.println("Lỗi HoaDonDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin một hóa đơn theo mã hóa đơn.
     *
     * @param h Đối tượng {@link HoaDon} chứa dữ liệu cập nhật.
     * @return {@code true} nếu cập nhật thành công, ngược lại {@code false}.
     */
    public boolean update(HoaDon h) {
        // Lệnh SQL cập nhật chi tiết hóa đơn
        String sql = "UPDATE hoa_don SET maLichDat = ?, maNhanVien = ?, ngayThanhToan = ?, chiPhiSan = ?, tongTienDichVu = ?, tongTienKho = ?, giamGia = ?, tongTien = ?, phuongThucThanhToan = ?, dichVuKem = ? WHERE maHoaDon = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            // Đảm bảo cấu trúc cột dichVuKem
            ensureColumnExists(conn);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán thông tin sửa đổi
                pstmt.setString(1, h.getMaLichDat());
                pstmt.setString(2, h.getMaNhanVien());
                pstmt.setString(3, h.getNgayThanhToan());
                pstmt.setDouble(4, h.getChiPhiSan());
                pstmt.setDouble(5, h.getTongTienDichVu());
                pstmt.setDouble(6, h.getTongTienKho());
                pstmt.setDouble(7, h.getGiamGia());
                pstmt.setDouble(8, h.getTongTien());
                pstmt.setString(9, h.getPhuongThucThanhToan());
                pstmt.setString(10, h.getDichVuKem());
                // Điều kiện maHoaDon
                pstmt.setString(11, h.getMaHoaDon());
                // Thực thi câu lệnh UPDATE
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi cập nhật hóa đơn
            System.err.println("Lỗi HoaDonDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Xóa một hóa đơn khỏi CSDL theo mã hóa đơn.
     *
     * @param maHoaDon Mã hóa đơn cần xóa.
     * @return {@code true} nếu xóa thành công, ngược lại {@code false}.
     */
    public boolean delete(String maHoaDon) {
        // Lệnh SQL xóa hóa đơn theo maHoaDon
        String sql = "DELETE FROM hoa_don WHERE maHoaDon = ?";
        try (Connection conn = DBConnect.getConnection()) {
            // Kiểm tra kết nối CSDL
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Gán tham số maHoaDon cần xóa
                pstmt.setString(1, maHoaDon);
                // Thực thi lệnh xóa
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            // Ghi nhận lỗi khi xóa hóa đơn
            System.err.println("Lỗi HoaDonDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    /**
     * Ánh xạ kết quả truy vấn từ {@link ResultSet} sang đối tượng {@link HoaDon}.
     *
     * @param rs Đối tượng {@link ResultSet} thu được sau truy vấn.
     * @return Đối tượng {@link HoaDon} tương ứng.
     * @throws SQLException Nếu có lỗi khi truy xuất giá trị trong ResultSet.
     */
    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
        // Tạo mới đối tượng HoaDon từ thông tin đọc từ ResultSet
        HoaDon h = new HoaDon(
                rs.getString("maHoaDon"),
                rs.getString("maLichDat"),
                rs.getString("maNhanVien"),
                rs.getString("ngayThanhToan"),
                rs.getDouble("chiPhiSan"),
                rs.getDouble("tongTienDichVu"),
                rs.getDouble("giamGia"),
                rs.getDouble("tongTien"),
                rs.getString("phuongThucThanhToan")
        );
        // Gán thuộc tính tổng tiền kho
        h.setTongTienKho(rs.getDouble("tongTienKho"));
        try {
            // Đọc trường dịch vụ kèm theo nếu cột dichVuKem có trong ResultSet
            h.setDichVuKem(rs.getString("dichVuKem"));
        } catch (SQLException ignored) {
            // Bỏ qua nếu cột dichVuKem chưa tồn tại trong bản ghi này
        }
        return h;
    }
}
