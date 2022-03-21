package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editPassword,editPasswordNew;
    private ProgressBar progressBar;
    private FirebaseAuth authprofile;
    FirebaseUser utente;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editPassword=findViewById(R.id.passwordInsertAuth);
        editPasswordNew=editPassword=findViewById(R.id.updatePassword);

    }

    public void authenticate(View view) {


    }

    public void chagePassord(View view) {
    }
}