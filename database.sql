-- ============================================================
-- CƠ SỞ DỮ LIỆU: DoAn1_QuanLySanBong
-- Hệ thống Quản lý Hoạt động Cho thuê Sân bóng
-- Tương thích MySQL 5.7+ / 8.0+ / MariaDB trên XAMPP
-- ============================================================

CREATE DATABASE IF NOT EXISTS `DoAn1_QuanLySanBong` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `DoAn1_QuanLySanBong`;

-- ------------------------------------------------------------
-- 1. BẢNG TÀI KHOẢN (TaiKhoan)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `TaiKhoan`;
CREATE TABLE `TaiKhoan` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `tenDangNhap` VARCHAR(50) NOT NULL UNIQUE,
  `matKhau` VARCHAR(100) NOT NULL,
  `hoTen` VARCHAR(100) NOT NULL,
  `soDienThoai` VARCHAR(20),
  `email` VARCHAR(100),
  `vaiTro` VARCHAR(20) DEFAULT 'NhanVien', -- Admin | NhanVien
  `trangThai` VARCHAR(20) DEFAULT 'HoatDong' -- HoatDong | Khoa
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `TaiKhoan` (`id`, `tenDangNhap`, `matKhau`, `hoTen`, `soDienThoai`, `email`, `vaiTro`, `trangThai`) VALUES
(1, 'admin', 'admin123', 'Chủ Sân Quản Lý', '0988111222', 'admin@sanbong.vn', 'Admin', 'HoatDong'),
(2, 'nhanvien01', 'nv123456', 'Nguyễn Văn Nhân', '0977222333', 'nv01@sanbong.vn', 'NhanVien', 'HoatDong'),
(3, 'nhanvien02', 'nv123456', 'Trần Thị Thu', '0966333444', 'nv02@sanbong.vn', 'NhanVien', 'HoatDong'),
(4, 'nhanvien03', 'nv123456', 'Lê Hoàng Nam', '0955444555', 'nv03@sanbong.vn', 'NhanVien', 'Khoa');


-- ------------------------------------------------------------
-- 2. BẢNG KHU VỰC SÂN BÓNG (KhuVucSan)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `KhuVucSan`;
CREATE TABLE `KhuVucSan` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `maSan` VARCHAR(20) NOT NULL UNIQUE,
  `tenSan` VARCHAR(100) NOT NULL,
  `loaiSan` VARCHAR(20) NOT NULL, -- San5 | San7 | San11
  `giaTheoGio` DOUBLE NOT NULL DEFAULT 0,
  `moTa` TEXT,
  `trangThai` VARCHAR(20) DEFAULT 'SanSang' -- SanSang | DangThue | BaoTri
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `KhuVucSan` (`id`, `maSan`, `tenSan`, `loaiSan`, `giaTheoGio`, `moTa`, `trangThai`) VALUES
(1, 'A1', 'Sân A1 (Sân 5)', 'San5', 250000, 'Sân cỏ nhân tạo tiêu chuẩn FIFA 5 người, có đèn thắp sáng', 'SanSang'),
(2, 'A2', 'Sân A2 (Sân 5)', 'San5', 250000, 'Sân cỏ nhân tạo 5 người, thoáng mát có lưới chắn bóng mới', 'SanSang'),
(3, 'B1', 'Sân B1 (Sân 7)', 'San7', 400000, 'Sân 7 người cỏ chất lượng cao, thoát nước tốt', 'SanSang'),
(4, 'B2', 'Sân B2 (Sân 7)', 'San7', 400000, 'Sân 7 người trang bị hệ thống chiếu sáng LED hiện đại', 'SanSang'),
(5, 'C1', 'Sân C1 (Sân 11)', 'San11', 800000, 'Sân 11 người đạt tiêu chuẩn thi đấu giải giao hữu chuyên nghiệp', 'BaoTri');


-- ------------------------------------------------------------
-- 3. BẢNG DỊCH VỤ & KHO VẬT TƯ (DichVu)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `DichVu`;
CREATE TABLE `DichVu` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `maDichVu` VARCHAR(20) UNIQUE,
  `tenDichVu` VARCHAR(100) NOT NULL,
  `loaiDichVu` VARCHAR(50),
  `donGia` DOUBLE NOT NULL DEFAULT 0,
  `donVi` VARCHAR(20),
  `trangThai` VARCHAR(20) DEFAULT 'DangBan',
  `soLuongTon` INT DEFAULT 0,
  `tonToiThieu` INT DEFAULT 5,
  `moTa` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `DichVu` (`id`, `maDichVu`, `tenDichVu`, `loaiDichVu`, `donGia`, `donVi`, `trangThai`, `soLuongTon`, `tonToiThieu`, `moTa`) VALUES
