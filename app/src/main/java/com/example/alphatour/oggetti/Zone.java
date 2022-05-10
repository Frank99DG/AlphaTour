package com.example.alphatour.oggetti;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Zone {

    private String name, idUser;
    //il grafo è non orientato (da A a B è uguale da B ad A)
    //private Graph<ElementString,DefaultEdge> elementsZone = new SimpleGraph<>(DefaultEdge.class);

    public Zone() {

    }

    public Zone(String name) {

        this.name = name;
    }


    public Zone(String name, String idUser) {

        this.name = name;
        this.idUser = idUser;

    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
