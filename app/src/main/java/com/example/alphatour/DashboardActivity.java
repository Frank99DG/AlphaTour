package com.example.alphatour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.alphatour.databinding.ActivityDashboardBinding;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.User;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.wizardcreazione.CreationWizard;
import com.example.alphatour.wizardpercorso.PercorsoWizard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private static List<String> placesZonesElementsList =new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
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
        placesZonesElementsList = getPlacesZonesElementsList();
        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, placesZonesElementsList);
        inputSearch.setAdapter(adapterItems);
        inputSearchClick();


        //impostazione del bottom navigation menu
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.tb_home); //per partire con la selezione su home
        bottomNavBarClick();

        //impostazioni notifiche
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        ft.replace(R.id.container,myFragment).hide(myFragment).commit();


    }


    @Override
    protected void onPause() {
        super.onPause();

        //notifiche
        TextView view = (TextView) findViewById(R.id.notificationNumber);
        String  counter_notify = (String) view.getText();
        NotificationCounter.setCount(Integer.parseInt(counter_notify));

    }

    @Override
    protected void onResume() {
        super.onResume();

        //notifiche
        if(NotificationCounter.getSend_notify()==true) {
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
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //salvataggio notifiche
       savedInstanceState.putCharSequence(KEY_COUNTER, notificationCounter.getNotificationNumber().getText());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore notifiche
      this.notificationCounter.setTextNotify(savedInstanceState,KEY_COUNTER);
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



    public List<String> getPlacesZonesElementsList() {

        List<String> list = new ArrayList<String>();

        db.collection("Elements").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista elementi

                    for (DocumentSnapshot d : listDocument) {
                        ElementString element = d.toObject(ElementString.class);

                        if( idUser.equals(element.getIdUser()) ){
                            String titleElement = element.getTitle();
                            list.add(titleElement);
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

                        if( idUser.equals(zone.getIdUser()) ){
                            String nameZone = zone.getName();
                            list.add(nameZone);
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

                        if( idUser.equals(place.getIdUser()) ){
                            String namePlace = place.getName();
                            list.add(namePlace);
                        }

                    }
                }
            }
        });

        return list;

    }



    private void inputSearchClick() {

        inputSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item = parent.getItemAtPosition(position).toString();

                db.collection("Elements").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista elementi

                            for (DocumentSnapshot d : listDocument) {
                                ElementString element = d.toObject(ElementString.class);

                                if( item.equals(element.getTitle()) ){
                                    startActivity(new Intent(DashboardActivity.this, ModifyObjectActivity.class));
                                    inputSearch.setText(null);
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

                                if( item.equals(zone.getName())  ){
                                    startActivity(new Intent(DashboardActivity.this, ModifyZoneActivity.class));
                                    inputSearch.setText(null);
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

                                if( item.equals(place.getName()) ){
                                    startActivity(new Intent(DashboardActivity.this, ModifyPlaceActivity.class));
                                    inputSearch.setText(null);
                                }

                            }
                        }
                    }
                });

            }
        });

    }

    public void openRouteWizard(View v){
        startActivity(new Intent(DashboardActivity.this, PercorsoWizard.class));
    }

    public void openCreationWizard(View v){
        startActivity(new Intent(DashboardActivity.this, CreationWizard.class));
    }

    /*public void openCalendar(View v){
        startActivity(new Intent(DashboardActivity.this, .class));
    }

    public void openUpdatePlace(View v){
        startActivity(new Intent(DashboardActivity.this, .class));
    }*/


    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.tb_home:
                startActivity( new Intent(DashboardActivity.this, DashboardActivity.class) );
                break;
            case R.id.tb_profile:
                startActivity( new Intent(DashboardActivity.this, ProfileActivity.class) );
                break;
            case R.id.tb_share_path:
                break;
            case R.id.tb_logout:
                loadingBar.setVisibility(View.VISIBLE);
                auth.signOut();
                startActivity( new Intent(DashboardActivity.this, LoginActivity.class) );
                Toast.makeText(this, "Ti sei disconnesso", Toast.LENGTH_LONG).show();
                finishAffinity(); //Chiude tutte le attività presenti nello Stack, evita che il tasto back porti alla dashboard dopo il logout
                loadingBar.setVisibility(View.GONE);
        }

        return false;
    }*/


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
                    /*case R.id.tb_routes:
                        startActivity(new Intent(DashboardActivity.this, .class));
                        overridePendingTransition(0,0);
                        return true;*/
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
