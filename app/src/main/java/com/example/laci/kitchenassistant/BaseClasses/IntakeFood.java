package com.example.laci.kitchenassistant.BaseClasses;

public class IntakeFood extends BasicFoodQuantity {
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public IntakeFood(BasicFood food, int quantity, long time) {
        super(food, quantity);
        this.time = time;
    }

    public IntakeFood() {
    }
}
