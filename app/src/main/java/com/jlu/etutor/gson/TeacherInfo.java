package com.jlu.etutor.gson;

import java.io.Serializable;

/**
 * Created by 程杰 on 2018/2/8.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class TeacherInfo implements Serializable{
    /**
     * 男
     */
    public static final int MALE=1;

    /**
     * 女
     */
    public static final int FEMALE=-1;

    private UserInfo userInfo;



    /**
     * 电话号码
     */
    private String phone;
    /**
     * 昵称
     */

    private String name;
    /**
     * 所在大学
     */
    private String college;

    /**
     * 所处专业
     */
    private String major;

    /**
     * 总体评价
     */
    private int score;

    /**
     * 个人标签
     */
    private String tag;
    /**
     * 预期薪水
     */
    private String salary;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 入学时间
     */
    private String time;

    /**
     * 性别
     */
    private int sex;

    /**
     * 个人简介
     */
    private String introduction;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public TeacherInfo(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
