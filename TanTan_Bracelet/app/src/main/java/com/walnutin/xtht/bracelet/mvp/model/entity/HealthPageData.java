package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/7/2.
 */

public class HealthPageData {

    /**
     * 1:心率
     * 2:血压
     * 1:血氧
     * 1:低运动量
     * 1:散步
     * 1:跑步
     * 1:睡眠
     * 1:摘下
     */
    private int type;

    private String time;

    private String leftTopName;

    private String leftButtom;

    private String rightTop;

    private String rightButtom;

    private String rightText;

    private boolean isRightIcon;

    private long sortTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public HealthPageData() {
    }

    public HealthPageData(int type, String time, String leftTopName, String leftButtom, String rightTop, String rightButtom, String rightText, boolean isRightIcon, long sortTime, String date) {
        this.type = type;
        this.time = time;
        this.leftTopName = leftTopName;
        this.leftButtom = leftButtom;
        this.rightTop = rightTop;
        this.rightButtom = rightButtom;
        this.rightText = rightText;
        this.isRightIcon = isRightIcon;
        this.sortTime = sortTime;
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLeftTopName() {
        return leftTopName;
    }

    public void setLeftTopName(String leftTopName) {
        this.leftTopName = leftTopName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeftButtom() {
        return leftButtom;
    }

    public void setLeftButtom(String leftButtom) {
        this.leftButtom = leftButtom;
    }

    public String getRightTop() {
        return rightTop;
    }

    public void setRightTop(String rightTop) {
        this.rightTop = rightTop;
    }

    public String getRightButtom() {
        return rightButtom;
    }

    public void setRightButtom(String rightButtom) {
        this.rightButtom = rightButtom;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public boolean isRightIcon() {
        return isRightIcon;
    }

    public void setRightIcon(boolean rightIcon) {
        isRightIcon = rightIcon;
    }

    public long getSortTime() {
        return sortTime;
    }

    public void setSortTime(long sortTime) {
        this.sortTime = sortTime;
    }
}
