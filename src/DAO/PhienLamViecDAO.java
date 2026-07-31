package DAO;

import Model.PhienLamViec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhienLamViecDAO {

    public List<PhienLamViec> getAll() {
        List<PhienLamViec> list = new ArrayList<>();
        String sql = "SELECT * FROM PhienLamViec ORDER BY id DESC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi PhienLamViecDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(PhienLamViec p) {
        String sql = "INSERT INTO PhienLamViec (sessionId, tenDangNhap, hoTen, vaiTro, thoiGianDangNhap, thoiGianDangXuat, trangThai, diaChiIp, thietBi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, p.getSessionId());
                pstmt.setString(2, p.getTenDangNhap());
                pstmt.setString(3, p.getHoTen());
                pstmt.setString(4, p.getVaiTro());
                pstmt.setString(5, p.getThoiGianDangNhap());
                pstmt.setString(6, p.getThoiGianDangXuat());
                pstmt.setString(7, p.getTrangThai());
                pstmt.setString(8, p.getDiaChiIp());
                pstmt.setString(9, p.getThietBi());
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet keys = pstmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            p.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi PhienLamViecDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(PhienLamViec p) {
        String sql = "UPDATE PhienLamViec SET thoiGianDangXuat = ?, trangThai = ? WHERE sessionId = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, p.getThoiGianDangXuat());
                pstmt.setString(2, p.getTrangThai());
                pstmt.setString(3, p.getSessionId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi PhienLamViecDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    private PhienLamViec mapResultSet(ResultSet rs) throws SQLException {
        PhienLamViec p = new PhienLamViec(
                rs.getString("sessionId"),
                rs.getString("tenDangNhap"),
                rs.getString("hoTen"),
                rs.getString("vaiTro"),
                rs.getString("thoiGianDangNhap"),
                rs.getString("thoiGianDangXuat"),
                rs.getString("trangThai"),
                rs.getString("diaChiIp"),
                rs.getString("thietBi")
        );
        p.setId(rs.getInt("id"));
        return p;
    }
}
