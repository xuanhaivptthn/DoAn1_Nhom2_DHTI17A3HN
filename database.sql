-- Database for DoAn1_QuanLySanBong (MariaDB/phpMyAdmin)

CREATE DATABASE IF NOT EXISTS DoAn1_QuanLySanBong CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE DoAn1_QuanLySanBong;

-- Table: TaiKhoan
CREATE TABLE TaiKhoan (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    vaiTro ENUM('CHU_SAN', 'NHAN_VIEN', 'KHACH_HANG') NOT NULL
) ENGINE=InnoDB;

-- Table: KhachHang
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15),
    email VARCHAR(100),
    username VARCHAR(50),
    diemTichLuy INT DEFAULT 0,
    FOREIGN KEY (username) REFERENCES TaiKhoan(username) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Table: NhanVien
CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15),
    email VARCHAR(100),
    username VARCHAR(50),
    luongCoBan DECIMAL(15, 2),
    caLamViec VARCHAR(50),
    FOREIGN KEY (username) REFERENCES TaiKhoan(username) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Table: SanBong
CREATE TABLE SanBong (
    maSan VARCHAR(20) PRIMARY KEY,
    tenSan VARCHAR(100) NOT NULL,
    loaiSan VARCHAR(50),
    giaSan DECIMAL(15, 2),
    trangThai VARCHAR(50)
) ENGINE=InnoDB;

-- Table: ThietBi
CREATE TABLE ThietBi (
    maTB VARCHAR(20) PRIMARY KEY,
    tenTB VARCHAR(100) NOT NULL,
    soLuong INT,
    tinhTrang VARCHAR(50),
    maSan VARCHAR(20),
    FOREIGN KEY (maSan) REFERENCES SanBong(maSan) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Table: DichVu
CREATE TABLE DichVu (
    maDV VARCHAR(20) PRIMARY KEY,
    tenDV VARCHAR(100) NOT NULL,
    loaiDichVu VARCHAR(50),
    donGia DECIMAL(15, 2),
    soLuongTon INT,
    donViTinh VARCHAR(20)
) ENGINE=InnoDB;

-- Table: ThongTinDatSan
CREATE TABLE ThongTinDatSan (
    maDatSan VARCHAR(20) PRIMARY KEY,
    maKH VARCHAR(20),
    maSan VARCHAR(20),
    maNV VARCHAR(20),
    thoiGianBatDau DATETIME,
    thoiGianKetThuc DATETIME,
    tienCoc DECIMAL(15, 2),
    trangThai VARCHAR(50),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE SET NULL,
    FOREIGN KEY (maSan) REFERENCES SanBong(maSan) ON DELETE SET NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Table: HoaDon
CREATE TABLE HoaDon (
    maHD VARCHAR(20) PRIMARY KEY,
    maDatSan VARCHAR(20),
    maNV VARCHAR(20),
    ngayLap DATETIME,
    tongTienSan DECIMAL(15, 2),
    tongTienDichVu DECIMAL(15, 2),
    tongTienThanhToan DECIMAL(15, 2),
    trangThai VARCHAR(50),
    FOREIGN KEY (maDatSan) REFERENCES ThongTinDatSan(maDatSan) ON DELETE SET NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Table: ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(20),
    maDV VARCHAR(20),
    soLuong INT,
    donGia DECIMAL(15, 2),
    thanhTien DECIMAL(15, 2),
    PRIMARY KEY (maHD, maDV),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    FOREIGN KEY (maDV) REFERENCES DichVu(maDV) ON DELETE CASCADE
) ENGINE=InnoDB;
