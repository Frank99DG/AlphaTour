package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.alphatour.R;
import com.example.alphatour.oggetti.Elemento;
import com.example.alphatour.oggetti.Luogo;
import com.example.alphatour.oggetti.Zona;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class DashBoardCuratore_prova extends AppCompatActivity {

    private EditText nomeLuogo, cittaLuogo, tipoLuogo, nomeZona, nomeZona2, titolo1, descrizione1, foto1, codiceQr1, attivita1, codiceSensore1,titolo2, descrizione2, foto2, codiceQr2, attivita2, codiceSensore2;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_curatore_prova);

        db = FirebaseFirestore.getInstance();

        nomeLuogo = findViewById(R.id.nomeLuogo);
        cittaLuogo = findViewById(R.id.cittaLuogo);
        tipoLuogo = findViewById(R.id.tipoLuogo);
        nomeZona = findViewById(R.id.nomeZona);
        nomeZona2 = findViewById(R.id.nomeZona2);
        titolo1 = findViewById(R.id.titolo1);
        descrizione1 = findViewById(R.id.descrizione1);
        foto1 = findViewById(R.id.foto1);
        codiceQr1 = findViewById(R.id.codiceQr1);
        attivita1 = findViewById(R.id.attivita1);
        codiceSensore1 = findViewById(R.id.codiceSensore1);
        titolo2 = findViewById(R.id.titolo2);
        descrizione2 = findViewById(R.id.descrizione2);
        foto2 = findViewById(R.id.foto2);
        codiceQr2 = findViewById(R.id.codiceQr2);
        attivita2 = findViewById(R.id.attivita2);
        codiceSensore2 = findViewById(R.id.codiceSensore2);

    }

    public void provaM(View v){

        String NomeLuogo = nomeLuogo.getText().toString();
        String CittaLuogo = cittaLuogo.getText().toString();
        String TipoLuogo = tipoLuogo.getText().toString();
        String NomeZona = nomeZona.getText().toString();
        String NomeZona2=nomeZona2.getText().toString();
        String Titolo1 = titolo1.getText().toString();
        String Descrizione1 = descrizione1.getText().toString();
        String Foto1 = foto1.getText().toString();
        String CodiceQr1 = codiceQr1.getText().toString();
        String Attivita1 = attivita1.getText().toString();
        String CodiceSensore1 = codiceSensore1.getText().toString();
        String Titolo2 = titolo2.getText().toString();
        String Descrizione2 = descrizione2.getText().toString();
        String Foto2 = foto2.getText().toString();
        String CodiceQr2 = codiceQr2.getText().toString();
        String Attivita2 = attivita2.getText().toString();
        String CodiceSensore2 = codiceSensore2.getText().toString();


        Elemento elemento1 = new Elemento(Titolo1,Descrizione1,Foto1,CodiceQr1,Attivita1,CodiceSensore1);
        Elemento elemento2 = new Elemento(Titolo2,Descrizione2,Foto2,CodiceQr2,Attivita2,CodiceSensore2);


        Graph<Elemento,DefaultEdge> elementiZona = new SimpleGraph<>(DefaultEdge.class);

        elementiZona.addVertex(elemento1);
        elementiZona.addVertex(elemento2);
        elementiZona.addEdge(elemento1,elemento2);

        Zona zona1 = new Zona(NomeZona,elementiZona);
        Zona zona2=new Zona(NomeZona2,elementiZona);

        Graph<Zona, DefaultEdge> zoneLuogo = new SimpleDirectedGraph<>(DefaultEdge.class);
        zoneLuogo.addVertex(zona1);
        zoneLuogo.addVertex(zona2);
        zoneLuogo.addEdge(zona1,zona2);

        Luogo luogo = new Luogo(NomeLuogo,CittaLuogo,TipoLuogo,zoneLuogo);


        Log.i("tagpez",luogo.toString());


    }
}