-- 1. Các gói dịch vụ riêng
(1, 'DV001', 'Dịch vụ thuê trọng tài chính', 'Nhân sự', 150000, 'Trận', 'DangBan', 0, 0, 'Trọng tài chuyên nghiệp điều hành 1 trận (90p)'),
(2, 'DV002', 'Huấn luyện viên cá nhân 1v1', 'HLV cá nhân', 300000, 'Giờ', 'DangBan', 0, 0, 'HLV hướng dẫn kỹ thuật cá nhân theo giờ'),
(3, 'DV003', 'Giặt sấy trang phục thi đấu', 'Giặt sấy', 30000, 'Bộ', 'DangBan', 0, 0, 'Giặt sấy tiệt trùng bộ quần áo sau trận'),
(4, 'DV004', 'Hỗ trợ truyền thông & Quay phim', 'Dịch vụ thi đấu', 250000, 'Trận', 'DangBan', 0, 0, 'Quay video trận đấu & phát lại Highlights'),

-- 2. Danh mục mặt hàng kho hàng & vật tư
(101, 'HH101', 'Nước suối Aquafina 500ml', 'Vật tư kho', 10000, 'Chai', 'DangBan', 150, 10, 'Công ty Nước khoáng Aquafina'),
(102, 'HH102', 'Nước điện giải Revive 500ml', 'Vật tư kho', 15000, 'Chai', 'DangBan', 120, 10, 'Công ty Pocari Sweat Việt Nam'),
(103, 'HH103', 'Áo lưới tập bib phân đội', 'Vật tư kho', 20000, 'Bộ', 'DangBan', 60, 5, 'Xưởng may Trang phục Thể thao'),
(104, 'HH104', 'Bóng đá FIFA Động Lực size 5', 'Vật tư kho', 30000, 'Lượt', 'DangBan', 15, 3, 'Tập đoàn Thể thao Động Lực'),
(105, 'HH105', 'Găng tay thủ môn Adidas', 'Vật tư kho', 50000, 'Đôi', 'DangBan', 10, 2, 'Adidas Việt Nam'),
(106, 'HH106', 'Giày đá bóng sân cỏ nhân tạo', 'Vật tư kho', 50000, 'Đôi', 'DangBan', 12, 3, 'NCS Sports Việt Nam'),
(107, 'HH107', 'Lưới bóng đá S7', 'Vật tư kho', 180000, 'Bộ', 'DangBan', 12, 3, 'NCS Sports Việt Nam'),
(108, 'HH108', 'Băng gối & khuỷu tay bảo vệ', 'Vật tư kho', 45000, 'Đôi', 'DangBan', 25, 5, 'Y tế Thể thao Chấn thương');


-- ------------------------------------------------------------
-- 4. BẢNG KHÁCH HÀNG (KhachHang)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `KhachHang`;
CREATE TABLE `KhachHang` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `hoTen` VARCHAR(100) NOT NULL,
  `soDienThoai` VARCHAR(20) NOT NULL UNIQUE,
  `email` VARCHAR(100),
  `ghiChu` TEXT,
  `soLanDat` INT DEFAULT 1,
  `ngayTao` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `KhachHang` (`id`, `hoTen`, `soDienThoai`, `email`, `ghiChu`, `soLanDat`) VALUES
(1, 'Anh Đức (FC Anh Em)', '0912345678', 'duc.fc@gmail.com', 'Khách quen đặt cố định thứ 3 & thứ 5', 8),
(2, 'Anh Tuấn (FC Thể Công)', '0987654321', 'tuan.tc@gmail.com', 'Khách hay đá khung 19h - 20h30', 5),
(3, 'Chị Mai (Công ty FPT)', '0905123456', 'mai.fpt@gmail.com', 'Đặt sân cố định cuối tuần cho công ty', 12);


-- ------------------------------------------------------------
-- 5. BẢNG ĐẶT LỊCH SÂN BÓNG (DatLich)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `DatLich`;
CREATE TABLE `DatLich` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `maPhieu` VARCHAR(20) NOT NULL UNIQUE,
  `khuVucId` INT NOT NULL,
  `tenSan` VARCHAR(100) NOT NULL,
  `tenKhach` VARCHAR(100) NOT NULL,
  `soDienThoai` VARCHAR(20) NOT NULL,
  `ngayDat` VARCHAR(20) NOT NULL, -- yyyy-MM-dd
  `gioBatDau` VARCHAR(10) NOT NULL, -- HH:mm
  `gioKetThuc` VARCHAR(10) NOT NULL,
  `tienSan` DOUBLE DEFAULT 0,
  `tienDichVu` DOUBLE DEFAULT 0,
  `tongTien` DOUBLE DEFAULT 0,
  `datCoc` DOUBLE DEFAULT 0,
  `trangThai` VARCHAR(20) DEFAULT 'ChoXacNhan', -- ChoXacNhan | DaXacNhan | HoanThanh | DaHuy
  `trangThaiTT` VARCHAR(20) DEFAULT 'ChuaThanhToan', -- ChuaThanhToan | DaThanhToan | ThanhToanMotPhan
  `nhanVienLap` VARCHAR(100),
  `ghiChu` TEXT,
  `dichVuKem` TEXT,
  `ngayTao` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`khuVucId`) REFERENCES `KhuVucSan`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `DatLich` (`id`, `maPhieu`, `khuVucId`, `tenSan`, `tenKhach`, `soDienThoai`, `ngayDat`, `gioBatDau`, `gioKetThuc`, `tienSan`, `tienDichVu`, `tongTien`, `datCoc`, `trangThai`, `trangThaiTT`, `nhanVienLap`, `ghiChu`, `dichVuKem`) VALUES
