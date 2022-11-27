package com.anahuac.proyectofinal.calendar;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventModel {
    private static DatabaseHandler db;
    private int id;
    private String event, time, date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static ArrayList<EventModel> eventsList = new ArrayList<>();

    public static List<EventModel> eventsForDate(LocalDate date)
    {
        List<EventModel> events = db.getAllEvents(date.toString());

        return events;
    }
}
