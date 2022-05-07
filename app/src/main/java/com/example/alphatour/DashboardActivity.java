package com.example.alphatour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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

import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.User;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.wizardcreazione.CreationWizard;
import com.example.alphatour.wizardpercorso.PercorsoWizard;
import com.example.alphatour.wizardpercorso.Step4;
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
import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView name,surname,email;
    private AutoCompleteTextView inputSearch;
    private static List<String> placesZonesElementsList =new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String idUser;
    private static int n_path=0;
    private static boolean firstZoneChosen=false;
    private static String zona_scelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //istanza oggetti firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        //istanza layout e progress bar della dashboard
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        loadingBar = findViewById(R.id.dashboardLoadingBar);

        //impostazione informazioni utenti nell'header del navigation header
        View headerView = navigationView.getHeaderView(0);
        name = (TextView) headerView.findViewById(R.id.nameProfile);
        surname = (TextView) headerView.findViewById(R.id.surnameProfile);
        email= (TextView) headerView.findViewById(R.id.emailProfile);
        takeNameSurnameEmailUser();

        //impostazione funzionamento navigazion view, toggle hamburger e pulsanti menu
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //personalizzazione icona del toggle hamburger
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);

        //impostazione barra di ricerca luogi,zone,elementi per la modifica
        inputSearch = findViewById(R.id.inputSearch);
        placesZonesElementsList = getPlacesZonesElementsList();
        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, placesZonesElementsList);
        inputSearch.setAdapter(adapterItems);

        //impostazione della botton navigation menu
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.tb_home); //per partire con la selezione su home
        bottonNavClick();

        //impostazioni notifiche
        if (savedInstanceState != null) {
            //Restore the fragment's instance
           // myFragment = (NotifyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

        Intent i = getIntent();
        n_notify = i.getIntExtra(N_NOTIFY,0);

        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        ft.replace(R.id.container,myFragment).hide(myFragment).commit();


    }


    @Override
    protected void onResume() {
        super.onResume();

        if(invioProva.getN_notify()>0  ){
            n_notify= invioProva.getN_notify();
            for(a=0;  a<n_notify; a++) {
                notificationCounter.increaseNumber();
            }invioProva.setN_notify(0);
            ageText.setText("Counter is: " + n_notify);
        }



        if(Step4.getN_path()>0  ){
            n_notify++;
            for(a=0;  a<n_notify; a++) {
                notificationCounter.increaseNumber();
            }Step4.setN_path(0);
            Toast.makeText(this, "You have a new notification", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "myFragment", myFragment);
     //  outState.putCharSequence(KEY_COUNTER, notificationCounter.getNotificationNumber().getText());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

     // this.notificationCounter.setTextNotify(savedInstanceState,KEY_COUNTER);
    }

    //To remove focus and keyboard when click outside AutoCompleteTextView
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


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
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


    private void takeNameSurnameEmailUser(){

        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista di utenti (id)

                    for (DocumentSnapshot d : listDocument) {    //d è un id ciclato dalla lista
                        User user = d.toObject(User.class);

                        if(idUser.equals( d.getId() )){     //se l'id dell'utente loggato corrisponde all'id della lista
                            name.setText(user.getName());
                            surname.setText(user.getSurname());
                            email.setText(user.getEmail());
                            break;
                        }
                    }
                }
            }
        });

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.tb_home:
                startActivity( new Intent(DashboardActivity.this, DashboardActivity.class) );
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
    }


    public void bottonNavClick(){

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

    public static void setN_path(int n_path) {
        DashboardActivity.n_path = n_path;
    }

    public static int getN_path() {
        return n_path;
    }

    public static int getN_notify() {
        return n_notify;
    }

    public static void setN_notify(int n_notify) {
        DashboardActivity.n_notify = n_notify;
    }

    public static String getZona_scelta() {
        return zona_scelta;
    }

    public static void setZona_scelta(String zona_scelta) {
        DashboardActivity.zona_scelta = zona_scelta;
    }

    public static boolean isFirstZoneChosen() {
        return firstZoneChosen;
    }

    public static void setFirstZoneChosen(boolean firstZoneChosen) {
        DashboardActivity.firstZoneChosen = firstZoneChosen;
    }


}
