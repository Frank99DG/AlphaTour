package com.example.alphatour.wizardpercorso;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devzone.checkabletextview.CheckableTextView;
import com.example.alphatour.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;


public class Step3 extends Fragment implements Step, BlockingStep {

    private boolean control= false;
    private LinearLayout list_object;
    private String stringa;
    private TextView stringaa;
    private String[] oggetti_zona1={"monumento 1","monumento 2", "monumento 3"};
    private String[] oggetti_zona2={"monumento 4","monumento 5", "monumento 6"};
    private String[] oggetti_zona3={"monumento 7","monumento 8", "monumento 9"};
    private List<CheckableTextView> oggetti_scelti = new ArrayList<CheckableTextView>();;
    private List<CheckableTextView> arrayMonumenti = new ArrayList<CheckableTextView>();
    private Dialog dialog;
    private Button dialog_avanti,dialog_aggiungizona;
    private List<View> togliview = new ArrayList<View>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_step3, container, false);
       stringaa = (TextView) view.findViewById(R.id.zone_selected);
       list_object = view.findViewById(R.id.list_object);

       dialog=new Dialog(getContext());
       dialog.setContentView(R.layout.dialog_step3);
       dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
       dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
       dialog.setCancelable(false);
       dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
       dialog_avanti = dialog.findViewById(R.id.btn_avanti);
       dialog_aggiungizona = dialog.findViewById(R.id.btn_aggiungiZona);




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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            stringaa.setText(Step2.getStringa_scelta());
            String scelta = Step2.getStringa_scelta();
            String a = "Zona centrale";
            String b = "Zona principale del museo";
            String c = "Zona quadri";

            if (scelta == a) {
                for (int i = 0; i < oggetti_zona1.length; i++) {
                    View object = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso, null, false);
                    CheckableTextView textZone1 = (CheckableTextView) object.findViewById(R.id.textZone1);

                    textZone1.setText(oggetti_zona1[i]);
                    arrayMonumenti.add(textZone1);
                    togliview.add(object);
                    list_object.addView(object);
                }

            }

            if (scelta == b) {
                for (int i = 0; i < oggetti_zona2.length; i++) {
                    View object = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso, null, false);
                    CheckableTextView textZone1 = (CheckableTextView) object.findViewById(R.id.textZone1);

                    textZone1.setText(oggetti_zona2[i]);
                    arrayMonumenti.add(textZone1);
                    togliview.add(object);
                    list_object.addView(object);
                }
            }

            if (scelta == c) {
                for (int i = 0; i < oggetti_zona3.length; i++) {
                    View object = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso, null, false);
                    CheckableTextView textZone1 = (CheckableTextView) object.findViewById(R.id.textZone1);

                    textZone1.setText(oggetti_zona3[i]);
                    arrayMonumenti.add(textZone1);
                    togliview.add(object);
                    list_object.addView(object);
                }
            }

        }
        else {
        }
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError error=null;
        control=false;

        for(int a=0; a<arrayMonumenti.size();a++){              //ciclo per contare quanti sono checkati
            if(arrayMonumenti.get(a).isChecked()){
                oggetti_scelti.add(arrayMonumenti.get(a));
                control=true;
            }
        }
        if (control==false) {
            error =new VerificationError("Seleziona almeno un oggetto");
            return error;
        } else return null;
    }

    @Override
    public void onSelected() {
        //getFragmentManager().beginTransaction().detach(this);
        //getFragmentManager().beginTransaction().attach(this);
 /*
        String b = Step2.getA();
        stringa=b;
        stringaa.setText(stringa);

        */
       // stringaa.setText((Step2.getA()));
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
        //getFragmentManager().beginTransaction().detach(this).attach(new Fragment()).commit();
        dialog.show();
        dialog_avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                for(int i =0; i< arrayMonumenti.size();i++){
                    list_object.removeView(togliview.get(i));

                }for(int i =0; i<oggetti_scelti.size();i++){
                    oggetti_scelti.remove(i);
                }
                callback.goToPrevStep();
            }
        });

        dialog_aggiungizona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i =0; i< arrayMonumenti.size();i++){
                    list_object.removeView(togliview.get(i));

                }for(int i =0; i<oggetti_scelti.size();i++){
                    oggetti_scelti.remove(i);
                }
                stringaa.setText("");
                callback.goToPrevStep();
                dialog.dismiss();
            }
        });
        Step2.setTitle_Step2("Seleziona la zona che vuoi visitare");
    }
}