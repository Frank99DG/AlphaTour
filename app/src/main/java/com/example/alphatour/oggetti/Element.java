package com.example.alphatour.oggetti;

import android.graphics.Bitmap;
import android.net.Uri;

public class Element{


    private String title;
    private String description;
    private String activity;
    private String sensorCode;
    private String zone;
    private long idPhotoAndQrCode;
    private  Uri photo;
    private  Bitmap qrCode;
    private String idZone;
    private String idUser;

    public Element(){

    }


    public Element(String idZone,String title, String description, String activity, String sensorCode){

        this.idZone=idZone;
        this.title=title;
        this.description=description;
        this.activity=activity;
        this.sensorCode=sensorCode;
    }


    public Element(String title, String description, Uri photo, Bitmap qrCode, String activity, String sensorCode){

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

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public void setQrCode(Bitmap qrCode) {
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

    public Uri getPhoto() {
        return photo;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }

    public String getActivity() {
        return activity;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public String getZoneRif() {
        return zone;
    }

    public void setZoneRif(String zone) {
        this.zone = zone;
    }

    public long getIdPhotoAndQrCode() {
        return idPhotoAndQrCode;
    }

    public void setIdPhotoAndQrCode(long idPhotoAndQrCode) {
        this.idPhotoAndQrCode = idPhotoAndQrCode;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
