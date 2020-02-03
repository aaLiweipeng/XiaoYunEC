package com.lwp.xiaoyun_core.app;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/6 1:25
 *     desc   :枚举类，为 Configurator 中的 数据Map 等 存储配置信息的集合 提供 Key,
 *             明了显式地展现 其数据Map 用到的、需要用到的 Key，方便调用、逻辑清晰
 * </pre>
 */
public enum ConfigKeys {
    API_HOST,//配置网络请求域名
    APPLICATION_CONTEXT,//全局上下文
    CONFIG_READY,//配置、初始化的控制信号，配置完成位
    ICON, //存储初始化项目
    LOADER_DELAYED,
    INTERCEPTOR,//拦截器
    WE_CHAT_APP_ID,
    WE_CHAT_APP_SECRET,
    ACTIVITY,
    HANDLER,
    JAVASCRIPT_INTERFACE,//接口标志名
    WEB_HOST,
    USER_AGENTS,
    COOKIE
}
