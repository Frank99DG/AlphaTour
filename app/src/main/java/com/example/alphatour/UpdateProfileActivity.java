package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    private String nome,cognome,dataNascita,email,uername;
    private ProgressBar progressBar;
    private FirebaseAuth authprofile;
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
        FirebaseUser utente=authprofile.getCurrentUser();
        db=FirebaseFirestore.getInstance();
        
        visualizzaProfilo(utente);


    }

    //visualizza i dati del profilo utente
    private void visualizzaProfilo(FirebaseUser utente) {

        String idUtente=utente.getUid();

        db.collection("Utenti").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listaDocumenti=queryDocumentSnapshots.getDocuments(); //lista utenti registrati

                    for(DocumentSnapshot d: listaDocumenti){

                        if(d.getId().matches(idUtente)){ // controllo se l'id dell'utente esiste

                            //recupero dati
                           /* registrato=true;
                            Utente utente = d.toObject(Utente.class);
                            nome=utente.nome;
                            cognome= utente.cognome;
                            email=utente.email;
                            dataNascita= utente.dataNascita;
                            username=utente.username;

                            //stampa dati nelle editText
                            textWelcome.setText("Benvenuto, "+nome+" "+cognome);
                            textNomeAndCognome.setText(nome+" "+cognome);
                            textEmail.setText(email);
                            textDataNascita.setText(dataNascita);
                            textUsername.setText(username);*/

                        }


                    }

                    /*if(registrato==false){
                        Toast.makeText(ProfileActivity.this,"Utente non registrato",Toast.LENGTH_LONG).show();
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }*/

                }

            }
        });
    }

    public void openChangePasswordActivity(View view) {
    }

    public void updateProfile(View view) {
    }
}