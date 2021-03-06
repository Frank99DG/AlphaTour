package com.example.alphatour.objectclass;

import java.util.ArrayList;
import java.util.List;

public class PathString {
    private String name,description,idUser;
    private List<ZoneChoosed> zoneChoosedMap = new ArrayList<ZoneChoosed>();

    public List<ZoneChoosed> getZoneChoosedMap() {
        return zoneChoosedMap;
    }

    public void setZoneChoosedMap(List<ZoneChoosed> zoneChoosedMap) {
        this.zoneChoosedMap = zoneChoosedMap;
    }

    public PathString() {
    }

    public PathString(String name, String description,String idUser,List<ZoneChoosed> zoneChoosedMap) {
        this.name = name;
        this.description = description;
        this.idUser = idUser;
        this.zoneChoosedMap = zoneChoosedMap;
    }



    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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


}
