package com.lwp.xiaoyun_core.app;

import android.content.Context;

import java.util.HashMap;

public final class XiaoYun {

    //初始化App
    public static Configurator init(Context context) {
        //初始化的时候获取配置类实例，同时把AppContext配置好
        getConfigurations().put(ConfigType.APPLICATION_CONTEXT.name(), context.getApplicationContext());
        return Configurator.getInstance();
    }

    //获取单例中的 配置数据Map
    // 通过获取Configurator单例进而
    public static HashMap<String, Object> getConfigurations() {
        return Configurator.getInstance().getXiaoyunConfigs();
    }

    //返回 全局应用Context
    public static Context getApplicationContext() {
        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTEXT.name());
    }
}
