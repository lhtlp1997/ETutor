package com.jlu.etutor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jlu.etutor.R;
import com.jlu.etutor.adpter.QuestionAdapter;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.dialog.RxDialogSure;

import java.util.ArrayList;

public class HelpActivity extends Activity {
    private RxDialogSure dialogSure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        RxTitle rxTitle = findViewById(R.id.title);
        rxTitle.getIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dialogSure=new RxDialogSure(this);

        dialogSure.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSure.dismiss();
            }
        });

        ListView list = findViewById(R.id.list);
        ArrayList<String> questions = new ArrayList<>();

        questions.add("什么是环信聊天账号？");

        questions.add("我为什么要使用环信聊天账号？");

        questions.add("环信聊天账号将会使用我的哪些信息？");

        questions.add("我该如何注册环信聊天账号?");

        questions.add("我如何添加聊天好友?");

        questions.add("我无法向好友发送消息?");

        questions.add("忘记用户名或密码？");

        questions.add("为什么我的账号被强制下线了？");

        questions.add("无法连接到服务器/服务器错误？");

        questions.add("我如何查找需要的信息？");

        questions.add("提交教师资料有什么作用？");

        questions.add("我如何提交我的教师资料");

        questions.add("我如何反馈意见或提出建议？");

        questions.add("关于ETutor");


        QuestionAdapter adapter = new QuestionAdapter(this, R.layout.question_item, questions);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialogSure.setTitle("关于环信");
                        dialogSure.setContent(getString(R.string.question0));
                        dialogSure.show();
                        break;
                    case 1:
                        dialogSure.setTitle("使用环信聊天账号");
                        dialogSure.setContent(getString(R.string.question1));
                        dialogSure.show();
                        break;
                    case 2:
                        dialogSure.setTitle("隐私政策");
                        dialogSure.setContent(getString(R.string.question2));
                        dialogSure.show();
                        break;
                    case 3:
                        dialogSure.setTitle("注册环信账号");
                        dialogSure.setContent(getString(R.string.question3));
                        dialogSure.show();
                        break;
                    case 4:
                        dialogSure.setTitle("添加好友");
                        dialogSure.setContent(getString(R.string.question4));
                        dialogSure.show();
                        break;
                    case 5:
                        dialogSure.setTitle("发送消息");
                        dialogSure.setContent(getString(R.string.question5));
                        dialogSure.show();
                        break;
                    case 6:
                        dialogSure.setTitle("无法登陆");
                        dialogSure.setContent(getString(R.string.question6));
                        dialogSure.show();
                        break;
                    case 7:
                        dialogSure.setTitle("被强制下线");
                        dialogSure.setContent(getString(R.string.question7));
                        dialogSure.show();
                        break;
                    case 8:
                        dialogSure.setTitle("服务器错误");
                        dialogSure.setContent(getString(R.string.question8));
                        dialogSure.show();
                        break;
                    case 9:
                        dialogSure.setTitle("查找信息");
                        dialogSure.setContent(getString(R.string.question9));
                        dialogSure.show();
                        break;
                    case 10:
                        dialogSure.setTitle("教师资料");
                        dialogSure.setContent(getString(R.string.question10));
                        dialogSure.show();
                        break;
                    case 11:
                        dialogSure.setTitle("提交资料");
                        dialogSure.setContent(getString(R.string.question11));
                        dialogSure.show();
                        break;
                    case 12:
                        dialogSure.setTitle("反馈");
                        dialogSure.setContent(getString(R.string.question12));
                        dialogSure.show();
                        break;
                    case 13:
                        dialogSure.setTitle("大创小分队");
                        dialogSure.setContent("吉林大学计算机科学与技术学院：\n 程杰，洪泽海，林朋，刘瀚霆，袁子易");
                        dialogSure.show();
                    default:
                        break;

                }
            }
        });

    }


}
