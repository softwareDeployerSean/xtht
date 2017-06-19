package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by Leiht on 2017/6/17.
 */

public class MarginMenue extends Menue {
    private boolean isSetMargin;
    private int margin;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public MarginMenue() {

    }

    public MarginMenue(int id, String name, String icon, boolean isSetMargin, int margin, boolean isChecked) {
        super(id, name, icon);
        this.isSetMargin = isSetMargin;
        this.margin = margin;
        this.isChecked = isChecked;
    }

    public boolean isSetMargin() {
        return isSetMargin;
    }

    public void setSetMargin(boolean setMargin) {
        isSetMargin = setMargin;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
