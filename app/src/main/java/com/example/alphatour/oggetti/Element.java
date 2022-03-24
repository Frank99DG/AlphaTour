package com.example.alphatour.oggetti;

import android.widget.ImageView;

import java.io.File;

public class Element {


    private String title,description,qrCode,activity,sensorCode;
    private Byte photo;

    public Element(){

    }


    public Element(String title, String description, String qrCode, String activity, String sensorCode){

        this.title=title;
        this.description=description;
        this.qrCode=qrCode;
        this.activity=activity;
        this.sensorCode=sensorCode;
    }


    public Element(String title, String description, Byte photo, String qrCode, String activity, String sensorCode){

        this.title=title;
        this.description=description;
        this.photo=photo;
        this.qrCode=qrCode;
        this.activity=activity;
        this.sensorCode=sensorCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(Byte photo) {
        this.photo = photo;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Byte getPhoto() {
        return photo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getActivity() {
        return activity;
    }

    public String getSensorCode() {
        return sensorCode;
    }


    @Override
    public String toString() {
        return "* Elemento:" +
                "titolo='" + title + '\'' +
                ", descrizione='" + description + '\'' +
                ", foto='" + photo + '\'' +
                ", codiceQr='" + qrCode + '\'' +
                ", attivita='" + activity + '\'' +
                ", codiceSensore='" + sensorCode +
                '*';
    }
}
