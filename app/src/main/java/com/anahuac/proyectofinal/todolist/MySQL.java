package com.anahuac.proyectofinal.todolist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL{

    public static final String DATABASE_NAME = "achiever";
    public static final String url = "jdbc:mysql://achiever.cvzzlgerihu7.us-east-1.rds.amazonaws.com:3306/" + DATABASE_NAME;
    public static final String username = "admin", password = "admin123";
    public static final String TABLE_NAME = "TASKS";

    public MySQL(){}

    public void insertTask(ToDoModel task){
        String taskStr = task.getTask();
        int status = task.getStatus();
        String user = task.getUser();
        int id = task.getId();

        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("INSERT INTO " + TABLE_NAME + "(TASK_ID, TASK, STATUS, USER) VALUES(" + id + ",'" + taskStr + "', " + status + ", '" + user + "')");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateStatus(int id, int status){
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("UPDATE " + TABLE_NAME + " SET STATUS = " + status + " WHERE TASK_ID = " + id );
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateTask(int id, String task) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("UPDATE " + TABLE_NAME + " SET TASK = '" + task + "' WHERE TASK_ID = "+id);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void deleteTask(int id){
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("DELETE FROM " + TABLE_NAME + " WHERE TASK_ID = "+id);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
