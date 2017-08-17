package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import java.io.Serializable;

/**
 * Created by Leiht on 2017/8/17.
 */

public class RateSP implements Serializable {

    private String rate;

    private int count;

    private String date;//yyyy-MM-dd HH:mm

    public RateSP() {

    }

    public RateSP(String rate, int count, String date) {
        this.rate = rate;
        this.count = count;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
