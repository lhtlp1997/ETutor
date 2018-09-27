package com.jlu.etutor.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.jlu.etutor.R;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.swipecaptcha.RxSwipeCaptcha;

/**
 * Created by cj597 on 2018/2/21.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class CaptchaDialog extends RxDialog {

    private RxSwipeCaptcha mRxSwipeCaptcha;

    private SeekBar mSeekBar;

    public CaptchaDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.captcha_layout, null);
        mRxSwipeCaptcha=dialogView.findViewById(R.id.swipeCaptchaView);
        mSeekBar=dialogView.findViewById(R.id.dragBar);
        setContentView(dialogView);
        setCancelable(false);
    }

    public RxSwipeCaptcha getSwipeCaptcha(){
        return this.mRxSwipeCaptcha;
    }

    public SeekBar getSeekBar(){
        return this.mSeekBar;
    }
}
