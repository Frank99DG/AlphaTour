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
import com.example.alphatour.mainUI.MyPathsActivity;
import com.example.alphatour.objectclass.ElementString;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ListElementsActivity extends AppCompatActivity {

    private TextView zoneText;
    private LinearLayout layout_list;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private String Place;
    private String idPlace;
    private String Zone;
    private String idZone;
    private String myQrData;
    private String dashboardFlag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_elements);

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        Intent intent = getIntent();
        Place = intent.getStringExtra("Place");
        idPlace = intent.getStringExtra("idPlace");
        Zone = intent.getStringExtra("Zone");
        idZone = intent.getStringExtra("idZone");
        dashboardFlag = intent.getStringExtra("dashboardFlag");

        zoneText = findViewById(R.id.specificZoneText);
        zoneText.setText(Zone);
        layout_list = findViewById(R.id.listElementsLayout);

        db.collection("Elements")
                .whereEqualTo("idZone",idZone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocuments = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listDocuments) {

                                ElementString element = d.toObject(ElementString.class);

                                final View elementView = getLayoutInflater().inflate(R.layout.row_update, null, false);
                                TextView editableElement = (TextView) elementView.findViewById(R.id.itemUpdateText);
                                ImageView deleteElement = elementView.findViewById(R.id.deleteItem);

                                editableElement.setText(element.getTitle());

                                layout_list.addView(elementView);

                                editableElement.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View elementView) {

                                        db.collection("Elements").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista elementi

                                                    for (DocumentSnapshot d : listDocument) {
                                                        ElementString element = d.toObject(ElementString.class);

                                                        if (editableElement.getText().toString().equals(element.getTitle())){

                                                            Intent intent = new Intent(ListElementsActivity.this, ModifyObjectActivity.class);
                                                            myQrData = element.getQrData();
                                                            intent.putExtra("data", myQrData);
                                                            intent.putExtra("Place",Place);
                                                            intent.putExtra("idPlace",idPlace);
                                                            intent.putExtra("Zone",Zone);
                                                            intent.putExtra("idZone",idZone);
                                                            intent.putExtra("Element",editableElement.getText().toString());
                                                            String dashboardFlag = "0"; //per indicare a ModifyObject che fa parte del Modifica Luogo
                                                            intent.putExtra("dashboardFlag", dashboardFlag);
                                                            startActivity(intent);
                                                            break;
                                                        }

                                                    }
                                                }
                                            }
                                        });

                                    }
                                });

                                deleteElement.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        layout_list.removeView(elementView);

                                        db.collection("Elements").
                                                whereEqualTo("idUser",idUser).
                                                whereEqualTo("title",editableElement.getText()).
                                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                    for (DocumentSnapshot d : listDocument) {

                                                        String idElement = d.getId();

                                                        db.collection("Elements").document(idElement).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(ListElementsActivity.this, R.string.deleted_object, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(ListElementsActivity.this, ModifyZoneActivity.class);
        intent.putExtra("Place", Place);
        intent.putExtra("idPlace", idPlace);
        intent.putExtra("Zone", Zone);
        intent.putExtra("dashboardFlag", dashboardFlag);
        startActivity(intent);
    }

}