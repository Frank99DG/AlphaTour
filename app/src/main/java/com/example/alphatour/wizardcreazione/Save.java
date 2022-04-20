package com.example.alphatour.wizardcreazione;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.Zone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class Save extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        storegeProfilePick= FirebaseStorage.getInstance().getReference();

        db.collection("Zones")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String zonj;

                        if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Zone zon = document.toObject(Zone.class);
                                    if (zon.getName().matches(intent.getStringExtra("description"))) {
                                        zonj=document.getId();

                                /*Element newElement=new Element(newElementSupport.getIdZone(),newElementSupport.getTitle(),
                                        newElementSupport.getDescription(),"activity",newElementSupport.getSensorCode());*/
                                    }
                                }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Save.this, "Salvataggio Zone e Oggetti non riuscito", Toast.LENGTH_LONG).show();
            }
        });
    }
}