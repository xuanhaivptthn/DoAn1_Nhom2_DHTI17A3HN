package DAO;

import Model.BaoTri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaoTriDAO {

    public List<BaoTri> getAll() {
        List<BaoTri> list = new ArrayList<>();
        String sql = "SELECT * FROM BaoTri ORDER BY id DESC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(BaoTri b) {
        String sql = "INSERT INTO BaoTri (maBaoTri, khuVucId, tenSan, noiDung, nguoiPhuTrach, ngayBatDau, ngayKetThuc, chiPhi, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, b.getMaBaoTri());
                pstmt.setInt(2, b.getKhuVucId());
                pstmt.setString(3, b.getTenSan());
                pstmt.setString(4, b.getNoiDung());
                pstmt.setString(5, b.getNguoiPhuTrach());
                pstmt.setString(6, b.getNgayBatDau());
                pstmt.setString(7, b.getNgayKetThuc());
                pstmt.setDouble(8, b.getChiPhi());
                pstmt.setString(9, b.getTrangThai());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            b.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(BaoTri b) {
        String sql = "UPDATE BaoTri SET maBaoTri = ?, khuVucId = ?, tenSan = ?, noiDung = ?, nguoiPhuTrach = ?, ngayBatDau = ?, ngayKetThuc = ?, chiPhi = ?, trangThai = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, b.getMaBaoTri());
                pstmt.setInt(2, b.getKhuVucId());
                pstmt.setString(3, b.getTenSan());
                pstmt.setString(4, b.getNoiDung());
                pstmt.setString(5, b.getNguoiPhuTrach());
                pstmt.setString(6, b.getNgayBatDau());
                pstmt.setString(7, b.getNgayKetThuc());
                pstmt.setDouble(8, b.getChiPhi());
                pstmt.setString(9, b.getTrangThai());
                pstmt.setInt(10, b.getId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM BaoTri WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private BaoTri mapResultSet(ResultSet rs) throws SQLException {
        return new BaoTri(
                rs.getInt("id"),
                rs.getString("maBaoTri"),
                rs.getInt("khuVucId"),
                rs.getString("tenSan"),
                rs.getString("noiDung"),
                rs.getString("nguoiPhuTrach"),
                rs.getString("ngayBatDau"),
                rs.getString("ngayKetThuc"),
                rs.getDouble("chiPhi"),
                rs.getString("trangThai")
        );
    }
}
