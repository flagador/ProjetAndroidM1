package com.example.projet2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private EditText titleEditText, dateEditText, timeEditText, coeffEditText, subjectNameEditText;
    private Spinner typeSpinner,subjectSpinner;
    private Button addSubjectButton;
    private List<Subject> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        coeffEditText = findViewById(R.id.coeffEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        subjectNameEditText = findViewById(R.id.subjectNameEditText);
        addSubjectButton = findViewById(R.id.addSubjectButton);
        subjectSpinner = findViewById(R.id.subjectSpinner);

        subjects = loadSubjects();
        setupSubjectSpinner();
        setupAddSubjectButton();

        setupDateAndTimePickers();
        setupTypeSpinner();

        Button addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String dateString = dateEditText.getText().toString();
                String timeString = timeEditText.getText().toString();
                Subject selectedSubject = (Subject) subjectSpinner.getSelectedItem();
                int coefficient = 0;
                if(!coeffEditText.getText().toString().equals("")) {
                    coefficient = Integer.parseInt(coeffEditText.getText().toString());
                }
                String type = typeSpinner.getSelectedItem().toString();

                if(title.equals("") || dateString.equals("") || timeString.equals("") || coefficient==0 || selectedSubject==null){
                    Toast.makeText(getApplicationContext(),"Missing value :(", Toast.LENGTH_LONG).show();
                } else {
                    Event event = new Event(title, dateString, timeString, coefficient, type, selectedSubject);
                    //saveEvent(event);

                    // Pass the event back to MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("event", event);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
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

    private void setupTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    private void setupSubjectSpinner() {
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
    }

    private void setupAddSubjectButton() {
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = subjectNameEditText.getText().toString();
                if (!subjectName.isEmpty()) {
                    Subject newSubject = new Subject(subjectName);
                    subjects.add(newSubject);
                    saveSubjects(subjects);
                    subjectNameEditText.setText("");
                    setupSubjectSpinner();
                }
            }
        });
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

    private void saveSubjects(List<Subject> subjectList) {
        SharedPreferences sharedPreferences = getSharedPreferences("subjects", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(subjectList);
        editor.putString("subjectList", json);
        editor.apply();
    }
}