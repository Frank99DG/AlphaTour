package com.example.alphatour.oggetti;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Zone {

    private String name;
    private String idPlace;
    private String idUser;
    private String namePlace;
    private int idPl;
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

    public Zone(String name,String idPlace,String namePlace,String idUser) {
        this.name = name;
        this.idPlace = idPlace;
        this.idUser = idUser;
        this.namePlace = namePlace;

    }


    public int getIdPl() {
        return idPl;
    }

    public void setIdPl(int idPl) {
        this.idPl = idPl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "name='" + name + '\'' +
                ", idUser='" + idUser + '\'' +
                '}';
    }
}
