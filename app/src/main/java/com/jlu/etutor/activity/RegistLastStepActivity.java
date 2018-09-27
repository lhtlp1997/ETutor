package com.jlu.etutor.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jlu.etutor.R;
import com.jlu.etutor.gson.UserInfo;
import com.jlu.etutor.util.Server;
import com.jlu.etutor.util.ToastUtil;
import com.jlu.etutor.util.UpdateUITools;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistLastStepActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler handler;

    private int keyHeight = 0; //软件盘弹起后所占高度
    private EditText name, password, confirm;
    private LinearLayout mContent;
    private ImageView tea, stu;
    private Context context;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_last_step);

        handler = new Handler();

        context = this;
        mContent = findViewById(R.id.content);
        stu = findViewById(R.id.stu);
        tea = findViewById(R.id.tea);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;

        findViewById(R.id.scrollView).addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -400);
                    mAnimatorTranslateY.setDuration(800);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(800);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.teacher).setOnClickListener(this);

        findViewById(R.id.student).setOnClickListener(this);

        findViewById(R.id.finish).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(context, RegistFirstStepActivity.class));
                finish();
                break;
            case R.id.student:
                type = 1;
                tea.setImageResource(R.drawable.teacher_gray);
                stu.setImageResource(R.drawable.student);
                break;
            case R.id.teacher:
                type = 0;
                tea.setImageResource(R.drawable.teacher);
                stu.setImageResource(R.drawable.student_gray);
                break;
            case R.id.finish:
                if (checkInput()) {
                    final RxDialogLoading dialogLoading = new RxDialogLoading(context);
                    dialogLoading.setLoadingText("加载中，请稍后");
                    dialogLoading.setCancelable(false);
                    dialogLoading.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String phone = getIntent().getStringExtra("phone");
                            Date date = new Date();
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String time = format.format(date);
                            UserInfo userInfo = new UserInfo(name.getText().toString().trim(), phone, password.getText().toString(), time, type);
                            if (Server.checkInfoExist(handler, userInfo.getName(),true) && Server.register(handler, userInfo)) {
                                userInfo = Server.login(handler, userInfo.getPhone(), userInfo.getPwd());
                                if (userInfo != null) {
                                    SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("name", userInfo.getName());
                                    editor.putString("phone", userInfo.getPhone());
                                    editor.putString("pwd", userInfo.getPwd());
                                    editor.putInt("type", userInfo.getType());
                                    editor.putString("time", userInfo.getTime());
                                    editor.apply();
                                    startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                }
                            }
                            handler.post(new UpdateUITools(dialogLoading));
                        }
                    }).start();
                }
        }
    }

    private boolean checkInput() {
        if (!name.getText().toString().trim().matches("^[\\u4e00-\\u9fa5a-zA-Z][\\u4e00-\\u9fa5a-zA-Z]+$")) {
            ToastUtil.showMessage(context, "用户名由中英字符开头且只能包含中英字符与数字");
            return false;
        }
        String pwd = password.getText().toString();
        String confirmPwd = confirm.getText().toString();
        if (!pwd.equals(confirmPwd)) {
            ToastUtil.showMessage(context, "您输入的密码不一致！");
            return false;
        }

        if (!pwd.matches("[A-Za-z0-9]+")) {
            ToastUtil.showMessage(context, "密码只能由数字或字母组成！");
            return false;
        }

        if (pwd.length() < 6 || pwd.length() > 18) {
            ToastUtil.showMessage(context, "密码长度6-18位！");
            return false;
        }

        return true;

    }
}
