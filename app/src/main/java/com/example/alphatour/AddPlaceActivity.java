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


import com.example.alphatour.oggetti.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPlaceActivity extends AppCompatActivity {

    private EditText namePlace,city;
    private AutoCompleteTextView typology;
    private List<String> typology_list = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private String idUser;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_curator);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

            namePlace = findViewById(R.id.inputNamePlace);
            city = findViewById(R.id.inputCityPlace);
            typology = findViewById(R.id.inputTypologyPlace);
            typology_list.add(getString(R.string.museo));
            typology_list.add(getString(R.string.fiera));
            typology_list.add(getString(R.string.sito_archeologico));
            typology_list.add(getString(R.string.mostra));
            adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, typology_list );
            typology.setAdapter(adapterItems);
            typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    String item = parent.getItemAtPosition(position).toString();
                }
            });

        loadingBar = findViewById(R.id.placeLoadingBar);
    }



    public boolean inputControl(String NamePlace, String City, String Typology){

        Boolean errorFlag = false;

        if (NamePlace.isEmpty()) {
            namePlace.setError(getString(R.string.campo_obbligatorio));
            namePlace.requestFocus();
            errorFlag = true;
        }

        if (City.isEmpty()) {
            city.setError(getString(R.string.campo_obbligatorio));
            city.requestFocus();
            errorFlag = true;
        }


        if (Typology.isEmpty()) {
            typology.setError(getString(R.string.campo_obbligatorio));
            typology.requestFocus();
            errorFlag = true;
        }

        return errorFlag;

    }

    public void savePlaceOnDbRemote(String NamePlace,String City,String Typology){

        loadingBar.setVisibility(View.VISIBLE);

        idUser = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        Place place = new Place(NamePlace, City, Typology, idUser);

        db.collection("Places")
                .add(place)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPlaceActivity.this, "Luogo Salvato", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddPlaceActivity.this, AddZoneActivity.class));
                        loadingBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPlaceActivity.this, "Salvataggio Luogo non riuscito" , Toast.LENGTH_LONG).show();
                        loadingBar.setVisibility(View.GONE);
                    }
                });


    }


    public void savePlaceOnDb(View v) {

        String NamePlace = namePlace.getText().toString();
        String City = city.getText().toString();
        String Typology = typology.getText().toString();

        Log.i("tipologia",Typology);

        boolean errorFlag = inputControl(NamePlace, City, Typology);

        if(errorFlag) {
            return;

        }else {

            //long result = saveUserOnDbLocal(NamePlace,City,Typology);

            //if(result == -1){
                //Toast.makeText(RegisterActivity.this, "Errore db local", Toast.LENGTH_LONG).show();
            //}else{
                savePlaceOnDbRemote(NamePlace,City,Typology);
                //startActivity(new Intent(AddPlaceActivity.this, AddZoneActivity.class));
                //loadingBar.setVisibility(View.GONE);
                //finish();
            }



        }



    public void openFirstZoneActivity(View v){

        String NamePlace = namePlace.getText().toString();
        String City = city.getText().toString();
        String Typology = typology.getText().toString();
        boolean errorFlag = inputControl(NamePlace, City, Typology);

        if(errorFlag) {
            return;

        }else {
            startActivity(new Intent(AddPlaceActivity.this, AddZoneActivity.class));
        }

    }

}