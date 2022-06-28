package com.example.alphatour.objectclass;

public class Constraint {

    private String fromZone,inZone;

    public Constraint(){

    }

    public Constraint(String fromZone, String inZone){

        this.fromZone=fromZone;
        this.inZone=inZone;
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
