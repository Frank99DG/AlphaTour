package com.example.alphatour.wizardpercorso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class PercorsoWizard extends AppCompatActivity {

    StepperLayout stepperLayout;
    Button button;
    private StepAdapter stepAdapter;
    private int i;
    private static String zone;
    private static boolean zonePassFromReview=false;

    public static boolean isZonePassFromReview() {
        return zonePassFromReview;
    }

    public static void setZonePassFromReview(boolean zonePassFromReview) {
        PercorsoWizard.zonePassFromReview = zonePassFromReview;
    }

    public static String getZone() {
        return zone;
    }

    public static void setZone(String zone) {
        PercorsoWizard.zone = zone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorso_wizard);

        stepperLayout=findViewById(R.id.stepperLayout);
       // button=findViewById(R.id.buttonShare);

        Intent intent=getIntent();
        i= intent.getIntExtra("val",-1);

        if(intent.getIntExtra("val",-1)==-1) {
            stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
            stepperLayout.setAdapter(stepAdapter);
        }else{
            stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
            stepperLayout.setAdapter(stepAdapter);
            stepperLayout.setCurrentStepPosition(i);
        }

        stepAdapter = new StepperAdapterWizard(getSupportFragmentManager(), getApplicationContext());
        stepperLayout.setAdapter(stepAdapter);

    }


    //per rimuovere il focus e la tastiera quando si clicca fuori dalla EditText
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

   /* public void ShareFile(View view) {

        String textMessage="send intent";
        Intent sendIntent=new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }*/
}