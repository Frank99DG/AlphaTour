package com.example.alphatour.wizardcreazione;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.Zone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SaveData {

    private String title,description,activity,sensorCode,zone;
    private Uri photo;
    private Bitmap qrCode;
    private String idZone;

    public SaveData(String title, String description, String activity, String sensorCode, String zone, Uri photo, Bitmap qrCode, String idZone) {
        this.title = title;
        this.description = description;
        this.activity = activity;
        this.sensorCode = sensorCode;
        this.zone = zone;
        this.photo = photo;
        this.qrCode = qrCode;
        this.idZone = idZone;
    }

    public SaveData(){

    }

    public boolean Save(Element elm, FirebaseFirestore db){

        boolean save=true;

        db.collection("Zones")
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Zone zon = document.toObject(Zone.class);
                                    if (zon.getName().matches(elm.getZoneRif())) {
                                        elm.setIdZone(document.getId());
                                    }
                                }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return save;
    }
}
