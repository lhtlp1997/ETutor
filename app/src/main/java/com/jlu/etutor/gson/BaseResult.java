package com.jlu.etutor.gson;

/**
 * Created by 医我一生 on 2018/2/1.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class BaseResult {
    /**
     * 描述信息
     */
    private String status;

    /**
     * 返回码
     */
    private int code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
