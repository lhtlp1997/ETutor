package com.jlu.etutor.gson;

/**
 * Created by 程杰 on 2018/3/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class EaseGsonRes {
    private String action;
    private long timestamp;
    private int duration;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
