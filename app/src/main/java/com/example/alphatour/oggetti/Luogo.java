package com.example.alphatour.oggetti;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class Luogo {

    public String nome,citta,tipologia;
    public static Graph<Zona,DefaultEdge> zoneLuogo = new SimpleDirectedGraph<>(DefaultEdge.class);


    public Luogo(){

    }

    public Luogo(String nome, String citta, String tipologia){
        this.nome = nome;
        this.citta = citta;
        this.tipologia = tipologia;
    }

    public Luogo(String nome, String citta, String tipologia, Graph zoneLuogo){
        this.nome = nome;
        this.citta = citta;
        this.tipologia = tipologia;
        this.zoneLuogo = zoneLuogo;
    }

    /*public static setZoneLuogo(Graph<Zona, DefaultEdge> zoneLuogo) {

        Zona zona1=new Zona("Stanza1",Zona.getElementiZona());
        Zona zona2=new Zona("Stanza2",Zona.getElementiZona());

        zoneLuogo.addVertex(zona2);
        zoneLuogo.addEdge(zona1,zona2);
    }*/

    public static void setZoneLuogo(Graph<Zona, DefaultEdge> zoneLuogo) {
        Luogo.zoneLuogo = zoneLuogo;
    }

    public static Graph<Zona, DefaultEdge> getZoneLuogo() {
        return zoneLuogo;
    }


    @Override
    public String toString() {
        return "* Luogo:" +
                "nome='" + nome + '\'' +
                ", citta='" + citta + '\'' +
                ", tipo='" + tipologia + '\'' +
                ", zoneLuogo='" + zoneLuogo.toString() +
                '*';
    }
}
