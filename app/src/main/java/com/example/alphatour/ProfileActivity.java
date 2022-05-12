package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.databinding.ActivityDashboardBinding;
import com.example.alphatour.databinding.ActivityProfileBinding;
import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.oggetti.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends DrawerBaseActivity {

    private CircleImageView profile;
    private FloatingActionButton changeProfile;
    private TextView textWelcome,textNomeAndCognome,textEmail,textDataNascita,textUsername;
    private String name, surname, dateOfBirth,email,username;
    private String image;
    private String idUtenteLocal;
    private ProgressBar loadingBar;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String idUser;
    boolean registered = false;
    private StorageReference storegeProfilePick;
    private StorageTask uploadTask;
    private  int i=0;
    private ActivityProfileBinding activityProfileBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();

        changeProfile = findViewById(R.id.changeProfile);
        profile = findViewById(R.id.profile_image);

        textWelcome = findViewById(R.id.showWelcome);
        textNomeAndCognome = findViewById(R.id.profileNameUser);
        textEmail = findViewById(R.id.profileEmailUser);
        textDataNascita = findViewById(R.id.profileDateBirthUser);
        textUsername = findViewById(R.id.profileUsernameUser);
        loadingBar = findViewById(R.id.profileLoadingBar);

        //impostazione del bottom navigation menu
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.tb_profile); //per partire con la selezione su home
        bottomNavBarClick();

        storegeProfilePick= FirebaseStorage.getInstance().getReference();
        if(user == null){
            Toast.makeText(ProfileActivity.this,"Si è verificato un errrore: i dati dell'utente non sono disponibili !!!",Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setVisibility(View.VISIBLE);
            showUserProfile();
        }
    }


    private void showUserProfile() {

        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista utenti registrati

                    for(DocumentSnapshot d: listDocument){

                        if(d.getId().matches(idUser)) { // controllo se l'id dell'utente esiste

                            //recupero dati
                            registered = true;
                            User user = d.toObject(User.class);
                            name = user.name;
                            i=-1;
                            surname = user.surname;
                            email = user.email;
                            dateOfBirth = user.dateBirth;
                            username = user.username;
                            image = user.image;

                            if (image != null){
                                final StorageReference fileRef = storegeProfilePick.child("UserImage").child(auth.getCurrentUser().getUid());

                                try{
                                    File localFile= File.createTempFile("tempfile",".png");
                                    fileRef.getFile(localFile)
                                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                    profile.setImageBitmap(bitmap);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }catch(IOException e){
                                    e.printStackTrace();
                                }
                        }

                            //stampa dati nelle editText
                            i=-2;
                            textWelcome.setText("Benvenuto, "+name+" "+ surname);
                            textNomeAndCognome.setText(name +" "+ surname);
                            textEmail.setText(email);
                            textDataNascita.setText(dateOfBirth);
                            textUsername.setText(username);
                            if(image!=null) {
                                //profile.setImageURI(Uri.parse(image));
                            }

                        }


                    }

                    if(registered == false){
                        Toast.makeText(ProfileActivity.this,"Utente non registrato",Toast.LENGTH_LONG).show();
                    }else{
                        loadingBar.setVisibility(View.GONE);
                    }

                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Uri uri = data.getData();

            profile.setImageURI(uri);
            saveImageProfileOnDbRemote(uri);
            int value=saveImageProfileOnDbLocal(uri);
           /* if(value){

            }*/
    }

    // salva l'uri dell'immagine di profilo
    private int saveImageProfileOnDbLocal(Uri uri) {

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(this);
        //recupero idUtente
        SQLiteDatabase db = dbAlpha.getReadableDatabase();
        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_USER_PROFILE,new String[]{email});
        if(cursor.moveToFirst()){
            idUtenteLocal= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID));
        }
        db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_IMAGE,uri.toString());
       return db.update(AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER,values,AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+
                CommandDbAlphaTour.Command.EQUAL+CommandDbAlphaTour.Command.VALUE,new String[] {idUtenteLocal});
    }

    private void saveImageProfileOnDbRemote(Uri uri) {
        loadingBar.setVisibility(View.VISIBLE);
        if(uri!=null) {
            final StorageReference fileRef = storegeProfilePick.child("UserImage").child(auth.getCurrentUser().getUid());

            uploadTask = fileRef.putFile(uri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        //scarico il link di Storage dell'immagine
                        Uri downloadUrl = (Uri) task.getResult();
                        String myUri= downloadUrl.toString();

                        //aggiorno il campo immagine
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("image",myUri);

                        db.collection("Users").document(user.getUid()).
                        update("image", myUri).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfileActivity.this, "Hai aggiornato l'immagine di profilo", Toast.LENGTH_LONG).show();
                                        loadingBar.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Non è stato possibile aggiornare l'immagine di profilo!", Toast.LENGTH_LONG).show();
                                loadingBar.setVisibility(View.GONE);
                            }
                        });

                    }

                }
            });
        }else{
            Toast.makeText(ProfileActivity.this,"Immagine non selezionata!",Toast.LENGTH_LONG).show();
            loadingBar.setVisibility(View.GONE);
        }

    }

    public void changeProfile(View v){

        ImagePicker.with(this)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(20);
    }

    public void updateProfile(View v){
        startActivity(new Intent(this,UpdateProfileActivity.class));
    }


    public void bottomNavBarClick(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.tb_profile:
                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tb_home:
                        startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
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


}