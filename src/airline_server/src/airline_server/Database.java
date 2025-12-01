/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airline_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Aaditya
 */
public class Database {
     public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/airline",
                    "root",
                    "password"
            );
        }catch (SQLException e){
            System.out.println("Database connection error");
            return null;
        }
    }
}
