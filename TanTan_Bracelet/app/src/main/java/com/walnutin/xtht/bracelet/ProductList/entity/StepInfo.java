package com.walnutin.xtht.bracelet.ProductList.entity;

/**
 * Created by chenliu on
 */

public class StepInfo {
    public String date;
    public int step;
    public int calories;
    public float distance;
    public int sportTime;

    public StepInfo() {
    }

    public StepInfo(String var1, int var2, int var3, float var4, int var5) {
        this.setDate(var1);
        this.setStep(var2);
        this.setCalories(var3);
        this.setDistance(var4);
        this.setSportTime(var5);
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String var1) {
        this.date = var1;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int var1) {
        this.step = var1;
    }

    public int getCalories() {
        return this.calories;
    }

    public void setCalories(int var1) {
        this.calories = var1;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float var1) {
        this.distance = var1;
    }

    public int getSportTime() {
        return this.sportTime;
    }

    public void setSportTime(int var1) {
        this.sportTime = var1;
    }
}
