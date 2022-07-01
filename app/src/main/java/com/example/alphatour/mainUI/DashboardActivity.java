package com.example.alphatour.mainUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.example.alphatour.R;
import com.example.alphatour.calendar.CalendarActivity;
import com.example.alphatour.databinding.ActivityDashboardBinding;
import com.example.alphatour.modifyplace.ListPlacesActivity;
import com.example.alphatour.modifyplace.ModifyObjectActivity;
import com.example.alphatour.modifyplace.ModifyPlaceActivity;
import com.example.alphatour.modifyplace.ModifyZoneActivity;
import com.example.alphatour.objectclass.TourItem;
import com.example.alphatour.objectclass.ElementString;
import com.example.alphatour.objectclass.Place;
import com.example.alphatour.objectclass.Zone;
import com.example.alphatour.qrcode.ScanQrCodeActivity;
import com.example.alphatour.wizardcreateplace.CreationWizard;
import com.example.alphatour.wizardcreatepath.PercorsoWizard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends DrawerBaseActivity {

    private AutoCompleteTextView inputSearch;
    private List<TourItem> placesZonesElementsList = new ArrayList<TourItem>();
    private TourAdapter adapterItems;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar loadingBar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private static int n_path=0;
    private static boolean firstZoneChosen=false;
    private static String zona_scelta;
    private static String zona_vecchia;
    private ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        //impostazione barra di ricerca luogi,zone,elementi per la modifica
        inputSearch = findViewById(R.id.inputSearch);
        fillPlacesZonesElementsList();
        adapterItems = new TourAdapter(this, placesZonesElementsList);
        inputSearch.setAdapter(adapterItems);
        inputSearchClick();


        //impostazione del bottom navigation menu
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.tb_home); //per partire con la selezione su home
        bottomNavBarClick();

    }


    //inserimento pulsanti toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_dashboard, menu);
        return true;
    }

    //funzionamento pulsanti toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            default:
                return onOptionsItemSelected(item);

        }

    }

    //per rimuovere il focus e la tastiera quando si clicca fuori dalla AutoCompleteTextView
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof AutoCompleteTextView) {
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



    public void fillPlacesZonesElementsList() {

        db.collection("Elements")
                .whereEqualTo("idUser",idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Step 1: if activity is no longer valid to use just skip this "task" response.
                if (isFinishing() || isDestroyed()) return;

                if(task.isSuccessful()){

                    if (task.getResult() != null) {

                        for (QueryDocumentSnapshot d : task.getResult()) {

                            ElementString element = d.toObject(ElementString.class);

                            placesZonesElementsList.add(new TourItem( R.drawable.ic_statue, element.getTitle() ));

                            // Step 2: items are updated
                            adapterItems.updateList(placesZonesElementsList);
                        }

                    }
                }
            }
        });


        db.collection("Zones")
                .whereEqualTo("idUser",idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // Step 1: if activity is no longer valid to use just skip this "task" response.
                        if (isFinishing() || isDestroyed()) return;

                        if(task.isSuccessful()){

                            if (task.getResult() != null) {

                                for (QueryDocumentSnapshot d : task.getResult()) {

                                    Zone zone = d.toObject(Zone.class);

                                    placesZonesElementsList.add(new TourItem( R.drawable.ic_zone, zone.getName() ));

                                    // Step 2: items are updated
                                    adapterItems.updateList(placesZonesElementsList);
                                }

                            }
                        }
                    }
                });

        db.collection("Places")
                .whereEqualTo("idUser",idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // Step 1: if activity is no longer valid to use just skip this "task" response.
                        if (isFinishing() || isDestroyed()) return;

                        if(task.isSuccessful()){

                            if (task.getResult() != null) {

                                for (QueryDocumentSnapshot d : task.getResult()) {

                                    Place place = d.toObject(Place.class);

                                    placesZonesElementsList.add(new TourItem( R.drawable.ic_museum_mini, place.getName() ));

                                    // Step 2: items are updated
                                    adapterItems.updateList(placesZonesElementsList);
                                }

                            }
                        }
                    }
                });

    }



    private void inputSearchClick() {

        inputSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TourItem clickedItem = (TourItem) parent.getItemAtPosition(position);
                String clickedTourText = clickedItem.getTourText();

                db.collection("Elements").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista elementi

                            for (DocumentSnapshot d : listDocument) {
                                ElementString element = d.toObject(ElementString.class);

                                if( clickedTourText.equals(element.getTitle()) ){
                                    Intent intent = new Intent(DashboardActivity.this, ModifyObjectActivity.class);
                                    intent.putExtra("data",element.getQrData());
                                    String dashboardFlag = "1";
                                    intent.putExtra("dashboardFlag", dashboardFlag);
                                    startActivity(intent);
                                    inputSearch.setText(null);
                                    break;
                                }
                            }
                        }
                    }
                });


                db.collection("Zones").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista zone

                            for (DocumentSnapshot d : listDocument) {
                                Zone zone = d.toObject(Zone.class);

                                if( clickedTourText.equals(zone.getName())  ){
                                    Intent intent = new Intent(DashboardActivity.this, ModifyZoneActivity.class);
                                    intent.putExtra("idPlace", zone.getIdPlace());
                                    intent.putExtra("Zone", zone.getName());
                                    String dashboardFlag = "1";
                                    intent.putExtra("dashboardFlag", dashboardFlag);
                                    startActivity(intent);
                                    inputSearch.setText(null);
                                    break;
                                }


                            }
                        }
                    }
                });


                db.collection("Places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista luoghi

                            for (DocumentSnapshot d : listDocument) {
                                Place place = d.toObject(Place.class);

                                if( clickedTourText.equals(place.getName()) ){
                                    Intent intent = new Intent(DashboardActivity.this, ModifyPlaceActivity.class);
                                    intent.putExtra("Place", place.getName());
                                    String dashboardFlag = "1";
                                    intent.putExtra("dashboardFlag", dashboardFlag);
                                    startActivity(intent);
                                    inputSearch.setText(null);
                                    break;
                                }

                            }
                        }
                    }
                });

            }
        });

    }


    public void scanQrCode(View v){
        startActivity(new Intent(DashboardActivity.this, ScanQrCodeActivity.class));
    }

    public void openRouteWizard(View v){
        startActivity(new Intent(DashboardActivity.this, PercorsoWizard.class));
    }

    public void openCreationWizard(View v){
        startActivity(new Intent(DashboardActivity.this, CreationWizard.class));
    }

    public void openCalendar(View v){
        startActivity(new Intent(DashboardActivity.this, CalendarActivity.class));
    }

    public void openUpdatePlace(View v){
        startActivity(new Intent(DashboardActivity.this, ListPlacesActivity.class));
    }



    public void bottomNavBarClick(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.tb_profile:
                        startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tb_home:
                        startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tb_routes:
                        startActivity(new Intent(DashboardActivity.this, MyPathsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }

    public static boolean isFirstZoneChosen() {
        return firstZoneChosen;
    }

    public static void setFirstZoneChosen(boolean firstZoneChosen) {
        DashboardActivity.firstZoneChosen = firstZoneChosen;
    }

    public static String getZona_scelta() {
        return zona_scelta;
    }

    public static void setZona_scelta(String zona_scelta) {
        DashboardActivity.zona_scelta = zona_scelta;
    }

    public static String getZona_vecchia() {
        return zona_vecchia;
    }

    public static void setZona_vecchia(String zona_vecchia) {
        DashboardActivity.zona_vecchia = zona_vecchia;
    }
}
