package com.example.alphatour.wizardcreateplace;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alphatour.R;
import com.example.alphatour.wizardcreatepath.StepperAdapterWizard;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class CreationWizard extends AppCompatActivity {

    private static StepperLayout stepperLayout;
    private static StepperLayout.StepperListener step;
    private static int i;
    private static int j=-1;
    private StepAdapter stepAdapter;

public static void setvalore(int val){
    j=val;
}


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_wizard);

        stepperLayout=findViewById(R.id.stepperLayoutCreazione);
       // button=findViewById(R.id.buttonShare);
       /* Intent intent=getIntent();
        int i=intent.getIntExtra("val",-1);*/

        Intent intent=getIntent();
        i= intent.getIntExtra("val",-1);
        if(intent.getIntExtra("val",-1)==-1) {
            stepAdapter = new StepperAdapterCreazioneWizard(getSupportFragmentManager(), getApplicationContext()/*, i*/);
            stepperLayout.setAdapter(stepAdapter);
        }else{
            Toast.makeText(this,"landscapeeeeeeeeeeee",Toast.LENGTH_LONG).show();
            stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
            stepperLayout.setAdapter(stepAdapter);
            stepperLayout.setCurrentStepPosition(i);
        }

        if(j!=-1){
            stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
            stepperLayout.setAdapter(stepAdapter);
            stepperLayout.setCurrentStepPosition(j);
        }

       /* StepViewModel.Builder stepViewModel= new StepViewModel.Builder(this);
        stepViewModel.setBackButtonVisible(false);*/

    }

    //per rimuovere il focus e la tastiera quando si clicca fuori dalla EditText e/o AutoCompleteTextView
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof AutoCompleteTextView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}