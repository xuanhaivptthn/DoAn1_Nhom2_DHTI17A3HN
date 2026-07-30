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
        String sql = "SELECT * FROM san_bong ORDER BY maSan ASC";
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
        String sql = "INSERT INTO san_bong (maSan, maChuSan, tenSan, loaiSan, giaThueTheoGio, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, k.getMaSan());
                pstmt.setString(2, k.getMaChuSan());
                pstmt.setString(3, k.getTenSan());
                pstmt.setString(4, k.getLoaiSan());
                pstmt.setDouble(5, k.getGiaThueTheoGio());
                pstmt.setString(6, k.getTrangThai());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(KhuVucSan k) {
        String sql = "UPDATE san_bong SET maChuSan = ?, tenSan = ?, loaiSan = ?, giaThueTheoGio = ?, trangThai = ? WHERE maSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, k.getMaChuSan());
                pstmt.setString(2, k.getTenSan());
                pstmt.setString(3, k.getLoaiSan());
                pstmt.setDouble(4, k.getGiaThueTheoGio());
                pstmt.setString(5, k.getTrangThai());
                pstmt.setString(6, k.getMaSan());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maSan) {
        String sql = "DELETE FROM san_bong WHERE maSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maSan);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi KhuVucSanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private KhuVucSan mapResultSet(ResultSet rs) throws SQLException {
        return new KhuVucSan(
                rs.getString("maSan"),
                rs.getString("maChuSan"),
                rs.getString("tenSan"),
                rs.getString("loaiSan"),
                rs.getDouble("giaThueTheoGio"),
                rs.getString("trangThai")
        );
    }
}
