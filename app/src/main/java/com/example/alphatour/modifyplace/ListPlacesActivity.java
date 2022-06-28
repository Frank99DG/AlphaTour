package com.example.alphatour.modifyplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.mainUI.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.objectclass.Place;
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
                                ImageView deletePlace = placeView.findViewById(R.id.deleteItem);

                                editablePlace.setText(place.getName());

                                layout_list.addView(placeView);

                                editablePlace.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View placeView) {
                                        Intent intent = new Intent(ListPlacesActivity.this, ModifyPlaceActivity.class);
                                        intent.putExtra("Place", editablePlace.getText().toString());
                                        String dashboardFlag = "0"; //per indicare a ModifyPlace che fa parte del Modifica Luogo
                                        intent.putExtra("dashboardFlag", dashboardFlag);
                                        startActivity(intent);

                                    }
                                });


                                deletePlace.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        layout_list.removeView(placeView);

                                        db.collection("Places").
                                                whereEqualTo("idUser",idUser).
                                                whereEqualTo("name",editablePlace.getText()).
                                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                    for (DocumentSnapshot d : listDocument) {

                                                        String idPlace = d.getId();

                                                        db.collection("Places").document(idPlace).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(ListPlacesActivity.this, R.string.deleted_place, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        db.collection("Zones").
                                                                whereEqualTo("idUser",idUser).
                                                                whereEqualTo("idPlace",idPlace).
                                                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                                    for (DocumentSnapshot d : listDocument) {

                                                                        String idZone = d.getId();

                                                                        db.collection("Zones").document(idZone).delete();

                                                                        db.collection("Elements").
                                                                                whereEqualTo("idUser",idUser).
                                                                                whereEqualTo("idZone",idZone).
                                                                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                                                    for (DocumentSnapshot d : listDocument) {

                                                                                        String idElement = d.getId();

                                                                                        db.collection("Elements").document(idElement).delete();
                                                                                    }
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            }
                                                        });

                                                    }
                                                }
                                            }
                                        });


                                    }
                                });




                            }

                        }
                    }
                });



    }


    public void onBackButtonClick(View view){
        startActivity(new Intent(ListPlacesActivity.this, DashboardActivity.class));
    }




}