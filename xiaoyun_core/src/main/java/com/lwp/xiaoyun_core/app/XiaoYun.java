package com.lwp.xiaoyun_core.app;

import android.content.Context;
import android.os.Handler;

import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/6 1:12
 *     desc   : 依赖于 Configurator ，
 *              提供 应用配置字段的初始化方法，
 *
 *              提供 Configurator 的 getter 及其中 各种 配置字段值 的 getter，
 *              区别， Configurator 的getter 中 封装了各种逻辑，提供给 XiaoYun Class
 *              这里的getter 只需要调用 Configurator 封装好的getter函数就好，往往一行完成get获取
 * </pre>
 */
public final class XiaoYun {

    //初始化App，返回 Configurator 实例，
    // 只能在初始化的时候调用一次！！
    public static Configurator init(Context context) {
        //初始化的时候获取配置类实例，同时把AppContext配置好
        getConfigurator()
                .getXiaoyunConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT,
                        context.getApplicationContext());
        //初始化Logger
        XiaoYunLogger.init();

        //旧版写法
//        getConfigurations().
//                put(ConfigKeys.APPLICATION_CONTEXT.name(),
//                        context.getApplicationContext());
        return Configurator.getInstance();
    }

    //获取 Configurator 实例
    public static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    //用于 获取 配置类Configurator 中 数据Map 的某个元素！！！
    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    //获取单例中的 配置数据Map
    // 通过获取Configurator单例进而
//    public static HashMap<Object, Object> getConfigurations() {
//        return Configurator.getInstance().getXiaoyunConfigs();
//    }

    //返回 全局应用Context
    public static Context getApplicationContext() {
        return (Context) getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
        //旧版写法，每次调用，都要获取数据Map，然后get，
        // 新版写法如上，封装了（拿到数据Map，然后get）的过程，
        // 直接传一个 ConfigKeys 即可
//        return (Context) getConfigurations().get(ConfigKeys.APPLICATION_CONTEXT.name());
    }

    public static Handler getHandler() {
        return getConfiguration(ConfigKeys.HANDLER);
    }
}
