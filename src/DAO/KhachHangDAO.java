package DAO;

import Model.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY maKhachHang ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhachHangDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO khach_hang (maKhachHang, tenKhachHang, soDienThoai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, kh.getMaKhachHang());
                pstmt.setString(2, kh.getTenKhachHang());
                pstmt.setString(3, kh.getSoDienThoai());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhachHangDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE khach_hang SET tenKhachHang = ?, soDienThoai = ? WHERE maKhachHang = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, kh.getTenKhachHang());
                pstmt.setString(2, kh.getSoDienThoai());
                pstmt.setString(3, kh.getMaKhachHang());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhachHangDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maKhachHang) {
        String sql = "DELETE FROM khach_hang WHERE maKhachHang = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maKhachHang);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhachHangDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        return new KhachHang(
                rs.getString("maKhachHang"),
                rs.getString("tenKhachHang"),
                rs.getString("soDienThoai")
        );
    }
}
