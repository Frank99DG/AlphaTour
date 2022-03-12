package com.example.alphatour.oggetti;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class Luogo {

    public String nome,citta,tipo;
    public static Graph<Zona,DefaultEdge> zoneLuogo = new SimpleDirectedGraph<>(DefaultEdge.class);


    public Luogo(){

    }

    public static void setZoneLuogo(Graph<Zona, DefaultEdge> zoneLuogo) {

        Zona zona1=new Zona("Stanza1",Zona.getElementiZona());
        Zona zona2=new Zona("Stanza2",Zona.getElementiZona());

        zoneLuogo.addVertex(zona2);
        zoneLuogo.addEdge(zona1,zona2);
    }

    public static Graph<Zona, DefaultEdge> getZoneLuogo() {
        return zoneLuogo;
    }

    public Luogo(String nome, String citta, String tipo, Graph zoneLuogo){
        this.nome = nome;
        this.citta = citta;
        this.tipo = tipo;
        this.zoneLuogo = zoneLuogo;
    }


}
