package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alphatour.oggetti.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ListPlacesActivity extends AppCompatActivity {

    private LinearLayout layout_list;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    //private String idPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_places);

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        layout_list = findViewById(R.id.listPlacesLayout);

        db.collection("Places")
                .whereEqualTo("idUser",idUser)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocuments = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listDocuments) {

                                Place place = d.toObject(Place.class);

                                final View placeView = getLayoutInflater().inflate(R.layout.row_update, null, false);
                                TextView editablePlace = (TextView) placeView.findViewById(R.id.itemUpdateText);

                                editablePlace.setText(place.getName());

                                //getIdPlace(place.getName());

                                layout_list.addView(placeView);

                                editablePlace.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View placeView) {
                                        //getIdPlace(place.getName());
                                        Intent intent = new Intent(ListPlacesActivity.this, ModifyPlaceActivity.class);
                                        intent.putExtra("Place", editablePlace.getText().toString());
                                        //intent.putExtra("idPlace", getIdPlace(editablePlace.getText().toString()));
                                        startActivity(intent);

                                    }
                                });

                            }

                        }
                    }
                });



    }




}