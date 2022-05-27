package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.connection.Receiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class LoginActivity extends AppCompatActivity {

    //CHIAVI PER ONSAVEINSTANCESTATE
    private static final String KEY_EMAIL="EmailUtente";
    private static final String KEY_PASSWORD="PasswordUtente";

    private EditText email, password;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Receiver receiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()

                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.loginInputEmail);
        password = findViewById(R.id.loginInputPassword);
        loadingBar = findViewById(R.id.loginLoadingBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        /**controllo connessione**/
        receiver=new Receiver();

        broadcastIntent();
    }

    private void broadcastIntent() {
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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


    public boolean inputControl(String Email, String Password){

        Boolean errorFlag = false;

        if (Email.isEmpty()) {
            email.setError(getString(R.string.required_field));
            email.requestFocus();
            errorFlag = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError(getString(R.string.invalid_email));
            email.requestFocus();
            errorFlag = true;
        }


        if (Password.isEmpty()) {
            password.setError(getString(R.string.required_field));
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
                                    Toast.makeText(LoginActivity.this, R.string.access_completed, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    loadingBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
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