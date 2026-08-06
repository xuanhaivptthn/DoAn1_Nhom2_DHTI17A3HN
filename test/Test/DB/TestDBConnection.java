/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.DB;

import DAO.DBConnect;
import java.sql.*;

/**
 * Lớp kiểm thử kết nối cơ sở dữ liệu (Database Connection Test).
 * <p>
 * Lớp này phục vụ cho công tác kiểm thử tự động hoặc chạy thử nghiệm trực tiếp
 * khả năng kết nối tới CSDL MySQL thông qua {@link DBConnect}.
 * </p>
 *
 * @author gmtfarcb
 * @version 1.0
 */
public class TestDBConnection {

    /**
     * Khởi tạo đối tượng {@code TestDBConnection} mặc định.
     */
    public TestDBConnection() {}

    /**
     * Kiểm tra trạng thái kết nối tới CSDL MySQL.
     *
     * @return {@code true} nếu kết nối thành công và Connection chưa bị đóng; {@code false} nếu không thể kết nối hoặc ngoại lệ CSDL xảy ra.
     */
    public static boolean testConnection() {
        // Mở kết nối tới CSDL trong khối try-with-resources để tự động đóng tài nguyên
        try (Connection conn = DBConnect.getConnection()) {
            // Trả về true nếu đối tượng Connection khác null và kết nối đang mở
            return conn != null && !conn.isClosed();
        } catch (SQLException ex) {
            // Ghi nhật ký lỗi nếu phát sinh SQLException trong quá trình lấy kết nối
            System.getLogger(TestDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    /**
     * Kiểm tra kết nối CSDL và trả về chuỗi thông điệp mô tả chi tiết trạng thái hoặc lỗi.
     *
     * @return Chuỗi mô tả trạng thái kết nối chi tiết (Ví dụ: bao gồm URL kết nối hoặc thông điệp lỗi).
     */
    public static String testConnectionDetailed() {
        // Thử thiết lập kết nối CSDL MySQL
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return "Connection is null (failed to obtain).";
            if (conn.isClosed()) return "Connection is closed.";
            // Trả về thông báo kết nối thành công kèm theo URL CSDL MySQL
            return "Connection successful: " + conn.getMetaData().getURL();
        } catch (SQLException ex) {
            // Ghi nhận vết lỗi hệ thống
            System.getLogger(TestDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return "Connection failed: " + ex.getMessage();
        }
    }

    /**
     * Phương thức main để chạy trực tiếp quá trình kiểm thử kết nối CSDL từ cửa sổ dòng lệnh/IDE.
     *
     * @param args Các tham số dòng lệnh truyền vào.
     */
    public static void main(String[] args) {
        // In kết quả kiểm thử chi tiết ra System Log
        System.getLogger(TestDBConnection.class.getName()).log(System.Logger.Level.INFO, testConnectionDetailed());
    }

}
