package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alphatour.R;

import java.time.LocalTime;

public class EventEditActivityProva extends AppCompatActivity {

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit_prova);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtilsProva.formattedDate(CalendarUtilsProva.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtilsProva.formattedTime(time));
    }

    private void initWidgets() {

        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }


    public void saveEventAction(View view) {

        String eventName = eventNameET.getText().toString();
        EventProva newEvent = new EventProva(eventName, CalendarUtilsProva.selectedDate, time);
        EventProva.eventsList.add(newEvent);
        finish();
    }
}