package com.todo.dao;

import com.todo.model.Todo;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDateTime;

import java.sql.SQLException;
import com.todo.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TodoAppDAO {

    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos ORDER BY created_at DESC";

    private Todo getTodoRow(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String title  = rs.getString("title");
        String description = rs.getString("description");
        Boolean completed = rs.getBoolean("completed");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        
        Todo todo = new Todo(id,title,description,completed,createdAt,updatedAt);

        return todo;
    }
    
    public List<Todo> getAllTodos() throws SQLException{
        List<Todo> todos = new ArrayList<Todo>();

        try (Connection conn = DatabaseConnection.getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TODOS);
        ResultSet res = stmt.executeQuery();
        ) {
            while(res.next()){
                todos.add(getTodoRow(res));
            }
        }
        return todos;
    }
}
