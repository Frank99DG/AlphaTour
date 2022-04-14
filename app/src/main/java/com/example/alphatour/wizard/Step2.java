package com.example.alphatour.wizard;

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
import android.widget.SearchView;
import android.widget.TextView;

import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class Step2 extends Fragment implements Step {

    private LinearLayout list_zone;
    private Button buttonAdd;
    boolean check = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        boolean check = false;
        list_zone = view.findViewById(R.id.list_zone);
        buttonAdd = view.findViewById(R.id.addZone);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              addZone();
            }
        });

        return view;
    }

    public void addZone(){
        View zone = getLayoutInflater().inflate(R.layout.row_add_zone_creazione_percorso,null ,false);
        TextView textZone = (TextView) zone.findViewById(R.id.textZone);


        textZone.setText("Zona 1");

        textZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView checked = zone.findViewById(R.id.checked);
                if(check ==true) {
                    checked.setVisibility(View.GONE);
                    check = false;
                }else {
                    checked.setVisibility(View.VISIBLE);
                    check = true;
                }
            }

        });

        list_zone.addView(zone);
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
}