package com.example.alphatour.wizardcreazione;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.AddZoneActivity;
import com.example.alphatour.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;


public class CreateConstraintsWizard<zone_list> extends Fragment implements Step, BlockingStep {

    private boolean stepFlag;
    private EditText prova;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private LinearLayout layout_list;
    private ArrayList<String> zone_list=new ArrayList<>();


    public CreateConstraintsWizard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_create_constraints_wizard, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        layout_list = view.findViewById(R.id.listConstraintLayout);

        zone_list = CreateZoneWizard.getZone_list();

        printZones();

        return view;
    }



    public void printZones() {

        for (String nameZone : zone_list){

            final View zoneView = getLayoutInflater().inflate(R.layout.row_from_zone, null, false);
            TextView zone = (TextView) zoneView.findViewById(R.id.inputZone);
            zone.setText(nameZone);
            layout_list.addView(zoneView);
        }

    }




    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        VerificationError error = null;
        //String Prova = prova.getText().toString();
        //stepFlag = inputControl(Prova);

        if(stepFlag){
            error = new VerificationError("Non puoi tornare indietro");
        }
        return error;



    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(),error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }



    /*public boolean inputControl(String Prova){

        Boolean errorFlag = false;

        if (Prova.isEmpty()) {
            prova.setError(getString(R.string.campo_obbligatorio));
            prova.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }*/

}