package com.anahuac.proyectofinal.todolist;

public class ToDoModel {
    private int id, status;
    private String task, user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getUser(){return user;}

    public void setUser(String user) {this.user = user;}
}
