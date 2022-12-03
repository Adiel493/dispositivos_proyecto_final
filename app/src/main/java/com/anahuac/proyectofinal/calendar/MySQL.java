package com.anahuac.proyectofinal.calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQL {
    public static final String DATABASE_NAME = "achiever";
    public static final String url = "jdbc:mysql://achiever.cvzzlgerihu7.us-east-1.rds.amazonaws.com:3306/" + DATABASE_NAME;
    public static final String username = "admin", password = "admin123";
    public static final String TABLE_NAME = "EVENTS";
    public ResultSet rs;
    public List<EventModel> eventList;

    public MySQL(){}

    public void insertEvent(EventModel event){
        String eventStr = event.getEvent();
        String time = event.getTime();
        String date = event.getDate();
        String user = event.getUser();
        int id = event.getId();

        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("INSERT INTO " + TABLE_NAME + "(EVENT_ID, EVENT, DATE, TIME, USER) VALUES(" + id + ",'" + eventStr + "', '" + date + "' , '"+ time + "', '" + user + "')");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateEvent(int id, String event){
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("UPDATE " + TABLE_NAME + " SET EVENT= '" + event + "' WHERE EVENT_ID = " + id );
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void updateTime(int id, String time) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("UPDATE " + TABLE_NAME + " SET TIME = '" + time + "' WHERE EVENT_ID = "+id);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void deleteEvent(int id){
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                statement.execute("DELETE FROM " + TABLE_NAME + " WHERE EVENT_ID = "+id);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public List<EventModel> getAllEvents(String date, String user) {
        new Thread(() -> {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME + " WHERE USER = " + user + " AND DATE = "+date);
                eventList = new ArrayList<>();

                while (rs.next()) {
                    EventModel event = new EventModel();
                    event.setId(rs.getInt(1));
                    event.setEvent(rs.getString(2));
                    event.setDate(rs.getString(3));
                    event.setTime(rs.getString(4));
                    event.setUser(rs.getString(5));
                    eventList.add(event);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return eventList;
    }
}
