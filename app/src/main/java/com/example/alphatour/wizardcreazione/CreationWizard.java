package com.example.alphatour.wizardcreazione;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alphatour.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class CreationWizard extends AppCompatActivity {

    StepperLayout stepperLayout;
    StepperLayout.StepperListener step;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_wizard);

        stepperLayout=findViewById(R.id.stepperLayoutCreazione);
       // button=findViewById(R.id.buttonShare);
       /* Intent intent=getIntent();
        int i=intent.getIntExtra("val",-1);*/

            StepAdapter stepAdapter = new StepperAdapterCreazioneWizard(getSupportFragmentManager(), getApplicationContext()/*, i*/);
            stepperLayout.setAdapter(stepAdapter);

       /* StepViewModel.Builder stepViewModel= new StepViewModel.Builder(this);
        stepViewModel.setBackButtonVisible(false);*/

    }

}