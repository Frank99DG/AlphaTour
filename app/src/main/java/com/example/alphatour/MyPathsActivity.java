package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alphatour.databinding.ActivityDashboardBinding;
import com.example.alphatour.databinding.ActivityMyPathsBinding;
import com.example.alphatour.oggetti.MapZoneAndObject;
import com.example.alphatour.oggetti.Path;
import com.example.alphatour.oggetti.PathString;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyPathsActivity extends DrawerBaseActivity {

    private LinearLayout list_paths;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ActivityMyPathsBinding activityMyPathsBinding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyPathsBinding = ActivityMyPathsBinding.inflate(getLayoutInflater());
        setContentView(activityMyPathsBinding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationBarPaths);
        bottomNavigationView.setSelectedItemId(R.id.tb_routes);
        bottomNavBarClick();

        list_paths = findViewById(R.id.list_mypaths);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        db.collection("Path").whereEqualTo("idUser", user.getUid() ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d : listDocument){

                        PathString path = d.toObject(PathString.class);

                        View path_view = getLayoutInflater().inflate(R.layout.row_path, null, false);
                        TextView name_path = (TextView) path_view.findViewById(R.id.name_path2);
                        TextView description_path = (TextView) path_view.findViewById(R.id.description_path2);


                        name_path.setText(path.getName());
                        description_path.setText(path.getDescription());

                        /*
                        Iterator it = mappa.entrySet().iterator();

                        while(it.hasNext()){
                            Map.Entry entry = (Map.Entry) it.next();

                            Object c = entry.getValue();

                        }
                         */
                        list_paths.addView(path_view);
                    }
                }
            }
        });


    }


    //inserimento pulsanti toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_profile, menu);
        return true;
    }

    //funzionamento pulsanti toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.tb_notify:
                return true;
            case R.id.tb_update:
                return true;
            default:
                return onOptionsItemSelected(item);

        }
    }

    public void bottomNavBarClick(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.tb_profile:
                        startActivity(new Intent(MyPathsActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tb_home:
                        startActivity(new Intent(MyPathsActivity.this, DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tb_routes:
                        startActivity(new Intent(MyPathsActivity.this, MyPathsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }

}