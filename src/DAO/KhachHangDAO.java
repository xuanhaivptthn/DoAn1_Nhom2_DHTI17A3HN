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
        String sql = "SELECT * FROM KhachHang ORDER BY id ASC";
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
        String sql = "INSERT INTO KhachHang (hoTen, soDienThoai, email, ghiChu, soLanDat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, kh.getHoTen());
                pstmt.setString(2, kh.getSoDienThoai());
                pstmt.setString(3, kh.getEmail());
                pstmt.setString(4, kh.getGhiChu());
                pstmt.setInt(5, kh.getSoLanDatSan());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            kh.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhachHangDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTen = ?, soDienThoai = ?, email = ?, ghiChu = ?, soLanDat = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, kh.getHoTen());
                pstmt.setString(2, kh.getSoDienThoai());
                pstmt.setString(3, kh.getEmail());
                pstmt.setString(4, kh.getGhiChu());
                pstmt.setInt(5, kh.getSoLanDatSan());
                pstmt.setInt(6, kh.getId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhachHangDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        return new KhachHang(
                rs.getInt("id"),
                rs.getString("hoTen"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getString("ghiChu"),
                rs.getInt("soLanDat")
        );
    }
}
