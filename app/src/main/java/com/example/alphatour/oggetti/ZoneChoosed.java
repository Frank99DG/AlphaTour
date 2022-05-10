package com.example.alphatour.oggetti;

import java.util.ArrayList;
import java.util.List;

public class ZoneChoosed {

    private String name;
    private List<String> objectChoosed=new ArrayList<String>();

    public ZoneChoosed() {
    }

    public ZoneChoosed(String name, List<String> objectChoosed) {
        this.name = name;
        this.objectChoosed = objectChoosed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getObjectChoosed() {
        return objectChoosed;
    }

    public void setObjectChoosed(String objectChoosed) {
        this.objectChoosed.add(objectChoosed);
    }
}
