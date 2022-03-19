package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.alphatour.oggetti.Luogo;
import com.example.alphatour.oggetti.Utente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPlaceCuratorActivity extends AppCompatActivity {

    private EditText nomeLuogo,citta,tipologia;
    private String idUtente;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    AppCompatSpinner spinnerLuogo;

    List<String> listaTipoLuogo=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_curator);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        spinnerLuogo=findViewById(R.id.spinnerTipologiaLuogo);

        listaTipoLuogo.add("Tipologia luogo");
        listaTipoLuogo.add("Museo");
        listaTipoLuogo.add("Fiera");
        listaTipoLuogo.add("Area Archeologica");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaTipoLuogo);
        spinnerLuogo.setAdapter(arrayAdapter);

    }


    public boolean inputControl(String Nome, String Citta, String Tipologia){

        Boolean errorFlag = false;

        if (Nome.isEmpty()) {
            nomeLuogo.setError(getString(R.string.campo_obbligatorio));
            nomeLuogo.requestFocus();
            errorFlag = true;
        }

        if (Citta.isEmpty()) {
            citta.setError(getString(R.string.email_non_valida));
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

    public void savePlaceOnDbRemote(String Nome,String Citta,String Tipologia){

        //barraCaricamento.setVisibility(View.VISIBLE);
        //auth.createUserWithEmailAndPassword(Email, Password)
                //.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    //@Override
                    //public void onComplete(@NonNull Task<AuthResult> task) {

                        //if (task.isSuccessful()) {
                            idUtente = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            Luogo luogo = new Luogo(Nome, Citta, Tipologia);

                            DocumentReference documentReference = db.collection("Luoghi").document(idUtente);

                            documentReference.set(luogo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddPlaceCuratorActivity.this, "Luogo Salvato", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(AddPlaceCuratorActivity.this, AddZoneCuratorActivity.class));
                                            //barraCaricamento.setVisibility(View.GONE);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddPlaceCuratorActivity.this, "Salvataggio Luogo non riuscito" , Toast.LENGTH_LONG).show();
                                    //barraCaricamento.setVisibility(View.GONE);
                                }
                            });
                            finish();

                        /*} else {
                            Toast.makeText(RegisterActivity.this, "failed firebase" , Toast.LENGTH_LONG).show();
                            //barraCaricamento.setVisibility(View.GONE);
                        }*/
                    //}
                //});

    }





    public void openFirstZoneActivity(View v){
        startActivity(new Intent(AddPlaceCuratorActivity.this, AddZoneCuratorActivity.class));

    }

}