package DAO;

import Model.DatLich;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatLichDAO {

    public List<DatLich> getAll() {
        List<DatLich> list = new ArrayList<>();
        String sql = "SELECT * FROM DatLich ORDER BY id DESC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.getAll(): " + ex.getMessage());
        }
        return list;
    }

    public boolean insert(DatLich d) {
        String sql = "INSERT INTO DatLich (maPhieu, khuVucId, tenSan, tenKhach, soDienThoai, ngayDat, gioBatDau, gioKetThuc, tienSan, tienDichVu, tongTien, datCoc, trangThai, trangThaiTT, nhanVienLap, ghiChu, dichVuKem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, d.getMaPhieu());
                pstmt.setInt(2, d.getKhuVucId());
                pstmt.setString(3, d.getTenSan());
                pstmt.setString(4, d.getTenKhach());
                pstmt.setString(5, d.getSoDienThoai());
                pstmt.setString(6, d.getNgayDat());
                pstmt.setString(7, d.getGioBatDau());
                pstmt.setString(8, d.getGioKetThuc());
                pstmt.setDouble(9, d.getTienSan());
                pstmt.setDouble(10, d.getTienDichVu());
                pstmt.setDouble(11, d.getTongTien());
                pstmt.setDouble(12, d.getDatCoc());
                pstmt.setString(13, d.getTrangThai());
                pstmt.setString(14, d.getTrangThaiTT());
                pstmt.setString(15, d.getNhanVienLap());
                pstmt.setString(16, d.getGhiChu());
                pstmt.setString(17, d.getDichVuKem());
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
            System.err.println("Lỗi DatLichDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(DatLich d) {
        String sql = "UPDATE DatLich SET maPhieu = ?, khuVucId = ?, tenSan = ?, tenKhach = ?, soDienThoai = ?, ngayDat = ?, gioBatDau = ?, gioKetThuc = ?, tienSan = ?, tienDichVu = ?, tongTien = ?, datCoc = ?, trangThai = ?, trangThaiTT = ?, nhanVienLap = ?, ghiChu = ?, dichVuKem = ? WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getMaPhieu());
                pstmt.setInt(2, d.getKhuVucId());
                pstmt.setString(3, d.getTenSan());
                pstmt.setString(4, d.getTenKhach());
                pstmt.setString(5, d.getSoDienThoai());
                pstmt.setString(6, d.getNgayDat());
                pstmt.setString(7, d.getGioBatDau());
                pstmt.setString(8, d.getGioKetThuc());
                pstmt.setDouble(9, d.getTienSan());
                pstmt.setDouble(10, d.getTienDichVu());
                pstmt.setDouble(11, d.getTongTien());
                pstmt.setDouble(12, d.getDatCoc());
                pstmt.setString(13, d.getTrangThai());
                pstmt.setString(14, d.getTrangThaiTT());
                pstmt.setString(15, d.getNhanVienLap());
                pstmt.setString(16, d.getGhiChu());
                pstmt.setString(17, d.getDichVuKem());
                pstmt.setInt(18, d.getId());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM DatLich WHERE id = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private DatLich mapResultSet(ResultSet rs) throws SQLException {
        DatLich d = new DatLich(
                rs.getInt("id"),
                rs.getString("maPhieu"),
                rs.getInt("khuVucId"),
                rs.getString("tenSan"),
                rs.getString("tenKhach"),
                rs.getString("soDienThoai"),
                rs.getString("ngayDat"),
                rs.getString("gioBatDau"),
                rs.getString("gioKetThuc"),
                rs.getDouble("tongTien"),
                rs.getString("trangThai"),
                rs.getString("nhanVienLap"),
                rs.getString("ghiChu")
        );
        d.setTienSan(rs.getDouble("tienSan"));
        d.setTienDichVu(rs.getDouble("tienDichVu"));
        d.setDatCoc(rs.getDouble("datCoc"));
        d.setTrangThaiTT(rs.getString("trangThaiTT"));
        d.setDichVuKem(rs.getString("dichVuKem"));
        return d;
    }
}
