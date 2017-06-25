package com.walnutin.xtht.bracelet.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by Leiht on 2017/6/25.
 */

public class Device implements Serializable {

    private String name;

    private String mac;

    public Device() {}

    public Device(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
