package com.lwp.xiaoyunec;

import android.app.Application;

import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lwp.xiaoyun.ec.database.DatabaseManager;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.Interceptor.DebugInterceptor;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

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

        XiaoYun.init(this)
                .withInterceptor(new DebugInterceptor("index", R.raw.test))
                .withApiHost("http://127.0.0.1/")
                .withIcon(new FontAwesomeModule())
                .configure();
        DatabaseManager.getInstance().init(this);
    }
}
