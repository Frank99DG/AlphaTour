package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private ProgressBar barraCaricamento;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.recoveryInputEmail);
        barraCaricamento = findViewById(R.id.recoveryBarraCaricamento);

    }


    public boolean inputControl(String Email){

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

        return errorFlag;

    }


    public void sendRecoveryEmail(View v){

        String Email = email.getText().toString();

        Boolean errorFlag = inputControl(Email);

        if (errorFlag) {
            return;

        } else {

            barraCaricamento.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.controlla_email), Toast.LENGTH_SHORT).show();
                        barraCaricamento.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.errore_generico), Toast.LENGTH_SHORT).show();
                        barraCaricamento.setVisibility(View.GONE);
                    }
                }
            });




        }

    }

}