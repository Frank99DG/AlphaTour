package com.example.alphatour.wizard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class Step2 extends Fragment implements Step {

    private LinearLayout list_zone;
    private Button buttonAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);
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
        CheckableTextView textZone1 = (CheckableTextView) zone.findViewById(R.id.textZone1);
        CheckableTextView textZone2 = (CheckableTextView) zone.findViewById(R.id.textZone2);
        CheckableTextView textZone3 = (CheckableTextView) zone.findViewById(R.id.textZone3);

        textZone1.setText("Zona 1");
        textZone2.setText("Zona 2");
        textZone3.setText("Zona 3");

        textZone1.setOnCheckChangeListener(new CheckedListener() {
            @Override
            public void onCheckChange(@NonNull View view, boolean b) {
                if(textZone2.isChecked() ||  textZone3.isChecked()){
                    textZone2.setChecked(false,false);
                    textZone3.setChecked(false,false);
                }
            }
        });

        textZone2.setOnCheckChangeListener(new CheckedListener() {
            @Override
            public void onCheckChange(@NonNull View view, boolean b) {
                if(textZone1.isChecked() || textZone3.isChecked()){
                    textZone1.setChecked(false,false);
                    textZone3.setChecked(false,false);
                }
            }
        });

        textZone3.setOnCheckChangeListener(new CheckedListener() {
            @Override
            public void onCheckChange(@NonNull View view, boolean b) {
                textZone1.setChecked(false,false);
                textZone2.setChecked(false,false);
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