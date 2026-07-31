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
        String sql = "SELECT * FROM dich_vu ORDER BY maDichVu ASC";
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
        String sql = "INSERT INTO dich_vu (maDichVu, tenDichVu, loaiDichVu, gia, moTa, soLuongTon, donVi) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getMaDichVu());
                pstmt.setString(2, d.getTenDichVu());
                pstmt.setString(3, d.getLoaiDichVu());
                pstmt.setDouble(4, d.getGia());
                pstmt.setString(5, d.getMoTa());
                pstmt.setInt(6, d.getSoLuongTon());
                pstmt.setString(7, d.getDonVi());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(DichVu d) {
        String sql = "UPDATE dich_vu SET tenDichVu = ?, loaiDichVu = ?, gia = ?, moTa = ?, soLuongTon = ?, donVi = ? WHERE maDichVu = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getTenDichVu());
                pstmt.setString(2, d.getLoaiDichVu());
                pstmt.setDouble(3, d.getGia());
                pstmt.setString(4, d.getMoTa());
                pstmt.setInt(5, d.getSoLuongTon());
                pstmt.setString(6, d.getDonVi());
                pstmt.setString(7, d.getMaDichVu());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maDichVu) {
        String sql = "DELETE FROM dich_vu WHERE maDichVu = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maDichVu);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DichVuDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private DichVu mapResultSet(ResultSet rs) throws SQLException {
        DichVu d = new DichVu(
                rs.getString("maDichVu"),
                rs.getString("tenDichVu"),
                rs.getString("loaiDichVu"),
                rs.getDouble("gia"),
                rs.getString("moTa")
        );
        d.setSoLuongTon(rs.getInt("soLuongTon"));
        d.setDonVi(rs.getString("donVi"));
        return d;
    }
}
