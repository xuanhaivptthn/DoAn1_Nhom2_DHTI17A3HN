package DAO;

import Model.HoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    private static boolean columnChecked = false;

    private static synchronized void ensureColumnExists(Connection conn) {
        if (columnChecked || conn == null) return;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE hoa_don ADD COLUMN dichVuKem TEXT DEFAULT NULL");
        } catch (SQLException ignored) {}
        columnChecked = true;
    }

    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoa_don ORDER BY maHoaDon DESC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            ensureColumnExists(conn);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi HoaDonDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(HoaDon h) {
        String sql = "INSERT INTO hoa_don (maHoaDon, maLichDat, maNhanVien, ngayThanhToan, chiPhiSan, tongTienDichVu, tongTienKho, giamGia, tongTien, phuongThucThanhToan, dichVuKem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            ensureColumnExists(conn);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi HoaDonDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(HoaDon h) {
        String sql = "UPDATE hoa_don SET maLichDat = ?, maNhanVien = ?, ngayThanhToan = ?, chiPhiSan = ?, tongTienDichVu = ?, tongTienKho = ?, giamGia = ?, tongTien = ?, phuongThucThanhToan = ?, dichVuKem = ? WHERE maHoaDon = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            ensureColumnExists(conn);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                pstmt.setString(11, h.getMaHoaDon());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi HoaDonDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maHoaDon) {
        String sql = "DELETE FROM hoa_don WHERE maHoaDon = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maHoaDon);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi HoaDonDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
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
        h.setTongTienKho(rs.getDouble("tongTienKho"));
        try {
            h.setDichVuKem(rs.getString("dichVuKem"));
        } catch (SQLException ignored) {}
        return h;
    }
}
