/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test.DB;

import DAO.DBConnect;
import java.sql.*;

/**
 *
 * @author gmtfarcb
 */
public class TestDBConnection {
    public static boolean testConnection() {
        try (Connection conn = DBConnect.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException ex) {
            System.getLogger(TestDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }

    public static String testConnectionDetailed() {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return "Connection is null (failed to obtain).";
            if (conn.isClosed()) return "Connection is closed.";
            return "Connection successful: " + conn.getMetaData().getURL();
        } catch (SQLException ex) {
            System.getLogger(TestDBConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return "Connection failed: " + ex.getMessage();
        }
    }

    public static void main(String[] args) {
        System.out.println(testConnectionDetailed());
    }
    
}
