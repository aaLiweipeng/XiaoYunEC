package com.lwp.xiaoyun_core.app;

import android.content.Context;

public final class XiaoYun {

    //初始化App
    public static Configurator init(Context context) {
        //初始化的时候获取配置类实例，同时把AppContext配置好
        Configurator.getInstance()
                .getXiaoyunConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT,
                        context.getApplicationContext());

        //旧版写法
//        getConfigurations().
//                put(ConfigKeys.APPLICATION_CONTEXT.name(),
//                        context.getApplicationContext());
        return Configurator.getInstance();
    }

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
}
