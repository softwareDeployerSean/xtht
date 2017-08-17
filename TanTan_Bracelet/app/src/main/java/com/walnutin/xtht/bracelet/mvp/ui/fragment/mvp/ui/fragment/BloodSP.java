package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import java.io.Serializable;

/**
 * Created by Leiht on 2017/8/17.
 */

public class BloodSP implements Serializable {

    private String diastolicStr;

    private String systolicStr;

    private String date;

    public BloodSP() {}

    public BloodSP(String diastolicStr, String systolicStr, String date) {
        this.diastolicStr = diastolicStr;
        this.systolicStr = systolicStr;
        this.date = date;
    }

    public String getDiastolicStr() {
        return diastolicStr;
    }

    public void setDiastolicStr(String diastolicStr) {
        this.diastolicStr = diastolicStr;
    }

    public String getSystolicStr() {
        return systolicStr;
    }

    public void setSystolicStr(String systolicStr) {
        this.systolicStr = systolicStr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
