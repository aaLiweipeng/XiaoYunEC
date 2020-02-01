package com.lwp.xiaoyun_core.delegates.web;

import com.alibaba.fastjson.JSON;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/30 18:38
 *     desc   : JavaScript接口，用于WebView跟原生进行交互
 * </pre>
 */
public class XiaoYunWebInterface {
    private final WebDelegate DELEGATE;


    public XiaoYunWebInterface(WebDelegate delegate) {
        DELEGATE = delegate;
    }
    //简单工厂方法
    static XiaoYunWebInterface create(WebDelegate delegate) {
        return new XiaoYunWebInterface(delegate);
    }

    public String event(String params) {
        final String action = JSON.parseObject(params).getString("action");
        return null;
    }
}
