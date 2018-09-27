package com.jlu.etutor.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.jlu.etutor.R;
import com.jlu.etutor.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.util.List;

/**
 * Created by cj597 on 2018/2/21.
 * Email  597021782@qq.com
 * Github https://github.com/Easoncheng0405
 */

public class MessageReceiver extends Service{
    EMMessageListener msgListener;
    public MessageReceiver() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                EMMessage message=messages.get(0);
                EaseUser user= EaseUserUtils.getUserInfo(message.getUserName());
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,message.getUserName());
                intent.putExtra("name",user.getNickname());
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification=new NotificationCompat.Builder(getApplicationContext()).setContentTitle("ETutor新消息提醒")
                        .setContentText("你收到了一条来自  "+user.getNickname()+"  的新消息").setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.fluidicon)
                        .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                        .setContentIntent(contentIntent)
                        .build();
                manager.notify((int) (System.currentTimeMillis() / 1000),notification);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(msgListener!=null)
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
