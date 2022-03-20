package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alphatour.oggetti.Utente;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editNomeAndCognome,editEmail,editDataNascita,editUsername;
    private String nome,cognome,dataNascita,email,username;
    private ProgressBar progressBar;
    boolean registrato=false;
    private FirebaseAuth authprofile;
    FirebaseUser utente;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        editNomeAndCognome=findViewById(R.id.updateFullName);
        editEmail=findViewById(R.id.updateEmail);
        editDataNascita=findViewById(R.id.updateDataNascita);
        editUsername=findViewById(R.id.updateUsername);
        progressBar=findViewById(R.id.profileUpdateBarraCaricamento);

        authprofile = FirebaseAuth.getInstance();
        utente=authprofile.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        
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
                            dataNascita= utente.dataNascita;
                            username=utente.username;

                            //stampa dati nelle editText
                            editNomeAndCognome.setText(nome+" "+cognome);
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

        Boolean controllo=inputControl(editNomeAndCognome.getText().toString(),editDataNascita.getText().toString(),
                editUsername.getText().toString(),editEmail.getText().toString());

        if(controllo){
            return;
        }else{

        }
    }

    public boolean inputControl(String Nomeandcognome,String DataNascita,String Username,String Email){
        Boolean errorFlag = false;

        if(Nomeandcognome.isEmpty()){
            editNomeAndCognome.setError(getString(R.string.campo_obbligatorio));
            editNomeAndCognome.requestFocus();
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
}