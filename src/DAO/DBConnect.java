package DAO;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp quản lý và cung cấp kết nối Cơ sở dữ liệu MySQL cho hệ thống Quản lý Sân bóng.
 * <p>
 * Lớp này sử dụng kỹ thuật Dynamic Proxy để tạo ra đối tượng {@link Connection} ảo.
 * Khi các lớp DAO gọi phương thức {@code close()} trên connection này, proxy sẽ ngăn
 * việc đóng kết nối thực tế nhằm tối ưu hiệu năng và tái sử dụng kết nối.
 * </p>
 *
 * @author Nhom2_DHTI17A3HN
 */
public class DBConnect {

    /** Đường dẫn JDBC kết nối tới CSDL MySQL DoAn1_QuanLySanBong */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DoAn1_QuanLySanBong?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";

    /** Tên tài khoản truy cập CSDL */
    private static final String DB_USER = "root";

    /** Mật khẩu truy cập CSDL */
    private static final String DB_PASS = "";

    /** Đối tượng kết nối thực tế đến MySQL (được dùng chung) */
    private static Connection realConnection = null;

    /**
     * Khởi tạo mặc định cho lớp DBConnect.
     */
    public DBConnect() {
    }

    /**
     * Khởi tạo hoặc lấy ra kết nối JDBC thực tế (Singleton connection).
     *
     * @return Đối tượng {@link Connection} kết nối thực tế tới CSDL.
     * @throws SQLException Nếu xảy ra lỗi trong quá trình kết nối CSDL.
     * @throws ClassNotFoundException Nếu không tìm thấy Driver JDBC MySQL.
     */
    private static synchronized Connection getRealConnection() throws SQLException, ClassNotFoundException {
        // Kiểm tra xem kết nối chưa tồn tại, đã bị đóng hoặc không còn hợp lệ (timeout 2s)
        if (realConnection == null || realConnection.isClosed() || !realConnection.isValid(2)) {
            // Nạp driver MySQL JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Thiết lập kết nối thực tế với URL, USER và PASS
            realConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }
        return realConnection;
    }

    /**
     * Lấy ra một đại diện Connection (Dynamic Proxy) phục vụ cho thao tác CSDL.
     * <p>
     * Phương thức close() của connection này được bọc lại để không làm đóng realConnection.
     * </p>
     *
     * @return Đối tượng {@link Connection} proxy, hoặc {@code null} nếu kết nối thất bại.
     */
    public static Connection getConnection() {
        try {
            // Đảm bảo realConnection đã được khởi tạo và hợp lệ
            getRealConnection();
            // Tạo Dynamic Proxy cho interface Connection
            return (Connection) Proxy.newProxyInstance(
                    DBConnect.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {
                        // Bỏ qua lời gọi hàm close() để duy trì realConnection
                        if ("close".equals(method.getName())) {
                            // Không đóng kết nối dùng chung thực tế để tái sử dụng connection
                            return null;
                        }
                        try {
                            // Chuyển tiếp lời gọi hàm tới realConnection
                            return method.invoke(getRealConnection(), args);
                        } catch (java.lang.reflect.InvocationTargetException e) {
                            throw e.getCause();
                        }
                    }
            );
        } catch (Exception ex) {
            // Bắt lỗi và in ra stderr nếu không tạo được kết nối
            System.err.println("Lỗi kết nối DBConnect: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Kiểm tra trạng thái kết nối tới Cơ sở dữ liệu.
     *
     * @return {@code true} nếu kết nối thành công và còn hợp lệ, ngược lại {@code false}.
     */
    public static boolean testConnection() {
        try {
            // Lấy kết nối thực tế và kiểm tra tính hợp lệ
            Connection conn = getRealConnection();
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (Exception ex) {
            // Trả về false nếu có bất kỳ ngoại lệ nào xảy ra
            return false;
        }
    }

    /**
     * Đóng kết nối thực tế tới Cơ sở dữ liệu (thường gọi khi dừng ứng dụng).
     */
    public static synchronized void closeRealConnection() {
        if (realConnection != null) {
            try {
                // Kiểm tra và đóng kết nối thực nếu chưa đóng
                if (!realConnection.isClosed()) {
                    realConnection.close();
                }
            } catch (Exception ignored) {
                // Bỏ qua ngoại lệ khi đóng kết nối
            }
            // Đặt lại biến realConnection về null
            realConnection = null;
        }
    }
}
