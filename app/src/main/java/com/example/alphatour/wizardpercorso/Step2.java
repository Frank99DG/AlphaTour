package com.example.alphatour.wizardpercorso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class Step2 extends Fragment implements Step, BlockingStep {

    private LinearLayout list_zone;
    private boolean control= false;
    private List <CheckableTextView> arrayZone = new ArrayList<CheckableTextView>();
    private int i=0;
    private int n = -1;
    private static int  zona_scelta;
    private static String[] array_database = {"Zona centrale", "Zona principale del museo", "Zona quadri"};
    private static String stringa_scelta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        list_zone = view.findViewById(R.id.list_zone);

        for(int i =0; i< array_database.length; i++){
            View zone = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso,null ,false);
            CheckableTextView textZone1 = (CheckableTextView) zone.findViewById(R.id.textZone1);

            textZone1.setText(array_database[i]);

            arrayZone.add(textZone1);
            list_zone.addView(zone);
        }

        for(i=0; i< arrayZone.size(); i++){

            arrayZone.get(i).setOnCheckChangeListener(new CheckedListener() {
                @Override
                public void onCheckChange(@NonNull View view, boolean b) {
                    int c = 0;                                          //contatore per capire quando ne seleziona 2

                    for(int a=0; a<arrayZone.size();a++){              //ciclo per contare quanti sono checkati
                        if(arrayZone.get(a).isChecked()){ c++;}
                    }

                   for(int j=0; j<arrayZone.size();j++) {               //se è checkato più di uno, il precedente che è checkato viene impostato a false
                       if (c > 1) {
                           if (n == j) {
                               arrayZone.get(j).setChecked(false, false);
                           }
                       }
                   }

                   for(int k=0; k<arrayZone.size();k++){                //serve per tenere la posizione dell'ultimo checkato
                       if(arrayZone.get(k).isChecked()){
                           n=k;  continue;
                       }
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        for(int i= 0;i<arrayZone.size();i++){
            arrayZone.get(i).setChecked(false,false);
        }
/*
        Bundle bundle = new Bundle();
        bundle.putString("zona scelta", array[zona_scelta]); // Put anything what you want
        Step3 fragment3 = new Step3();
        fragment3.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments,fragment3).commit();
        Fragment fragment = new Fragment();

 */     //getFragmentManager().beginTransaction().detach(new Fragment()).attach(new Fragment()).commit();
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
        for(int i=0; i<arrayZone.size(); i++){
            if (!arrayZone.get(i).isChecked()) {
                control=false;
            } else {
                control=true;
                zona_scelta = i;
                stringa_scelta = array_database[i];
                return null;
            }
        }
        if(control==false) error =new VerificationError("Seleziona una zona di partenza");
        return error;
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

    public static String getStringa_scelta() {
        return stringa_scelta;
    }
}