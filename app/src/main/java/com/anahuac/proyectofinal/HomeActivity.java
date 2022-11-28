package com.anahuac.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anahuac.proyectofinal.calendar.CalendarActivity;
import com.anahuac.proyectofinal.todolist.TodoList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    TextView userName;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userName.setText("¡Bienvenido "+user.getDisplayName()+"!");
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