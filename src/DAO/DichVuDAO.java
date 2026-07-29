package DAO;

import Model.DichVu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO {

    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM DichVu ORDER BY id ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(DichVu d) {
        String sql = "INSERT INTO DichVu (maDichVu, tenDichVu, loaiDichVu, donGia, donVi, trangThai, soLuongTon, tonToiThieu, moTa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, d.getMaDichVu());
                pstmt.setString(2, d.getTenDichVu());
                pstmt.setString(3, d.getLoaiDichVu());
                pstmt.setDouble(4, d.getDonGia());
                pstmt.setString(5, d.getDonVi());
                pstmt.setString(6, d.getTrangThai());
                pstmt.setInt(7, d.getSoLuongTon());
                pstmt.setInt(8, d.getTonToiThieu());
                pstmt.setString(9, d.getMoTa());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            d.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(DichVu d) {
        String sql = "UPDATE DichVu SET maDichVu = ?, tenDichVu = ?, loaiDichVu = ?, donGia = ?, donVi = ?, trangThai = ?, soLuongTon = ?, tonToiThieu = ?, moTa = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getMaDichVu());
                pstmt.setString(2, d.getTenDichVu());
                pstmt.setString(3, d.getLoaiDichVu());
                pstmt.setDouble(4, d.getDonGia());
                pstmt.setString(5, d.getDonVi());
                pstmt.setString(6, d.getTrangThai());
                pstmt.setInt(7, d.getSoLuongTon());
                pstmt.setInt(8, d.getTonToiThieu());
                pstmt.setString(9, d.getMoTa());
                pstmt.setInt(10, d.getId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM DichVu WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private DichVu mapResultSet(ResultSet rs) throws SQLException {
        return new DichVu(
                rs.getInt("id"),
                rs.getString("tenDichVu"),
                rs.getString("moTa"),
                rs.getDouble("donGia"),
                rs.getString("donVi"),
                rs.getString("trangThai"),
                rs.getInt("soLuongTon"),
                rs.getInt("tonToiThieu")
        );
    }
}
