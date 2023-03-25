package com.example.projet2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

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

    public void sortBySubject() {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                return event1.getSubject().getName().compareToIgnoreCase(event2.getSubject().getName());
            }
        });
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
            holder.subjectTextView = convertView.findViewById(R.id.subjectTextView);
            holder.coefficientTextView = convertView.findViewById(R.id.coefficientTextView);
            holder.typeTextView = convertView.findViewById(R.id.typeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Event event = getItem(position);
        MaterialCardView cardView = convertView.findViewById(R.id.card_view);
        int color = getColorFromSubject(event.getSubject());
        cardView.setCardBackgroundColor(color);
        holder.dateTextView.setText(event.getDate());
        holder.timeTextView.setText("Time: " + event.getTime());
        holder.titleTextView.setText(event.getTitle());
        holder.subjectTextView.setText(event.getSubject().getName());
        holder.coefficientTextView.setText("  Coefficient: " + event.getCoefficient());
        holder.typeTextView.setText("  Type: " + event.getType());

        return convertView;
    }
    public void updateEvents(List<Event> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    int getColorFromSubject(Subject subject) {
        int hash = subject.getName().hashCode();
        int red = (hash & 0xFF0000) >> 16;
        int green = (hash & 0x00FF00) >> 8;
        int blue = hash & 0x0000FF;

        // You can adjust the brightness of the color if needed (e.g., to make it lighter).
        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        hsv[1] = Math.min(hsv[1], 0.8f); // Limit saturation
        hsv[2] = Math.max(hsv[2], 1.0f); // Limit brightness

        return Color.HSVToColor(hsv);
    }
    private static class ViewHolder {
        TextView subjectTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView titleTextView;
        TextView coefficientTextView;
        TextView typeTextView;
    }
}
