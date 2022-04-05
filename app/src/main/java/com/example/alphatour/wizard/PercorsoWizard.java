package com.example.alphatour.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alphatour.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class PercorsoWizard extends AppCompatActivity {

    StepperLayout stepperLayout;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorso_wizard);

        stepperLayout=findViewById(R.id.stepperLayout);
       // button=findViewById(R.id.buttonShare);

        StepAdapter stepAdapter=new StepperAdapterWizard(getSupportFragmentManager(),getApplicationContext());
        stepperLayout.setAdapter(stepAdapter);
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