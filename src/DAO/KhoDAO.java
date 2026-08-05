package DAO;

import Model.Kho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KhoDAO {

    public List<Kho> getAll() {
        List<Kho> list = new ArrayList<>();
        String sql = "SELECT * FROM kho ORDER BY maHangHoa ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhoDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(Kho k) {
        String sql = "INSERT INTO kho (maHangHoa, tenHangHoa, soLuongTon, donGia, nhaCungCap) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, k.getMaHangHoa());
                pstmt.setString(2, k.getTenHangHoa());
                pstmt.setInt(3, k.getSoLuongTon());
                pstmt.setDouble(4, k.getDonGia());
                pstmt.setString(5, k.getNhaCungCap());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhoDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(Kho k) {
        String sql = "UPDATE kho SET tenHangHoa = ?, soLuongTon = ?, donGia = ?, nhaCungCap = ? WHERE maHangHoa = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, k.getTenHangHoa());
                pstmt.setInt(2, k.getSoLuongTon());
                pstmt.setDouble(3, k.getDonGia());
                pstmt.setString(4, k.getNhaCungCap());
                pstmt.setString(5, k.getMaHangHoa());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhoDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maHangHoa) {
        String sql = "DELETE FROM kho WHERE maHangHoa = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maHangHoa);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhoDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private Kho mapResultSet(ResultSet rs) throws SQLException {
        return new Kho(
                rs.getString("maHangHoa"),
                rs.getString("tenHangHoa"),
                rs.getInt("soLuongTon"),
                rs.getDouble("donGia"),
                rs.getString("nhaCungCap")
        );
    }
}
