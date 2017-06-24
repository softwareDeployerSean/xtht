package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/6/19.
 */

public class BasicItemSupport {
    private int msgPush = -1;

    private int fanwanModel = -1;

    private int longSeat = -1;

    private int findDevice = -1;

    public BasicItemSupport() {}

    public BasicItemSupport(int msgPush, int fanwanModel, int longSeat, int findDevice) {
        this.msgPush = msgPush;
        this.fanwanModel = fanwanModel;
        this.longSeat = longSeat;
        this.findDevice = findDevice;
    }

    public int getMsgPush() {
        return msgPush;
    }

    public void setMsgPush(int msgPush) {
        this.msgPush = msgPush;
    }

    public int getLongSeat() {
        return longSeat;
    }

    public void setLongSeat(int longSeat) {
        this.longSeat = longSeat;
    }

    public int getFindDevice() {
        return findDevice;
    }

    public void setFindDevice(int findDevice) {
        this.findDevice = findDevice;
    }

    public int getFanwanModel() {
        return fanwanModel;
    }

    public void setFanwanModel(int fanwanModel) {
        this.fanwanModel = fanwanModel;
    }
}
