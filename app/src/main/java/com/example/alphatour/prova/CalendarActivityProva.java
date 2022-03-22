package com.example.alphatour.prova;

import static com.example.alphatour.prova.CalendarUtilsProva.daysInMonthArray;
import static com.example.alphatour.prova.CalendarUtilsProva.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.alphatour.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarActivityProva extends AppCompatActivity implements CalendarAdapterProva.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_prova);
        initWidgets();
        CalendarUtilsProva.selectedDate = LocalDate.now();
        setMonthView();
    }


    private void initWidgets() {

        calendarRecycleView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {

        monthYearText.setText(monthYearFromDate(CalendarUtilsProva.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtilsProva.selectedDate);

        CalendarAdapterProva calendarAdapter = new CalendarAdapterProva(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecycleView.setLayoutManager(layoutManager);
        calendarRecycleView.setAdapter(calendarAdapter);
    }


    public void previousMonthAction(View view){

        CalendarUtilsProva.selectedDate = CalendarUtilsProva.selectedDate.minusMonths(1);
        setMonthView();

    }

    public void nextMonthAction(View view) {

        CalendarUtilsProva.selectedDate = CalendarUtilsProva.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {

        if(date != null) {
            CalendarUtilsProva.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view) {

        startActivity(new Intent(this, WeekViewActivityProva.class));
    }
}