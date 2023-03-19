package com.example.projet2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MonthlyFragment extends Fragment {

    private CompactCalendarView calendarView;
    private List<com.example.projet2.Event> events;
    private SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault());
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        // Get events from MainActivity (or any other way you prefer)
        events = ((MainActivity) getActivity()).getEvents();

        calendarView = view.findViewById(R.id.calendarView);
        TextView monthYearTextView = view.findViewById(R.id.monthYearTextView);
        ListView eventsListView = view.findViewById(R.id.eventsListView);

        // Set up the RecyclerView
        EventAdapter eventsAdapter = new EventAdapter(getContext(), new ArrayList<>());
        eventsListView.setAdapter(eventsAdapter);

        // Set the initial month and year TextView value
        monthYearTextView.setText(monthYearFormat.format(calendarView.getFirstDayOfCurrentMonth()));
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date clickedDay) {
                String selectedDate = dateFormat.format(clickedDay);
                List<com.example.projet2.Event> selectedDayEvents = new ArrayList<>();

                for (com.example.projet2.Event event : events) {
                    if (event.getDate().equals(selectedDate)) {
                        selectedDayEvents.add(event);
                    }
                }

                eventsAdapter.updateEvents(selectedDayEvents);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthYearTextView.setText(monthYearFormat.format(firstDayOfNewMonth));
            }
        });

        displayEventIndicators();

        return view;
    }

    private void displayEventIndicators() {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault());
        outputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (com.example.projet2.Event event : events) {
            String dateStr = event.getDate();
            try {
                Date eventDate = inputDateFormat.parse(dateStr);
                long eventTimeInMillis = eventDate.getTime();

                com.github.sundeepk.compactcalendarview.domain.Event compactCalendarEvent =
                        new com.github.sundeepk.compactcalendarview.domain.Event(Color.BLUE, eventTimeInMillis);
                calendarView.addEvent(compactCalendarEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}