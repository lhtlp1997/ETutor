package com.jlu.etutor.adpter;

/**
 * Created by 程杰 on 2018/2/8.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class Model {
    public String name;
    private int iconRes;

    public Model(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getIconRes() {
        return iconRes;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", iconRes=" + iconRes +
                '}';
    }
}
