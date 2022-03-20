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
import com.example.alphatour.oggetti.Utente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editNome,editCognome,editEmail,editDataNascita,editUsername;
    private String nome,cognome,dataNascita,email,username,idUtenteLocal;
    private String emailLocal;
    private ProgressBar progressBar;
    boolean registrato=false;
    private FirebaseAuth authprofile;
    FirebaseUser utente;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        editNome=findViewById(R.id.updateName);
        editCognome=findViewById(R.id.updateCognome);
        editEmail=findViewById(R.id.updateEmail);
        editDataNascita=findViewById(R.id.updateDataNascita);
        editUsername=findViewById(R.id.updateUsername);
        progressBar=findViewById(R.id.profileUpdateBarraCaricamento);

        authprofile = FirebaseAuth.getInstance();
        utente=authprofile.getCurrentUser();
        db=FirebaseFirestore.getInstance();
       /* String em=editEmail.getText().toString();
        String em1=em.toString();
        emailLocal=new String[]{em1};*/
        visualizzaProfilo(utente);


    }

    //visualizza i dati del profilo utente
    private void visualizzaProfilo(FirebaseUser utente) {

        String idUtente=utente.getUid();
        progressBar.setVisibility(View.VISIBLE);

        db.collection("Utenti").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listaDocumenti=queryDocumentSnapshots.getDocuments(); //lista utenti registrati

                    for(DocumentSnapshot d: listaDocumenti){

                        if(d.getId().matches(idUtente)){ // controllo se l'id dell'utente esiste

                            //recupero dati
                            registrato=true;
                            Utente utente = d.toObject(Utente.class);
                            nome=utente.nome;
                            cognome= utente.cognome;
                            email=utente.email;
                            emailLocal=utente.email;
                            dataNascita= utente.dataNascita;
                            username=utente.username;

                            //stampa dati nelle editText
                            editNome.setText(nome);
                            editCognome.setText(cognome);
                            editEmail.setText(email);
                            editDataNascita.setText(dataNascita);
                            editUsername.setText(username);

                        }


                    }

                    if(registrato==false){
                        Toast.makeText(UpdateProfileActivity.this,"Utente non registrato",Toast.LENGTH_LONG).show();
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }

                }

            }
        });
    }

    public void openChangePasswordActivity(View view) {
    }

    public void buttonUpdateProfile(View view) {
        updateProfile(utente);
    }

    private void updateProfile(FirebaseUser utente){

        Boolean controllo=inputControl(editNome.getText().toString(),editCognome.getText().toString(),editDataNascita.getText().toString(),
                editUsername.getText().toString(),editEmail.getText().toString());

        if(controllo){
            return;
        }else{

            //recupero dati modificati
            nome=editNome.getText().toString();
            cognome=editCognome.getText().toString();
            email=editEmail.getText().toString();
            dataNascita=editDataNascita.getText().toString();
            username=editUsername.getText().toString();

            //salvataggio modifiche su SQLITE

            int update=updateUserOnDbLocal(nome,cognome,dataNascita,username,email,emailLocal);
            startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));

            updateUserOnDbRemote(nome,cognome,dataNascita,username,email);


            //salvataggio modifiche su Firebase

        }
    }

    public boolean inputControl(String Nome,String Cognome,String DataNascita,String Username,String Email){
        Boolean errorFlag = false;

        if(Nome.isEmpty()){
            editNome.setError(getString(R.string.campo_obbligatorio));
            editNome.requestFocus();
            errorFlag = true;
        }

        if(Cognome.isEmpty()){
            editCognome.setError(getString(R.string.campo_obbligatorio));
            editCognome.requestFocus();
            errorFlag = true;
        }

        if(DataNascita.isEmpty()){
            editDataNascita.setError(getString(R.string.campo_obbligatorio));
            editDataNascita.requestFocus();
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

    public int updateUserOnDbLocal(String Nome,String Cognome,String DataNascita,String Username,String Email,String EmailLocal){

        AlphaTourDbHelper dbAlpha = new AlphaTourDbHelper(this);
        //recupero idUtente
        SQLiteDatabase db = dbAlpha.getReadableDatabase();
        Cursor cursor=db.rawQuery(CommandDbAlphaTour.Command.SELECT_USER_PROFILE,new String[]{emailLocal});
        if(cursor.moveToFirst()){
            idUtenteLocal= cursor.getString(cursor.getColumnIndexOrThrow(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID));
        }

        //aggiornamento dati utente

        db=dbAlpha.getWritableDatabase();
        ContentValues valori = new ContentValues();

        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_NOME,Nome);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_COGNOME,Cognome);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_DATA_NASCITA,DataNascita);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_USERNAME,Username);
        valori.put(AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_EMAIL,Email);

        return db.update(AlphaTourContract.AlphaTourEntry.NOME_TABELLA_UTENTE,valori,AlphaTourContract.AlphaTourEntry.NOME_COLONNA_UTENTE_ID+
                CommandDbAlphaTour.Command.EGUAL+CommandDbAlphaTour.Command.VALUE,new String[] {idUtenteLocal});


    }

    public void updateUserOnDbRemote(String Nome,String Cognome,String DataNascita,String Username,String Email){

        progressBar.setVisibility(View.VISIBLE);
        Utente utenteUpdate=new Utente(Nome,Cognome,DataNascita,Username,Email);
        db.collection("Utenti").document(utente.getUid()).
                set(utenteUpdate).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateProfileActivity.this, "Account aggiornato correttamente", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateProfileActivity.this, ProfileActivity.class));
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Non è stato possibile aggiornare l'account", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}