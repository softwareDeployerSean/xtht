package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by suns on 2017-06-18.
 */

public class UserBean extends BaseJson {


    /**
     * userId : 9f9b3e0380614946adc5edda32de2161
     * token : b0ae0fd19dd644a3aa374d2429c8e810
     * phone : null
     * email : null
     * nickname : null
     * sex : null
     * birth : null
     * height : null
     * weight : null
     * weightOfUnit : null
     * dailyGoals : null
     * avatar : null
     * braceletMac : null
     */

    private String userId;
    private String token;
    private Object phone;
    private Object email;
    private Object nickname;
    private Object sex;
    private Object birth;
    private Object height;
    private Object weight;
    private Object weightOfUnit;
    private Object dailyGoals;
    private Object avatar;
    private Object braceletMac;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    public Object getBirth() {
        return birth;
    }

    public void setBirth(Object birth) {
        this.birth = birth;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getWeightOfUnit() {
        return weightOfUnit;
    }

    public void setWeightOfUnit(Object weightOfUnit) {
        this.weightOfUnit = weightOfUnit;
    }

    public Object getDailyGoals() {
        return dailyGoals;
    }

    public void setDailyGoals(Object dailyGoals) {
        this.dailyGoals = dailyGoals;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public Object getBraceletMac() {
        return braceletMac;
    }

    public void setBraceletMac(Object braceletMac) {
        this.braceletMac = braceletMac;
    }
}
