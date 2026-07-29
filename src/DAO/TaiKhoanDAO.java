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
        String sql = "SELECT * FROM TaiKhoan ORDER BY id ASC";
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
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
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
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, hoTen, soDienThoai, email, vaiTro, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, tk.getTenDangNhap());
                pstmt.setString(2, tk.getMatKhau());
                pstmt.setString(3, tk.getHoTen());
                pstmt.setString(4, tk.getSoDienThoai());
                pstmt.setString(5, tk.getEmail());
                pstmt.setString(6, tk.getVaiTro());
                pstmt.setString(7, tk.getTrangThai());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            tk.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET tenDangNhap = ?, matKhau = ?, hoTen = ?, soDienThoai = ?, email = ?, vaiTro = ?, trangThai = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tk.getTenDangNhap());
                pstmt.setString(2, tk.getMatKhau());
                pstmt.setString(3, tk.getHoTen());
                pstmt.setString(4, tk.getSoDienThoai());
                pstmt.setString(5, tk.getEmail());
                pstmt.setString(6, tk.getVaiTro());
                pstmt.setString(7, tk.getTrangThai());
                pstmt.setInt(8, tk.getId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM TaiKhoan WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi TaiKhoanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private TaiKhoan mapResultSet(ResultSet rs) throws SQLException {
        return new TaiKhoan(
                rs.getInt("id"),
                rs.getString("tenDangNhap"),
                rs.getString("matKhau"),
                rs.getString("hoTen"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getString("vaiTro"),
                rs.getString("trangThai")
        );
    }
}
