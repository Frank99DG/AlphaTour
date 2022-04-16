package com.example.alphatour.wizardpercorso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class Step2 extends Fragment implements Step, BlockingStep {

    private LinearLayout list_zone;
    private Button buttonAdd;
    private boolean control= false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        list_zone = view.findViewById(R.id.list_zone);
        buttonAdd = view.findViewById(R.id.addZone);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              control = addZone();
            }
        });

        return view;
    }

    public boolean addZone(){
        View zone = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso,null ,false);
        CheckableTextView textZone1 = (CheckableTextView) zone.findViewById(R.id.textZone1);
        CheckableTextView textZone2 = (CheckableTextView) zone.findViewById(R.id.textZone2);
        CheckableTextView textZone3 = (CheckableTextView) zone.findViewById(R.id.textZone3);

        textZone1.setText("Zona centrale");
        textZone2.setText("Zona principale del museo");
        textZone3.setText("Zona quadri");

        textZone1.setOnCheckChangeListener(new CheckedListener() {
            @Override
            public void onCheckChange(@NonNull View view, boolean b) {
                if(textZone1.isChecked()) {control=true;} else control=false;   //controllo errore
                if(textZone2.isChecked() ||  textZone3.isChecked()){
                    textZone2.setChecked(false,false);
                    textZone3.setChecked(false,false);
                }
            }
        });

        textZone2.setOnCheckChangeListener(new CheckedListener() {
            @Override
            public void onCheckChange(@NonNull View view, boolean b) {
                if(textZone2.isChecked()) {control=true;} else control=false;   //controllo errore
                if(textZone1.isChecked() || textZone3.isChecked()){
                    textZone1.setChecked(false,false);
                    textZone3.setChecked(false,false);
                }
            }
        });

        textZone3.setOnCheckChangeListener(new CheckedListener() {
            @Override
            public void onCheckChange(@NonNull View view, boolean b) {
                if(textZone3.isChecked()) {control=true;} else control=false;   //controllo errore
                if(textZone1.isChecked() || textZone2.isChecked()){
                textZone1.setChecked(false,false);
                textZone2.setChecked(false,false);
                }
            }
        });

        list_zone.addView(zone);
        return control;


    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError error=null;
        if(control==false){
            error =new VerificationError("Seleziona una zona di partenza");
        } return error;
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSelected() {

    }


    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

}