package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
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
            Toast.makeText(this,R.string.profile_data_view_error,
                    Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(ChangePasswordActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else{
            reAuthenticateUser(user);
        }

    }

    //To remove focus and keyboard when click outside EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    //ri-autenticazione dell'utente prima di cambiare password
    private void reAuthenticateUser(FirebaseUser user) {

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPassword=editPassword.getText().toString();
                if(TextUtils.isEmpty(currentPassword)){
                    Toast.makeText(ChangePasswordActivity.this,R.string.password_required,
                            Toast.LENGTH_SHORT).show();
                    editPassword.setError(getString(R.string.password_required_set_error));
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

                                        Toast.makeText(ChangePasswordActivity.this,R.string.authentication_completed,
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
                                        Toast.makeText(ChangePasswordActivity.this, R.string.wrong_password/*getString(R.string.login_fallito)*/, Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }

                            });
                }
            }
        });

    }


    private void changePassword(FirebaseUser user) {
        String newPassword=editPasswordNew.getText().toString();

        if(TextUtils.isEmpty(newPassword)){
            Toast.makeText(ChangePasswordActivity.this,R.string.password_required,
                    Toast.LENGTH_SHORT).show();
            editPasswordNew.setError(getString(R.string.new_password_required_set_error));
            editPasswordNew.requestFocus();
        }else{
            if(newPassword.matches(currentPassword)){
                editPasswordNew.setError(getString(R.string.new_same_password));
                editPasswordNew.requestFocus();
            }else{
                progressBar.setVisibility(View.VISIBLE);

                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(ChangePasswordActivity.this, R.string.password_changed,
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            try{
                                throw task.getException();
                            }catch (Exception e){
                                Toast.makeText(ChangePasswordActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        }
    }
}