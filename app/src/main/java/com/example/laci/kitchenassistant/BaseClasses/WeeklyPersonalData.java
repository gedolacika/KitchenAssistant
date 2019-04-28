package com.example.laci.kitchenassistant.BaseClasses;

import java.util.Date;

public class WeeklyPersonalData {
    private static final int minimumNumbersOfRealDataPerWeek = 2;
    private Date start, end;
    private int averageBurnedCalories, averageConsumedCalories, weightCHangeFromTheLastWeek;
    private boolean isRealData, weightFound;

    public WeeklyPersonalData(Date start, Date end, int averageBurnedCalories, int averageConsumedCalories, int weightCHangeFromTheLastWeek) {
        this.start = start;
        this.end = end;
        this.averageBurnedCalories = averageBurnedCalories;
        this.averageConsumedCalories = averageConsumedCalories;
        this.weightCHangeFromTheLastWeek = weightCHangeFromTheLastWeek;
        this.isRealData = true;
        this.weightFound = false;
    }

    public int getWeightCHangeFromTheLastWeek() {
        return weightCHangeFromTheLastWeek;
    }

    public void setWeightCHangeFromTheLastWeek(int weightCHangeFromTheLastWeek) {
        this.weightCHangeFromTheLastWeek = weightCHangeFromTheLastWeek;
    }

    public boolean isWeightFound() {
        return weightFound;
    }

    public void setWeightFound(boolean weightFound) {
        this.weightFound = weightFound;
    }

    public static int getMinimumNumbersOfRealDataPerWeek() {
        return minimumNumbersOfRealDataPerWeek;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getAverageBurnedCalories() {
        return averageBurnedCalories;
    }

    public void setAverageBurnedCalories(int averageBurnedCalories) {
        this.averageBurnedCalories = averageBurnedCalories;
    }

    public int getAverageConsumedCalories() {
        return averageConsumedCalories;
    }

    public void setAverageConsumedCalories(int averageConsumedCalories) {
        this.averageConsumedCalories = averageConsumedCalories;
    }

    public boolean isRealData() {
        return isRealData;
    }

    public void setRealData(boolean realData) {
        isRealData = realData;
    }
}
