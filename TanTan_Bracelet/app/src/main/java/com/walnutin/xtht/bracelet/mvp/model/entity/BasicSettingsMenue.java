package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/6/17.
 */

public class BasicSettingsMenue extends MarginMenue {
    private boolean isChangeTextColor;

    private boolean needCheckBox;

    public boolean isNeedCheckBox() {
        return needCheckBox;
    }

    public void setNeedCheckBox(boolean needCheckBox) {
        this.needCheckBox = needCheckBox;
    }

    private String color;
    private int colorValue;

    public boolean isChangeTextColor() {
        return isChangeTextColor;
    }

    public void setChangeTextColor(boolean changeTextColor) {
        isChangeTextColor = changeTextColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    public BasicSettingsMenue(int id, String name, String icon, boolean isSetMargin, int margin, boolean isChecked,
                              boolean isChangeTextColor, String color, int colorValue, boolean needCheckBox) {
        super(id, name, icon, isSetMargin, margin, isChecked);
        this.isChangeTextColor = isChangeTextColor;
        this.color = color;
        this.colorValue = colorValue;
        this.needCheckBox = needCheckBox;
    }

}
