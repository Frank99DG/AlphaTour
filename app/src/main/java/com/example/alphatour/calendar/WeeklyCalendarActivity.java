package com.example.alphatour.calendar;

import static com.example.alphatour.calendar.CalendarUtils.daysInWeekArray;
import static com.example.alphatour.calendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alphatour.R;
import com.example.alphatour.objectclass.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeeklyCalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecycleView;
    private ListView eventListView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setContentView(R.layout.activity_weekly_calendar);
        initWidgets();
        setWeekView();
        showEvents();
    }

    private void initWidgets() {

        calendarRecycleView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView() {

        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecycleView.setLayoutManager(layoutManager);
        calendarRecycleView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }


    public void nextWeekAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }


    @Override
    public void onItemClick(int position, LocalDate date) {

            CalendarUtils.selectedDate = date;
            setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {

        ArrayList<EventProva> dailyEvents = EventProva.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {

        startActivity(new Intent(this, EventEditActivity.class));
    }


    public void dailyAction(View view) {

        startActivity(new Intent(this, DailyCalendarActivity.class));
    }

    public void onBackButtonClick(View view){

        startActivity(new Intent(WeeklyCalendarActivity.this, CalendarActivity.class));
        finish();
    }

    public LocalDate dateFromString(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    public LocalTime timeFromString(String time) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime;
    }

    public void showEvents(){

        db.collection("Events").whereEqualTo("idUser",user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: listDocument){

                        Event event = d.toObject(Event.class);
                        String date = event.getDate();
                        String time = event.getStartingTime();
                        try {
                            LocalDate dateEvent = dateFromString(date);
                            LocalTime localTime = timeFromString(time);
                            EventProva newEvent = new EventProva(event.getName(), dateEvent, localTime);
                            EventProva.eventsList.add(newEvent);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }


                }

            }
        });


    }
}