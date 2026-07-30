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
        String sql = "SELECT * FROM bao_tri ORDER BY ngayBatDau DESC";
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
        String sql = "INSERT INTO bao_tri (maPhieuBaoTri, maSan, noiDung, ngayBatDau, ngayKetThuc, trangThaiPhieu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, b.getMaPhieuBaoTri());
                pstmt.setString(2, b.getMaSan());
                pstmt.setString(3, b.getNoiDung());
                pstmt.setString(4, b.getNgayBatDau());
                pstmt.setString(5, b.getNgayKetThuc());
                pstmt.setString(6, b.getTrangThaiPhieu());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(BaoTri b) {
        String sql = "UPDATE bao_tri SET maSan = ?, noiDung = ?, ngayBatDau = ?, ngayKetThuc = ?, trangThaiPhieu = ? WHERE maPhieuBaoTri = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, b.getMaSan());
                pstmt.setString(2, b.getNoiDung());
                pstmt.setString(3, b.getNgayBatDau());
                pstmt.setString(4, b.getNgayKetThuc());
                pstmt.setString(5, b.getTrangThaiPhieu());
                pstmt.setString(6, b.getMaPhieuBaoTri());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maPhieuBaoTri) {
        String sql = "DELETE FROM bao_tri WHERE maPhieuBaoTri = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maPhieuBaoTri);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi BaoTriDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private BaoTri mapResultSet(ResultSet rs) throws SQLException {
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
