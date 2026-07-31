package DAO;

import Model.ChuSan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChuSanDAO {

    public List<ChuSan> getAll() {
        List<ChuSan> list = new ArrayList<>();
        String sql = "SELECT * FROM chu_san ORDER BY maChuSan ASC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi ChuSanDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public ChuSan findByMaTaiKhoan(String maTaiKhoan) {
        String sql = "SELECT * FROM chu_san WHERE maTaiKhoan = ?";
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
            System.err.println("Lỗi ChuSanDAO.findByMaTaiKhoan(): " + ex.getMessage());
        }
        return null;
    }

    public boolean insert(ChuSan cs) {
        String sql = "INSERT INTO chu_san (maChuSan, maTaiKhoan, tenChuSan, soDienThoaiChuSan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, cs.getMaChuSan());
                pstmt.setString(2, cs.getMaTaiKhoan());
                pstmt.setString(3, cs.getTenChuSan());
                pstmt.setString(4, cs.getSoDienThoaiChuSan());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi ChuSanDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(ChuSan cs) {
        String sql = "UPDATE chu_san SET tenChuSan = ?, soDienThoaiChuSan = ? WHERE maChuSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, cs.getTenChuSan());
                pstmt.setString(2, cs.getSoDienThoaiChuSan());
                pstmt.setString(3, cs.getMaChuSan());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi ChuSanDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maChuSan) {
        String sql = "DELETE FROM chu_san WHERE maChuSan = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maChuSan);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi ChuSanDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private ChuSan mapResultSet(ResultSet rs) throws SQLException {
        return new ChuSan(
                rs.getString("maChuSan"),
                rs.getString("maTaiKhoan"),
                rs.getString("tenChuSan"),
                rs.getString("soDienThoaiChuSan")
        );
    }
}
