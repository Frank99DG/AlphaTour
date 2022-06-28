package com.example.alphatour.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.alphatour.R;

import java.time.LocalTime;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET, hourEventET;
    private TextView eventDateTV, eventTimeTV;
    private LocalTime time;
    private LocalTime timeSelected;
    private int hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
    }

    private void initWidgets() {

        eventNameET = findViewById(R.id.eventNameET);
        hourEventET = findViewById(R.id.hourEventET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);

    }


    public void popTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                hour = selectedHour;
                minute = selectedMinute;
                timeSelected = LocalTime.of(hour,minute);
                hourEventET.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }


    public void saveEventAction(View view) {

        String eventName = eventNameET.getText().toString();
        EventProva newEvent = new EventProva(eventName, CalendarUtils.selectedDate, timeSelected);
        EventProva.eventsList.add(newEvent);
        finish();
    }

    public void onBackButtonClick(View view){
        finish();
    }
}