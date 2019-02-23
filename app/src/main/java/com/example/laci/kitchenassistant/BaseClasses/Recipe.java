package com.example.laci.kitchenassistant.BaseClasses;

import java.util.ArrayList;

public class Recipe extends BasicFoodQuantity {

    private ArrayList<BasicFoodQuantity> ingredients;
    //preparation - description of preparation
    //origin - the country where the recipe is from
    // recipeType - soup, main course, dessert, breakfast, lunch
    private String preparation,origin,recipeType,uploaderID;
    //difficulty
    //          0 - easy
    //          1 - intermediate
    //          2 - hard
    private int preparation_time, portion, difficulty;
    //true - it can see everyone
    //false - only the uploader can see
    private boolean visibility;

    private ArrayList<String> pictures;

    public Recipe() {
        preparation_time = -1;
        portion = -1;
        difficulty = -1;
        visibility = false;

        preparation = null;
        origin = null;
        recipeType = null;
        uploaderID = null;

        ingredients = new ArrayList<BasicFoodQuantity>();
        pictures = new ArrayList<String>();


    }

    public Recipe(String name, int calorie, int protein, int carbohydrate, int fat, ArrayList<BasicFoodQuantity> ingredients, String preparation, String origin, String recipeType, String uploaderID, int preparation_time, int portion, int difficulty, boolean visibility, ArrayList<String> pictures) {
        super(name, calorie, protein, carbohydrate, fat);
        this.ingredients = ingredients;
        this.preparation = preparation;
        this.origin = origin;
        this.recipeType = recipeType;
        this.uploaderID = uploaderID;
        this.preparation_time = preparation_time;
        this.portion = portion;
        this.difficulty = difficulty;
        this.visibility = visibility;
        this.pictures = pictures;
    }

    public Recipe(String name, int calorie, int protein, int carbohydrate, int fat) {
        super(name, calorie, protein, carbohydrate, fat);
    }

    public Recipe(String name, int calorie, int protein, int carbohydrate, int fat, ArrayList<BasicFoodQuantity> basicFoods, String preparation) {
        super(name, calorie, protein, carbohydrate, fat);
        this.ingredients = basicFoods;
        this.preparation = preparation;
    }

    public ArrayList<BasicFoodQuantity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<BasicFoodQuantity> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public void addBasicFood(BasicFoodQuantity basicFood){
        this.ingredients.add(basicFood);
    }

    public BasicFood getABasicFood(int i){
        return ingredients.get(i);
    }

    public int getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(int preparation_time) {
        this.preparation_time = preparation_time;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public void addPicture(String picture) {this.pictures.add(picture);}

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
