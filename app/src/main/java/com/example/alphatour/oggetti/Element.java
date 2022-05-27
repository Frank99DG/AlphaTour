package com.example.alphatour.oggetti;

import android.graphics.Bitmap;
import android.net.Uri;

public class Element{

    private String title,description,zone,idZone,idUser,qrData;
    private long idPhotoAndQrCode;
    private String idPhotoAndQrCodeString;
    private  Uri photo;
    private  Bitmap qrCode;
    private int idZon;
    private Boolean saved=false;

    public Element(){

    }


    public Element(String idZone,String title, String description){

        this.idZone=idZone;
        this.title=title;
        this.description=description;
    }


    public Element(String title, String description, Uri photo, Bitmap qrCode){

        this.title=title;
        this.description=description;
        this.photo=photo;
        this.qrCode=qrCode;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public int getIdZon() {
        return idZon;
    }

    public void setIdZon(int idZon) {
        this.idZon = idZon;
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

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public String getIdPhotoAndQrCodeString() {
        return idPhotoAndQrCodeString;
    }

    public void setIdPhotoAndQrCodeString(String idPhotoAndQrCodeString) {
        this.idPhotoAndQrCodeString = idPhotoAndQrCodeString;
    }

    @Override
    public String toString() {
        return "* Elemento:" +
                "titolo='" + title + '\'' +
                ", descrizione='" + description + '\'' +
                ", foto='" + photo + '\'' +
                ", codiceQr='" + qrCode + '\'' +
                '*';
    }
}
