package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout container;
    private NavigationView navigationView;
    private View headerView;
    private CircleImageView photoHeader;
    private TextView nameHeader,surnameHeader;
    private StorageReference storegeProfilePick;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);

        storegeProfilePick= FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        //impostazione del drawer layout, contenitore delle activity ospitate, toolbar e del navigation menu
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        container = drawerLayout.findViewById(R.id.activityContainer);
        toolbar = drawerLayout.findViewById(R.id.toolbar);
        navigationView =  drawerLayout.findViewById(R.id.nav_view);
        //loadingBar = findViewById(R.id.dashboardLoadingBar);

        //impostazione della toolbar
        setSupportActionBar(toolbar);

        //impostazione del toggle della toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //personalizzazione icona del toggle hamburger
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);

        //impostazione della navigation view
        navigationView.setNavigationItemSelectedListener(this);

        //impostazione informazioni utenti nell'header del navigation view
        headerView = navigationView.getHeaderView(0);
        photoHeader = (CircleImageView) headerView.findViewById(R.id.photoProfile);
        nameHeader = (TextView) headerView.findViewById(R.id.nameProfile);
        surnameHeader = (TextView) headerView.findViewById(R.id.surnameProfile);
        takePhotoNameSurnameUser();



    }


    @Override
    public void setContentView(View view){

        //imposto la vista dell'activity
        container.addView(view);
        super.setContentView(drawerLayout);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void takePhotoNameSurnameUser(){

        final StorageReference fileRef = storegeProfilePick.child("UserImage").child(idUser);

        try{
            File localFile= File.createTempFile("tempfile",".png");
            fileRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            photoHeader.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }


        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista di utenti (id)

                    for (DocumentSnapshot d : listDocument) {    //d è un id ciclato dalla lista
                        if(idUser.equals( d.getId() )){     //se l'id dell'utente loggato corrisponde all'id della lista
                            User user = d.toObject(User.class);
                            nameHeader.setText(user.getName());
                            surnameHeader.setText(user.getSurname());
                            break;
                        }
                    }
                }
            }
        });

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.tb_home:
                startActivity( new Intent(this, DashboardActivity.class) );
                break;
            case R.id.tb_profile:
                startActivity( new Intent(this, ProfileActivity.class) );
                break;
            case R.id.tb_routes:
                startActivity( new Intent(this, MyPathsActivity.class) );
                break;
            case R.id.tb_share_path:
              //startActivity( new Intent(this, ) );
                break;
            case R.id.tb_logout:
                //loadingBar.setVisibility(View.VISIBLE);
                auth.signOut();
                startActivity( new Intent(this, LoginActivity.class) );
                Toast.makeText(this, R.string.logged_out, Toast.LENGTH_LONG).show();
                finishAffinity(); //Chiude tutte le attività presenti nello Stack, evita che il tasto back porti alla dashboard dopo il logout
                //loadingBar.setVisibility(View.GONE);
        }

        return false;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}