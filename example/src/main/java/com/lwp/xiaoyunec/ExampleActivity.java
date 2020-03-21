package com.lwp.xiaoyunec;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.widget.Toast;

import com.lwp.xiaoyun.ec.launcher.LauncherDelegate;
import com.lwp.xiaoyun.ec.main.EcBottomDelegate;
import com.lwp.xiaoyun.ec.sign.ISignListener;
import com.lwp.xiaoyun.ec.sign.SignInDelegate;
import com.lwp.xiaoyun.ui.launcher.ILauncherListener;
import com.lwp.xiaoyun.ui.launcher.OnLauncherFinishTag;
import com.lwp.xiaoyun_core.activities.ProxyActivity;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import cn.jpush.android.api.JPushInterface;
import qiu.niorgai.StatusBarCompat;


public class ExampleActivity extends ProxyActivity implements
        ISignListener,
        ILauncherListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏 ActionBar
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        XiaoYun.getConfigurator().withActivity(this);//配置全局 Activity

        //沉浸式状态栏
        StatusBarCompat.translucentStatusBar(this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public XiaoYunDelegate setRootDelegate() {
        //一个new ，一行代码，即可实现，把ExampleDelegate中传入的布局！！！！！！！
        // 通过 BaseDelegate 的onCreateView()进行类型判断，
        // 并完成 **布局**绑定到**根布局**，**根布局** 绑定到 **Fragment（Delegate） （BaseDelegate继承的SwipeBackFragment）**
        // 通过 ProxyActivity . onCreate . initContainer，
        // 又把 **Fragment（Delegate）** 绑定到 id 又到 new FrameLayout(this); 即 绑定到了Activity中 （的 this Context 对应的 FrameLayout中）
        // 然后 setContentView(FrameLayout); 最后显示出来在屏幕上，这就是框架的魅力！！！
//        return new ExampleDelegate();

        //测试倒计时启动图
        return new LauncherDelegate();

        //测试 滚动启动图
//        return new LauncherScrollDelegate();

        //测试注册碎片、登录碎片（注册碎片中的Link 可以跳转到 登录碎片）
//        return new SignUpDelegate();
    }

    @Override
    public void onSignInSuccess() {
        XiaoYunLogger.v("SIGN_IN_SUCCESS", "登录成功");
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccess() {
        XiaoYunLogger.v("SIGN_UP_SUCCESS", "登录成功");
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag) {
            case SIGNED:
                Toast.makeText(this, "启动结束，用户登录了", Toast.LENGTH_SHORT).show();
                //测试
//                startWithPop(new ExampleDelegate());
                startWithPop(new EcBottomDelegate());
                break;

            case NOT_SIGNED:
                Toast.makeText(this, "启动结束，用户没登录", Toast.LENGTH_SHORT).show();
                //在start 的同时 ，把栈中上一个元素清除掉，
                // 启动图 完了之后就可以清除掉了（启动图模块 启动结束后 会调用到这里 ）
                //进入 登录页面
                startWithPop(new SignInDelegate());
                break;

            default:
                break;
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        //简单配置
////        XiaoYun.init(this)
////                .configure();
////
//////        //连缀配置诸信息，通过建立with方法，连缀配置即可
//////        XiaoYun.init(this)
//////                .withApiHost("http://127.0.0.1/")
//////                .withIcon(new FontAwesomeModule())
////////                .withIcon(new FontEcModule())
//////                .configure();
////        /**/
////
////        //如果成功弹出Toast，
////        // 证明Application Context 已经成功传入配置到 Configurator的 配置信息Map中
////        // 也证明 XiaoYun.java 中的 init()初始化成功了，代码没有问题
////        Toast.makeText(XiaoYun.getApplicationContext(), "测试XiaoYun.getApplicationContext()成功", Toast.LENGTH_SHORT).show();
//
//    }


}
