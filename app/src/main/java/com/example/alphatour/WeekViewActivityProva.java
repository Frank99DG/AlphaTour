package com.example.alphatour;

import static com.example.alphatour.prova.CalendarUtilsProva.daysInWeekArray;
import static com.example.alphatour.prova.CalendarUtilsProva.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.alphatour.prova.CalendarAdapterProva;
import com.example.alphatour.prova.CalendarUtilsProva;
import com.example.alphatour.prova.EventEditActivityProva;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivityProva extends AppCompatActivity implements CalendarAdapterProva.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecycleView;

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
    }

    private void setWeekView() {

        monthYearText.setText(monthYearFromDate(CalendarUtilsProva.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtilsProva.selectedDate);

        CalendarAdapterProva calendarAdapter = new CalendarAdapterProva(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecycleView.setLayoutManager(layoutManager);
        calendarRecycleView.setAdapter(calendarAdapter);
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


    public void newEventAction(View view) {

        startActivity(new Intent(this, EventEditActivityProva.class));
    }
}