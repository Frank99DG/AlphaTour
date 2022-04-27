package com.example.alphatour.wizardpercorso;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.wizardcreazione.StepperAdapterCreazioneWizard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.StepAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Step2 extends Fragment implements Step, BlockingStep {

    private LinearLayout list_zone;
    private boolean control= false;
    private List <CheckableTextView> arrayZone = new ArrayList<CheckableTextView>();
    private int i=0;
    private int n = -1;
    private static int  zona_scelta;
    private static List<String> array_database =new ArrayList<String>();
    private static String stringa_scelta;
    private static TextView title_Step2;
    private FirebaseFirestore db;
    private ProgressBar loadingbar;
    private List<View> deleteView = new ArrayList<View>();
    Button callFragment;
    private Step5 step5_fragment = new Step5();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        list_zone = view.findViewById(R.id.list_zone);
        title_Step2 = view.findViewById(R.id.title_step2);
        loadingbar=view.findViewById(R.id.zoneLoadingBar);


        db = FirebaseFirestore.getInstance();

        getFragmentManager().beginTransaction().replace(R.id.riepilogo,step5_fragment).hide(step5_fragment).commit();

        callFragment =view.findViewById(R.id.call_fragment);
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

            loadingbar.setVisibility(View.VISIBLE);
            db.collection("Zones").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista zone

                        for (DocumentSnapshot d : listDocument) {
                            Zone zon = d.toObject(Zone.class);

                            View zone = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso,null ,false);
                            CheckableTextView textZone1 = (CheckableTextView) zone.findViewById(R.id.textZone1);

                            textZone1.setText(zon.getName());
                            array_database.add(zon.getName());

                            arrayZone.add(textZone1);
                            list_zone.addView(zone);
                            deleteView.add(zone);

                        }

                        loadingbar.setVisibility(View.GONE);

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

                    }
                }
            });


        }else{

        }



        }




    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        for(int i= 0;i<arrayZone.size();i++){
            arrayZone.get(i).setChecked(false,false);
        }

        for(int i =0; i< arrayZone.size();i++){
            list_zone.removeView(deleteView.get(i));

        }

        /*for(int i =0; i<oggetti_scelti.size();i++){
            oggetti_scelti.remove(i);
        }*/

        callback.goToNextStep();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        for(int i= 0;i<arrayZone.size();i++){
            arrayZone.get(i).setChecked(false,false);
        }

        for(int i =0; i< arrayZone.size();i++){
            list_zone.removeView(deleteView.get(i));

        }
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
                stringa_scelta = array_database.get(i);
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

    public static void setTitle_Step2(String title_Step2) {
        Step2.title_Step2.setText(title_Step2);
    }

    public static String getStringa_scelta() {
        return stringa_scelta;
    }

/*
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.call_fragment){
            getFragmentManager().beginTransaction().replace(R.id.container,new Step5()).commit();

        }

    }
 */

}