package com.jlu.etutor.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jlu.etutor.R;
import com.jlu.etutor.util.UpdateUITools;
import com.vondear.rxtools.view.dialog.RxDialog;

/**
 * Created by 程杰 on 2018/3/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class CodeInputDialog extends RxDialog {
    private EditText editText;

    private TextView time;

    private View ok, cancel;

    private Activity activity;

    private boolean enable;

    public CodeInputDialog(Activity context) {
        super(context);
        this.activity = context;
        initView();
    }

    private void initView() {
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(activity).inflate(R.layout.code_edit_layout, null);

        editText = dialogView.findViewById(R.id.editText);

        time = dialogView.findViewById(R.id.code_time);

        ok = dialogView.findViewById(R.id.ok);

        cancel = dialogView.findViewById(R.id.cancel);

        setCancelable(false);
        setContentView(dialogView);
        enable = true;

    }

    public String getCode() {
        return editText.getText().toString().trim();
    }

    public void setTime(String text) {
        time.setText(text);
    }

    public View getOk() {
        return ok;
    }

    public View getCancel() {
        return cancel;
    }

    public View getTime() {
        return time;
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean b) {
        this.enable = b;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                time.setEnabled(enable);
                if (enable)
                    time.setTextColor(Color.argb(255,255,105,180));
                else
                    time.setTextColor(Color.GRAY);
            }
        });

    }

}
