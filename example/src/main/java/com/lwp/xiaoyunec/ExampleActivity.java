package com.lwp.xiaoyunec;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lwp.xiaoyun.ec.icon.FontEcModule;
import com.lwp.xiaoyun.ec.launcher.LauncherDelegate;
import com.lwp.xiaoyun_core.activities.ProxyActivity;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.Interceptor.DebugInterceptor;


public class ExampleActivity extends ProxyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        XiaoYun.init(this)
                .withInterceptor(new DebugInterceptor("index", R.raw.test))
                .withApiHost("http://127.0.0.1/")
                .configure();
        super.onCreate(savedInstanceState);
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
