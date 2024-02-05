package com.example.gymapp.entity;

public class Weight {
    int id;
    int userId;
    int execiseId;
    int lastWeight;
    int nbReps;
    String date;

    public Weight() {
    }

    public void setExeciseId(int execiseId) {
        this.execiseId = execiseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getExeciseId() {
        return execiseId;
    }

    public int getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(int lastWeight) {
        this.lastWeight = lastWeight;
    }

    public int getNbReps() {
        return nbReps;
    }

    public void setNbReps(int nbReps) {
        this.nbReps = nbReps;
    }
}
