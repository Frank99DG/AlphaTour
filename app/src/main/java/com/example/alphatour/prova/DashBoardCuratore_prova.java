package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.alphatour.R;
import com.example.alphatour.oggetti.Elemento;
import com.example.alphatour.oggetti.Luogo;
import com.example.alphatour.oggetti.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class DashBoardCuratore_prova extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_curatore_prova);
        provaM();
    }

    public void provaM(){


        Elemento statua =new Elemento("Statua","Questa è una bella statua","foto","codiceQr","attività","codiceSensore");
        Elemento quadro =new Elemento("Quadro","Questa è un bel quadro","foto","codiceQr","attività","codiceSensore");


        Graph<Elemento,DefaultEdge> elementiZona = new SimpleGraph<>(DefaultEdge.class);

        elementiZona.addVertex(statua);
        elementiZona.addVertex(quadro);
        elementiZona.addEdge(statua,quadro);

        Zona zona1=new Zona("Stanza",elementiZona);

        Graph<Zona, DefaultEdge> zoneLuogo = new SimpleDirectedGraph<>(DefaultEdge.class);
        zoneLuogo.addVertex(zona1);

        Luogo luogo =new Luogo("Museo arcivescovile","Manfredonia","Museo",zoneLuogo);


        Log.i("tagpez",zoneLuogo.toString());


    }
}