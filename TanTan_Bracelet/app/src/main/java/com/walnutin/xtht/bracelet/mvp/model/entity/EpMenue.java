package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/6/15.
 */

public class EpMenue {
    private int id;
    private String icon;
    private String name;

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "EpMenue{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name=" + name +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EpMenue() {


    }
    public EpMenue(int id, String icon,  String  name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }
}
