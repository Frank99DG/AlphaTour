package com.example.alphatour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profile;
    private FloatingActionButton changeProfile;
    private TextView textWelcome,textNomeAndCognome,textEmail,textDataNascita,textUsername;
    private String nome,cognome,dataNascita,email,username;
    private ProgressBar progressBar;
    private FirebaseAuth authprofile;
    private FirebaseFirestore db;
    boolean registrato=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

       // getSupportActionBar().setTitle("Home");

        changeProfile=findViewById(R.id.changeProfile);
        profile=findViewById(R.id.profile_image);

        textWelcome=findViewById(R.id.showWelcome);
        textNomeAndCognome=findViewById(R.id.profileNameUser);
        textEmail=findViewById(R.id.profileEmailUser);
        textDataNascita=findViewById(R.id.profileDateBirthUser);
        textUsername=findViewById(R.id.profileUsernameUser);
        progressBar=findViewById(R.id.profileLoadingBar);

        authprofile = FirebaseAuth.getInstance();
        FirebaseUser utente=authprofile.getCurrentUser();
        db=FirebaseFirestore.getInstance();

        if(utente==null){
            Toast.makeText(ProfileActivity.this,"Si è verificato un errrore: i dati dell'utente non sono disponibili !!!",Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            visualizzaProfiloUtente(utente);
        }
    }

    private void visualizzaProfiloUtente(FirebaseUser utente) {

        String idUtente=utente.getUid();

        db.collection("Utenti").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> listaDocumenti=queryDocumentSnapshots.getDocuments(); //lista utenti registrati

                    for(DocumentSnapshot d: listaDocumenti){

                        if(d.getId().matches(idUtente)){ // controllo se l'id dell'utente esiste

                            //recupero dati
                            registrato=true;
                            User utente = d.toObject(User.class);
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
                            textUsername.setText(username);

                        }


                    }

                    if(registrato==false){
                        Toast.makeText(ProfileActivity.this,"Utente non registrato",Toast.LENGTH_LONG).show();
                    }else{
                        progressBar.setVisibility(View.GONE);
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
    }

    public void changeProfile(View v){

        ImagePicker.with(this)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)*/
                .start(20);
    }

    public void updateProfile1(View v){
        Intent intent=new Intent(this,UpdateProfileActivity.class);
        startActivity(intent);
    }
}