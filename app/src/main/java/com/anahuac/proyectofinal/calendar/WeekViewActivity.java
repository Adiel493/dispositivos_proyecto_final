package com.anahuac.proyectofinal.calendar;

import static com.anahuac.proyectofinal.calendar.CalendarUtils.daysInWeekArray;
import static com.anahuac.proyectofinal.calendar.CalendarUtils.monthYearFromDate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anahuac.proyectofinal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, DialogCloseListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView, eventRecyclerView;
    private FloatingActionButton fabweek;
    private DatabaseHandler db;
    private CalendarAdapter calendarAdapter;
    private List<EventModel> eventList;
    private EventAdapter eventAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        db = new DatabaseHandler(this);
        db.openDatabase();
        initWidgets();
        setWeekView();

    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabweek = findViewById(R.id.fabweek);
        fabweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewEvent.newInstance().show(getSupportFragmentManager(), AddNewEvent.TAG);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventAdapter()
    {
        eventAdapter = new EventAdapter(db, WeekViewActivity.this);
        eventRecyclerView.setAdapter(eventAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(eventAdapter));
        itemTouchHelper.attachToRecyclerView(eventRecyclerView);
        eventList = db.getAllEvents(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        if(eventList != null){
            Collections.reverse(eventList);
            eventAdapter.setEvents(eventList);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        eventList =  db.getAllEvents(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        Collections.reverse(eventList);
        eventAdapter.setEvents(eventList);
        eventAdapter.notifyDataSetChanged();
    }
}