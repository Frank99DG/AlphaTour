package com.example.alphatour.wizardpercorso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class PercorsoWizard extends AppCompatActivity {

    StepperLayout stepperLayout;
    Button button;
    private StepAdapter stepAdapter;
    private int i;

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