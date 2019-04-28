package com.example.laci.kitchenassistant.BaseClasses;

import java.util.ArrayList;

public class UserNeedPersonalInformations {
    private static ArrayList<BasicFoodQuantity> foodsToEat;
    private static int goalSteps;
    private static int goalCalorieBurnByTrainings;

    public static ArrayList<BasicFoodQuantity> getFoodsToEat() {
        return foodsToEat;
    }

    public static void setFoodsToEat(ArrayList<BasicFoodQuantity> foodsToEat) {
        UserNeedPersonalInformations.foodsToEat = foodsToEat;
    }

    public static int getGoalSteps() {
        return goalSteps;
    }

    public static void setGoalSteps(int goalSteps) {
        UserNeedPersonalInformations.goalSteps = goalSteps;
    }

    public static int getGoalCalorieBurnByTrainings() {
        return goalCalorieBurnByTrainings;
    }

    public static void setGoalCalorieBurnByTrainings(int goalCalorieBurnByTrainings) {
        UserNeedPersonalInformations.goalCalorieBurnByTrainings = goalCalorieBurnByTrainings;
    }
}
