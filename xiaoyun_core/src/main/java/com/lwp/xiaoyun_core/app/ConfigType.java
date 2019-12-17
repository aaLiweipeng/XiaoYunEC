package com.lwp.xiaoyun_core.app;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/6 1:25
 *     desc   :枚举类，为 Configurator中的 数据Map 提供 Key,
 *             其实就是明了显式地展现 其数据Map 用到的、需要用到的 Key
 * </pre>
 */
public enum ConfigType {
    API_HOST,//配置网络请求域名
    APPLICATION_CONTEXT,//全局上下文
    CONFIG_READY,//配置、初始化的控制信号，配置完成位
    ICON //存储初始化项目
}
