package com.jlu.etutor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.jlu.etutor.gson.TeacherInfo;
import com.jlu.etutor.gson.UserInfo;
import com.hyphenate.easeui.EaseUI;
import com.vondear.rxtools.RxTool;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by 医我一生 on 2018/2/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class InitApplication extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static UserInfo userInfo;

    private static TeacherInfo teacherInfo;

    private static ArrayList<TeacherInfo> teaInfoList;

    private static HashMap<String,String> map;

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        context = this;
        userInfo=null;
        teaInfoList=new ArrayList<>();
        map=new HashMap<>();
        EaseUI.getInstance().init(this, null);
    }




    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        InitApplication.userInfo = userInfo;
    }

    public static TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public static void setTeacherInfo(TeacherInfo teacherInfo) {
        InitApplication.teacherInfo = teacherInfo;
    }

    public static ArrayList<TeacherInfo> getTeaInfoList() {
        return teaInfoList;
    }

    public static void setTeaInfoList(ArrayList<TeacherInfo> list) {
        teaInfoList.clear();
        teaInfoList.addAll(list);
        setMap();
    }

    public static Context getContext() {
        return context;
    }


    private static void setMap(){
        for(TeacherInfo info:teaInfoList){
            map.put(info.getPhone(),info.getName());
        }
    }

    public static String getName(String key){
        return map.get(key);
    }
}

