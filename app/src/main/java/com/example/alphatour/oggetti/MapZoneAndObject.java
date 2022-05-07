package com.example.alphatour.oggetti;

import java.util.ArrayList;
import java.util.List;

public class MapZoneAndObject {

    private String zone;
    private List<String> listObj=new ArrayList<String>();

    public MapZoneAndObject(String zone, List<String> listObj) {
        this.zone = zone;
        this.listObj = listObj;
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
