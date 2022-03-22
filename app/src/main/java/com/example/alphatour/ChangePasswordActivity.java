package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editPassword,editPasswordNew;
    private ProgressBar progressBar;
    private FirebaseAuth authprofile;
    private Button btnAuth,btnAuthNewPassword;
    private String currentPassword;
    private FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editPassword=findViewById(R.id.passwordInsertAuth);
        editPasswordNew=findViewById(R.id.updatePassword);
        progressBar=findViewById(R.id.changePasswordProgressBar);
        btnAuth=findViewById(R.id.auth);
        btnAuthNewPassword=findViewById(R.id.authNew);

        //Disabilita edit text e bottone autenticazione dopo l'autenticazione
        editPasswordNew.setEnabled(false);
        btnAuthNewPassword.setEnabled(false);
        btnAuthNewPassword.setBackgroundColor(getResources().getColor(R.color.gray_button));


        authprofile = FirebaseAuth.getInstance();
        user=authprofile.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        if(user.equals("")){
            Toast.makeText(this,R.string.errore_visualizzazione_dati_profilo,
                    Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(ChangePasswordActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else{
            reAuthenticateUser(user);
        }

    }

    //ri-autenticazione dell'utente prima di cambiare password
    private void reAuthenticateUser(FirebaseUser user) {

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPassword=editPassword.getText().toString();
                if(TextUtils.isEmpty(currentPassword)){
                    Toast.makeText(ChangePasswordActivity.this,R.string.password_necessaria,
                            Toast.LENGTH_SHORT).show();
                    editPassword.setError(getString(R.string.password_necessaria_set_error));
                    editPassword.requestFocus();
                }else{

                    progressBar.setVisibility(View.VISIBLE);
                    authprofile.signInWithEmailAndPassword(user.getEmail(), currentPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        //disabilito tasto e edit text password autenticazione
                                        progressBar.setVisibility(View.GONE);
                                        editPassword.setEnabled(false);
                                        btnAuth.setEnabled(false);
                                        btnAuth.setBackgroundColor(getResources().getColor(R.color.gray_button));

                                        //abilito l'inserimento della nuova password
                                        btnAuthNewPassword.setEnabled(true);
                                        editPasswordNew.setEnabled(true);

                                        Toast.makeText(ChangePasswordActivity.this,R.string.riautenticazione_con_successo,
                                                Toast.LENGTH_SHORT).show();

                                        btnAuthNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this,
                                                R.color.DarkBlue));

                                        btnAuthNewPassword.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                changePassword(user);
                                            }
                                        });

                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Autenticazione fallita, password errata!"/*getString(R.string.login_fallito)*/, Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }

                            });
                }
            }
        });

    }


    private void changePassword(FirebaseUser user) {
        Toast.makeText(ChangePasswordActivity.this, "change password", Toast.LENGTH_LONG).show();
    }
}