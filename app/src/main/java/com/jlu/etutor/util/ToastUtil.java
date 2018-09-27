package com.jlu.etutor.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 医我一生 on 2018/1/31.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class ToastUtil {
    /**
     * 用于显示的Toast对象
     */
    private static Toast toast;


    public ToastUtil(){
        throw new RuntimeException("util class can not be instance!");
    }

    /**
     *
     * @param context 用于显示时的上下文
     * @param message 要显示的内容
     */
    public static void showMessage(Context context, String message){
        if(toast==null)
            toast=Toast.makeText(context,message,Toast.LENGTH_LONG);
        else
            toast.setText(message);
        toast.show();
    }

}
