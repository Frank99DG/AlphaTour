package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddPlaceCuratorActivity extends AppCompatActivity {

    FloatingActionButton bottone;
    EditText nomeLuogo;
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