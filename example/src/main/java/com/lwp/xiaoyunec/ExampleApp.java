package com.lwp.xiaoyunec;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lwp.xiaoyun.ec.database.DatabaseManager;
import com.lwp.xiaoyun.ec.icon.FontEcModule;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.rx.AddCookieInterceptor;
import com.lwp.xiaoyunec.evnet.TestEvent;
import com.lwp.xiaoyun_core.net.Interceptor.DebugInterceptor;

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
