package com.jlu.etutor.gson;

/**
 * Created by 程杰 on 2018/3/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class CodeResult {

    private Result result;

    private String reason;

    private int error_code;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

class Result{
    private int count;
    private int fee;
    private String sid;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
