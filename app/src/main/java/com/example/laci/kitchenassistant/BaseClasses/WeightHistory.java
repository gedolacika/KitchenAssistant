package com.example.laci.kitchenassistant.BaseClasses;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeightHistory {
    private Long weight,time;

    public WeightHistory(Long weight, Long time) {
        this.weight = weight;
        this.time = time;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getTimeMillis() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTime() {
        Date date = new Date(time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }




}
