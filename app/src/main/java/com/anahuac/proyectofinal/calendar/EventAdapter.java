package com.anahuac.proyectofinal.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anahuac.proyectofinal.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
{

    private List<EventModel> eventList;
    private DatabaseHandler db;
    private WeekViewActivity activity;

    public EventAdapter(DatabaseHandler db, WeekViewActivity activity)
    {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_cell, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final EventModel item = eventList.get(position);
        holder.event.setText(item.getEvent()+"\n"+item.getTime());
    }

    @Override
    public int getItemCount(){return eventList.size();}

    public Context getContext() {
        return activity;
    }

    public void setEvents(List<EventModel> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        EventModel item = eventList.get(position);
        db.deleteEvent(item.getId());
        eventList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        EventModel item = eventList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("event", item.getEvent());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        AddNewEvent fragment = new AddNewEvent();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewEvent.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView event;

        ViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.eventTextView);
        }
    }
}
