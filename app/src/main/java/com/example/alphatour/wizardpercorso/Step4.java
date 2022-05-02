package com.example.alphatour.wizardpercorso;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.R;
import com.example.alphatour.oggetti.Zone;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Step4 extends Fragment implements Step, BlockingStep {

    public static List<String> zone_select = new ArrayList<>();
    public static List<String> oggetti_select = new ArrayList<>();
    private int i=0;


    private LinearLayout list_zoneRiepilogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step4, container, false);
        list_zoneRiepilogo = view.findViewById(R.id.list_zoneRiepilogo);


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            for(i=0;i<zone_select.size();i++){
                View zone = getLayoutInflater().inflate(R.layout.row_add_zone_review_creazione_percorso, null, false);
                TextView textZone = (TextView) zone.findViewById(R.id.textZoneReview);
                ImageView deleteZone = zone.findViewById(R.id.deleteZone_review);

             //   CheckableTextView textObject = (CheckableTextView) zone.findViewById(R.id.textObjectt);

                deleteZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list_zoneRiepilogo.removeView(zone);
                    }
                });

                textZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ReviewZoneSelected.class);
                        intent.putExtra("zone",textZone.getText().toString());
                        startActivity(intent);
                    }
                });

                textZone.setText(zone_select.get(i));
                list_zoneRiepilogo.addView(zone);

            }
        }
        else{

        }
    }



    public static List<String> getZone_select() {
        return zone_select;
    }

    public static List<String> getOggetti_select() {
        return oggetti_select;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

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
    }
}