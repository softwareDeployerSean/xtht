package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/6/14.
 */

public class Epuipment {
    private String name;
    private int state;
    private String picName;


    public Epuipment() {
    }

    public Epuipment(String name, int state, String picName) {
        this.name = name;
        this.state = state;
        this.picName = picName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    @Override
    public String toString() {
        return "Epuipment{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", picName='" + picName + '\'' +
                '}';
    }
}
