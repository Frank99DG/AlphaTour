package com.example.alphatour.calendar;

import static com.example.alphatour.calendar.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyCalendarActivity extends AppCompatActivity {

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        initWidgets();
        showEvents();
    }

    private void initWidgets() {

        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
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

    @Override
    protected void onResume() {
        super.onResume();
        setDayView();
    }

    private void setDayView() {

        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter() {

        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }


    private ArrayList<HourEvent> hourEventList() {

        ArrayList<HourEvent> list = new ArrayList<>();
        for(int hour = 0; hour < 24;hour++){

            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<EventProva> events = EventProva.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }


    public void previousDayAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }


    public void nextDayAction(View view) {

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view) {

        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void onBackButtonClick(View view){

        startActivity(new Intent(DailyCalendarActivity.this, WeeklyCalendarActivity.class));
        finish();
    }
}