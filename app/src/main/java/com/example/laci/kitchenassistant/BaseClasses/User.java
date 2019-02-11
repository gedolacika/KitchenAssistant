package com.example.laci.kitchenassistant.BaseClasses;

import java.util.ArrayList;

public class User {
    private String name;
    private int age,height,weight,calorie_requirement;
    //lose weight = 0
    //keep weight = 1
    //gain weight = 2
    //gender male = 0
    //gender female = 1
    private int lose_keep_gain_weight,gender;

    public static int LOSE_WEIGHT = 0;
    public static int KEEP_WEIGHT = 1;
    public static int GAIN_WEIGHT = 2;

    public User(String name, int age, int height, int weight, int calorie_requirement, int lose_keep_gain_weight,int gender) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.calorie_requirement = calorie_requirement;
        this.lose_keep_gain_weight = lose_keep_gain_weight;
        this.gender = gender;
    }

    public User() {
        age = -1;
        height = -1;
        weight = -1;
        calorie_requirement = -1;
        lose_keep_gain_weight = 1;
        name = "";
        gender = 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", calorie_requirement=" + calorie_requirement +
                ", lose_keep_gain_weight=" + lose_keep_gain_weight +
                '}';
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

    public int getLose_keep_gain_weight() {
        return lose_keep_gain_weight;
    }

    public void setLose_keep_gain_weight(int lose_keep_gain_weight) {
        this.lose_keep_gain_weight = lose_keep_gain_weight;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
