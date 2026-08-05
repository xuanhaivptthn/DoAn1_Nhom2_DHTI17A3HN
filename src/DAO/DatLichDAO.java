package DAO;

import Model.DatLich;
import Model.DichVu;
import Utils.DataStore;

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
        String sql = "SELECT * FROM lich_dat_san ORDER BY ngayDat DESC";
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
        String sql = "INSERT INTO lich_dat_san (maLichDat, maSan, maTaiKhoan, maKhachHang, tenKhach, soDienThoaiKhach, ngayDat, gioBatDau, gioKetThuc, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getMaLichDat());
                pstmt.setString(2, d.getMaSan());
                pstmt.setString(3, d.getMaTaiKhoan());
                pstmt.setString(4, d.getMaKhachHang());
                pstmt.setString(5, d.getTenKhach());
                pstmt.setString(6, d.getSoDienThoaiKhach());
                pstmt.setString(7, d.getNgayDat());
                pstmt.setString(8, d.getGioBatDau());
                pstmt.setString(9, d.getGioKetThuc());
                pstmt.setString(10, d.getTrangThai());
                pstmt.setString(11, d.getGhiChu());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(DatLich d) {
        String sql = "UPDATE lich_dat_san SET maSan = ?, maTaiKhoan = ?, maKhachHang = ?, tenKhach = ?, soDienThoaiKhach = ?, ngayDat = ?, gioBatDau = ?, gioKetThuc = ?, trangThai = ?, ghiChu = ? WHERE maLichDat = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, d.getMaSan());
                pstmt.setString(2, d.getMaTaiKhoan());
                pstmt.setString(3, d.getMaKhachHang());
                pstmt.setString(4, d.getTenKhach());
                pstmt.setString(5, d.getSoDienThoaiKhach());
                pstmt.setString(6, d.getNgayDat());
                pstmt.setString(7, d.getGioBatDau());
                pstmt.setString(8, d.getGioKetThuc());
                pstmt.setString(9, d.getTrangThai());
                pstmt.setString(10, d.getGhiChu());
                pstmt.setString(11, d.getMaLichDat());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maLichDat) {
        String sql = "DELETE FROM lich_dat_san WHERE maLichDat = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maLichDat);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.delete(): " + ex.getMessage());
        }
        return false;
    }

    private DatLich mapResultSet(ResultSet rs) throws SQLException {
        DatLich d = new DatLich();
        d.setMaLichDat(rs.getString("maLichDat"));
        d.setMaSan(rs.getString("maSan"));
        d.setMaTaiKhoan(rs.getString("maTaiKhoan"));
        d.setMaKhachHang(rs.getString("maKhachHang"));
        d.setTenKhach(rs.getString("tenKhach"));
        d.setSoDienThoaiKhach(rs.getString("soDienThoaiKhach"));
        d.setNgayDat(rs.getString("ngayDat"));
        d.setGioBatDau(rs.getString("gioBatDau"));
        d.setGioKetThuc(rs.getString("gioKetThuc"));
        d.setTrangThai(rs.getString("trangThai"));
        d.setGhiChu(rs.getString("ghiChu"));
        return d;
    }
}
