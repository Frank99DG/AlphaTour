package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddPlaceCuratorActivity extends AppCompatActivity {

    AppCompatSpinner spinnerLuogo;

    List<String> listaTipoLuogo=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_curator);

        spinnerLuogo=findViewById(R.id.spinnerTipologiaLuogo);

        listaTipoLuogo.add("Tipologia luogo");
        listaTipoLuogo.add("Museo");
        listaTipoLuogo.add("Fiera");
        listaTipoLuogo.add("Area Archeologica");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaTipoLuogo);
        spinnerLuogo.setAdapter(arrayAdapter);

    }


    public void openFirstZoneActivity(View v){
        startActivity(new Intent(AddPlaceCuratorActivity.this, AddZoneCuratorActivity.class));

    }

}