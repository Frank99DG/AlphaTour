package com.example.alphatour.wizardpercorso;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.alphatour.oggetti.ElementString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;


public class Step3 extends Fragment implements Step, BlockingStep {

    private boolean control = false;
    private LinearLayout list_object;
    private String stringa;
    private TextView zone_selected;
    private String[] oggetti_zona1;
    private String[] oggetti_zona2 = {"monumento 4", "monumento 5", "monumento 6"};
    private String[] oggetti_zona3 = {"monumento 7", "monumento 8", "monumento 9"};
    private List<CheckableTextView> oggetti_scelti = new ArrayList<CheckableTextView>();
    ;
    private List<CheckableTextView> arrayMonumenti = new ArrayList<CheckableTextView>();
    private Dialog dialog;
    private Button dialog_avanti, dialog_aggiungizona;
    private List<View> togliview = new ArrayList<View>();
    private FirebaseFirestore db;
    private Button addZone;
    private Button callFragment;
    private Step5 step5_fragment = new Step5();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step3, container, false);
        zone_selected = (TextView) view.findViewById(R.id.zone_selected);
        list_object = view.findViewById(R.id.list_object);

        db = FirebaseFirestore.getInstance();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_step3);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_avanti = dialog.findViewById(R.id.btn_avanti);
        dialog_aggiungizona = dialog.findViewById(R.id.btn_aggiungiZona);

        getFragmentManager().beginTransaction().replace(R.id.riepilogo2, step5_fragment).hide(step5_fragment).commit();
        callFragment = view.findViewById(R.id.call_fragment2);
        callFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().show(step5_fragment).commit();
            }
        });

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            zone_selected.setText(Step2.getStringa_scelta());
            String scelta = Step2.getStringa_scelta();

            db.collection("Zones")
                    .whereEqualTo("name", scelta)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String idZone = document.getId();

                                    db.collection("Elements")
                                            .whereEqualTo("idZone", idZone)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            ElementString element = document.toObject(ElementString.class);

                                                            View object = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso, null, false);
                                                            CheckableTextView textZone1 = (CheckableTextView) object.findViewById(R.id.textZone1);

                                                            textZone1.setText(element.getTitle());
                                                            arrayMonumenti.add(textZone1);
                                                            togliview.add(object);
                                                            list_object.addView(object);

                                                        }
                                                    } else {
                                                        Toast.makeText(getContext(), "Non è stato possibile caricare le zone !!!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(getContext(), "Non è stato possibile caricare le zone !!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
        }
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        dialog.show();
        dialog_avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                for (int i = 0; i < arrayMonumenti.size(); i++) {
                    list_object.removeView(togliview.get(i));
                }
                callback.goToNextStep();
            }
        });

        dialog_aggiungizona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < arrayMonumenti.size(); i++) {
                    list_object.removeView(togliview.get(i));

                }
                for (int i = 0; i < oggetti_scelti.size(); i++) {
                    oggetti_scelti.remove(i);
                }
                zone_selected.setText("");
                Intent intent = new Intent(getContext(), PercorsoWizard.class);
                intent.putExtra("val", 1);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        //getFragmentManager().beginTransaction().detach(this).attach(new Fragment()).commit();
        for(int i =0; i< arrayMonumenti.size();i++) {
            list_object.removeView(togliview.get(i));
        }
        callback.goToPrevStep();
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError error = null;
        control = false;

        for (int a = 0; a < arrayMonumenti.size(); a++) {              //ciclo per contare quanti sono checkati
            if (arrayMonumenti.get(a).isChecked()) {
                oggetti_scelti.add(arrayMonumenti.get(a));
                control = true;
            }
        }
        if (control == false) {
            error = new VerificationError("Seleziona almeno un oggetto");
            return error;
        }
        return null;

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(), error.getErrorMessage().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
    }

}