package com.example.alphatour.oggetti;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Zone {

    private String name;
    //il grafo è non orientato (da A a B è uguale da B ad A)
    //public static Graph<Element,DefaultEdge> elementsZone = new SimpleGraph<>(DefaultEdge.class);

    public Zone(){

    }

    public Zone(String name){

        this.name = name;
    }

    public Zone(String name, Graph elementsZone){
        this.name = name;
        //this.elementsZone = elementsZone;
    }


    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    /*public static  Graph<Element, DefaultEdge> getElementsZone() {

        return elementsZone;
    }*/

    /*public void setElementsZone(Graph<Element, DefaultEdge> elementsZone) {
        this.elementsZone = elementsZone;

    }*/


   /* public String toString() {
        return "* Zona:" +
                "nome='" + name + '\'' +
                ", elementiZona='" + elementsZone.toString() +
                '*';
    }*/
}
