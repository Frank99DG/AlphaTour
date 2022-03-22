package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.alphatour.oggetti.Element;

import java.util.ArrayList;

public class AddZoneActivity extends AppCompatActivity {



    private int numeroZona = 1;

    Button aggiungiElemento;
    LinearLayout listaLayout;

    private EditText nomeZona,titolo,descrizione,foto,codiceQr,attivita,codiceSensore;

    ArrayList<Element> listaElementi = new ArrayList<>();

    /*FloatingActionButton bottone;
    EditText nomeLuogo;
    private   Animation rotateOpen;
    private   Animation rotateClose;
    private   Animation fromBottom;
    private   Animation toBottom;
    private  boolean clicked=false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zone_curator);

        /*bottone=findViewById(R.id.addLuogo);
        nomeLuogo=findViewById(R.id.textNomeLuogo);
        rotateOpen= AnimationUtils.loadAnimation(this,R.anim.rotate_open_animation);
        rotateClose= AnimationUtils.loadAnimation(this,R.anim.rotate_close_animation);
        fromBottom= AnimationUtils.loadAnimation(this,R.anim.from_bottom_animation);
        toBottom= AnimationUtils.loadAnimation(this,R.anim.to_bottom_animation);*/

        aggiungiElemento = findViewById(R.id.buttonAggiungiElemento);
        nomeZona = findViewById(R.id.inputNomeZona);
        listaLayout = findViewById(R.id.listaElementiLayout);

    }


    public void addViewElemento(View v) {

        final View elementView = getLayoutInflater().inflate(R.layout.row_add_elemento,null,false);
        ImageView removeElement = (ImageView) elementView.findViewById(R.id.buttonDeleteElement);

        removeElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaLayout.removeView(elementView);
            }
        });

        listaLayout.addView(elementView);
    }


    public void /*boolean*/ inputControl(View v){
        listaElementi.clear();
        Boolean errorFlag = false;

        String NomeZona = nomeZona.getText().toString();
        if(NomeZona.isEmpty()){
            nomeZona.setError(getString(R.string.campo_obbligatorio));
            nomeZona.requestFocus();
        }

        for(int i = 0; i < listaLayout.getChildCount(); i++){

            View elementView = listaLayout.getChildAt(i);

             titolo = elementView.findViewById(R.id.inputTitolo);
             descrizione = elementView.findViewById(R.id.inputDescrizione);
             //foto = elementView.findViewById( );
             codiceQr = elementView.findViewById(R.id.inputCodiceQr);
             attivita = elementView.findViewById(R.id.inputAttivita);
             codiceSensore = elementView.findViewById(R.id.inputCodiceSensore);

             Element elemento = new Element();

             String Titolo = titolo.getText().toString();
             String Descrizione = descrizione.getText().toString();
             //String Foto = foto.getText().toString();
             String CodiceQr = codiceQr.getText().toString();
             String Attivita = attivita.getText().toString();
             String CodiceSensore = codiceSensore.getText().toString();


            if(Titolo.isEmpty()){
                titolo.setError(getString(R.string.campo_obbligatorio));
                titolo.requestFocus();
                errorFlag = true;
            }else{
                elemento.setTitolo(Titolo);
            }

            if(Descrizione.isEmpty()){
                descrizione.setError(getString(R.string.campo_obbligatorio));
                descrizione.requestFocus();
                errorFlag = true;
            }else{
                elemento.setDescrizione(Descrizione);
            }

            if(CodiceQr.isEmpty()){
                codiceQr.setError(getString(R.string.campo_obbligatorio));
                codiceQr.requestFocus();
                errorFlag = true;
            }else{
                elemento.setCodiceQr(CodiceQr);
            }

            if(Attivita.isEmpty()){
                attivita.setError(getString(R.string.campo_obbligatorio));
                attivita.requestFocus();
                errorFlag = true;
            }else{
                elemento.setAttivita(Attivita);
            }

            if(CodiceSensore.isEmpty()){
                codiceSensore.setError(getString(R.string.campo_obbligatorio));
                codiceSensore.requestFocus();
                errorFlag = true;
            }else{
                elemento.setCodiceSensore(CodiceSensore);
            }

            if(!errorFlag){
                listaElementi.add(elemento);
            }

        }

        if(listaLayout.getChildCount() == 0){
            Toast.makeText(this,"Aggiungi almeno un elemento", Toast.LENGTH_SHORT).show();
        }else if( listaElementi.size() < listaLayout.getChildCount() || NomeZona.isEmpty()){
            Toast.makeText(this,"Completa correttamente i campi", Toast.LENGTH_SHORT).show();
        }else {
            Log.i("elementi", listaElementi.toString());
            Toast.makeText(this,"Zona ed elementi salvati", Toast.LENGTH_SHORT).show();
        }

        //return errorFlag;
    }


    /*public void saveElements(View v) {

        String Nome = nome.getText().toString();
        String Cognome = cognome.getText().toString();
        String DataNascita = dataNascita.getText().toString();
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
    }*/


    public void createZoneActivity(View v){
        startActivity(new Intent(this, AddZoneActivity.class));

    }

/*
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

 */

}