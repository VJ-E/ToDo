package com.todo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import io.github.cdimascio.dotenv.Dotenv;


public class DatabaseConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/todo";
    public static final String USERNAME = "root";
    public static final String PASSWORD = Dotenv.load().get("DB_PASSWORD");

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            System.out.println("JDBC Driver is missing");
            System.exit(1);
        }
    }

    public Connection getDBConnection() throws SQLException{
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }


}
