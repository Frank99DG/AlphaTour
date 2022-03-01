package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText nome, cognome, dataNascita, username, email, password,mDateFormat;
    FirebaseFirestore db;
    DatePickerDialog.OnDateSetListener onDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        db = FirebaseFirestore.getInstance();

        nome = findViewById(R.id.registerInputNome);
        cognome = findViewById(R.id.registerInputCognome);
        dataNascita = findViewById(R.id.registerInputDataNascita);
        username = findViewById(R.id.registerInputUsername);
        email = findViewById(R.id.registerInputEmail);
        password = findViewById(R.id.registerInputPassword);



    }

    public void insertUserOnDB(View v) {

        String Nome = nome.getText().toString();
        String Cognome = nome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("Nome", Nome);
        user.put("Cognome", Cognome);
        user.put("Data di Nascita", DataNascita);
        user.put("Username", Username);
        user.put("Email", Email);
        user.put("Password", Password);

        db.collection("user")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegisterActivity.this, "Succeful", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void chooseBirthDate(View v){

        final Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog= new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;
                dataNascita.setText(date);

            }
        };
     }
}