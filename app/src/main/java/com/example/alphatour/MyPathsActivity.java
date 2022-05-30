package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

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
    private Dialog dialog;
    private Button dialog_delete_path, dialog_dismiss;
    private TextView dialog_title,dialog_text;
    private ImageView dialog_delete_image;

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

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_delete_path = dialog.findViewById(R.id.btn_okay);
        dialog_dismiss = dialog.findViewById(R.id.btn_cancel);
        dialog_title = dialog.findViewById(R.id.titleDialog);
        dialog_text = dialog.findViewById(R.id.textDialog);
        dialog_delete_image= dialog.findViewById(R.id.imageDialog);
        dialog_delete_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));

        dialog_title.setText("Elimina percorso");
        dialog_text.setText("Sei sicuro di voler eliminare il percorso creato?");


        db.collection("Path").whereEqualTo("idUser", user.getUid() ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : listDocument) {

                        PathString path = d.toObject(PathString.class);

                        View path_view = getLayoutInflater().inflate(R.layout.row_path, null, false);
                        TextView name_path = (TextView) path_view.findViewById(R.id.name_path2);
                        TextView description_path = (TextView) path_view.findViewById(R.id.description_path2);
                        ImageView delete_path = path_view.findViewById(R.id.delete_path);


                        name_path.setText(path.getName());
                        description_path.setText(path.getDescription());

                        delete_path.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.show();

                                dialog_delete_path.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        list_paths.removeView(path_view);
                                        dialog.dismiss();
                                        db.collection("Path").whereEqualTo("idUser",user.getUid()).
                                                whereEqualTo("name",path.getName()).
                                                whereEqualTo("description",path.getDescription()).
                                                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {

                                                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments();

                                                    for (DocumentSnapshot d : listDocument) {

                                                        String idPath = d.getId();

                                                        db.collection("Path").document(idPath).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(MyPathsActivity.this, "Percorso cancellato con successo", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });

                                dialog_dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

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
        inflater.inflate(R.menu.toolbar_dashboard, menu);
        return true;
    }

    //funzionamento pulsanti toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            /*
            case R.id.tb_notify:
                return true;
            case R.id.tb_update:
                return true;
             */
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