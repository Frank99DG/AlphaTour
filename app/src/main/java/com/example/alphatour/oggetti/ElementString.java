package com.example.alphatour.oggetti;

import android.graphics.Bitmap;
import android.net.Uri;

public class ElementString {

    private String title;
    private String description;
    private String activity;
    private String sensorCode;
    private String zone;
    private String photo;
    private String qrCode;
    private String idZone;
    private long IdPhotoAndQrCode;
    private String idUser;

    public ElementString(){

    }

    public ElementString(String title, String description, String photo, String qrCode, String activity, String sensorCode){

        this.title=title;
        this.description=description;
        this.photo=photo;
        this.qrCode=qrCode;
        this.activity=activity;
        this.sensorCode=sensorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public long getIdPhotoAndQrCode() {
        return IdPhotoAndQrCode;
    }

    public void setIdPhotoAndQrCode(long idPhotoAndQrCode) {
        IdPhotoAndQrCode = idPhotoAndQrCode;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
