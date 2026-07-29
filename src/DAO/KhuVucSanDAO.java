package DAO;

import Model.KhuVucSan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KhuVucSanDAO {

    public List<KhuVucSan> getAll() {
        List<KhuVucSan> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuVucSan ORDER BY id ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(KhuVucSan k) {
        String sql = "INSERT INTO KhuVucSan (maSan, tenSan, loaiSan, giaTheoGio, moTa, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, k.getMaSan());
                pstmt.setString(2, k.getTenSan());
                pstmt.setString(3, k.getLoaiSan());
                pstmt.setDouble(4, k.getGiaTheoGio());
                pstmt.setString(5, k.getMoTa());
                pstmt.setString(6, k.getTrangThai());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            k.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(KhuVucSan k) {
        String sql = "UPDATE KhuVucSan SET maSan = ?, tenSan = ?, loaiSan = ?, giaTheoGio = ?, moTa = ?, trangThai = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, k.getMaSan());
                pstmt.setString(2, k.getTenSan());
                pstmt.setString(3, k.getLoaiSan());
                pstmt.setDouble(4, k.getGiaTheoGio());
                pstmt.setString(5, k.getMoTa());
                pstmt.setString(6, k.getTrangThai());
                pstmt.setInt(7, k.getId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM KhuVucSan WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private KhuVucSan mapResultSet(ResultSet rs) throws SQLException {
        return new KhuVucSan(
                rs.getInt("id"),
                rs.getString("maSan"),
                rs.getString("tenSan"),
                rs.getString("loaiSan"),
                rs.getDouble("giaTheoGio"),
                rs.getString("moTa"),
                rs.getString("trangThai")
        );
    }
}
