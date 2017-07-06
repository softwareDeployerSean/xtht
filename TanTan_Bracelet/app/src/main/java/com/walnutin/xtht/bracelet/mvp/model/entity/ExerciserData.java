package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/7/5.
 */

public class ExerciserData {

    //yyyy-MM-dd HH:mm
    private String date;

    private boolean isDisplayMonthTitle = false;

    public ExerciserData(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDisplayMonthTitle() {
        return isDisplayMonthTitle;
    }

    public void setDisplayMonthTitle(boolean displayMonthTitle) {
        isDisplayMonthTitle = displayMonthTitle;
    }

    @Override
    public String toString() {
        return "ExerciserData{" +
                "date='" + date + '\'' +
                ", isDisplayMonthTitle=" + isDisplayMonthTitle +
                '}';
    }
}
