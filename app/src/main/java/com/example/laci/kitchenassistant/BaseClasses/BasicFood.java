package com.example.laci.kitchenassistant.BaseClasses;

public class BasicFood {
    private String name,picture;
    private int calorie,protein,carbohydrate,fat,sugar,saturated;

    public BasicFood(String name, int calorie, int protein, int carbohydrate, int fat) {
        this.name = name;
        this.calorie = calorie;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.sugar = 0;
        this.saturated = 0;
    }

    public BasicFood(String name, String picture, int calorie, int protein, int carbohydrate, int fat) {
        this.name = name;
        this.picture = picture;
        this.calorie = calorie;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.sugar = 0;
        this.saturated = 0;
    }

    public BasicFood() {
        calorie = -1;
        protein = -1;
        carbohydrate = -1;
        fat = -1;
        this.sugar = 0;
        this.saturated = 0;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getSaturated() {
        return saturated;
    }

    public void setSaturated(int saturated) {
        this.saturated = saturated;
    }

    public BasicFood(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
