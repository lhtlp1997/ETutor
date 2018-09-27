package com.jlu.etutor.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.jlu.etutor.InitApplication;
import com.jlu.etutor.R;
import com.jlu.etutor.gson.TeacherInfo;
import com.jlu.etutor.gson.UserInfo;
import com.jlu.etutor.util.Server;
import com.jlu.etutor.util.ToastUtil;
import com.jlu.etutor.util.UpdateUITools;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.dialog.RxDialogWheelYearMonthDay;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;


public class PersonalInfoActivity extends Activity implements View.OnClickListener {

    private boolean flag;

    private UserInfo info;

    private boolean option;

    private TeacherInfo teacherInfo;

    private int temp;

    private RxDialogEditSureCancel dialog;

    private RxDialogWheelYearMonthDay mRxDialogWheelYearMonthDay;

    private SuperTextView trueName;
    private SuperTextView sex;
    private SuperTextView college;
    private SuperTextView major;
    private SuperTextView time;
    private SuperTextView salary;

    private ImageView imageView;

    private Handler handler;

    private int cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_info);

        handler = new Handler();

        flag = false;
        RxTitle title = findViewById(R.id.rx_title);
        title.getIvLeft().setOnClickListener(this);
        dialog = new RxDialogEditSureCancel(this);
        dialog.getSureView().setOnClickListener(this);
        dialog.getCancelView().setOnClickListener(this);
        dialog.getTitleView().setTextSize(18);
        dialog.setCancelable(false);
        cancel = dialog.getCancelView().getId();
        info = (UserInfo) getIntent().getSerializableExtra("info");
        teacherInfo = (TeacherInfo) getIntent().getSerializableExtra("teaInfo");
        option=getIntent().getBooleanExtra("option",true);
        initViews();
        if (info.getType() == 0)
            initTeaViews();

    }

    private void initViews() {
        ((TextView) findViewById(R.id.name)).setText(info.getName());
        ((TextView) findViewById(R.id.phone)).setText(info.getPhone());
        ((TextView) findViewById(R.id.tv_address)).setText(info.getEmail());
        ((TextView) findViewById(R.id.tv_lables)).setText(info.getTag());

        imageView = findViewById(R.id.header);

        Glide.with(this).load(Server.getURL() + "image/" + info.getPhone()).signature(new StringSignature(String.valueOf(Server.mills))).into(imageView);

        if (info.getType() == 0)
            ((TextView) findViewById(R.id.type)).setText("教师");
        else
            ((TextView) findViewById(R.id.type)).setText("学生");


        if (info.getPhone().equals(InitApplication.getUserInfo().getPhone())) {
            findViewById(R.id.header).setOnClickListener(this);

            findViewById(R.id.email).setOnClickListener(this);

            findViewById(R.id.tag).setOnClickListener(this);
        }

    }

    private void initTeaViews() {

        findViewById(R.id.teaInfo).setVisibility(View.VISIBLE);

        trueName = findViewById(R.id.trueName);
        sex = findViewById(R.id.sex);
        college = findViewById(R.id.college);
        major = findViewById(R.id.major);
        time = findViewById(R.id.time);
        salary = findViewById(R.id.salary);
        ((TextView) findViewById(R.id.introduction)).setText(teacherInfo.getIntroduction());
        if (teacherInfo != null) {
            trueName.setRightString(teacherInfo.getTrueName());
            if (teacherInfo.getSex() == 1)
                sex.setRightString("男");
            else if (teacherInfo.getSex() == 0)
                sex.setRightString("未选择");
            else
                sex.setRightString("女");
            college.setRightString(teacherInfo.getCollege());
            major.setRightString(teacherInfo.getMajor());
            time.setRightString(teacherInfo.getTime());
            salary.setRightString(teacherInfo.getSalary());

        }

        if (info.getPhone().equals(InitApplication.getUserInfo().getPhone())) {
            trueName.setOnClickListener(this);
            trueName.setRightIcon(R.drawable.arrow_right_red);
            sex.setOnClickListener(this);
            sex.setRightIcon(R.drawable.arrow_right_red);
            college.setOnClickListener(this);
            college.setRightIcon(R.drawable.arrow_right_red);
            major.setOnClickListener(this);
            major.setRightIcon(R.drawable.arrow_right_red);
            time.setOnClickListener(this);
            time.setRightIcon(R.drawable.arrow_right_red);
            salary.setOnClickListener(this);
            salary.setRightIcon(R.drawable.arrow_right_red);
            findViewById(R.id.button).setVisibility(View.INVISIBLE);
        } else
            findViewById(R.id.button).setOnClickListener(this);

        if(!option)
            findViewById(R.id.button).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == cancel) {
            dialog.getEditText().setText("");
            dialog.getEditText().setHint("");
            dialog.dismiss();
            return;
        }
        if (v.getId() != R.id.header && v.getId() != R.id.iv_left && R.id.button != v.getId())
            flag = true;
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent(PersonalInfoActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,info.getPhone());
                intent.putExtra("name",info.getName());
                startActivity(intent);
                finish();
                break;
            case R.id.header:
                Server.setMills(System.currentTimeMillis());
                new RxDialogChooseImage(this).show();
                break;
            case R.id.email:
                temp = R.id.email;
                dialog.setTitle("输入邮箱地址");
                dialog.show();
                break;
            case R.id.tag:
                temp = R.id.tag;
                dialog.setTitle("输入标签(最多5个字符)");
                dialog.show();
                break;
            case R.id.trueName:
                temp = R.id.trueName;
                dialog.setTitle("输入姓名(最多5个字符)");
                dialog.show();
                break;
            case R.id.sex:
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
                rxDialogSureCancel.getSureView().setTextColor(Color.BLACK);
                rxDialogSureCancel.getCancelView().setTextColor(Color.BLACK);
                rxDialogSureCancel.getSureView().setText("男");
                rxDialogSureCancel.getCancelView().setText("女");
                rxDialogSureCancel.setTitle("请选择");
                rxDialogSureCancel.setContent("选择您的性别");
                rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        teacherInfo.setSex(1);
                        sex.setRightString("男");
                        rxDialogSureCancel.dismiss();
                    }
                });
                rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        teacherInfo.setSex(-1);
                        sex.setRightString("女");
                        rxDialogSureCancel.dismiss();
                    }
                });
                rxDialogSureCancel.show();
                break;
            case R.id.college:
                temp = R.id.college;
                dialog.setTitle("大学名称(10个字符以内)");
                dialog.show();
                break;
            case R.id.major:
                temp = R.id.major;
                dialog.setTitle("专业名称(15个字符以内)");
                dialog.show();
                break;
            case R.id.time:
                if (mRxDialogWheelYearMonthDay == null) {
                    initWheelYearMonthDayDialog();
                }
                mRxDialogWheelYearMonthDay.show();
                break;
            case R.id.salary:
                temp = R.id.salary;
                dialog.setTitle("输入预期薪水(10个以内)");
                dialog.getEditText().setHint("面谈,50每小时等");
                dialog.show();
                break;
            case R.id.tv_sure:    //0x7f0f014d 输入对话框的确认ID
                setMessage();
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void setMessage() {
        String message = dialog.getEditText().getText().toString();
        switch (temp) {
            case R.id.email:
                if (Server.isEmail(message)) {
                    if (message.length() <= 20) {
                        info.setEmail(message);
                        ((TextView) findViewById(R.id.tv_address)).setText(message);
                        dialog.dismiss();
                        dialog.getEditText().setText("");
                    } else
                        ToastUtil.showMessage(this, "最多输入20个字符！");

                } else
                    ToastUtil.showMessage(this, "您输入了错误的邮箱地址！");
                break;
            case R.id.tag:
                if (message.length() <= 5) {
                    info.setTag(message);
                    if (info.getType() == 0)
                        teacherInfo.setTag(message);
                    ((TextView) findViewById(R.id.tv_lables)).setText(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 标签最多五个字！");
                break;
            case R.id.trueName:
                if (message.length() <= 5) {
                    if (teacherInfo != null)
                        teacherInfo.setTrueName(message);
                    trueName.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 姓名最多5个字符！");
                break;
            case R.id.college:
                if (message.length() <= 10) {
                    if (teacherInfo != null)
                        teacherInfo.setCollege(message);
                    college.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 最多输入10个字符！");
                break;
            case R.id.major:
                if (message.length() <= 15) {
                    if (teacherInfo != null)
                        teacherInfo.setMajor(message);
                    major.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                } else
                    ToastUtil.showMessage(this, " 最多输入15个字符！");
                break;
            case R.id.salary:
                if (message.length() <= 10) {
                    if (teacherInfo != null)
                        teacherInfo.setSalary(message);
                    salary.setRightString(message);
                    dialog.dismiss();
                    dialog.getEditText().setText("");
                    dialog.getEditText().setHint("");
                } else
                    ToastUtil.showMessage(this, " 最多输入10个字符！");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (flag) {
            final RxDialogLoading dialogLoading = new RxDialogLoading(this);
            dialogLoading.setLoadingText("上传资料中...");
            dialogLoading.setCancelable(false);
            final RxDialogSureCancel confirmDialog = new RxDialogSureCancel(this);
            confirmDialog.setCancelable(false);
            confirmDialog.setTitle("提示");
            confirmDialog.getTitleView().setTextSize(20);
            confirmDialog.getContentView().setTextSize(16);
            confirmDialog.setContent("您已做出了修改，是否立即保存？");
            confirmDialog.getSureView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogLoading.show();
                    confirmDialog.dismiss();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Server.updateUserInfo(handler, info);
                            if (info.getType() == 0)
                                Server.updateTeaInfo(handler, teacherInfo);
                            handler.post(new UpdateUITools(dialogLoading));
                            reInitMessage();
                            finish();
                        }
                    }).start();

                }
            });
            confirmDialog.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    finish();
                }
            });
            confirmDialog.show();
        } else
            finish();

    }

    private void initWheelYearMonthDayDialog() {
        mRxDialogWheelYearMonthDay = new RxDialogWheelYearMonthDay(this, 1994, 2018);
        mRxDialogWheelYearMonthDay.getSureView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        String date;
                        if (mRxDialogWheelYearMonthDay.getCheckBoxDay().isChecked()) {
                            date = mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                    + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月"
                                    + mRxDialogWheelYearMonthDay.getSelectorDay() + "日";

                        } else {
                            date = mRxDialogWheelYearMonthDay.getSelectorYear() + "年"
                                    + mRxDialogWheelYearMonthDay.getSelectorMonth() + "月";
                        }
                        time.setRightString(date);
                        if (teacherInfo != null)
                            teacherInfo.setTime(date);
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
        mRxDialogWheelYearMonthDay.getCancleView().setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mRxDialogWheelYearMonthDay.cancel();
                    }
                });
    }

    private void reInitMessage() {
        InitApplication.getUserInfo().setEmail(info.getEmail());
        InitApplication.getUserInfo().setTag(info.getTag());
        if (info.getType() == 0) {
            InitApplication.getTeacherInfo().setTrueName(teacherInfo.getTrueName());
            InitApplication.getTeacherInfo().setSex(teacherInfo.getSex());
            InitApplication.getTeacherInfo().setCollege(teacherInfo.getCollege());
            InitApplication.getTeacherInfo().setMajor(teacherInfo.getMajor());
            InitApplication.getTeacherInfo().setTime(teacherInfo.getTime());
            InitApplication.getTeacherInfo().setSalary(teacherInfo.getSalary());
            InitApplication.getTeacherInfo().setIntroduction(teacherInfo.getIntroduction());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    if (resultUri != null) {
                        Server.uploadFile(handler, RxPhotoTool.getImageAbsolutePath(this, resultUri), this, imageView);
                        SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("path", RxPhotoTool.getImageAbsolutePath(this, resultUri));
                        editor.apply();
                    } else
                        ToastUtil.showMessage(this, "裁剪失败了！要不再试一次？");

                }
                break;
            case UCrop.RESULT_ERROR:
                ToastUtil.showMessage(this, "裁剪失败了！要不再试一次？");
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUCrop(Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), InitApplication.getUserInfo().getPhone()));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));
        //设置裁剪质量
        options.setCompressionQuality(30);
        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

}
