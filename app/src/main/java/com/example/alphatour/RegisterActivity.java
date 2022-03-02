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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {

    private EditText nome, cognome, dataNascita, username, email, password;
    private ProgressBar barraCaricamento;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


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

    public void saveUserOnDbRemote(View v) {

        String Nome = nome.getText().toString();
        String Cognome = nome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        Boolean errorFlag = false;

        if(Nome.isEmpty()){
            nome.setError(getString(R.string.campo_obbligatorio));
            nome.requestFocus();
            errorFlag = true;
        }

        if(Cognome.isEmpty()){
            cognome.setError(getString(R.string.campo_obbligatorio));
            cognome.requestFocus();
            errorFlag = true;
        }

        if(DataNascita.isEmpty()){
            dataNascita.setError(getString(R.string.campo_obbligatorio));
            dataNascita.requestFocus();
            errorFlag = true;
        }

        if(Username.isEmpty()){
            username.setError(getString(R.string.campo_obbligatorio));
            username.requestFocus();
            errorFlag = true;
        }

        if(Username.length() < 4 ){
            username.setError(getString(R.string.username_vincoli));
            username.requestFocus();
            errorFlag = true;
        }

        if(Email.isEmpty()){
            email.setError(getString(R.string.campo_obbligatorio));
            email.requestFocus();
            errorFlag = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError(getString(R.string.email_non_valida));
            email.requestFocus();
            errorFlag = true;
        }

        if(Password.isEmpty()){
            password.setError(getString(R.string.campo_obbligatorio));
            password.requestFocus();
            errorFlag = true;
        }

        if(password.length() < 8 /*METTERE CONDIZIONI: min 1 MAIU , min 1 CAR SPECIAL*/){
            password.setError(getString(R.string.password_vincoli));
            password.requestFocus();
            errorFlag = true;
        }

        if(errorFlag == true) {
            return;

        }else {


            barraCaricamento.setVisibility(View.GONE);
            auth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Utente utente = new Utente(Nome, Cognome, DataNascita, Username, Email);

                                db.collection("Utenti")
                                        .add(utente)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RegisterActivity.this, "Succeful", Toast.LENGTH_LONG).show();
                                                barraCaricamento.setVisibility(View.VISIBLE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                        barraCaricamento.setVisibility(View.GONE);
                                    }
                                });

                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                barraCaricamento.setVisibility(View.GONE);
                            }
                        }
                    });
        }



    }

    public void chooseBirthDate(View v){

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog= new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;
                dataNascita.setText(date);

            }
        };
     }
}