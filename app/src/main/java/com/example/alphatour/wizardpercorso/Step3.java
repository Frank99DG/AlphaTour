package com.example.alphatour.wizardpercorso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alphatour.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class Step3 extends Fragment implements Step, BlockingStep {

    public String stringa;
    private TextView stringaa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_step3, container, false);
       TextView stringaa = (TextView) view.findViewById(R.id.stringaa);
       Button abc = view.findViewById(R.id.ciollino);

       abc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               stringaa.setText(Step2.getStringa_scelta());
           }
       });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            stringa = bundle.getString("zona scelta");

        }

 */



    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
 /*
        String b = Step2.getA();
        stringa=b;
        stringaa.setText(stringa);

        */
       // stringaa.setText((Step2.getA()));
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}