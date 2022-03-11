package com.example.alphatour.oggetti;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Zona {
    public String nome;
    public static Graph<Elemento,DefaultEdge> elementiZona = new SimpleGraph<>(DefaultEdge.class);

    public Zona(){

    }

    public Zona(String nome, Graph elementiZona){
        this.nome = nome;
        this.elementiZona = elementiZona;


    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static  Graph<Elemento, DefaultEdge> getElementiZona() {
        return elementiZona;
    }

    public void setElementiZona(Graph<Elemento, DefaultEdge> elementiZona) {
        Elemento statua =new Elemento("Statua","Questa è una bella statua","foto","codiceQr","attività","codiceSensore");
        Elemento quadro =new Elemento("Quadro","Questa è un bel quadro","foto","codiceQr","attività","codiceSensore");
        elementiZona.addVertex(statua);
        elementiZona.addVertex(quadro);
        elementiZona.addEdge(statua,quadro);
    }
}
