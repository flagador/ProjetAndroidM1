package com.example.projetm1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class EventFormActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private Spinner mTypeEditText;
    private DatePicker mDatePicker;
    private TimePicker mHourEditText; // new field for the hour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form_activity);
        mTitleEditText = findViewById(R.id.event_title_edit_text);
        mTypeEditText = findViewById(R.id.event_type_spinner);
        mDatePicker = findViewById(R.id.event_date_picker);
        mHourEditText = findViewById(R.id.edit_text_hour); // find the new hour EditText field

        Calendar calendar = Calendar.getInstance();
        mHourEditText.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mHourEditText.setCurrentMinute(calendar.get(Calendar.MINUTE));

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent(v);
            }
        });
    }

    public void saveEvent(View view) {
        // Get the values entered by the user
        String title = mTitleEditText.getText().toString();
        String type = mTypeEditText.getSelectedItem().toString();
        int hour = mHourEditText.getHour(); // get the value of the new hour EditText field
        int minute = mHourEditText.getMinute();
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int dayOfMonth = mDatePicker.getDayOfMonth();

        // Create a new Date object with the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();

        // Create a new Event object with the entered data
        Event event = new Event(date, title, type, hour, minute);

        // Send the new Event object back to the MainActivity
        Intent intent = new Intent();
        intent.putExtra("event", event);
        setResult(RESULT_OK, intent);
        finish();
    }
}