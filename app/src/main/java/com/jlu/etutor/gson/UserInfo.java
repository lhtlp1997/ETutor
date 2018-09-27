package com.jlu.etutor.gson;

import java.io.Serializable;

/**
 * Created by 医我一生 on 2018/2/1.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class UserInfo implements Serializable{
    /**
     * 电话号码
     */
    private String name;

    /**
     * 姓名
     */
    private String phone;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 注册时间
     */
    private String time;

    /**
     * 用户类型
     */
    private int type;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 标签
     */
    private String tag;



    public UserInfo(){

    }

    public UserInfo(String name, String phone, String pwd, String time, int type) {
        this.name = name;
        this.phone = phone;
        this.pwd = pwd;
        this.time = time;
        this.type = type;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
