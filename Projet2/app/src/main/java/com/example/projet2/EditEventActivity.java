package com.example.projet2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditEventActivity extends AppCompatActivity {
    // ...
    private EditText titleEditText, dateEditText, timeEditText, coeffEditText, subjectNameEditText;
    private Spinner typeSpinner,subjectSpinner;
    private Button addSubjectButton;
    private List<Subject> subjects;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        coeffEditText = findViewById(R.id.coeffEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);

        subjects = loadSubjects();
        setupSubjectSpinner();


        setupDateAndTimePickers();
        String[] types = new String[]{"Examen", "Oral", "TP Noté"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");

        if (event != null) {
            titleEditText.setText(event.getTitle());
            dateEditText.setText(event.getDate());
            timeEditText.setText(event.getTime());
            coeffEditText.setText(String.valueOf(event.getCoefficient()));
            typeSpinner.setSelection(adapter.getPosition(event.getType()));
        }

        Button saveEventButton = findViewById(R.id.saveEventButton);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

    }

    private void setupDateAndTimePickers() {
        final Calendar calendar = Calendar.getInstance();

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month++; // Months are zero-based
                                String dateString = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", month) + "-" + year;
                                dateEditText.setText(dateString);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String timeString = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                                timeEditText.setText(timeString);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void saveEvent() {
        String title = titleEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        int coefficient = Integer.parseInt(coeffEditText.getText().toString());
        String type = typeSpinner.getSelectedItem().toString();
        Subject selectedSubject = (Subject) subjectSpinner.getSelectedItem();

        Event e = new Event(title, date, time, coefficient, type, selectedSubject);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("event", e);
        resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void setupSubjectSpinner() {
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
    }

    private List<Subject> loadSubjects() {
        SharedPreferences sharedPreferences = getSharedPreferences("subjects", MODE_PRIVATE);
        String json = sharedPreferences.getString("subjectList", null);
        Type type = new TypeToken<ArrayList<Subject>>() {}.getType();
        List<Subject> subjectList = new Gson().fromJson(json, type);

        if (subjectList == null) {
            subjectList = new ArrayList<>();
        }
        return subjectList;
    }

}
