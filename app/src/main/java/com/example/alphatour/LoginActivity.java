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

import com.example.alphatour.wizard.PercorsoWizard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    //CHIAVI PER ONSAVEINSTANCESTATE
    private static final String KEY_EMAIL="EmailUtente";
    private static final String KEY_PASSWORD="PasswordUtente";

    private EditText email, password;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.loginInputEmail);
        password = findViewById(R.id.loginInputPassword);
        loadingBar = findViewById(R.id.loginLoadingBar);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void openForgotPasswordActivity(View v){
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Log.i("tag1","Entra in onSave");

        outState.putCharSequence(KEY_EMAIL, email.getText());
        outState.putCharSequence(KEY_PASSWORD, password.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Log.i("tag2","Entra in onRestore");

        this.email.setText(savedInstanceState.getCharSequence(KEY_EMAIL));
        this.password.setText(savedInstanceState.getCharSequence(KEY_PASSWORD));

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

            loadingBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Accesso compiuto", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, PercorsoWizard.class /*AddPlaceActivity.class*//*DashBoardActivity.class*/));
                                    loadingBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.login_fallito), Toast.LENGTH_LONG).show();
                                loadingBar.setVisibility(View.GONE);
                            }

                        }

                    });
        }

    }

    public void openRegisterActivity(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

}