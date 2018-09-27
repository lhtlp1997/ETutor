package com.jlu.etutor.gson;

/**
 * Created by 程杰 on 2018/3/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class HuanXinTokenBean {

    //管理员Token
    private String access_token;

    //Token有效期
    private long expires_in;

    private String application;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
