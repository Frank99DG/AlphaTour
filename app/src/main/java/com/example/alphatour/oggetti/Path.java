package com.example.alphatour.oggetti;

import java.util.ArrayList;
import java.util.List;

public class Path {

   private String name,description;
   private List<ZoneChoosed> zonePath=new ArrayList<ZoneChoosed>();

    public Path() {

    }

    public Path(String name, String description, List<ZoneChoosed> zonePath) {
        this.name = name;
        this.description = description;
        this.zonePath = zonePath;
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

    public List<ZoneChoosed> getZonePath() {
        return zonePath;
    }

    public void setZonePath(ZoneChoosed zonePath) {
        this.zonePath.add(zonePath);
    }

}
