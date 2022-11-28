package com.anahuac.proyectofinal.calendar;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventModel {
    private static DatabaseHandler db;
    private int id;
    private String event, time, date, user;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<EventModel> eventsForDate(LocalDate date, String user)
    {
        String dateS = CalendarUtils.formattedDate(date);
        List<EventModel> events = db.getAllEvents(dateS, user);

        return events;
    }
}