(1, 'DL001', 1, 'Sân A1 (Sân 5)', 'Anh Đức (FC Anh Em)', '0912345678', '2026-07-29', '17:30', '19:00', 375000, 50000, 425000, 100000, 'DaXacNhan', 'ThanhToanMotPhan', 'Nguyễn Văn Nhân', 'Đặt cọc trước 100k', 'Nước suối Aquafina 500ml (x5): 50,000 VNĐ'),
(2, 'DL002', 3, 'Sân B1 (Sân 7)', 'Anh Tuấn (FC Thể Công)', '0987654321', '2026-07-29', '19:00', '20:30', 600000, 80000, 680000, 200000, 'DaXacNhan', 'ThanhToanMotPhan', 'Trần Thị Thu', 'Thanh toán cọc qua CK', 'Nước điện giải Revive (x4): 60,000 VNĐ\nÁo bít tập luyện (Bộ) (x1): 20,000 VNĐ'),
(3, 'DL003', 2, 'Sân A2 (Sân 5)', 'Chị Mai (Công ty FPT)', '0905123456', '2026-07-29', '20:30', '22:00', 375000, 0, 375000, 375000, 'HoanThanh', 'DaThanhToan', 'Chủ Sân Quản Lý', 'Đã chuyển khoản đủ 100%', '');


-- ------------------------------------------------------------
-- 6. BẢNG BẢO TRÌ SÂN BÓNG (BaoTri)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `BaoTri`;
CREATE TABLE `BaoTri` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `maBaoTri` VARCHAR(20) NOT NULL UNIQUE,
  `khuVucId` INT NOT NULL,
  `tenSan` VARCHAR(100) NOT NULL,
  `noiDung` TEXT NOT NULL,
  `nguoiPhuTrach` VARCHAR(100),
  `ngayBatDau` VARCHAR(20),
  `ngayKetThuc` VARCHAR(20),
  `chiPhi` DOUBLE DEFAULT 0,
  `trangThai` VARCHAR(20) DEFAULT 'ChoXuLy', -- ChoXuLy | DangXuLy | HoanThanh | DaHuy
  FOREIGN KEY (`khuVucId`) REFERENCES `KhuVucSan`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `BaoTri` (`id`, `maBaoTri`, `khuVucId`, `tenSan`, `noiDung`, `nguoiPhuTrach`, `ngayBatDau`, `ngayKetThuc`, `chiPhi`, `trangThai`) VALUES
(1, 'BT001', 5, 'Sân C1 (Sân 11)', 'Thay lại thảm cỏ nhân tạo vùng cấm địa & kiểm tra hệ thống đèn pha LED', 'Lê Minh Tuấn', '2026-07-25', '2026-08-05', 4500000, 'DangXuLy'),
(2, 'BT002', 4, 'Sân B2 (Sân 7)', 'Bảo dưỡng định kỳ lưới chắn bóng xung quanh', 'Trần Thị Lan', '2026-07-20', '2026-07-22', 800000, 'HoanThanh');


-- ------------------------------------------------------------
-- 7. BẢNG PHIÊN LÀM VIỆC (PhienLamViec)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `PhienLamViec`;
CREATE TABLE `PhienLamViec` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `sessionId` VARCHAR(50) NOT NULL UNIQUE,
  `tenDangNhap` VARCHAR(50) NOT NULL,
  `hoTen` VARCHAR(100),
  `vaiTro` VARCHAR(20),
  `thoiGianDangNhap` VARCHAR(50),
  `thoiGianDangXuat` VARCHAR(50),
  `trangThai` VARCHAR(20) DEFAULT 'DangHoatDong'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `PhienLamViec` (`id`, `sessionId`, `tenDangNhap`, `hoTen`, `vaiTro`, `thoiGianDangNhap`, `thoiGianDangXuat`, `trangThai`) VALUES
(1, 'SESS-1001', 'admin', 'Chủ Sân Quản Lý', 'Admin', '2026-07-29 08:00:00', NULL, 'DangHoatDong');

-- ============================================================
-- HOÀN TẤT TẠO CSDL
-- ============================================================
