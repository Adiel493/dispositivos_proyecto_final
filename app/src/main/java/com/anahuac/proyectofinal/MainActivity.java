package com.anahuac.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anahuac.proyectofinal.calendar.CalendarActivity;
import com.anahuac.proyectofinal.todolist.TodoList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCalendar(View view){
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void openTodo(View view){
        Intent intent = new Intent(this, TodoList.class);
        startActivity(intent);
    }
}