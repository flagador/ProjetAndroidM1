package com.example.projet2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventAdapter extends BaseAdapter {
    List<Event> events;
    LayoutInflater inflater;

    public EventAdapter(Context context, List<Event> events) {
        this.events = events;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void sortByDate() {
        Collections.sort(events, Comparator.comparing(Event::getDate).thenComparing(Event::getTime));
        notifyDataSetChanged();
    }

    public void sortByCoefficient() {
        Collections.sort(events, Comparator.comparingInt(Event::getCoefficient).reversed());
        notifyDataSetChanged();
    }

    public void sortByType() {
        Collections.sort(events, Comparator.comparing(Event::getType));
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_event, parent, false);
            holder = new ViewHolder();
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            holder.timeTextView = convertView.findViewById(R.id.timeTextView);
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.coefficientTextView = convertView.findViewById(R.id.coefficientTextView);
            holder.typeTextView = convertView.findViewById(R.id.typeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event event = getItem(position);
        holder.dateTextView.setText(event.getDate());
        holder.timeTextView.setText("Time: " + event.getTime());
        holder.titleTextView.setText(event.getTitle());
        holder.coefficientTextView.setText("  Coefficient: " + event.getCoefficient());
        holder.typeTextView.setText("  Type: " + event.getType());

        return convertView;
    }
    public void updateEvents(List<Event> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }
    private static class ViewHolder {
        TextView dateTextView;
        TextView timeTextView;
        TextView titleTextView;
        TextView coefficientTextView;
        TextView typeTextView;
    }
}
