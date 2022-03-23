package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.oggetti.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editName,editSurname,editEmail,editDateOfBirth,editUsername;
    private String name,surname,dateOfBirth,email,username,idUtenteLocal;
    private String emailLocal;
    private ProgressBar loadingBar;
    boolean registered = false;
    private FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseFirestore db;

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

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
       /* String em=editEmail.getText().toString();
        String em1=em.toString();
        emailLocal=new String[]{em1};*/
        visualizzaProfilo(user);


    }

    //visualizza i dati del profilo utente
    private void visualizzaProfilo(FirebaseUser utente) {

        String idUtente=utente.getUid();
        loadingBar.setVisibility(View.VISIBLE);

        db.collection("Utenti").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listaDocumenti=queryDocumentSnapshots.getDocuments(); //lista utenti registrati

                    for(DocumentSnapshot d: listaDocumenti){

                        if(d.getId().matches(idUtente)){ // controllo se l'id dell'utente esiste

                            //recupero dati
                            registered =true;
                            User user = d.toObject(User.class);
                            name = user.nome;
                            surname = user.surname;
                            email = user.email;
                            emailLocal = user.email;
                            dateOfBirth = user.dateOfBirth ;
                            username=user.username;

                            //stampa dati nelle editText
                            editName.setText(name);
                            editSurname.setText(surname);
                            editEmail.setText(email);
                            editDateOfBirth.setText(dateOfBirth);
                            editUsername.setText(username);

                        }


                    }

                    if(registered == false){
                        Toast.makeText(UpdateProfileActivity.this,"Utente non registrato",Toast.LENGTH_LONG).show();
                    }else{
                        loadingBar.setVisibility(View.GONE);
                    }

                }

            }
        });
    }

    public void openChangePasswordActivity(View view) {

        Intent intent =new Intent(UpdateProfileActivity.this,ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void buttonUpdateProfile(View view) {

        updateProfile(user);
    }

    private void updateProfile(FirebaseUser utente){

        Boolean controllo=inputControl(editName.getText().toString(),editSurname.getText().toString(),editDateOfBirth.getText().toString(),
                editUsername.getText().toString(),editEmail.getText().toString());

        if(controllo){
            return;
        }else{

            //recupero dati modificati
            name = editName.getText().toString();
            surname = editSurname.getText().toString();
            email = editEmail.getText().toString();
            dateOfBirth = editDateOfBirth.getText().toString();
            username = editUsername.getText().toString();

            //salvataggio modifiche su SQLITE

            int update=updateUserOnDbLocal(name,surname,dateOfBirth,username,email,emailLocal);
            startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));

            updateUserOnDbRemote(name,surname,dateOfBirth,username,email);


            //salvataggio modifiche su Firebase

        }
    }

    public boolean inputControl(String Name,String Surname,String DateOfBirth,String Username,String Email){
        Boolean errorFlag = false;

        if(Name.isEmpty()){
            editName.setError(getString(R.string.campo_obbligatorio));
            editName.requestFocus();
            errorFlag = true;
        }

        if(Surname.isEmpty()){
            editSurname.setError(getString(R.string.campo_obbligatorio));
            editSurname.requestFocus();
            errorFlag = true;
        }

        if(DateOfBirth.isEmpty()){
            editDateOfBirth.setError(getString(R.string.campo_obbligatorio));
            editDateOfBirth.requestFocus();
            errorFlag = true;
        }

        if(Username.isEmpty()){
            editUsername.setError(getString(R.string.campo_obbligatorio));
            editUsername.requestFocus();
            errorFlag = true;
        }

        if(Username.length() < 4 ){
            editUsername.setError(getString(R.string.username_vincoli));
            editUsername.requestFocus();
            errorFlag = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editEmail.setError(getString(R.string.email_non_valida));
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
            idUtenteLocal= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID));
        }

        //aggiornamento dati utente

        db = dbAlpha.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_NOME,Name);
        values.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_COGNOME,Surname);
        values.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_DATA_NASCITA,DateOfBirth);
        values.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_USERNAME,Username);
        values.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_EMAIL,Email);

        return db.update(AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE,values,AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID+
                CommandDbAlphaTour.Command.EGUAL+CommandDbAlphaTour.Command.VALUE,new String[] {idUtenteLocal});


    }

    public void updateUserOnDbRemote(String Name,String Surname,String DateOfBirth,String Username,String Email){

        loadingBar.setVisibility(View.VISIBLE);
        User userUpdate = new User(Name,Surname,DateOfBirth,Username,Email);
        db.collection("Users").document(user.getUid()).
                set(userUpdate).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateProfileActivity.this, "Account aggiornato correttamente", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                        loadingBar.setVisibility(View.GONE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Non è stato possibile aggiornare l'account", Toast.LENGTH_LONG).show();
                loadingBar.setVisibility(View.GONE);
            }
        });

    }
}