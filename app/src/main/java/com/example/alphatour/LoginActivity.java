package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private ProgressBar barraCaricamento;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.recoveryInputEmail);
        password = findViewById(R.id.loginInputPassword);
        barraCaricamento = findViewById(R.id.loginBarraCaricamento);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void openForgotPasswordActivity(View v){
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }


    public boolean inputControl(String Email, String Password){

        Boolean errorFlag = false;

        if (Email.isEmpty()) {
            email.setError(getString(R.string.campo_obbligatorio));
            email.requestFocus();
            errorFlag = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError(getString(R.string.email_non_valida));
            email.requestFocus();
            errorFlag = true;
        }


        if (Password.isEmpty()) {
            password.setError(getString(R.string.campo_obbligatorio));
            password.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }


    public void userLogin(View v) {

        String Email = email.getText().toString();
        String Password = password.getText().toString();

        Boolean errorFlag = inputControl(Email, Password);


        if (errorFlag) {
            return;

        } else {

            barraCaricamento.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                barraCaricamento.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.login_fallito), Toast.LENGTH_LONG).show();
                                barraCaricamento.setVisibility(View.GONE);
                            }

                        }

                    });
        }

    }

    public void openRegisterActivity(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

}