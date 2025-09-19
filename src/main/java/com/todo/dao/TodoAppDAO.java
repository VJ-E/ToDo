package com.todo.dao;

import com.todo.util.DatabaseConnection;
import com.todo.model.Todo;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import java.sql.*;

public class TodoAppDAO {

    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos ORDER BY created_at DESC";
    private static final String INSERT_TODO = "INSERT INTO todos(title,description,completed,created_at,updated_at) VALUES(?,?,?,?,?)";
    private static final String SELECT_TODO_BY_ID = "SELECT * FROM todos WHERE id = ?";
    private static final String UPDATE_TODO = "UPDATE todos SET title = ?, description = ?, completed = ?, updated_at = ? WHERE id = ?";
    private static final String DELETE_TODO = "DELETE FROM todos WHERE id = ?";
    private static final String FILTER_TODO = "SELECT * FROM todos WHERE completed = ?"; 

    public int createtodo(Todo todo) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1,todo.getTitle());
            stmt.setString(2,todo.getDescription());
            stmt.setBoolean(3,todo.isCompleted());
            stmt.setTimestamp(4, Timestamp.valueOf(todo.getCreated_at()));
            stmt.setTimestamp(5, Timestamp.valueOf(todo.getUpdated_at()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if(rowsAffected == 0){
                throw new SQLException("Creating todo failed, no rows affected.");
            }

            try(ResultSet generatedKeys = stmt.getGeneratedKeys();){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Creating todo failed, no ID obtained.");
                }
            }
        }
    }

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

    public Todo getTodoById(int todoId) throws SQLException{
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_TODO_BY_ID);
        )
        {
            stmt.setInt(1,todoId);
            try(ResultSet res = stmt.executeQuery();){
                if(res.next()){
                    return getTodoRow(res);
                }
            }
        }
        return null;
    }

    public boolean updateTodo(Todo todo) throws SQLException{
        try(Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TODO);
        )
        {
            stmt.setString(1,todo.getTitle());
            stmt.setString(2,todo.getDescription());
            stmt.setBoolean(3,todo.isCompleted());
            stmt.setTimestamp(4,Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(5,todo.getId());
            
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public boolean deleteTodo(int todoId) throws SQLException{
        try(Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_TODO);
        ){
            stmt.setInt(1,todoId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Todo> getTodosByFilter(String filter) throws SQLException{

        List<Todo> todos = new ArrayList<Todo>();

        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(FILTER_TODO);
        )
        {
            stmt.setBoolean(1,filter.equals("Completed"));
            try(ResultSet res = stmt.executeQuery();){
                while(res.next()){
                    todos.add(getTodoRow(res));
                }
                return todos;
            }
        }
    }
}
