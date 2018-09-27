package com.jlu.etutor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jlu.etutor.R;
import com.jlu.etutor.util.Server;
import com.jlu.etutor.util.UpdateUITools;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

public class EditPWDActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler handler;

    private TextView pwd, confirmPwd;

    private String phone;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        activity=this;
        handler=new Handler();

        phone=getIntent().getStringExtra("phone");

        pwd = findViewById(R.id.password);

        confirmPwd = findViewById(R.id.confirm);

        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.finish).setOnClickListener(this);
    }

    private void editPWD() {
        final String p=pwd.getText().toString();
        String cp=confirmPwd.getText().toString();
        if(p.equals(cp)){
            final RxDialogLoading dialogLoading=new RxDialogLoading(this);
            dialogLoading.setLoadingText("加载中，请稍后...");
            dialogLoading.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(Server.editPWD(handler,phone,p)){
                        handler.post(new UpdateUITools("修改成功！"));
                        Server.logout(activity);
                        startActivity(new Intent(EditPWDActivity.this,LoginActivity.class));
                    }else{
                        handler.post(new UpdateUITools("密码修改失败！"));
                    }
                    dialogLoading.dismiss();
                }
            }).start();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                editPWD();
                break;
            default:
                break;
        }
    }
}
