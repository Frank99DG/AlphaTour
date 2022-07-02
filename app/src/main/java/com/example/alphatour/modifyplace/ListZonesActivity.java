package com.example.alphatour.modifyplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.R;
import com.example.alphatour.objectclass.Constraint;
import com.example.alphatour.objectclass.Zone;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ListZonesActivity extends AppCompatActivity {

    private TextView placeText;
    private LinearLayout layout_list;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private String Place;
    private String idPlace;
    private String dashboardFlag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_zones);

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        Intent intent = getIntent();
        Place = intent.getStringExtra("Place");
        idPlace = intent.getStringExtra("idPlace");
        dashboardFlag = intent.getStringExtra("dashboardFlag");

        placeText = findViewById(R.id.specificPlaceText);
        placeText.setText(Place);
        layout_list = findViewById(R.id.listZonesLayout);

        db.collection("Zones")
                .whereEqualTo("idPlace",idPlace)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocuments = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listDocuments) {

                                Zone zone = d.toObject(Zone.class);

                                final View zoneView = getLayoutInflater().inflate(R.layout.row_update, null, false);
                                TextView editableZone = (TextView) zoneView.findViewById(R.id.itemUpdateText);
                                ImageView deleteZone = zoneView.findViewById(R.id.deleteItem);

                                editableZone.setText(zone.getName());

                                layout_list.addView(zoneView);

                                editableZone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View zoneView) {
                                        Intent intent = new Intent(ListZonesActivity.this, ModifyZoneActivity.class);
                                        intent.putExtra("Place", Place);
                                        intent.putExtra("idPlace", idPlace);
                                        intent.putExtra("Zone", editableZone.getText().toString());
                                        String dashboardFlag = "0"; //per indicare a ModifyZone che fa parte del Modifica Luogo
                                        intent.putExtra("dashboardFlag", dashboardFlag);
                                        startActivity(intent);

                                    }
                                });

                                deleteZone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        layout_list.removeView(zoneView);

                                        db.collection("Zones").
                                                whereEqualTo("idUser",idUser).
                                                whereEqualTo("name",editableZone.getText()).
                                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                    for (DocumentSnapshot d : listDocument) {

                                                        String idZone = d.getId();

                                                        db.collection("Zones").document(idZone).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(ListZonesActivity.this, R.string.deleted_zone, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

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


                                                        db.collection("Constraints")
                                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                                    for (DocumentSnapshot d : listDocument) {
                                                                        Constraint constraint = d.toObject(Constraint.class);
                                                                        String idConstraint = d.getId();

                                                                        assert constraint != null;
                                                                        if(constraint.getFromZone().equals(editableZone.getText())){
                                                                            db.collection("Constraints").document(idConstraint).delete();
                                                                        }

                                                                        if(constraint.getInZone().equals(editableZone.getText())){
                                                                            db.collection("Constraints").document(idConstraint).delete();
                                                                        }

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
        Intent intent = new Intent(ListZonesActivity.this, ModifyPlaceActivity.class);
        intent.putExtra("Place", Place);
        intent.putExtra("dashboardFlag", dashboardFlag);
        startActivity(intent);
    }

}