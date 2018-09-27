package com.jlu.etutor.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jlu.etutor.R;
import com.jlu.etutor.dialog.CaptchaDialog;
import com.jlu.etutor.dialog.CodeInputDialog;
import com.jlu.etutor.util.JUHECode;
import com.jlu.etutor.util.Server;
import com.jlu.etutor.util.ToastUtil;
import com.jlu.etutor.util.UpdateUITools;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.swipecaptcha.RxSwipeCaptcha;

public class RegistFirstStepActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler handler;
    private String code;
    private String v;
    private int keyHeight = 0; //软件盘弹起后所占高度
    private LinearLayout mContent;
    private RelativeLayout service;
    private EditText phone;
    private Activity activity;
    private CodeInputDialog codeInputDialog;
    private RxDialogLoading dialogLoading;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_first_step);
        activity = this;
        codeInputDialog = new CodeInputDialog(activity);
        dialogLoading = new RxDialogLoading(activity);
        ScrollView scrollView = findViewById(R.id.scrollView);
        service = findViewById(R.id.service);
        phone = findViewById(R.id.phone);
        phone.requestFocus();
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;
        mContent = findViewById(R.id.content);
        handler = new Handler();
        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -250);
                    mAnimatorTranslateY.setDuration(800);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                    service.setVisibility(View.INVISIBLE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(800);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        service.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        findViewById(R.id.login_cancel).setOnClickListener(this);

        findViewById(R.id.registButton).setOnClickListener(this);

        findViewById(R.id.role1).setOnClickListener(this);

        findViewById(R.id.role2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_cancel:
                finish();
                break;
            case R.id.registButton:
                if (!Server.isPhoneNum(phone.getText().toString().trim())) {
                    ToastUtil.showMessage(getApplicationContext(), "输入正确的电话号码");
                    break;
                }
                final CaptchaDialog dialog = new CaptchaDialog(this);
                dialog.show();
                final RxSwipeCaptcha mRxSwipeCaptcha = dialog.getSwipeCaptcha();
                final SeekBar mSeekBar = dialog.getSeekBar();
                mRxSwipeCaptcha.setOnCaptchaMatchCallback(new RxSwipeCaptcha.OnCaptchaMatchCallback() {
                    @Override
                    public void matchSuccess(RxSwipeCaptcha rxSwipeCaptcha) {
                        dialog.dismiss();
                        mSeekBar.setEnabled(false);
                        register();
                    }

                    @Override
                    public void matchFailed(RxSwipeCaptcha rxSwipeCaptcha) {
                        ToastUtil.showMessage(getApplicationContext(), "验证失败:拖动滑块将悬浮头像正确拼合");
                        rxSwipeCaptcha.resetCaptcha();
                        mSeekBar.setProgress(0);
                    }
                });
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mRxSwipeCaptcha.setCurrentSwipeValue(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mSeekBar.setMax(mRxSwipeCaptcha.getMaxSwipeValue());
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mRxSwipeCaptcha.matchCaptcha();
                    }
                });

                //测试从网络加载图片是否ok
                Glide.with(getApplicationContext())
                        .load(R.mipmap.wall)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mRxSwipeCaptcha.setImageBitmap(resource);
                                mRxSwipeCaptcha.createCaptcha();
                            }
                        });
                break;
            case R.id.role1:
                Intent privacy = new Intent(activity, WebActivity.class);
                privacy.putExtra("URL", Server.getURL() + "privacy.html");
                startActivity(privacy);
                break;
            case R.id.role2:
                Intent role = new Intent(activity, WebActivity.class);
                role.putExtra("URL", Server.getURL() + "role.html");
                startActivity(role);
                break;
            default:
                break;
        }
    }

    private void register() {
        if (timer == null)
            timer = new CountDownTimer(60300, 1000) {
                @Override
                public void onTick(long l) {
                    codeInputDialog.setTime("重新发送(" + ((l / 1000) - 1) + "s) ");
                }

                @Override
                public void onFinish() {
                    codeInputDialog.setTime("重新发送 ");
                    codeInputDialog.setEnable(true);
                }
            };
        dialogLoading = new RxDialogLoading(activity);
        dialogLoading.setLoadingText("加载中，请稍后");
        dialogLoading.setCancelable(false);
        dialogLoading.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = Server.checkInfoExist(handler, phone.getText().toString().trim(),true);
                dialogLoading.dismiss();
                handler.post(new UpdateUITools(codeInputDialog, UpdateUITools.Show));
                if (flag && codeInputDialog.isEnable()) {
                    v = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                    code = "#code#=" + v;
                    dialogLoading.show();
                    flag = JUHECode.sendCode(handler, phone.getText().toString().trim(), code);
                    codeInputDialog.setEnable(false);
                    dialogLoading.dismiss();
                    if (flag) {
                        timer.start();
                        handler.post(new UpdateUITools("验证码已发送，请注意查收"));
                        codeInputDialog.getOk().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (v.equals(codeInputDialog.getCode())) {
                                    Intent intent = new Intent(RegistFirstStepActivity.this, RegistLastStepActivity.class);
                                    intent.putExtra("phone", phone.getText().toString().trim());
                                    startActivity(intent);
                                    codeInputDialog.dismiss();
                                } else
                                    ToastUtil.showMessage(activity, "验证码错误！");
                            }
                        });

                        codeInputDialog.getCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                codeInputDialog.dismiss();
                            }
                        });

                        codeInputDialog.getTime().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                codeInputDialog.setEnable(false);
                                v = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                                code = "#code#=" + v;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JUHECode.sendCode(handler, phone.getText().toString().trim(), code);
                                        timer.start();
                                    }
                                }).start();
                            }
                        });
                    } else
                        codeInputDialog.dismiss();
                    dialogLoading.dismiss();

                }
            }
        }).start();
    }


}
