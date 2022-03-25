package com.example.alphatour.oggetti;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;

public class Place {

    public String name,city,typology,idCurator;
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
        this.idCurator = idCurator;
    }

    public Place(String name, String city, String typology, String idCurator, Graph zonesPlace){
        this.name = name;
        this.city = city;
        this.typology = typology;
        this.idCurator = idCurator;
        this.zonesPlace = zonesPlace;
    }

    /*public static setZoneLuogo(Graph<Zona, DefaultEdge> zoneLuogo) {

        Zona zona1=new Zona("Stanza1",Zona.getElementiZona());
        Zona zona2=new Zona("Stanza2",Zona.getElementiZona());

        zoneLuogo.addVertex(zona2);
        zoneLuogo.addEdge(zona1,zona2);
    }*/

    public static void setZonesPlace(Graph<Zone, DefaultEdge> zonesPlace) {
        Place.zonesPlace = zonesPlace;
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
                ", zoneLuogo='" + zonesPlace.toString() +
                '*';
    }
}