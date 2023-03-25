package com.example.projet2;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
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
    private EventAdapter eventsAdapter;
    private static final int EDIT_EVENT_REQUEST_CODE = 2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        // Get events from MainActivity (or any other way you prefer)
        events = ((MainActivity) getActivity()).getEvents();

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        TextView monthYearTextView = view.findViewById(R.id.monthYearTextView);
        ListView eventsListView = view.findViewById(R.id.eventsListView);

        // Set up the RecyclerView
        eventsAdapter = new EventAdapter(getContext(), new ArrayList<>());
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

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = eventsAdapter.getItem(position);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        "transition_event"
                );
                Intent intent = new Intent(getContext(), EditEventActivity.class);
                intent.putExtra("event", event);
                intent.putExtra("position", position);
                startActivityForResult(intent, EDIT_EVENT_REQUEST_CODE, options.toBundle());
            }
        });

        eventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = eventsAdapter.getItem(position);
                showDeleteConfirmationDialog(event, position);
                return true;
            }
        });

        displayEventIndicators();

        return view;
    }

    void displayEventIndicators() {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault());
        outputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (com.example.projet2.Event event : events) {
            String dateStr = event.getDate();
            try {
                Date eventDate = inputDateFormat.parse(dateStr);
                long eventTimeInMillis = eventDate.getTime();

                com.github.sundeepk.compactcalendarview.domain.Event compactCalendarEvent =
                        new com.github.sundeepk.compactcalendarview.domain.Event(eventsAdapter.getColorFromSubject(event.getSubject()), eventTimeInMillis);
                calendarView.addEvent(compactCalendarEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDeleteConfirmationDialog(final Event event, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Event")
                .setMessage("Do you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Event deletedEvent = eventsAdapter.events.remove(position);
                        List<Event> eventsUpdated = ((MainActivity) getActivity()).getEvents();
                        eventsUpdated.remove(deletedEvent);
                        eventsAdapter.notifyDataSetChanged();
                        ((MainActivity) getActivity()).saveEventsToSharedPreferences(eventsUpdated);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just close the dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EVENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);
            Event editedEvent = (Event) data.getSerializableExtra("event");

            if (position >= 0 && editedEvent != null) {
                List<Event> eventsUpdated = ((MainActivity) getActivity()).getEvents();
                eventsUpdated.set(eventsUpdated.indexOf(eventsAdapter.events.get(position)), editedEvent);
                eventsAdapter.events.set(position, editedEvent);
                eventsAdapter.notifyDataSetChanged();
                ((MainActivity) getActivity()).saveEventsToSharedPreferences(eventsUpdated);
            }
        }
    }
}