package com.example.laci.kitchenassistant.BaseClasses;

public class Training extends TrainingBase {
    private int duration;
    private long timeTo;


    public Training(TrainingBase trainingBase, int duration, long timeTo) {
        super(trainingBase);
        this.duration = duration;
        this.timeTo = timeTo;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }
}
