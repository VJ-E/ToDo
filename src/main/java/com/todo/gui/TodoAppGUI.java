package com.todo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.todo.dao.TodoAppDAO;
import com.todo.model.Todo;

import java.time.LocalDateTime;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;


public class TodoAppGUI extends JFrame {
    private TodoAppDAO todoDAO;
    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;

    public TodoAppGUI() {
        this.todoDAO = new TodoAppDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadTodos();
    }
    private void initializeComponents() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setLocationRelativeTo(null);
        String[] columnNames = {"ID", "Title", "Description", "Completed", "Created At", "Updated At"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        todoTable = new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(
            e -> {
                if(!e.getValueIsAdjusting()){
                    loadSelectedTodo();
                }
            }
        );
        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        completedCheckBox = new JCheckBox("Completed");
        addButton = new JButton("Add Todo");
        updateButton = new JButton("Update Todo");
        deleteButton = new JButton("Delete Todo");
        refreshButton = new JButton("Refresh Todo");
        String[] filterOptions = {"All", "Completed", "Pending"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.addActionListener(e -> {
            // filterTodos();
        });
    }
    private void setupLayout(){
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx  = 0;
        gbc.gridy = 0;

        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("Title"),gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField,gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description"),gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(descriptionArea),gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(completedCheckBox,gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);
        
        // add(inputPanel,BorderLayout.NORTH);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(filterPanel,BorderLayout.NORTH);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);

        add(northPanel,BorderLayout.NORTH);

        add(new JScrollPane(todoTable),BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a todo to update or delete"));
        add(statusPanel,BorderLayout.SOUTH);
    }

    private void setupEventListeners(){
        addButton.addActionListener((e) -> {addTodo();}); 
        updateButton.addActionListener((e) -> {updateTodo();});
        deleteButton.addActionListener((e) -> {deleteTodo();});
        refreshButton.addActionListener((e) -> {refreshTodo();});

    }

    private void addTodo(){
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();
        
        if(title.isEmpty()){
            JOptionPane.showMessageDialog(this,"Title is required","Validation Error",JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            Todo todo = new Todo(title,description);
            todo.setCompleted(completed);
            
            todoDAO.createtodo(todo);

            JOptionPane.showMessageDialog(this,"Todo Added Successfully","Success",JOptionPane.INFORMATION_MESSAGE);

            loadTodos();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Error Adding todo :"+e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void updateTodo(){
        int row = todoTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this, "Please select a a row to update","Validation Error",JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = titleField.getText().trim();
        if(title.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter a title","Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int)todoTable.getValueAt(row,0);
        String description = descriptionArea.getText().trim();
        try{
            Todo todo = todoDAO.getTodoById(id);
            
            if(todo != null){
                todo.setTitle(title);
                todo.setDescription(description);
                todo.setCompleted(completedCheckBox.isSelected());

                if(todoDAO.updateTodo(todo)){
                    JOptionPane.showMessageDialog(this,"Todo updated Successfully","Success",JOptionPane.INFORMATION_MESSAGE);
                    loadTodos();
                }
                else{
                    JOptionPane.showMessageDialog(this,"Failed to update todo","Update Error",JOptionPane.ERROR_MESSAGE);
                }
            } 
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Update Error :"+e.getMessage(),"Update Error",JOptionPane.ERROR_MESSAGE);
        }


    }
    private void deleteTodo(){
        int row = todoTable.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(this, "Please select a row to delete","Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int)todoTable.getValueAt(row,0);
        try{
            if(todoDAO.deleteTodo(id)){
                JOptionPane.showMessageDialog(this, "Todo deleted successfully","Delete Success",JOptionPane.INFORMATION_MESSAGE);
                loadTodos();
            }
            else{
                JOptionPane.showMessageDialog(this,"Failed to delete todo","Delete Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Delete Error : "+e.getMessage());
        }
    }
    private void refreshTodo(){

    }


    private void loadTodos(){
        try{
            List<Todo> todos = todoDAO.getAllTodos();
            updateTable(todos);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Error Loading todos : "+e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo t : todos){
            Object[] row = {t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isCompleted(),
                t.getCreated_at(),
                t.getUpdated_at()
            };
            
            tableModel.addRow(row);

        }
    }

    private void loadSelectedTodo(){
        int row = todoTable.getSelectedRow();
        if (row != -1){
            String title = tableModel.getValueAt(row,1).toString();
            String description = tableModel.getValueAt(row,2).toString();
            boolean completed = (boolean)tableModel.getValueAt(row, 3);

            titleField.setText(title);
            descriptionArea.setText(description);
            completedCheckBox.setSelected(completed);
        }

    }
    
}