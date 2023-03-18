package com.example.projet2;

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

        return view;
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
        if (eventAdapter != null) {
            eventAdapter.events.clear();
            eventAdapter.events.addAll(events);
            eventAdapter.notifyDataSetChanged();
        } else {
            eventAdapter = new EventAdapter(getContext(), events);
            eventsListView.setAdapter(eventAdapter);
            setupSortSpinner(requireView().findViewById(R.id.sortSpinner));
        }
    }
}