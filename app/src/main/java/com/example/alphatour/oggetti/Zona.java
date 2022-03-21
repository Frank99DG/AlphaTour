package com.example.alphatour.oggetti;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Zona {

    public String nome;
    public static Graph<Elemento,DefaultEdge> elementiZona = new SimpleGraph<>(DefaultEdge.class);

    public Zona(){

    }

    public Zona(String nome){
        this.nome = nome;
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
        this.elementiZona = elementiZona;

    }

    @Override
    public String toString() {
        return "* Zona:" +
                "nome='" + nome + '\'' +
                ", elementiZona='" + elementiZona.toString() +
                '*';
    }
}
