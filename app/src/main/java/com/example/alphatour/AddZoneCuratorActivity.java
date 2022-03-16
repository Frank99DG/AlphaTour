package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class AddZoneCuratorActivity extends AppCompatActivity {

    FloatingActionButton bottone;
    EditText nomeLuogo;
    LinearLayout layoutListZona;
    Button addElemento;


    private   Animation rotateOpen;
    private   Animation rotateClose;
    private   Animation fromBottom;
    private   Animation toBottom;
    private  boolean clicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zone_curator);

        //bottone=findViewById(R.id.addLuogo);
        //nomeLuogo=findViewById(R.id.textNomeLuogo);
        rotateOpen= AnimationUtils.loadAnimation(this,R.anim.rotate_open_animation);
        rotateClose= AnimationUtils.loadAnimation(this,R.anim.rotate_close_animation);
        fromBottom= AnimationUtils.loadAnimation(this,R.anim.from_bottom_animation);
        toBottom= AnimationUtils.loadAnimation(this,R.anim.to_bottom_animation);

        addElemento=findViewById(R.id.buttonAggiungiElemento);
        layoutListZona=findViewById(R.id.linearLayoutZone);

    }


    public void addViewElemento(View v) {

        final View viewAddElement= getLayoutInflater().inflate(R.layout.row_add_elemento,null,false);
        Button removeElemento=(Button) viewAddElement.findViewById(R.id.buttonDeleteElement);

        removeElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeViewElemento(viewAddElement);
            }
        });

        layoutListZona.addView(viewAddElement);
    }

    public void removeViewElemento(View v) {

        layoutListZona.removeView(v);
    }


    public void aggiungiLuogo(View v){

        setVisibility(clicked);
        setAnimation(clicked);
        clicked=!clicked; //stessa cosa che fare if then else

    }

    private void setVisibility(boolean clicked) {

        if(!clicked){
            nomeLuogo.setVisibility(View.VISIBLE);
        }else{
            nomeLuogo.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {

        if(!clicked){
            bottone.startAnimation(rotateOpen);
            nomeLuogo.startAnimation(fromBottom);

        }else{
            nomeLuogo.startAnimation(toBottom);
            bottone.startAnimation(rotateClose);

        }
    }

}