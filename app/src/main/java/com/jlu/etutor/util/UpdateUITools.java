package com.jlu.etutor.util;

import android.app.Activity;
import android.view.View;

import com.jlu.etutor.InitApplication;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.yalantis.phoenix.PullToRefreshView;


/**
 * Created by 医我一生 on 2018/2/2.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 * 通过构造不同的UpdateUITools对象来实现不同的UI更新操作
 */

public class UpdateUITools implements Runnable {

    public static final int DialogMessage = 0;

    public static final int ToastMessage = 1;

    public static final int DissMiss = 2;

    public static final int PullToRefresh=3;

    public static final int TitleBar=4;

    public static final int Enable=5;

    public static final int Show=6;

    public static final int ForceClose = 0;

    private static final int DoNothing = 1;
    /**
     *
     */
    private int option;
    private RxDialogSure sureDialog;
    private String title, message;
    private Activity activity;
    private int action;
    private RxDialog rxDialog;
    private PullToRefreshView pullToRefreshView;
    private EaseTitleBar titleBar;
    private View view;
    private boolean flag;

    public UpdateUITools(EaseTitleBar titleBar,String title){
        this.option=TitleBar;
        this.title=title;
        this.titleBar=titleBar;
    }

    public UpdateUITools(Activity activity, String title, String message, int action) {
        this.activity = activity;
        this.title = title;
        this.message = message;
        this.action = action;
        this.option = DialogMessage;
    }

    public UpdateUITools(RxDialog dialog) {
        this.rxDialog = dialog;
        this.option=DissMiss;
    }

    public UpdateUITools(RxDialog dialog,int option) {
        this.rxDialog = dialog;
        this.option=option;
    }

    public UpdateUITools(String message) {
        this.message = message;
        this.option = ToastMessage;
    }

    public UpdateUITools(PullToRefreshView pullToRefreshView){
        this.pullToRefreshView=pullToRefreshView;
        this.option=PullToRefresh;
    }

    public UpdateUITools(View v,boolean flag){
        this.view=v;
        this.flag=false;
        this.option=Enable;
    }

    @Override
    public void run() {
        switch (option) {
            case DialogMessage:
                initSureDialog();
                break;
            case ToastMessage:
                ToastUtil.showMessage(InitApplication.getContext(), message);
                break;
            case DissMiss:
                rxDialog.dismiss();
                break;
            case Show:
                rxDialog.show();
                break;
            case PullToRefresh:
                pullToRefreshView.setRefreshing(false);
                break;
            case TitleBar:
                titleBar.setTitle(title);
                break;
            case Enable:
                view.setEnabled(flag);
            default:
                break;
        }
    }

    public void initSureDialog() {
        sureDialog = new RxDialogSure(activity);
        sureDialog.setCancelable(false);
        sureDialog.getContentView().setText(message);
        sureDialog.getContentView().setGravity(android.view.Gravity.CENTER);
        sureDialog.setTitle(title);
        sureDialog.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (action) {
                    case ForceClose:
                        Server.logout(activity);
                        break;
                    case DoNothing:
                        sureDialog.dismiss();
                        break;
                    default:
                        break;

                }
            }
        });
        sureDialog.show();
    }

}
