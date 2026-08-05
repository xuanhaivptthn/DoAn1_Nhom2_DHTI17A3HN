-- ============================================================
-- CƠ SỞ DỮ LIỆU: DoAn1_QuanLySanBong
-- Hệ thống Quản lý Hoạt động Cho thuê Sân bóng
-- Khớp với DAO/*.java (DBConnect dùng mysql-connector-j 8.x)
-- Tương thích MySQL 5.7+ / 8.0+ / MariaDB trên XAMPP
-- ============================================================

DROP DATABASE IF EXISTS `DoAn1_QuanLySanBong`;
CREATE DATABASE `DoAn1_QuanLySanBong` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `DoAn1_QuanLySanBong`;

-- ------------------------------------------------------------
-- 1. BẢNG TÀI KHOẢN (tai_khoan) — Model.TaiKhoan / DAO.TaiKhoanDAO
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `hoa_don`;
DROP TABLE IF EXISTS `kho`;
DROP TABLE IF EXISTS `nhan_vien`;
DROP TABLE IF EXISTS `chu_san`;
DROP TABLE IF EXISTS `bao_tri`;
DROP TABLE IF EXISTS `lich_dat_san`;
DROP TABLE IF EXISTS `san_bong`;
DROP TABLE IF EXISTS `dich_vu`;
DROP TABLE IF EXISTS `khach_hang`;
DROP TABLE IF EXISTS `tai_khoan`;

CREATE TABLE `tai_khoan` (
  `maTaiKhoan` VARCHAR(20) NOT NULL PRIMARY KEY,
  `tenDangNhap` VARCHAR(50) NOT NULL UNIQUE,
  `matKhau` VARCHAR(100) NOT NULL,
  `quyenHan` VARCHAR(20) DEFAULT 'NHAN_VIEN', -- ADMIN | CHU_SAN | NHAN_VIEN | KHACH_HANG
  `trangThai` VARCHAR(20) DEFAULT 'HOAT_DONG' -- HOAT_DONG | KHOA
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `tai_khoan` (`maTaiKhoan`, `tenDangNhap`, `matKhau`, `quyenHan`, `trangThai`) VALUES
('TK001', 'admin',      'admin123', 'ADMIN',     'HOAT_DONG'),
('TK002', 'nhanvien01', 'nv123456', 'NHAN_VIEN', 'HOAT_DONG'),
('TK003', 'nhanvien02', 'nv123456', 'NHAN_VIEN', 'HOAT_DONG'),
('TK004', 'nhanvien03', 'nv123456', 'NHAN_VIEN', 'KHOA');


-- ------------------------------------------------------------
-- 2. BẢNG CHỦ SÂN (chu_san) — Model.ChuSan / DAO.ChuSanDAO
-- ------------------------------------------------------------
CREATE TABLE `chu_san` (
  `maChuSan` VARCHAR(20) NOT NULL PRIMARY KEY,
  `maTaiKhoan` VARCHAR(20) NOT NULL UNIQUE,
  `tenChuSan` VARCHAR(100) NOT NULL,
  `soDienThoaiChuSan` VARCHAR(15),
  FOREIGN KEY (`maTaiKhoan`) REFERENCES `tai_khoan`(`maTaiKhoan`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `chu_san` (`maChuSan`, `maTaiKhoan`, `tenChuSan`, `soDienThoaiChuSan`) VALUES
('CS001', 'TK001', 'Chủ Sân Quản Lý', '0988111222');


-- ------------------------------------------------------------
-- 3. BẢNG NHÂN VIÊN (nhan_vien) — Model.NhanVien / DAO.NhanVienDAO
-- ------------------------------------------------------------
CREATE TABLE `nhan_vien` (
  `maNhanVien` VARCHAR(20) NOT NULL PRIMARY KEY,
  `maTaiKhoan` VARCHAR(20) NOT NULL UNIQUE,
  `hoTenNhanVien` VARCHAR(100) NOT NULL,
  `soDienThoaiNhanVien` VARCHAR(15) NOT NULL,
  `diaChi` VARCHAR(255),
  FOREIGN KEY (`maTaiKhoan`) REFERENCES `tai_khoan`(`maTaiKhoan`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `nhan_vien` (`maNhanVien`, `maTaiKhoan`, `hoTenNhanVien`, `soDienThoaiNhanVien`, `diaChi`) VALUES
('NV001', 'TK002', 'Nguyễn Văn Nam', '0912345678', 'Hà Nội'),
('NV002', 'TK003', 'Trần Thị Hằng', '0987654321', 'Hà Nội'),
('NV003', 'TK004', 'Lê Hoàng Long', '0905123456', 'Hà Nội');


-- ------------------------------------------------------------
-- 3. BẢNG KHU VỰC SÂN BÓNG (san_bong) — Model.KhuVucSan / DAO.KhuVucSanDAO
-- ------------------------------------------------------------
CREATE TABLE `san_bong` (
  `maSan` VARCHAR(20) NOT NULL PRIMARY KEY,
  `tenSan` VARCHAR(100) NOT NULL,
  `loaiSan` VARCHAR(20) NOT NULL, -- San5 | San7 | San11
  `giaThueTheoGio` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `trangThai` VARCHAR(20) DEFAULT 'HOAT_DONG' -- HOAT_DONG | DANG_THUE | BAO_TRI
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `san_bong` (`maSan`, `tenSan`, `loaiSan`, `giaThueTheoGio`, `trangThai`) VALUES
('SAN001', 'Sân A1 (Sân 5)', 'San5', 250000, 'HOAT_DONG'),
('SAN002', 'Sân A2 (Sân 5)', 'San5', 250000, 'HOAT_DONG'),
('SAN003', 'Sân B1 (Sân 7)', 'San7', 400000, 'HOAT_DONG'),
('SAN004', 'Sân B2 (Sân 7)', 'San7', 400000, 'HOAT_DONG'),
('SAN005', 'Sân C1 (Sân 11)', 'San11', 800000, 'BAO_TRI');


-- ------------------------------------------------------------
-- 4. BẢNG DỊCH VỤ & KHO VẬT TƯ (dich_vu) — Model.DichVu / DAO.DichVuDAO
--    soLuongTon/donVi dùng chung cho cả dịch vụ thường lẫn "Vật tư kho".
-- ------------------------------------------------------------
CREATE TABLE `dich_vu` (
  `maDichVu` VARCHAR(20) NOT NULL PRIMARY KEY,
  `tenDichVu` VARCHAR(100) NOT NULL UNIQUE,
  `loaiDichVu` VARCHAR(50) NOT NULL,
  `gia` DECIMAL(15,0) NOT NULL DEFAULT 0,
  `moTa` TEXT,
  `soLuongTon` INT DEFAULT 0,
  `donVi` VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `dich_vu` (`maDichVu`, `tenDichVu`, `loaiDichVu`, `gia`, `moTa`, `soLuongTon`, `donVi`) VALUES
-- 4.1 Các gói dịch vụ riêng (không quản lý tồn kho)
('DV001', 'Dịch vụ thuê trọng tài chính', 'Nhân sự', 150000, 'Trọng tài chuyên nghiệp điều hành 1 trận (90p)', 0, 'Trận'),
('DV002', 'Huấn luyện viên cá nhân 1v1', 'HLV cá nhân', 300000, 'HLV hướng dẫn kỹ thuật cá nhân theo giờ', 0, 'Giờ'),
('DV003', 'Giặt sấy trang phục thi đấu', 'Giặt sấy', 30000, 'Giặt sấy tiệt trùng bộ quần áo sau trận', 0, 'Bộ'),
('DV004', 'Hỗ trợ truyền thông & Quay phim', 'Dịch vụ thi đấu', 250000, 'Quay video trận đấu & phát lại Highlights', 0, 'Trận'),

-- 4.2 Danh mục mặt hàng kho hàng hóa & vật tư (loaiDichVu = 'Vật tư kho')
('HH101', 'Nước suối Aquafina 500ml', 'Vật tư kho', 10000, 'Công ty Nước khoáng Aquafina', 120, 'Chai'),
('HH102', 'Nước điện giải Revive 500ml', 'Vật tư kho', 15000, 'Công ty Pocari Sweat Việt Nam', 85, 'Chai'),
('HH103', 'Áo bib tập luyện phân đội', 'Vật tư kho', 30000, 'Xưởng may Trang phục Thể thao', 25, 'Bộ'),
('HH104', 'Bóng thi đấu Động Lực', 'Vật tư kho', 50000, 'Tập đoàn Thể thao Động Lực', 15, 'Lượt'),
('HH105', 'Găng tay thủ môn cao cấp', 'Vật tư kho', 40000, 'Adidas Việt Nam', 8, 'Đôi'),
('HH106', 'Giày đá bóng sân cỏ nhân tạo', 'Vật tư kho', 50000, 'NCS Sports Việt Nam', 12, 'Đôi');


-- ------------------------------------------------------------
-- 5. BẢNG KHÁCH HÀNG (khach_hang) — Model.KhachHang / DAO.KhachHangDAO
-- ------------------------------------------------------------
CREATE TABLE `khach_hang` (
  `maKhachHang` VARCHAR(20) NOT NULL PRIMARY KEY,
  `tenKhachHang` VARCHAR(100) NOT NULL,
  `soDienThoai` VARCHAR(15) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `khach_hang` (`maKhachHang`, `tenKhachHang`, `soDienThoai`) VALUES
('KH001', 'Anh Đức (FC Anh Em)', '0912345678'),
('KH002', 'Anh Tuấn (FC Thể Công)', '0987654321'),
('KH003', 'Chị Mai (Công ty FPT)', '0905123456');


-- ------------------------------------------------------------
-- 6. BẢNG ĐẶT LỊCH SÂN BÓNG (lich_dat_san) — Model.DatLich / DAO.DatLichDAO
-- ------------------------------------------------------------
CREATE TABLE `lich_dat_san` (
  `maLichDat` VARCHAR(20) NOT NULL PRIMARY KEY,
  `maSan` VARCHAR(20) NOT NULL,
  `maTaiKhoan` VARCHAR(50),
  `maKhachHang` VARCHAR(20) NOT NULL,
  `ngayDat` VARCHAR(20) NOT NULL,   -- yyyy-MM-dd
  `gioBatDau` VARCHAR(10) NOT NULL, -- HH:mm
  `gioKetThuc` VARCHAR(10) NOT NULL,
  `trangThai` VARCHAR(20) DEFAULT 'ChoXacNhan', -- ChoXacNhan | DaXacNhan | HoanThanh | DaHuy
  `ghiChu` TEXT,
  FOREIGN KEY (`maSan`) REFERENCES `san_bong`(`maSan`) ON DELETE CASCADE,
  FOREIGN KEY (`maKhachHang`) REFERENCES `khach_hang`(`maKhachHang`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `lich_dat_san` (`maLichDat`, `maSan`, `maTaiKhoan`, `maKhachHang`, `ngayDat`, `gioBatDau`, `gioKetThuc`, `trangThai`, `ghiChu`) VALUES
('DL001', 'SAN001', 'TK002', 'KH001', CURDATE(), '17:30', '19:00', 'DaXacNhan', 'Đặt cọc trước 100k'),
('DL002', 'SAN003', 'TK003', 'KH002', CURDATE(), '19:00', '20:30', 'DaXacNhan', 'Thanh toán cọc qua CK'),
('DL003', 'SAN002', 'TK001', 'KH003', CURDATE(), '20:30', '22:00', 'HoanThanh', 'Đã chuyển khoản đủ 100%');


-- ------------------------------------------------------------
-- 7. BẢNG BẢO TRÌ SÂN BÓNG (bao_tri) — Model.BaoTri / DAO.BaoTriDAO
-- ------------------------------------------------------------
CREATE TABLE `bao_tri` (
  `maPhieuBaoTri` VARCHAR(20) NOT NULL PRIMARY KEY,
  `maSan` VARCHAR(20) NOT NULL,
  `noiDung` TEXT NOT NULL,
  `ngayBatDau` VARCHAR(20),
  `ngayKetThuc` VARCHAR(20),
  `trangThaiPhieu` VARCHAR(20) DEFAULT 'DANG_BAO_TRI', -- DANG_BAO_TRI | HOAN_THANH | HUY
  FOREIGN KEY (`maSan`) REFERENCES `san_bong`(`maSan`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `bao_tri` (`maPhieuBaoTri`, `maSan`, `noiDung`, `ngayBatDau`, `ngayKetThuc`, `trangThaiPhieu`) VALUES
('BT001', 'SAN005', 'Thay lại thảm cỏ nhân tạo vùng cấm địa & kiểm tra hệ thống đèn pha LED', '2026-07-25', '2026-08-05', 'DANG_BAO_TRI'),
('BT002', 'SAN004', 'Bảo dưỡng định kỳ lưới chắn bóng xung quanh', '2026-07-20', '2026-07-22', 'HOAN_THANH');


-- ------------------------------------------------------------
-- 9. BẢNG HÓA ĐƠN (hoa_don) — Model.HoaDon
-- ------------------------------------------------------------
CREATE TABLE `hoa_don` (
  `maHoaDon` VARCHAR(20) NOT NULL PRIMARY KEY,
  `maLichDat` VARCHAR(20) NOT NULL UNIQUE,
  `maNhanVien` VARCHAR(20) NOT NULL,
  `ngayThanhToan` VARCHAR(50) NOT NULL,
  `chiPhiSan` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `tongTienDichVu` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `tongTienKho` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `giamGia` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `tongTien` DECIMAL(12,2) NOT NULL DEFAULT 0,
  `phuongThucThanhToan` VARCHAR(100) NOT NULL,
  FOREIGN KEY (`maLichDat`) REFERENCES `lich_dat_san`(`maLichDat`) ON DELETE CASCADE,
  FOREIGN KEY (`maNhanVien`) REFERENCES `nhan_vien`(`maNhanVien`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `hoa_don` (`maHoaDon`, `maLichDat`, `maNhanVien`, `ngayThanhToan`, `chiPhiSan`, `tongTienDichVu`, `tongTienKho`, `giamGia`, `tongTien`, `phuongThucThanhToan`) VALUES
('HD001', 'DL003', 'NV001', '2026-08-05 22:05:00', 375000.00, 0.00, 50000.00, 0.00, 425000.00, 'Chuyển khoản');

-- ------------------------------------------------------------
-- 10. BẢNG KHO HÀNG (kho) — Model.Kho
-- ------------------------------------------------------------
CREATE TABLE `kho` (
  `maHangHoa` VARCHAR(20) NOT NULL PRIMARY KEY,
  `tenHangHoa` VARCHAR(100) NOT NULL UNIQUE,
  `soLuongTon` INT NOT NULL DEFAULT 0,
  `donGia` DECIMAL(15,0) NOT NULL DEFAULT 0,
  `nhaCungCap` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- HOÀN TẤT TẠO CSDL
-- ============================================================
