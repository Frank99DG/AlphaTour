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

public class ModifyPlaceActivity extends AppCompatActivity {

    private TextView placeText;
    private EditText namePlace,city;
    private AutoCompleteTextView typology;
    private List<String> typology_list = new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private String item;
    private ProgressBar loadingBar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String name;
    private String idUser;
    private String Place;
    private String idPlace;
    private String dashboardFlag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_place);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        Intent intent= getIntent();
        Place = intent.getStringExtra("Place");
        getIdPlace(Place);
        dashboardFlag = intent.getStringExtra("dashboardFlag");

        placeText = findViewById(R.id.myPlaceText);
        namePlace = findViewById(R.id.updateNamePlace);
        city = findViewById(R.id.updateCityPlace);
        typology = findViewById(R.id.updateTypologyPlace);
        typology_list.add(getString(R.string.museum));
        typology_list.add(getString(R.string.fair));
        typology_list.add(getString(R.string.archaeological_site));
        typology_list.add(getString(R.string.museum_exhibition));
        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typology_list);
        typology.setAdapter(adapterItems);
        typology.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                typology.setError(null);
                item = parent.getItemAtPosition(position).toString();
            }
        });
        loadingBar = findViewById(R.id.modifyPlaceLoadingBar);


        db.collection("Places")
                .whereEqualTo("idUser",idUser)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocuments = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : listDocuments) {

                                Place place= d.toObject(Place.class);

                                if(Place.equals(place.getName())){

                                    placeText.setText(place.getName());
                                    namePlace.setText(place.getName());
                                    city.setText(place.getCity());
                                    typology.setText(place.getTypology());
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



    public void getIdPlace(String namePlace) {

        db.collection("Places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista luoghi

                    for (DocumentSnapshot d : listDocument) {
                        Place place = d.toObject(Place.class);

                        if (namePlace.equals(place.getName())){
                            idPlace = d.getId();
                            break;
                        }

                    }
                }
            }
        });
    }


    public void updatePlace(View view) {

        //recupero dati modificati
        String Name = namePlace.getText().toString();
        String City = city.getText().toString();
        String Typology = typology.getText().toString();

        Boolean control = inputControl(Name,City,Typology);

        if(control){
            return;
        }else{

            //salvataggio modifiche su SQLITE
            //int update = updatePlaceOnDbLocal(Name,City,Typology);

            //salvataggio modifiche su Firebase
            updatePlaceOnDbRemote(Name,City,Typology);

        }
    }


    public boolean inputControl(String Name,String City,String Typology){
        Boolean errorFlag = false;

        if(Name.isEmpty()){
            namePlace.setError(getString(R.string.required_field));
            namePlace.requestFocus();
            errorFlag = true;
        }

        if(City.isEmpty()){
            city.setError(getString(R.string.required_field));
            city.requestFocus();
            errorFlag = true;
        }

        if(Typology.isEmpty()){
            typology.setError(getString(R.string.required_field));
            typology.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }


    /*public int updatePlaceOnDbLocal(String Name,String Surname,String DateOfBirth,String Username,String Email,String EmailLocal){

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

    public void updatePlaceOnDbRemote(String Name, String City, String Typology){

        loadingBar.setVisibility(View.VISIBLE);

        db.collection("Places").document(idPlace)
                .update("name",Name,"city",City,"typology",Typology)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ModifyPlaceActivity.this, "Luogo aggiornato correttamente", Toast.LENGTH_LONG).show();
                        if(dashboardFlag.equals("1")){
                            startActivity(new Intent(ModifyPlaceActivity.this, DashboardActivity.class));
                            loadingBar.setVisibility(View.GONE);
                            finish();
                        }else {
                            startActivity(new Intent(ModifyPlaceActivity.this, ListPlacesActivity.class));
                            loadingBar.setVisibility(View.GONE);
                            finishAffinity();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyPlaceActivity.this, "Non è stato possibile aggiornare il luogo", Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }


    public void openZonesList(View view){
        Intent intent = new Intent(ModifyPlaceActivity.this, ListZonesActivity.class);
        intent.putExtra("Place", Place);
        intent.putExtra("idPlace", idPlace);
        intent.putExtra("dashboardFlag", dashboardFlag);
        startActivity(intent);
    }

    public void onBackButtonClick(View view){
        if(dashboardFlag.equals("1")){
            startActivity(new Intent(ModifyPlaceActivity.this, DashboardActivity.class));
            finish();
        }else {
            startActivity(new Intent(ModifyPlaceActivity.this, ListPlacesActivity.class));
            finishAffinity();
        }
    }



}