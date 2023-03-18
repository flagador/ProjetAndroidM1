package com.example.projet2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditEventActivity extends AppCompatActivity {
    // ...
    private EditText titleEditText, dateEditText, timeEditText, coeffEditText;
    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        coeffEditText = findViewById(R.id.coeffEditText);
        typeSpinner = findViewById(R.id.typeSpinner);

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
    private void saveEvent() {
        String title = titleEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        int coefficient = Integer.parseInt(coeffEditText.getText().toString());
        String type = typeSpinner.getSelectedItem().toString();

        Event e = new Event(title, date, time, coefficient, type);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("event", e);
        resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
