package com.example.alphatour.prova;

import static com.example.alphatour.prova.CalendarUtilsProva.daysInWeekArray;
import static com.example.alphatour.prova.CalendarUtilsProva.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alphatour.R;
import com.example.alphatour.prova.CalendarAdapterProva;
import com.example.alphatour.prova.CalendarUtilsProva;
import com.example.alphatour.prova.EventAdapter;
import com.example.alphatour.prova.EventEditActivityProva;
import com.example.alphatour.prova.EventProva;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivityProva extends AppCompatActivity implements CalendarAdapterProva.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecycleView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view_prova);
        initWidgets();
        setWeekView();
    }

    private void initWidgets() {

        calendarRecycleView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView() {

        monthYearText.setText(monthYearFromDate(CalendarUtilsProva.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtilsProva.selectedDate);

        CalendarAdapterProva calendarAdapter = new CalendarAdapterProva(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecycleView.setLayoutManager(layoutManager);
        calendarRecycleView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view) {

        CalendarUtilsProva.selectedDate = CalendarUtilsProva.selectedDate.minusWeeks(1);
        setWeekView();
    }


    public void nextWeekAction(View view) {

        CalendarUtilsProva.selectedDate = CalendarUtilsProva.selectedDate.plusWeeks(1);
        setWeekView();
    }


    @Override
    public void onItemClick(int position, LocalDate date) {

            CalendarUtilsProva.selectedDate = date;
            setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {

        ArrayList<EventProva> dailyEvents = EventProva.eventsForDate(CalendarUtilsProva.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {

        startActivity(new Intent(this, EventEditActivityProva.class));
    }


    public void dailyAction(View view) {

        startActivity(new Intent(this, DailyCalendarActivityProva.class));
    }
}