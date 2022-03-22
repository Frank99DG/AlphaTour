package com.example.alphatour.prova;

import static com.example.alphatour.prova.CalendarUtilsProva.selectedDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alphatour.R;

import java.time.format.TextStyle;
import java.util.Locale;

public class DailyCalendarActivityProva extends AppCompatActivity {

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar_prova);
        initWidgets();
    }

    private void initWidgets() {

        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDayView();
    }

    private void setDayView() {

        monthDayText.setText(CalendarUtilsProva.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
    }


    public void previousDayAction(View view) {

        CalendarUtilsProva.selectedDate = CalendarUtilsProva.selectedDate.minusDays(1);
        setDayView();
    }


    public void nextDayAction(View view) {

        CalendarUtilsProva.selectedDate = CalendarUtilsProva.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view) {

        startActivity(new Intent(this, EventEditActivityProva.class));
    }
}