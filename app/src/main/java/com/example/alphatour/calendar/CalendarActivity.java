package com.example.alphatour.calendar;

import static com.example.alphatour.calendar.CalendarUtils.daysInMonthArray;
import static com.example.alphatour.calendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.alphatour.ChangePasswordActivity;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.UpdateProfileActivity;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }


    private void initWidgets() {

        calendarRecycleView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {

        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecycleView.setLayoutManager(layoutManager);
        calendarRecycleView.setAdapter(calendarAdapter);
    }


    public void previousMonthAction(View view){

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();

    }

    public void nextMonthAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {

        if(date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view) {

        startActivity(new Intent(this, WeekViewActivity.class));
    }

    public void onBackButtonClick(View view){

        startActivity(new Intent(CalendarActivity.this, DashboardActivity.class));
        finish();
    }
}