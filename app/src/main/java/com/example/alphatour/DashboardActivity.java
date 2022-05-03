package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.wizardcreazione.CreationWizard;
import com.example.alphatour.wizardpercorso.PercorsoWizard;
import com.example.alphatour.wizardpercorso.Step4;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    public static final String AGE = "AGE";
    public static final String N_NOTIFY = "N_NOTIFY";
    private static final String KEY_COUNTER="KEY_COUNTER";

    private TextView ageText;
    private static int n_notify;
    private int a=0;
    private NotificationCounter notificationCounter;
    private NotifyFragment myFragment = new NotifyFragment();
    private HamburgerMenuFragment myFragmentMenu = new HamburgerMenuFragment();
    private Bundle data = new Bundle();
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    private FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private String namePlace,nameZone,titleElement;
    private AutoCompleteTextView inputSearch;
    private static List<String> placesZonesElementsList =new ArrayList<String>();
    private ArrayAdapter<String> adapterItems;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String idUser;
    private static int n_path=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        inputSearch = findViewById(R.id.inputSearch);
        //inputSearch.setFocusable(false);
        placesZonesElementsList = getPlacesZonesElementsList();
        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, placesZonesElementsList);
        inputSearch.setAdapter(adapterItems);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.tb_home); //per partire con la selezione su home
        bottonNavClick();


        if (savedInstanceState != null) {
            //Restore the fragment's instance
           // myFragment = (NotifyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

        Intent i = getIntent();
        n_notify = i.getIntExtra(N_NOTIFY,0);

        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        ft.replace(R.id.container,myFragment).hide(myFragment).commit();
        fragmentTransaction.replace(R.id.menu,myFragmentMenu).hide(myFragmentMenu).commit();


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

                        //if(idUser == element.idCurator){
                            titleElement = element.getTitle();
                            list.add(titleElement);
                        //}
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

                        //if(idUser == zone.idCurator){
                        nameZone = zone.getName();
                        list.add(nameZone);
                        //}


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

                        //if(idUser == place.idCurator){
                        namePlace = place.name;
                        list.add(namePlace);
                        //}

                    }
                }
            }
        });

        return list;

    }

    public void menu(View view){
        getSupportFragmentManager().beginTransaction().show(myFragmentMenu).commit();

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
}
