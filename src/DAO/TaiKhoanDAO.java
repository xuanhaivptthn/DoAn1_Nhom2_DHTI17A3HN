package DAO;

import Model.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM tai_khoan ORDER BY maTaiKhoan ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public TaiKhoan findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM tai_khoan WHERE tenDangNhap = ? AND matKhau = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSet(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.findByUsernameAndPassword(): " + ex.getMessage());
        }
        return null;
    }

    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO tai_khoan (maTaiKhoan, tenDangNhap, matKhau, quyenHan, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tk.getMaTaiKhoan());
                pstmt.setString(2, tk.getTenDangNhap());
                pstmt.setString(3, tk.getMatKhau());
                pstmt.setString(4, tk.getQuyenHan());
                pstmt.setString(5, tk.getTrangThai());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE tai_khoan SET tenDangNhap = ?, matKhau = ?, quyenHan = ?, trangThai = ? WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tk.getTenDangNhap());
                pstmt.setString(2, tk.getMatKhau());
                pstmt.setString(3, tk.getQuyenHan());
                pstmt.setString(4, tk.getTrangThai());
                pstmt.setString(5, tk.getMaTaiKhoan());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maTaiKhoan) {
        String sql = "DELETE FROM tai_khoan WHERE maTaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maTaiKhoan);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private TaiKhoan mapResultSet(ResultSet rs) throws SQLException {
        return new TaiKhoan(
                rs.getString("maTaiKhoan"),
                rs.getString("tenDangNhap"),
                rs.getString("matKhau"),
                rs.getString("quyenHan"),
                rs.getString("trangThai")
        );
    }
}
