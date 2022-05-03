package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.recoveryInputEmail);
        loadingBar = findViewById(R.id.recoveryLoadingBar);

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


    public boolean inputControl(String Email){

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

        return errorFlag;

    }


    public void sendRecoveryEmail(View v){

        String Email = email.getText().toString();

        Boolean errorFlag = inputControl(Email);

        if (errorFlag) {
            return;

        } else {

            loadingBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                        loadingBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.required_field), Toast.LENGTH_SHORT).show();
                        loadingBar.setVisibility(View.GONE);
                    }
                }
            });




        }

    }

}