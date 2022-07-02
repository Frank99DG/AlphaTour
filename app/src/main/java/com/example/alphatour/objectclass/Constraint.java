package com.example.alphatour.objectclass;

public class Constraint {

    private String namePlace;
    private String fromZone,inZone;

    public Constraint(){

    }

    public Constraint(String namePlace,String fromZone, String inZone){
        this.namePlace = namePlace;
        this.fromZone =fromZone;
        this.inZone = inZone;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getFromZone() {
        return fromZone;
    }

    public void setFromZone(String fromZone) {
        this.fromZone = fromZone;
    }

    public String getInZone() {
        return inZone;
    }

    public void setInZone(String inZone) {
        this.inZone = inZone;
    }
}
