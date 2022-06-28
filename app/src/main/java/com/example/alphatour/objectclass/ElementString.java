package com.example.alphatour.objectclass;

public class ElementString {

    private String title,description,zone,photo,qrCode,idZone,idUser,qrData;
    private long IdPhotoAndQrCode;


    public ElementString(){

    }

    public ElementString(String title, String description, String photo, String qrCode){

        this.title=title;
        this.description=description;
        this.photo=photo;
        this.qrCode=qrCode;
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

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    @Override
    public String toString() {
        return "ElementString{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", zone='" + zone + '\'' +
                ", photo='" + photo + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", idZone='" + idZone + '\'' +
                ", idUser='" + idUser + '\'' +
                ", qrData='" + qrData + '\'' +
                ", IdPhotoAndQrCode=" + IdPhotoAndQrCode +
                '}';
    }
}
