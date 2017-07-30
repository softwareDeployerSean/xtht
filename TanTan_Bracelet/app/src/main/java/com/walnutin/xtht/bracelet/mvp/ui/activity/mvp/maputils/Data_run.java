package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils;

import java.io.Serializable;

/**
 * Created by suns on 2017-07-30.
 */

public class Data_run implements Serializable {
    int cishu;
    String time;
    double distances;

    public int getCishu() {
        return cishu;
    }

    public void setCishu(int cishu) {
        this.cishu = cishu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDistances() {
        return distances;
    }

    public void setDistances(double distances) {
        this.distances = distances;
    }
}
