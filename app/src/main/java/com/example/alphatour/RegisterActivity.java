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
import com.example.alphatour.oggetti.User;
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
    private static final String KEY_NAME="NameUser";
    private static final String KEY_SURNAME="SurnameUser";
    private static final String KEY_DATE_BIRTH="DateBirthUser";
    private static final String KEY_USERNAME="UsernameUser";
    private static final String KEY_EMAIL="EmailUser";
    private static final String KEY_PASSWORD="PasswordUser";

    //attributi privati per db e edittex data
    private EditText name, surname, dateBirth, username, email, password;
    private ProgressBar loadingBar;
    private String idUser;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private FirebaseAuth auth;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


                name = findViewById(R.id.registerInputName);
                surname = findViewById(R.id.registerInputSurname);
                dateBirth = findViewById(R.id.registerInputDateBirth);
                username = findViewById(R.id.registerInputUsername);
                email = findViewById(R.id.registerInputEmail);
                password = findViewById(R.id.registerInputPassword);




        loadingBar = findViewById(R.id.registerLoadingBar);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Log.i("tag1","Entra in onSave");
        outState.putCharSequence(KEY_NAME, name.getText());
        outState.putCharSequence(KEY_SURNAME, surname.getText());
        outState.putCharSequence(KEY_DATE_BIRTH, dateBirth.getText());
        outState.putCharSequence(KEY_USERNAME, username.getText());
        outState.putCharSequence(KEY_EMAIL, email.getText());
        outState.putCharSequence(KEY_PASSWORD, password.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Log.i("tag2","Entra in onRestore");

        this.name.setText(savedInstanceState.getCharSequence(KEY_NAME));
        this.surname.setText(savedInstanceState.getCharSequence(KEY_SURNAME));
        this.dateBirth.setText(savedInstanceState.getCharSequence(KEY_DATE_BIRTH));
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
                dateBirth.setText(date);

            }
        };

        DatePickerDialog datePickerDialog= new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();


    }


    public boolean inputControl(String Name,String Surname,String DateBirth,String Username,String Email,String Password){
        Boolean errorFlag = false;

        if(Name.isEmpty()){
            name.setError(getString(R.string.required_field));
            name.requestFocus();
            errorFlag = true;
        }

        if(Surname.isEmpty()){
            surname.setError(getString(R.string.required_field));
            surname.requestFocus();
            errorFlag = true;
        }

        if(DateBirth.isEmpty()){
            dateBirth.setError(getString(R.string.required_field));
            dateBirth.requestFocus();
            errorFlag = true;
        }

        if(Username.isEmpty()){
            username.setError(getString(R.string.required_field));
            username.requestFocus();
            errorFlag = true;
        }

        if(Username.length() < 4 ){
            username.setError(getString(R.string.username_constraints));
            username.requestFocus();
            errorFlag = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError(getString(R.string.invalid_email));
            email.requestFocus();
            errorFlag = true;
        }

        if( password.length() < 8 || !(Password.matches("(.*[0-9].*)")) || !(Password.matches("(.*[|!$%&/()=?^@#§<>,;.:_*+].*)")) ){
            password.setError(getString(R.string.password_constraints));
            password.requestFocus();
            errorFlag = true;
        }
        return errorFlag;
    }

    public long saveUserOnDbLocal(String Name,String Surname,String DateBirth,String Username,String Email){

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(this);
        SQLiteDatabase db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();
        long newRowId;

        //valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID,2);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_NAME,Name);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_SURNAME,Surname);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_DATE_BIRTH,DateBirth);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_USERNAME,Username);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL,Email);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_IMAGE,"null");

        newRowId=db.insert(AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER,AlphaTourContract.AlphaTourEntry.COLUMN_NAME_NULLABLE,values);

         return newRowId;

    }


    public void saveUserOnDbRemote(String Name,String Surname,String DateBirth,String Username,String Email,String Password){

        loadingBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            idUser = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                            User user = new User(Name, Surname, DateBirth, Username, Email);

                            DocumentReference documentReference = db.collection("Users").document(idUser);

                            documentReference.set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, getString(R.string.registration_completed), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            loadingBar.setVisibility(View.GONE);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.registration_failed) , Toast.LENGTH_LONG).show();
                                    loadingBar.setVisibility(View.GONE);
                                }
                            });
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "failed firebase" , Toast.LENGTH_LONG).show();
                            loadingBar.setVisibility(View.GONE);
                        }
                    }
                });

    }



    public void saveUserOnDb(View v) {

        String Name = name.getText().toString();
        String Surname = surname.getText().toString();
        String DateBirth = dateBirth.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();


        boolean errorFlag = inputControl(Name,Surname,DateBirth,Username,Email,Password);

        if(errorFlag) {
            return;

        }else {

            long result = saveUserOnDbLocal(Name,Surname,DateBirth,Username,Email);

            if(result == -1){
                Toast.makeText(RegisterActivity.this, "Errore db local", Toast.LENGTH_LONG).show();
            }else{
                saveUserOnDbRemote(Name,Surname,DateBirth,Username,Email,Password);
                //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                //barraCaricamento.setVisibility(View.GONE);
                finish();
            }



        }



    }

}