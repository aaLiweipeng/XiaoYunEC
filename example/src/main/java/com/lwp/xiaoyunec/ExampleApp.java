package com.lwp.xiaoyunec;

import android.app.Application;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lwp.xiaoyun.ec.database.DatabaseManager;
import com.lwp.xiaoyun.ec.icon.FontEcModule;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.rx.AddCookieInterceptor;
import com.lwp.xiaoyun_core.util.callback.CallbackManager;
import com.lwp.xiaoyun_core.util.callback.CallbackType;
import com.lwp.xiaoyun_core.util.callback.IGlobalCallback;
import com.lwp.xiaoyunec.evnet.TestEvent;
import com.lwp.xiaoyun_core.net.Interceptor.DebugInterceptor;

import cn.jpush.android.api.JPushInterface;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/26 16:12
 *     desc   :
 * </pre>
 */
public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化MultiDex
        MultiDex.install(this);

        XiaoYun.init(this)
                .withInterceptor(new DebugInterceptor("user_profile", R.raw.user_profile))
                .withInterceptor(new AddCookieInterceptor())//添加cookie同步拦截器
                .withApiHost("https://127.0.0.1/")
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withJavaScriptInterface("xiaoyun")
                .withWebEvent("test",new TestEvent())
                .withWebHost("https://www.baidu.com/")
//                .withWeChatAppId("")
//                .withWeChatAppSecret("")
                .configure();
        DatabaseManager.getInstance().init(this);

        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //使用全局回调 实现 推送控制
        // 这里添加接口和回调方法  SettingsDelegate中获得接口并调用方法
        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (JPushInterface.isPushStopped(XiaoYun.getApplicationContext())) {
                            //判断如果推送是关的，则开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(XiaoYun.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (!JPushInterface.isPushStopped(XiaoYun.getApplicationContext())) {
                            JPushInterface.stopPush(XiaoYun.getApplicationContext());
                        }
                    }
                });
//        initStetho();
    }

//    private void initStetho() {
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                        .build()
//        );
//    }
}
