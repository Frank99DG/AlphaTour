package com.example.alphatour.mainUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.R;
import com.example.alphatour.connection.Receiver;
import com.example.alphatour.databinding.ActivityProfileBinding;
import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.objectclass.User;
import com.example.alphatour.userfuction.UpdateProfileActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private Dialog dialog;
    private TextView yesFinal,titleDialog,textDialog;
    private Receiver receiver;
    private String[] Permission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};



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


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog = dialog.findViewById(R.id.titleDialog_permission);
        textDialog = dialog.findViewById(R.id.textDialog_permission);

        //impostazione del bottom navigation menu
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.tb_profile); //per partire con la selezione su home
        bottomNavBarClick();

        storegeProfilePick= FirebaseStorage.getInstance().getReference();
        if(user == null){
            Toast.makeText(ProfileActivity.this,R.string.user_data_not_available,Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setVisibility(View.VISIBLE);
            showUserProfile();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        /**controllo connessione**/

        /** 1 indica che questa è una classe che non può funzionare
         * senza connessione
         */
        receiver=new Receiver(1);

        broadcastIntent();
    }

    private void broadcastIntent() {
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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
            case R.id.tb_update:
                startActivity(new Intent(this, UpdateProfileActivity.class));
                return true;
            default:
                return onOptionsItemSelected(item);

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
                        Toast.makeText(ProfileActivity.this,R.string.unregistered_user,Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(ProfileActivity.this, R.string.updated_picture, Toast.LENGTH_LONG).show();
                                        loadingBar.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, R.string.picture_not_update, Toast.LENGTH_LONG).show();
                                loadingBar.setVisibility(View.GONE);
                            }
                        });

                    }

                }
            });
        }else{
            Toast.makeText(ProfileActivity.this,R.string.image_not_selected,Toast.LENGTH_LONG).show();
            loadingBar.setVisibility(View.GONE);
        }

    }

    public void changeProfile(View v){

        if (checkPermission()){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.CAMERA)) {
                dialog.show();
                yesFinal.setText("OK");

                titleDialog.setText(R.string.permit_required);
                textDialog.setText(R.string.permission_text);
                textDialog.setTextColor(getResources().getColor(R.color.black));

                yesFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        requestPermission();
                    }
                });
            } else {
                requestPermission();
            }
        }else{
            ImagePicker.with(ProfileActivity.this)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                    .start(20);
        }

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
                    case R.id.tb_routes:
                        startActivity(new Intent(ProfileActivity.this, MyPathsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }

    private boolean checkPermission(){

        for(String permission:Permission){
            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission)== PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission(){
        final int permissionCode=100;//codice definito da me, servirà nel caso in cui serva controllare più permessi
        ActivityCompat.requestPermissions(this,Permission,permissionCode);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ) {

            ImagePicker.with(this)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                    .start(20);
        }else{
            Toast.makeText(this,R.string.permission_denied,Toast.LENGTH_LONG).show();

        }
    }


}