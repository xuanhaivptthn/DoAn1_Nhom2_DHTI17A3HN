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

    private static boolean tableChecked = false;
    private void ensureTableExists(Connection conn) {
        if (tableChecked) return;
        String sql = "CREATE TABLE IF NOT EXISTS `lich_dat_san_dich_vu` ("
                   + "  `maLichDat` VARCHAR(20) NOT NULL,"
                   + "  `maDichVu` VARCHAR(20) NOT NULL,"
                   + "  `soLuong` INT NOT NULL DEFAULT 1,"
                   + "  PRIMARY KEY (`maLichDat`, `maDichVu`),"
                   + "  FOREIGN KEY (`maLichDat`) REFERENCES `lich_dat_san`(`maLichDat`) ON DELETE CASCADE,"
                   + "  FOREIGN KEY (`maDichVu`) REFERENCES `dich_vu`(`maDichVu`) ON DELETE CASCADE"
                   + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            tableChecked = true;
        } catch (SQLException ex) {
            System.err.println("Lỗi ensureTableExists: " + ex.getMessage());
        }
    }

    private void saveAccompanyingServices(Connection conn, DatLich d) throws SQLException {
        String sql = "INSERT INTO lich_dat_san_dich_vu (maLichDat, maDichVu, soLuong) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (d.getSelectedDvMap() != null) {
                for (java.util.Map.Entry<Integer, Integer> entry : d.getSelectedDvMap().entrySet()) {
                    int id = entry.getKey();
                    int qty = entry.getValue();
                    if (qty <= 0) continue;
                    
                    DichVu dv = DataStore.get().findDichVuById(id);
                    if (dv != null && dv.getMaDichVu() != null) {
                        pstmt.setString(1, d.getMaLichDat());
                        pstmt.setString(2, dv.getMaDichVu());
                        pstmt.setInt(3, qty);
                        pstmt.addBatch();
                    }
                }
            }
            if (d.getSelectedDoAnMap() != null) {
                for (java.util.Map.Entry<Integer, Integer> entry : d.getSelectedDoAnMap().entrySet()) {
                    int id = entry.getKey();
                    int qty = entry.getValue();
                    if (qty <= 0) continue;
                    
                    DichVu dv = DataStore.get().findDichVuById(id);
                    if (dv != null && dv.getMaDichVu() != null) {
                        pstmt.setString(1, d.getMaLichDat());
                        pstmt.setString(2, dv.getMaDichVu());
                        pstmt.setInt(3, qty);
                        pstmt.addBatch();
                    }
                }
            }
            pstmt.executeBatch();
        }
    }

    private void loadAccompanyingServices(Connection conn, DatLich d) {
        String sql = "SELECT * FROM lich_dat_san_dich_vu WHERE maLichDat = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, d.getMaLichDat());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maDichVu = rs.getString("maDichVu");
                    int soLuong = rs.getInt("soLuong");
                    if (soLuong <= 0) continue;
                    
                    int id = maDichVu.hashCode() & 0x7FFFFFFF;
                    if (maDichVu.startsWith("HH")) {
                        d.getSelectedDoAnMap().put(id, soLuong);
                    } else {
                        d.getSelectedDvMap().put(id, soLuong);
                    }
                    
                    DichVu dv = DataStore.get().findDichVuById(id);
                    if (dv != null) {
                        d.addDichVuKem(dv.getTenDichVu(), soLuong, dv.getDonGia() * soLuong);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi loadAccompanyingServices: " + ex.getMessage());
        }
    }

    public List<DatLich> getAll() {
        List<DatLich> list = new ArrayList<>();
        String sql = "SELECT * FROM lich_dat_san ORDER BY ngayDat DESC";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return list;
            ensureTableExists(conn);
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
            ensureTableExists(conn);
            
            boolean ok = false;
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
                ok = pstmt.executeUpdate() > 0;
            }
            if (ok) {
                saveAccompanyingServices(conn, d);
            }
            return ok;
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.insert(): " + ex.getMessage());
        }
        return false;
    }

    public boolean update(DatLich d) {
        String sql = "UPDATE lich_dat_san SET maSan = ?, maTaiKhoan = ?, maKhachHang = ?, tenKhach = ?, soDienThoaiKhach = ?, ngayDat = ?, gioBatDau = ?, gioKetThuc = ?, trangThai = ?, ghiChu = ? WHERE maLichDat = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            ensureTableExists(conn);
            
            boolean ok = false;
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
                ok = pstmt.executeUpdate() > 0;
            }
            if (ok) {
                String deleteSql = "DELETE FROM lich_dat_san_dich_vu WHERE maLichDat = ?";
                try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
                    pstmtDelete.setString(1, d.getMaLichDat());
                    pstmtDelete.executeUpdate();
                }
                saveAccompanyingServices(conn, d);
            }
            return ok;
        } catch (SQLException ex) {
            System.err.println("Lỗi DatLichDAO.update(): " + ex.getMessage());
        }
        return false;
    }

    public boolean delete(String maLichDat) {
        String sql = "DELETE FROM lich_dat_san WHERE maLichDat = ?";
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return false;
            ensureTableExists(conn);
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

        Connection conn = rs.getStatement().getConnection();
        ensureTableExists(conn);
        loadAccompanyingServices(conn, d);
        return d;
    }
}
