package com.anaghizdavat.activity.model;

public class Round {

    private int number;
    private int points;

    public Round(int number, int points) {
        this.number = number;
        this.points = points;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
