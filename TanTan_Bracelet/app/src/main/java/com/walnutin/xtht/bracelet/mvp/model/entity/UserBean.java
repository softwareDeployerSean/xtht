package com.walnutin.xtht.bracelet.mvp.model.entity;

/**
 * Created by suns on 2017-06-18.
 */

public class UserBean extends BaseJson {


    /**
     * avatar : string
     * birth : string
     * braceletMac : string
     * dailyGoals : 0
     * email : string
     * height : string
     * heightOfUnit : string
     * nickname : string
     * phone : string
     * sex : male
     * token : string
     * userId : string
     * weight : string
     * weightOfUnit : lb
     */

    private String avatar;
    private String birth;
    private String braceletMac;
    private int dailyGoals;
    private String email;
    private String height;
    private String heightOfUnit;
    private String nickname;
    private String phone;
    private String sex;
    private String token;
    private String userId;
    private String weight;
    private String weightOfUnit;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBraceletMac() {
        return braceletMac;
    }

    public void setBraceletMac(String braceletMac) {
        this.braceletMac = braceletMac;
    }

    public int getDailyGoals() {
        return dailyGoals;
    }

    public void setDailyGoals(int dailyGoals) {
        this.dailyGoals = dailyGoals;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeightOfUnit() {
        return heightOfUnit;
    }

    public void setHeightOfUnit(String heightOfUnit) {
        this.heightOfUnit = heightOfUnit;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightOfUnit() {
        return weightOfUnit;
    }

    public void setWeightOfUnit(String weightOfUnit) {
        this.weightOfUnit = weightOfUnit;
    }
}
