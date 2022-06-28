package com.example.alphatour.userfuction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.R;
import com.example.alphatour.connection.Receiver;
import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.mainUI.ProfileActivity;
import com.example.alphatour.objectclass.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editName,editSurname,editEmail,editDateOfBirth,editUsername;
    private String name,surname,dateOfBirth,email,username,idUtenteLocal;
    private String emailLocal;
    private ProgressBar loadingBar;
    boolean registered = false;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String idUser;
    private Receiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        editName = findViewById(R.id.updateName);
        editSurname = findViewById(R.id.updateSurname);
        editEmail = findViewById(R.id.updateEmail);
        editDateOfBirth=findViewById(R.id.updateDateBirth);
        editUsername = findViewById(R.id.updateUsername);
        loadingBar = findViewById(R.id.profileUpdateLoadingBar);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        idUser = user.getUid();
       /* String em=editEmail.getText().toString();
        String em1=em.toString();
        emailLocal=new String[]{em1};*/
        showUserProfile();


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


    //To remove focus and keyboard when click outside EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
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


    //visualizza i dati del profilo utente
    private void showUserProfile() {

        loadingBar.setVisibility(View.VISIBLE);

        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listDocument = queryDocumentSnapshots.getDocuments(); //lista utenti registrati

                    for(DocumentSnapshot d: listDocument){

                        if(d.getId().matches(idUser)){ // controllo se l'id dell'utente esiste

                            //recupero dati
                            registered = true;
                            User user = d.toObject(User.class);
                            name = user.name;
                            surname = user.surname;
                            email = user.email;
                            emailLocal = user.email;
                            dateOfBirth = user.dateBirth ;
                            username=user.username;

                            //stampa dati nelle editText
                            editName.setText(name);
                            editSurname.setText(surname);
                            editEmail.setText(email);
                            editDateOfBirth.setText(dateOfBirth);
                            editUsername.setText(username);

                            break;
                        }


                    }

                    if(registered == false){
                        Toast.makeText(UpdateProfileActivity.this,R.string.unregistered_user,Toast.LENGTH_LONG).show();
                    }else{
                        loadingBar.setVisibility(View.GONE);
                    }

                }

            }
        });
    }

    public void openChangePasswordActivity(View view) {

        Intent intent =new Intent(UpdateProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }


    public void updateProfile(View view) {

        Boolean control = inputControl(editName.getText().toString(),editSurname.getText().toString(),editDateOfBirth.getText().toString(),
                editUsername.getText().toString(),editEmail.getText().toString());

        if(control){
            return;
        }else{

            //recupero dati modificati
            name = editName.getText().toString();
            surname = editSurname.getText().toString();
            email = editEmail.getText().toString();
            dateOfBirth = editDateOfBirth.getText().toString();
            username = editUsername.getText().toString();

            //salvataggio modifiche su SQLITE

            int update = updateUserOnDbLocal(name,surname,dateOfBirth,username,email,emailLocal);
            startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));

            updateUserOnDbRemote(name,surname,dateOfBirth,username,email);


            //salvataggio modifiche su Firebase

        }
    }

    public boolean inputControl(String Name,String Surname,String DateOfBirth,String Username,String Email){
        Boolean errorFlag = false;

        if(Name.isEmpty()){
            editName.setError(getString(R.string.required_field));
            editName.requestFocus();
            errorFlag = true;
        }

        if(Surname.isEmpty()){
            editSurname.setError(getString(R.string.required_field));
            editSurname.requestFocus();
            errorFlag = true;
        }

        if(DateOfBirth.isEmpty()){
            editDateOfBirth.setError(getString(R.string.required_field));
            editDateOfBirth.requestFocus();
            errorFlag = true;
        }

        if(Username.isEmpty()){
            editUsername.setError(getString(R.string.required_field));
            editUsername.requestFocus();
            errorFlag = true;
        }

        if(Username.length() < 4 ){
            editUsername.setError(getString(R.string.username_constraints));
            editUsername.requestFocus();
            errorFlag = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editEmail.setError(getString(R.string.invalid_email));
            editEmail.requestFocus();
            errorFlag = true;
        }

        return errorFlag;
    }

    public int updateUserOnDbLocal(String Name,String Surname,String DateOfBirth,String Username,String Email,String EmailLocal){

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(this);
        //recupero idUtente
        SQLiteDatabase db = dbAlpha.getReadableDatabase();
        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_USER_PROFILE,new String[]{emailLocal});
        if(cursor.moveToFirst()){
            idUtenteLocal= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID));
        }

        //aggiornamento dati utente

        db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_NAME,Name);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_SURNAME,Surname);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_DATE_BIRTH,DateOfBirth);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_USERNAME,Username);
        values.put(AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_EMAIL,Email);

        return db.update(AlphaTourContract.AlphaTourEntry.NAME_TABLE_USER,values,AlphaTourContract.AlphaTourEntry.NAME_COLUMN_USER_ID+
                CommandDbAlphaTour.Command.EQUAL+CommandDbAlphaTour.Command.VALUE,new String[] {idUtenteLocal});


    }

    public void updateUserOnDbRemote(String Name,String Surname,String DateOfBirth,String Username,String Email){

        loadingBar.setVisibility(View.VISIBLE);
        User userUpdate = new User(Name,Surname,DateOfBirth,Username,Email);
        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",Name);
        userMap.put("surname",Surname);
        userMap.put("email",Email);
        userMap.put("dateBirth",DateOfBirth);
        userMap.put("username",Username);

        db.collection("Users").document(user.getUid()).
                update("name",Name,"surname",Surname,"email",Email,"dateBirth",DateOfBirth,"username",Username).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateProfileActivity.this, R.string.account_update, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                        loadingBar.setVisibility(View.GONE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, R.string.account_not_update, Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }


    public void onBackButtonClick(View view){

        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
        finish();
    }

}