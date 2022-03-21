package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.alphatour.oggetti.Luogo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPlaceActivity extends AppCompatActivity {

    private EditText nomeLuogo,citta;
    private AutoCompleteTextView tipologia;
    private List<String> lista_tipologie = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private String idCuratore;
    private ProgressBar barraCaricamento;
    private FirebaseAuth auth;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_curator);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

            nomeLuogo = findViewById(R.id.inputNomeLuogo);
            citta = findViewById(R.id.inputCittaLuogo);
            tipologia = findViewById(R.id.inputTipologia);
            lista_tipologie.add(getString(R.string.museo));
            lista_tipologie.add(getString(R.string.fiera));
            lista_tipologie.add(getString(R.string.sito_archeologico));
            lista_tipologie.add(getString(R.string.mostra));
            adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, lista_tipologie );
            tipologia.setAdapter(adapterItems);
            tipologia.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    String item = parent.getItemAtPosition(position).toString();
                }
            });

        barraCaricamento = findViewById(R.id.placeBarraCaricamento);
    }



    public boolean inputControl(String NomeLuogo, String Citta, String Tipologia){

        Boolean errorFlag = false;

        if (NomeLuogo.isEmpty()) {
            nomeLuogo.setError(getString(R.string.campo_obbligatorio));
            nomeLuogo.requestFocus();
            errorFlag = true;
        }

        if (Citta.isEmpty()) {
            citta.setError(getString(R.string.campo_obbligatorio));
            citta.requestFocus();
            errorFlag = true;
        }


        if (Tipologia.isEmpty()) {
            tipologia.setError(getString(R.string.campo_obbligatorio));
            tipologia.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }

    public void savePlaceOnDbRemote(String NomeLuogo,String Citta,String Tipologia){

        barraCaricamento.setVisibility(View.VISIBLE);

        idCuratore = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        Luogo luogo = new Luogo(NomeLuogo, Citta, Tipologia, idCuratore);

        db.collection("Luoghi")
                .add(luogo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPlaceActivity.this, "Luogo Salvato", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddPlaceActivity.this, AddZoneActivity.class));
                        barraCaricamento.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPlaceActivity.this, "Salvataggio Luogo non riuscito" , Toast.LENGTH_LONG).show();
                        barraCaricamento.setVisibility(View.GONE);
                    }
                });


    }


    public void savePlaceOnDb(View v) {

        String NomeLuogo = nomeLuogo.getText().toString();
        String Citta = citta.getText().toString();
        String Tipologia = tipologia.getText().toString();

        Log.i("tipologia",Tipologia);

        boolean errorFlag = inputControl(NomeLuogo, Citta, Tipologia);

        if(errorFlag) {
            return;

        }else {

            //long result = saveUserOnDbLocal(NomeLuogo,Citta,Tipologia);

            //if(result == -1){
                //Toast.makeText(RegisterActivity.this, "Errore db local", Toast.LENGTH_LONG).show();
            //}else{
                savePlaceOnDbRemote(NomeLuogo,Citta,Tipologia);
                //startActivity(new Intent(AddPlaceActivity.this, AddZoneActivity.class));
                //barraCaricamento.setVisibility(View.GONE);
                //finish();
            }



        }



    public void openFirstZoneActivity(View v){
        startActivity(new Intent(AddPlaceActivity.this, AddZoneActivity.class));

    }

}