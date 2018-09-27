package com.jlu.etutor.gson;

import java.util.ArrayList;

/**
 * Created by cj597 on 2018/2/17.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class TeaInfoListResult extends BaseResult {
    private ArrayList<TeacherInfo> result;

    public ArrayList<TeacherInfo> getResult() {
        return result;
    }

    public void setResult(ArrayList<TeacherInfo> result) {
        this.result = result;
    }
}
