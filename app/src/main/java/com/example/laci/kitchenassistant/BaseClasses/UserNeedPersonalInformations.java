package com.example.laci.kitchenassistant.BaseClasses;

import com.example.laci.kitchenassistant.Tools.CalorieNeedCounter;
import com.example.laci.kitchenassistant.Tools.DateFunctions;

import java.util.ArrayList;
import java.util.Date;

public class UserNeedPersonalInformations {
    private static ArrayList<Recipe> breakfasts = new ArrayList<>();
    private static ArrayList<BasicFoodQuantity> snacks = new ArrayList<>();
    private static ArrayList<Recipe> lunches = new ArrayList<>();
    private static ArrayList<BasicFoodQuantity> afternoonSnacks = new ArrayList<>();
    private static ArrayList<Recipe> dinners = new ArrayList<>();
    private static int goalSteps;
    private static int goalCalorieBurnByTrainings;
    private static int averageCalorieBurn, averageCalorieIntake, minimumCalorieBurn, maximumCalorieBurn, minimumCalorieIntake, maximumCalorieIntake;
    private static int weeklyAverageConsumedCalories = 0;
    private static int dailyConsumedCalories = 0;


    public static void addToBreakfasts(Recipe recipe){breakfasts.add(recipe);}
    public static void addToSnacks(BasicFoodQuantity basicFoodQuantity){snacks.add(basicFoodQuantity);}
    public static void addToLunches(Recipe recipe){lunches.add(recipe);}
    public static void addToAfternoonSnacks(BasicFoodQuantity basicFoodQuantity){afternoonSnacks.add(basicFoodQuantity);}
    public static void addToDinners(Recipe recipe){dinners.add(recipe);}

    public static void calculateWeeklyAverageConsumedCalories(ArrayList<IntakeFood> foods){
        WeeklyPersonalData thisWeek = new WeeklyPersonalData(
                DateFunctions.getTheStartOfDate(new Date()),
                DateFunctions.getTheEndOfDate(new Date()),
                0,
                0,
                0
        );
        CalorieNeedCounter.setWeeklyConsumedCalories(thisWeek.getStart(),
                thisWeek.getEnd(),
                foods,
                thisWeek);
        weeklyAverageConsumedCalories = thisWeek.getAverageConsumedCalories();
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        Date tempDate = new Date();
        for(int i = 0; i < foods.size(); ++i){
            tempDate.setTime(foods.get(i).getTime());
            if(tempDate.getYear() == today.getYear() && tempDate.getMonth() == today.getMonth() && tempDate.getDay() == tempDate.getDay()){
                dailyConsumedCalories += foods.get(i).getCalorie();
            }
        }
    }

    public static int getWeeklyAverageConsumedCalories() {
        return weeklyAverageConsumedCalories;
    }

    public static void setWeeklyAverageConsumedCalories(int weeklyAverageConsumedCalories) {
        UserNeedPersonalInformations.weeklyAverageConsumedCalories = weeklyAverageConsumedCalories;
    }

    public static int getDailyConsumedCalories() {
        return dailyConsumedCalories;
    }

    public static void setDailyConsumedCalories(int dailyConsumedCalories) {
        UserNeedPersonalInformations.dailyConsumedCalories = dailyConsumedCalories;
    }

    public static ArrayList<BasicFoodQuantity> getSnacks() {
        return snacks;
    }

    public static void setSnacks(ArrayList<BasicFoodQuantity> snacks) {
        UserNeedPersonalInformations.snacks = snacks;
    }

    public static ArrayList<Recipe> getLunches() {
        return lunches;
    }

    public static void setLunches(ArrayList<Recipe> lunches) {
        UserNeedPersonalInformations.lunches = lunches;
    }

    public static ArrayList<BasicFoodQuantity> getAfternoonSnacks() {
        return afternoonSnacks;
    }

    public static void setAfternoonSnacks(ArrayList<BasicFoodQuantity> afternoonSnacks) {
        UserNeedPersonalInformations.afternoonSnacks = afternoonSnacks;
    }

    public static ArrayList<Recipe> getDinners() {
        return dinners;
    }

    public static void setDinners(ArrayList<Recipe> dinners) {
        UserNeedPersonalInformations.dinners = dinners;
    }

    public static int getAverageCalorieBurn() {
        return averageCalorieBurn;
    }

    public static void setAverageCalorieBurn(int averageCalorieBurn) {
        UserNeedPersonalInformations.averageCalorieBurn = averageCalorieBurn;
    }

    public static int getAverageCalorieIntake() {
        return averageCalorieIntake;
    }

    public static void setAverageCalorieIntake(int averageCalorieIntake) {
        UserNeedPersonalInformations.averageCalorieIntake = averageCalorieIntake;
    }

    public static int getMinimumCalorieBurn() {
        return minimumCalorieBurn;
    }

    public static void setMinimumCalorieBurn(int minimumCalorieBurn) {
        UserNeedPersonalInformations.minimumCalorieBurn = minimumCalorieBurn;
    }

    public static int getMaximumCalorieBurn() {
        return maximumCalorieBurn;
    }

    public static void setMaximumCalorieBurn(int maximumCalorieBurn) {
        UserNeedPersonalInformations.maximumCalorieBurn = maximumCalorieBurn;
    }

    public static int getMinimumCalorieIntake() {
        return minimumCalorieIntake;
    }

    public static void setMinimumCalorieIntake(int minimumCalorieIntake) {
        UserNeedPersonalInformations.minimumCalorieIntake = minimumCalorieIntake;
    }

    public static int getMaximumCalorieIntake() {
        return maximumCalorieIntake;
    }

    public static void setMaximumCalorieIntake(int maximumCalorieIntake) {
        UserNeedPersonalInformations.maximumCalorieIntake = maximumCalorieIntake;
    }

    public static ArrayList<Recipe> getBreakfasts() {
        return breakfasts;
    }

    public static void setBreakfasts(ArrayList<Recipe> breakfasts) {
        UserNeedPersonalInformations.breakfasts = breakfasts;
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
