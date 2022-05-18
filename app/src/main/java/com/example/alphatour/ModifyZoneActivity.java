package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifyZoneActivity extends AppCompatActivity {

    private TextView zoneText;
    private EditText nameZone;
    private ProgressBar loadingBar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String name;
    private String idUser;
    private String Zone;
    private String idZone;
    private String idPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_zone);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        Intent intent= getIntent();
        Zone = intent.getStringExtra("Zone");
        getIdZone(Zone);
        idPlace = intent.getStringExtra("idPlace");

        zoneText = findViewById(R.id.myZoneText);
        nameZone = findViewById(R.id.updateNameZone);

        loadingBar = findViewById(R.id.modifyZoneLoadingBar);


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

                                if(Zone.equals(zone.getName())){

                                    zoneText.setText(zone.getName());
                                    nameZone.setText(zone.getName());
                                    break;
                                }

                            }
                        }
                    }
                });


    }


    //per rimuovere il focus e la tastiera quando si clicca fuori dalla EditText e/o AutoCompleteTextView
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    public void getIdZone(String nameZone) {

        db.collection("Zones").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista luoghi

                    for (DocumentSnapshot d : listDocument) {
                        Zone zone = d.toObject(Zone.class);

                        if (nameZone.equals(zone.getName())){
                            idZone = d.getId();
                            break;
                        }

                    }
                }
            }
        });
    }


    public void updateZone(View view) {

        //recupero dati modificati
        String Name = nameZone.getText().toString();

        Boolean control = inputControl(Name);

        if(control){
            return;
        }else{

            //salvataggio modifiche su SQLITE
            //int update = updateZoneOnDbLocal(Name,City,Typology);

            //salvataggio modifiche su Firebase
            updateZoneOnDbRemote(Name);

            startActivity(new Intent(ModifyZoneActivity.this, ListZonesActivity.class));

        }
    }


    public boolean inputControl(String Name){
        Boolean errorFlag = false;

        if(Name.isEmpty()){
            nameZone.setError(getString(R.string.required_field));
            nameZone.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }


    /*public int updateZoneOnDbLocal(String Name,String Surname,String DateOfBirth,String Username,String Email,String EmailLocal){

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(this);
        //recupero idUtente
        SQLiteDatabase db = dbAlpha.getReadableDatabase();
        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_USER_PROFILE,new String[]{emailLocal});
        if(cursor.moveToFirst()){
            idUtenteLocal= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID));
        }

        //aggiornamento dati utente

        db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_NAME,Name);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_SURNAME,Surname);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_DATE_BIRTH,DateOfBirth);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_USERNAME,Username);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL,Email);

        return db.update(AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER,values,AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+
                CommandDbAlphaTour.Command.EQUAL+CommandDbAlphaTour.Command.VALUE,new String[] {idUtenteLocal});


    }*/

    public void updateZoneOnDbRemote(String Name){

        loadingBar.setVisibility(View.VISIBLE);
        HashMap<String,Object> placeMap = new HashMap<>();
        placeMap.put("name",Name);


        db.collection("Zone").document(idZone)
                .update("name",Name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ModifyZoneActivity.this, "Zona aggiornata correttamente", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ModifyZoneActivity.this, ListZonesActivity.class));
                        loadingBar.setVisibility(View.GONE);
                        finishAffinity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyZoneActivity.this, "Non è stato possibile aggiornare la zona", Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }


    public void openElementsList(View v){
        Intent intent = new Intent(ModifyZoneActivity.this, ListElementsActivity.class);
        intent.putExtra("Zone", Zone);
        intent.putExtra("idPlace", idPlace);
        startActivity(intent);
    }



}