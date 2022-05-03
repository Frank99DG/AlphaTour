package com.example.alphatour.oggetti;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class Place {

    private String name,city,typology,idUser;
    //il grafo è orientato (da A a B è diverso da B ad A)
    public static Graph<Zone,DefaultEdge> zonesPlace = new SimpleDirectedGraph<>(DefaultEdge.class);


    public Place(){

    }

    public Place(String name, String city, String typology){
        this.name = name;
        this.city = city;
        this.typology = typology;
    }

    public Place(String name, String city, String typology, String idCurator){
        this.name = name;
        this.city = city;
        this.typology = typology;
        this.idUser = idCurator;
    }

    public Place(String name, String city, String typology, String idCurator, Graph zonesPlace){
        this.name = name;
        this.city = city;
        this.typology = typology;
        this.idUser = idCurator;
        this.zonesPlace = zonesPlace;
    }



    public static void setZonesPlace(Graph<Zone, DefaultEdge> zonesPlace) {
        Place.zonesPlace = zonesPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTypology() {
        return typology;
    }

    public void setTypology(String typology) {
        this.typology = typology;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public static Graph<Zone, DefaultEdge> getZonesPlace() {
        return zonesPlace;
    }


    @Override
    public String toString() {
        return "* Luogo:" +
                "nome='" + name + '\'' +
                ", citta='" + city + '\'' +
                ", tipo='" + typology + '\'' +
                ", id curatore='" + idUser + '\'' +
                ", zone Luogo='" + zonesPlace.toString() +
                '*';
    }
}
