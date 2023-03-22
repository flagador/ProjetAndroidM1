package com.example.projet2;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DailyFragment extends Fragment {
    private ListView eventsListView;
    private EventAdapter eventAdapter;
    private static final int EDIT_EVENT_REQUEST_CODE = 2;

    public DailyFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        eventsListView = view.findViewById(R.id.eventsListView);
        Spinner sortSpinner = view.findViewById(R.id.sortSpinner);
        setupSortSpinner(sortSpinner);
        List<Event> events = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext(), events);
        eventsListView.setAdapter(eventAdapter);

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = eventAdapter.getItem(position);
                Intent intent = new Intent(getContext(), EditEventActivity.class);
                intent.putExtra("event", event);
                intent.putExtra("position", position);
                startActivityForResult(intent, EDIT_EVENT_REQUEST_CODE);
            }
        });

        eventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = eventAdapter.getItem(position);
                showDeleteConfirmationDialog(event, position);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EVENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);
            Event editedEvent = (Event) data.getSerializableExtra("event");

            if (position >= 0 && editedEvent != null) {
                eventAdapter.events.set(position, editedEvent);
                eventAdapter.notifyDataSetChanged();
                ((MainActivity) getActivity()).saveEventsToSharedPreferences(eventAdapter.events);
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
                        eventAdapter.events.remove(position);
                        eventAdapter.notifyDataSetChanged();
                        ((MainActivity) getActivity()).saveEventsToSharedPreferences(eventAdapter.events);
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

    private void setupSortSpinner(Spinner sortSpinner) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (eventAdapter != null) {
                    switch (position) {
                        case 0: // Sort by date
                            eventAdapter.sortByDate();
                            break;
                        case 1: // Sort by coefficient
                            eventAdapter.sortByCoefficient();
                            break;
                        case 2: // Sort by type
                            eventAdapter.sortByType();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Call this method to update the events list with the actual data
    public void updateEvents(List<Event> events) {
        eventAdapter = new EventAdapter(getContext(), events);
        eventsListView.setAdapter(eventAdapter);
        setupSortSpinner(requireView().findViewById(R.id.sortSpinner));
    }
}