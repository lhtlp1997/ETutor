package com.jlu.etutor.gson;

/**
 * Created by 医我一生 on 2018/2/1.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class LoginResult extends BaseResult{
    /**
     * 返回的用户信息
     */
    private UserInfo userInfo;

    private TeacherInfo teaInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public TeacherInfo getTeaInfo() {
        return teaInfo;
    }

    public void setTeaInfo(TeacherInfo teaInfo) {
        this.teaInfo = teaInfo;
    }
}
