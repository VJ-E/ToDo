package com.todo;

import java.sql.Connection;
import java.sql.SQLException;

import com.todo.util.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db_connection = new DatabaseConnection();
        try{
            Connection cn = db_connection.getDBConnection();
            System.out.println("Database connection has been established");
        }
        catch(SQLException e){
            System.out.println("Database connection has failed");
        }
    }
}