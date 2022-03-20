package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.oggetti.Utente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    //CHIAVI PER ONSAVEINSTANCESTATE
    private static final String KEY_NOME="NomeUtente";
    private static final String KEY_COGNOME="CognomeUtente";
    private static final String KEY_DATA_NASCITA="CognomeUtente";
    private static final String KEY_USERNAME="UsernameUtente";
    private static final String KEY_EMAIL="EmailUtente";
    private static final String KEY_PASSWORD="PasswordUtente";

    //attributi privati per db e edittex data
    private EditText nome, cognome, dataNascita, username, email, password;
    private ProgressBar barraCaricamento;
    private String idUtente;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Log.i("tag1","Entra in onSave");
        outState.putCharSequence(KEY_NOME, nome.getText());
        outState.putCharSequence(KEY_COGNOME, cognome.getText());
        outState.putCharSequence(KEY_DATA_NASCITA, dataNascita.getText());
        outState.putCharSequence(KEY_USERNAME, username.getText());
        outState.putCharSequence(KEY_EMAIL, email.getText());
        outState.putCharSequence(KEY_PASSWORD, password.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Log.i("tag2","Entra in onRestore");

        this.nome.setText(savedInstanceState.getCharSequence(KEY_NOME));
        this.cognome.setText(savedInstanceState.getCharSequence(KEY_COGNOME));
        this.dataNascita.setText(savedInstanceState.getCharSequence(KEY_DATA_NASCITA));
        this.username.setText(savedInstanceState.getCharSequence(KEY_USERNAME));
        this.email.setText(savedInstanceState.getCharSequence(KEY_EMAIL));
        this.password.setText(savedInstanceState.getCharSequence(KEY_PASSWORD));

    }

    public void chooseBirthDate(View v){

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;
                dataNascita.setText(date);

            }
        };

        DatePickerDialog datePickerDialog= new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();


    }


    public boolean inputControl(String Nome,String Cognome,String DataNascita,String Username,String Email,String Password){
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

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError(getString(R.string.email_non_valida));
            email.requestFocus();
            errorFlag = true;
        }

        if( password.length() < 8 || !(Password.matches("(.*[0-9].*)")) || !(Password.matches("(.*[|!$%&/()=?^@#§<>,;.:_*+].*)")) ){
            password.setError(getString(R.string.password_vincoli));
            password.requestFocus();
            errorFlag = true;
        }
        return errorFlag;
    }

    public long saveUserOnDbLocal(String Nome,String Cognome,String DataNascita,String Username,String Email){

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(this);
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues valori = new ContentValues();
        long newRowId;

        //valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID,2);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_NOME,Nome);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_COGNOME,Cognome);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_DATA_NASCITA,DataNascita);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_USERNAME,Username);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_EMAIL,Email);

        newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,valori);

         return newRowId;

    }


    public void saveUserOnDbRemote(String Nome,String Cognome,String DataNascita,String Username,String Email,String Password){

        barraCaricamento.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            idUtente = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            Utente utente = new Utente(Nome, Cognome, DataNascita, Username, Email);

                            DocumentReference documentReference = db.collection("Utenti").document(idUtente);

                            documentReference.set(utente)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.registrazione_completata), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            barraCaricamento.setVisibility(View.GONE);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.registrazione_fallita) , Toast.LENGTH_LONG).show();
                                    barraCaricamento.setVisibility(View.GONE);
                                }
                            });
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "failed firebase" , Toast.LENGTH_LONG).show();
                            barraCaricamento.setVisibility(View.GONE);
                        }
                    }
                });

    }


    public void saveUserOnDb(View v) {

        String Nome = nome.getText().toString();
        String Cognome = cognome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();


        boolean errorFlag = inputControl(Nome,Cognome,DataNascita,Username,Email,Password);

        if(errorFlag) {
            return;

        }else {

            long result = saveUserOnDbLocal(Nome,Cognome,DataNascita,Username,Email);

            if(result == -1){
                Toast.makeText(RegisterActivity.this, "Errore db local", Toast.LENGTH_LONG).show();
            }else{
                //saveUserOnDbRemote(Nome,Cognome,DataNascita,Username,Email,Password);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                barraCaricamento.setVisibility(View.GONE);
                finish();
            }



        }



    }

}