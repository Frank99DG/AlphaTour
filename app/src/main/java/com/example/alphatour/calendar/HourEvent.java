package com.example.alphatour.calendar;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent {

    LocalTime time;
    ArrayList<EventProva> events;

    public HourEvent(LocalTime time, ArrayList<EventProva> events) {
        this.time = time;
        this.events = events;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ArrayList<EventProva> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventProva> events) {
        this.events = events;
    }
}
