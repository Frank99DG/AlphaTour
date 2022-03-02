package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nome, cognome, dataNascita, username, email, password,mDateFormat;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ProgressBar barraCaricamento;
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nome = findViewById(R.id.registerInputNome);
        cognome = findViewById(R.id.registerInputCognome);
        dataNascita = findViewById(R.id.registerInputDataNascita);
        username = findViewById(R.id.registerInputUsername);
        email = findViewById(R.id.registerInputEmail);
        password = findViewById(R.id.registerInputPassword);

        barraCaricamento = findViewById(R.id.registerBarraCaricamento);

    }

    public void insertUserOnDB(View v) {

        String Nome = nome.getText().toString();
        String Cognome = nome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        if(Nome.isEmpty()){
            nome.setError("@string/campo_obbligatorio");
            nome.requestFocus();
            return;
        }

        if(Cognome.isEmpty()){
            cognome.setError("@string/campo_obbligatorio");
            cognome.requestFocus();
            return;
        }

        if(DataNascita.isEmpty()){
            dataNascita.setError("@string/campo_obbligatorio");
            dataNascita.requestFocus();
            return;
        }

        if(Username.isEmpty()){
            username.setError("@string/campo_obbligatorio");
            username.requestFocus();
            return;
        }

        if(Email.isEmpty()){
            email.setError("@string/campo_obbligatorio");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email.setError("@string/email_non_valida");
            email.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            password.setError("@string/campo_obbligatorio");
            password.requestFocus();
            return;
        }

        if(password.length() < 8 /*METTERE CONDIZIONI: min 1 MAIU , min 1 CAR SPECIAL*/){
            password.setError("@string/password_vincoli");
            password.requestFocus();
            return;
        }

        barraCaricamento



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