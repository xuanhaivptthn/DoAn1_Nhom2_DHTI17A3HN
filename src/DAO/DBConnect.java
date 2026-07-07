package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
     public static Connection getConnection() {
        String dbPort = "3306";
        String dbUsername = "root";
        String dbPassword = "";

        String dbName = "DoAn1_QuanLySanBong";

        String dbUrl = "jdbc:mysql://localhost:" + dbPort + "/" + dbName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            System.getLogger(DBConnect.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }
}

