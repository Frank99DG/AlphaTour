package com.example.alphatour.oggetti;

public class TourItem {

    private int tourImage;
    private String tourText;


    public TourItem(int tourImage, String tourText) {
        this.tourImage = tourImage;
        this.tourText = tourText;
    }

    public int getTourImage() {
        return tourImage;
    }

    public String getTourText() {
        return tourText;
    }

}

