package com.example.alphatour;

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

import com.example.alphatour.databinding.ActivityDashboardBinding;
import com.example.alphatour.TourAdapter;
import com.example.alphatour.oggetti.TourItem;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.qrcode.ScanQrCodeActivity;
import com.example.alphatour.wizardcreazione.CreationWizard;
import com.example.alphatour.wizardpercorso.PercorsoWizard;
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

    public static final String AGE = "AGE";
    public static final String N_NOTIFY = "N_NOTIFY";
    private static final String KEY_COUNTER="KEY_COUNTER";

    private TextView ageText;
    private static int n_notify;
    private int a=0;
    private NotificationCounter notificationCounter;
    private NotifyFragment myFragment = new NotifyFragment();
    private Bundle data = new Bundle();
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    private FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private AutoCompleteTextView inputSearch;
    private List<TourItem> placesZonesElementsList = new ArrayList<TourItem>();
    private TourAdapter adapterItems;
    private String item;
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
    private int count;

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

        //impostazioni notifiche
        //notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        //ft.replace(R.id.container,myFragment).hide(myFragment).commit();


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
            case R.id.tb_notify:
                return true;
            default:
                return onOptionsItemSelected(item);

        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        //notifiche
        /*TextView view = (TextView) findViewById(R.id.notificationNumber);
        String  counter_notify = (String) view.getText();
        NotificationCounter.setCount(Integer.parseInt(counter_notify));*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        //notifiche
        /*if(NotificationCounter.getSend_notify()==true) {
            Toast.makeText(this, "You have a new notification", Toast.LENGTH_LONG).show();
            NotificationCounter.setSend_notify(false);
        }

        if(NotificationCounter.getCount()>0){
            n_notify=NotificationCounter.getCount();
            for (a=0;a<n_notify;a++){
                notificationCounter.increaseNumber();
            }NotificationCounter.setCount(0);
        }

        if(invioProva.getN_notify()>0  ){
            n_notify= invioProva.getN_notify();
            for(a=0;  a<n_notify; a++) {
                notificationCounter.increaseNumber();
            }invioProva.setN_notify(0);
            ageText.setText("Counter is: " + n_notify);
        }*/

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //salvataggio notifiche
       //savedInstanceState.putCharSequence(KEY_COUNTER, notificationCounter.getNotificationNumber().getText());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore notifiche
      //this.notificationCounter.setTextNotify(savedInstanceState,KEY_COUNTER);
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


    public void openFragment(View v) {

        getSupportFragmentManager().beginTransaction().show(myFragment).commit();

        for (a = 0; a < n_notify; a++){             //Ciclo per aggiungere n_notify da
            notifyPath();                           //un'altra activity al fragment
            if(a==n_notify-1) n_notify =0;
        }
    }

    public void createPath(View v) {
        data.putString("Notify", "You have created a path");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);
    }


    public void buttonNotify(View view) {
        data.putString("Notify", "Random notify");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);
    }

    public void notifyPath(){
        data.putString("Notify", "You have created a path");
        myFragment.setArguments(data);
        myFragment.addView(notificationCounter);

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

    /*public void openCalendar(View v){
        startActivity(new Intent(DashboardActivity.this, .class));
    }*/

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
