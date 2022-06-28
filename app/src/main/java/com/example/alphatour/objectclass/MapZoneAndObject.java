package com.example.alphatour.objectclass;

import java.util.ArrayList;
import java.util.List;

public class MapZoneAndObject {

    private String zone,name,description;
    private List<String> listObj=new ArrayList<String>();

    public MapZoneAndObject() {

    }

    public MapZoneAndObject(String zone, List<String> listObj, String name,String description) {
        this.zone = zone;
        this.listObj = listObj;
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<String> getListObj() {
        return listObj;
    }

    public void setListObj(List<String> listObj) {
        this.listObj = listObj;
    }
}
