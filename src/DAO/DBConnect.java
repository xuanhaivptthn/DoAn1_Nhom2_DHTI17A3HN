package DAO;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DoAn1_QuanLySanBong?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static Connection realConnection = null;

    private static synchronized Connection getRealConnection() throws SQLException, ClassNotFoundException {
        if (realConnection == null || realConnection.isClosed() || !realConnection.isValid(2)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            realConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }
        return realConnection;
    }

    public static Connection getConnection() {
        try {
            getRealConnection();
            return (Connection) Proxy.newProxyInstance(
                    DBConnect.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {
                        if ("close".equals(method.getName())) {
                            // Không đóng kết nối dùng chung thực tế để tái sử dụng connection
                            return null;
                        }
                        try {
                            return method.invoke(getRealConnection(), args);
                        } catch (java.lang.reflect.InvocationTargetException e) {
                            throw e.getCause();
                        }
                    }
            );
        } catch (Exception ex) {
            System.err.println("Lỗi kết nối DBConnect: " + ex.getMessage());
            return null;
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getRealConnection();
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (Exception ex) {
            return false;
        }
    }

    public static synchronized void closeRealConnection() {
        if (realConnection != null) {
            try {
                if (!realConnection.isClosed()) {
                    realConnection.close();
                }
            } catch (Exception ignored) {}
            realConnection = null;
        }
    }
}

