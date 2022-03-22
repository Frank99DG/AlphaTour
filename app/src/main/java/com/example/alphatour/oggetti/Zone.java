package com.example.alphatour.oggetti;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Zone {

    public String name;
    public static Graph<Element,DefaultEdge> elementsZone = new SimpleGraph<>(DefaultEdge.class);

    public Zone(){

    }

    public Zone(String name){

        this.name = name;
    }

    public Zone(String name, Graph elementsZone){
        this.name = name;
        this.elementsZone = elementsZone;
    }


    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public static  Graph<Element, DefaultEdge> getElementsZone() {

        return elementsZone;
    }

    public void setElementsZone(Graph<Element, DefaultEdge> elementsZone) {
        this.elementsZone = elementsZone;

    }

    @Override
    public String toString() {
        return "* Zona:" +
                "nome='" + name + '\'' +
                ", elementiZona='" + elementsZone.toString() +
                '*';
    }
}
