package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.devzone.checkabletextview.CheckableTextView;
import com.devzone.checkabletextview.CheckedListener;
import com.example.alphatour.oggetti.Element;
import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Place;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.wizardcreazione.CreationWizard;
import com.example.alphatour.wizardpercorso.PercorsoWizard;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private int n_notify;
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
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        inputSearch = findViewById(R.id.inputSearch);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        idUser = user.getUid();

        if (savedInstanceState != null) {
            //Restore the fragment's instance
           // myFragment = (NotifyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

        Intent i = getIntent();
        n_notify = i.getIntExtra(N_NOTIFY,0);

        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        ft.replace(R.id.container,myFragment).hide(myFragment).commit();
        fragmentTransaction.replace(R.id.menu,myFragmentMenu).hide(myFragmentMenu).commit();


        placesZonesElementsList = getPlacesZonesElementsList(/*user*/);
        adapterItems = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, placesZonesElementsList);
        inputSearch.setAdapter(adapterItems);


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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "myFragment", myFragment);
       outState.putCharSequence(KEY_COUNTER, notificationCounter.getNotificationNumber().getText());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

       this.notificationCounter.setTextNotify(savedInstanceState,KEY_COUNTER);
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

    public List<String> getPlacesZonesElementsList(/*FirebaseUser user*/) {

        String idUser = user.getUid();
        List<String> list =new ArrayList<String>();


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

}
