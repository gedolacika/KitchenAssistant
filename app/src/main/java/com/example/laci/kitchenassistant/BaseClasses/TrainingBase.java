package com.example.laci.kitchenassistant.BaseClasses;

public class TrainingBase {
    private String name;
    private int burnCalorie;

    public TrainingBase(TrainingBase trainingBase){
        this.name = trainingBase.name;
        this.burnCalorie = trainingBase.burnCalorie;
    }

    public TrainingBase(String name, int burnCalorie) {
        this.name = name;
        this.burnCalorie = burnCalorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBurnCalorie() {
        return burnCalorie;
    }

    public void setBurnCalorie(int burnCalorie) {
        this.burnCalorie = burnCalorie;
    }
}
