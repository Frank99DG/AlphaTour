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

public class AddPlaceCuratorActivity extends AppCompatActivity {

    FloatingActionButton bottone;
    EditText nomeLuogo;
    LinearLayout layoutListZona;
    LinearLayout layoutListElemento;
    Button addZona,addElemento;
    AppCompatSpinner spinnerLuogo;
    View viewAddElement;
    int elementiCreati=0;

    List<String> listaTipoLuogo=new ArrayList<>();
    List<View>elementList=new ArrayList<>();

    private   Animation rotateOpen;
    private   Animation rotateClose;
    private   Animation fromBottom;
    private   Animation toBottom;
    private  boolean clicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_curator);

        bottone=findViewById(R.id.addLuogo);
        nomeLuogo=findViewById(R.id.textNomeLuogo);
        rotateOpen= AnimationUtils.loadAnimation(this,R.anim.rotate_open_animation);
        rotateClose= AnimationUtils.loadAnimation(this,R.anim.rotate_close_animation);
        fromBottom= AnimationUtils.loadAnimation(this,R.anim.from_bottom_animation);
        toBottom= AnimationUtils.loadAnimation(this,R.anim.to_bottom_animation);

        layoutListZona=findViewById(R.id.linearLayoutZone);
        layoutListElemento=findViewById(R.id.linearLayoutElementi);
        addZona=findViewById(R.id.buttonAggiungiZona);
        addElemento=findViewById(R.id.buttonAggiungiElemento);
        spinnerLuogo=findViewById(R.id.spinnerTipologiaLuogo);

        listaTipoLuogo.add("Tipologia luogo");
        listaTipoLuogo.add("Museo");
        listaTipoLuogo.add("Fiera");
        listaTipoLuogo.add("Area Archeologica");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaTipoLuogo);
        spinnerLuogo.setAdapter(arrayAdapter);
    }



    public void addViewZona(View v) {

        View viewAddPlace= getLayoutInflater().inflate(R.layout.row_add_zona,null,false);
        Button addElemento=(Button) viewAddPlace.findViewById(R.id.buttonAggiungiElemento);
        Button removeZona=(Button) viewAddPlace.findViewById(R.id.buttonDeletePlace);

        removeZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewAddElement==null) {
                    removeViewZonaAndElemento(viewAddPlace);
                }else{
                    removeViewZonaAndElemento(viewAddPlace);
                    for(int i=0;i<elementList.size();i++) {
                        removeViewZonaAndElemento(elementList.get(i));
                    }

                }
            }
        });
        layoutListZona.addView(viewAddPlace);
    }


    public void addViewElemento(View v) {

        viewAddElement= getLayoutInflater().inflate(R.layout.row_add_elemento,null,false);
        /*EditText nomeZona= (EditText) viewAddPlace.findViewById(R.id.inputNomeZona);*/
        Button removeElemento=(Button) viewAddElement.findViewById(R.id.buttonDeleteElement);

        removeElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeViewZonaAndElemento(viewAddElement);
            }
        });

        layoutListZona.addView(viewAddElement);
        elementList.add(viewAddElement);
        elementiCreati++;
    }

    public void removeViewZonaAndElemento(View v) {

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