package com.anaghizdavat.activity.model;

public class Pawn {

    private int resImage;
    private int resColor;

    public Pawn(int resImage, int resColor) {
        this.resImage = resImage;
        this.resColor = resColor;
    }

    public Pawn(Pawn pawn) {
        this.resImage = pawn.resImage;
        this.resColor = pawn.resColor;
    }

    public int getResImage() {
        return resImage;
    }

    public int getResColor() {
        return resColor;
    }
}
