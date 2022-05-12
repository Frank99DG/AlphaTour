package com.example.alphatour.wizardpercorso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alphatour.DashboardActivity;
import com.example.alphatour.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class Step1 extends Fragment implements Step, BlockingStep {

    private static EditText namePath, descriptionPath;
    private boolean errorFlag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        namePath = view.findViewById(R.id.namePath);
        descriptionPath = view.findViewById(R.id.descriptionPath);
        // Inflate the layout for this fragment
        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        VerificationError error=null;
        String NamePath = namePath.getText().toString();
        String DescriptionPath = descriptionPath.getText().toString();
        errorFlag = inputControl(NamePath, DescriptionPath);
        if(errorFlag){
            error =new VerificationError("Compila tutti i campi");
        }
        return error;
    }

    private boolean inputControl(String NamePath, String DescriptionPath) {

        Boolean errorFlag = false;

        if (NamePath.isEmpty()) {
            namePath.setError(getString(R.string.required_field));
            namePath.requestFocus();
            errorFlag = true;
        }

        if (DescriptionPath.isEmpty()) {
            descriptionPath.setError(getString(R.string.required_field));
            descriptionPath.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
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
        Intent intent= new Intent(getContext(), DashboardActivity.class);
        startActivity(intent);
    }

    public static String getDescriptionPath() {
        return descriptionPath.getText().toString();
    }

    public static String getNamePath() {
        return namePath.getText().toString();
    }

}