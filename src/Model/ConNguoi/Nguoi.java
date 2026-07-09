package Model.ConNguoi;

public abstract class Nguoi {
    protected String ma; // Sử dụng chung cho maKH và maNV
    protected String hoTen; // Sử dụng chung cho tenKH và tenNV
    protected String soDienThoai;
    protected String email;
    protected String username; // Liên kết với bảng TaiKhoan
}
