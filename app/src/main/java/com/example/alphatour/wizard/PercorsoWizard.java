package com.example.alphatour.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.alphatour.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class PercorsoWizard extends AppCompatActivity {

    StepperLayout stepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorso_wizard);

        stepperLayout=findViewById(R.id.stepperLayout);

        StepAdapter stepAdapter=new StepperAdapterWizard(getSupportFragmentManager(),getApplicationContext());
        stepperLayout.setAdapter(stepAdapter);
    }
}