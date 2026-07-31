package DAO;

import Model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhan_vien ORDER BY maNhanVien ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi NhanVienDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public NhanVien findByMaTaiKhoan(String maTaiKhoan) {
        String sql = "SELECT * FROM nhan_vien WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maTaiKhoan);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi NhanVienDAO.findByMaTaiKhoan(): " + ex.getMessage());
        }
        return null;
    }

    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO nhan_vien (maNhanVien, maTaiKhoan, hoTenNhanVien, soDienThoaiNhanVien, diaChi) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nv.getMaNhanVien());
                pstmt.setString(2, nv.getMaTaiKhoan());
                pstmt.setString(3, nv.getHoTenNhanVien());
                pstmt.setString(4, nv.getSoDienThoaiNhanVien());
                pstmt.setString(5, nv.getDiaChi());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi NhanVienDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(NhanVien nv) {
        String sql = "UPDATE nhan_vien SET hoTenNhanVien = ?, soDienThoaiNhanVien = ?, diaChi = ? WHERE maNhanVien = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nv.getHoTenNhanVien());
                pstmt.setString(2, nv.getSoDienThoaiNhanVien());
                pstmt.setString(3, nv.getDiaChi());
                pstmt.setString(4, nv.getMaNhanVien());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi NhanVienDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maNhanVien) {
        String sql = "DELETE FROM nhan_vien WHERE maNhanVien = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maNhanVien);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi NhanVienDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private NhanVien mapResultSet(ResultSet rs) throws SQLException {
        return new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("maTaiKhoan"),
                rs.getString("hoTenNhanVien"),
                rs.getString("soDienThoaiNhanVien"),
                rs.getString("diaChi")
        );
    }
}
