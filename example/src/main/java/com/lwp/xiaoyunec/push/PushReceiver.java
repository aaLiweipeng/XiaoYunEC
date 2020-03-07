package com.lwp.xiaoyunec.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;
import com.lwp.xiaoyunec.ExampleActivity;


import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/8 5:07
 *     desc   :
 * </pre>
 */
public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        //判断 极光推送 推送的所有信息
        final Set<String> keys = bundle.keySet();
        final JSONObject json = new JSONObject();
        for (String key : keys) {
            final Object val = bundle.get(key);
            json.put(key, val);
        }

        //打印调试
        XiaoYunLogger.json("PushReceiver", json.toJSONString());

        //获取事件类型
        final String pushAction = intent.getAction();
        //判断事件类型 并处理
        if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {

            //处理接收到的信息
            onReceivedMessage(bundle);

        } else if (pushAction.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {

            //打开相应的Notification
            onOpenNotification(context, bundle);
        }
    }

    //处理 接收到的信息
    private void onReceivedMessage(Bundle bundle) {
        final String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        final String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        final int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        final String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        final String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
    }

    //打开相应的Notification
    private void onOpenNotification(Context context, Bundle bundle) {

        final String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        final Bundle openActivityBundle = new Bundle();//加入一个Bundle可以用来传递信息
        //点击Notification就打开我们的APP
        final Intent intent = new Intent(context, ExampleActivity.class);
        intent.putExtras(openActivityBundle);
        //应用可能是在后台运行的，
        // 需要另起一个Activity的Task FLAG_ACTIVITY_NEW_TASK
        // 把之前的 Activity清掉 FLAG_ACTIVITY_CLEAR_TOP
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ContextCompat.startActivity(context, intent, null);
    }
}

