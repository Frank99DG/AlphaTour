package com.example.alphatour.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alphatour.R;
import com.example.alphatour.objectclass.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET, hourEventET;
    private TextView eventDateTV, eventTimeTV;
    private LocalTime time;
    private LocalTime timeSelected;
    private int hour,minute;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String idEvent;
    private static boolean success=false, load=true,visible=false;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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

        saveEvent();
        String eventName = eventNameET.getText().toString();
        EventProva newEvent = new EventProva(eventName, CalendarUtils.selectedDate, timeSelected);
        EventProva.eventsList.add(newEvent);
        finish();
    }

    public void saveEvent(){

        Event event = new Event( eventNameET.getText().toString(), user.getUid(), hourEventET.getText().toString(), eventDateTV.getText().toString(), eventTimeTV.getText().toString());

        db.collection("Events")
                .add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                idEvent = documentReference.getId();
                Toast.makeText(EventEditActivity.this, "Salvataggio dell'evento riuscito", Toast.LENGTH_LONG).show();
                success=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EventEditActivity.this, "Salvataggio dell'evento non riuscito", Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });
    }


    /*public void showEvents(){

        db.collection("Events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: listDocument){

                        if(d.getId().matches(idUser))
                    }


                }

            }
        })


    }*/

    public void onBackButtonClick(View view){
        finish();
    }
}