package com.example.laci.kitchenassistant.BaseClasses;

public class StepCount {
    private long time;
    private int steps;

    public StepCount(long time, int steps) {
        this.time = time;
        this.steps = steps;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void addStep(int steps){
        this.steps += steps;
    }
}
