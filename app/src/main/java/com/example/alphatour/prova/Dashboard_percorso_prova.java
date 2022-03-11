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

public class Dashboard_percorso_prova extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_percorso_prova);
        provaM();
    }

    public void provaM(){

        Graph<Zona, DefaultEdge> zoneLuogo = new SimpleDirectedGraph<>(DefaultEdge.class);
        Graph<Elemento,DefaultEdge> elementiZona = new SimpleGraph<>(DefaultEdge.class);


        Luogo luogo =new Luogo("Museo arcivescovile","Manfredonia","Museo",zoneLuogo);
        Zona zona=new Zona("Stanza",elementiZona);
        zona.setElementiZona(elementiZona);
        luogo.setZoneLuogo(zoneLuogo);
        zoneLuogo=luogo.getZoneLuogo();
        Log.i("tagpez",zoneLuogo.toString());
    }
}