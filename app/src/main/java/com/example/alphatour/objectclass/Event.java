package com.example.alphatour.objectclass;

public class Event {

    private String name;
    private String idUser;
    private String startingTime;
    private String date;
    private String hour;

    public Event() {
    }

    public Event(String name, String startingTime, String date, String hour) {
        this.name = name;
        this.startingTime = startingTime;
        this.date = date;
        this.hour = hour;
    }

    public Event(String name, String idUser, String startingTime, String date, String hour) {
        this.name = name;
        this.idUser = idUser;
        this.startingTime = startingTime;
        this.date = date;
        this.hour = hour;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}

