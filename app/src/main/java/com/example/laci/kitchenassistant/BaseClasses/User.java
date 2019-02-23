package com.example.laci.kitchenassistant.BaseClasses;

import java.util.ArrayList;

public class User {
    private String name;
    private int age,height,weight,calorie_requirement,goal_weight;
    //lose weight = 0
    //keep weight = 1
    //gain weight = 2
    //gender male = 0
    //gender female = 1
    private int gender;
    private ArrayList<WeightHistory> weightHistories;

    public static int LOSE_WEIGHT = 0;
    public static int KEEP_WEIGHT = 1;
    public static int GAIN_WEIGHT = 2;

    public User(String name, int age, int height, int weight, int calorie_requirement,int gender) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.calorie_requirement = calorie_requirement;
        this.gender = gender;
    }

    public User() {
        age = -1;
        height = -1;
        weight = -1;
        calorie_requirement = -1;
        name = "";
        gender = 0;
        goal_weight = -1;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", calorie_requirement=" + calorie_requirement  +
                '}';
    }

    public int getGoal_weight() {
        return goal_weight;
    }

    public void setGoal_weight(int goal_weight) {
        this.goal_weight = goal_weight;
    }

    public ArrayList<WeightHistory> getWeightHistories() {
        return weightHistories;
    }

    public void setWeightHistories(ArrayList<WeightHistory> weightHistories) {
        this.weightHistories = weightHistories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCalorie_requirement() {
        return calorie_requirement;
    }

    public void setCalorie_requirement(int calorie_requirement) {
        this.calorie_requirement = calorie_requirement;
    }



    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
