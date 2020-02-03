package com.lwp.xiaoyun_core.delegates.web;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.lwp.xiaoyun_core.delegates.web.event.Event;
import com.lwp.xiaoyun_core.delegates.web.event.EventManager;
import com.lwp.xiaoyun_core.delegates.web.event.TestEvent;

import retrofit2.http.DELETE;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/30 18:38
 *     desc   : JavaScript接口，用于WebView跟原生进行交互
 * </pre>
 */
final class XiaoYunWebInterface {

    private final WebDelegate DELEGATE;

    private XiaoYunWebInterface(WebDelegate delegate) {
        DELEGATE = delegate;
    }
    //简单工厂方法
    static XiaoYunWebInterface create(WebDelegate delegate) {
        return new XiaoYunWebInterface(delegate);
    }



    //必须加上这个注解才能响应，否则会被认为是 不安全的、不可应用的
    @JavascriptInterface
    public String event(String params) {

        final String actionValue = JSON.parseObject(params).getString("action");

        if (actionValue != null) {
            switch (actionValue) {
                case "test":
                    //这里自然是使用了 向上转型，即 TestEvent实例 当做 父类Evnent实例 来用
                    EventManager.getInstance().addEvent("test", new TestEvent());
                    break;

                default:
                    break;
            }

            //体会一下这里的两点巧妙地设计，
            // 其一，将 WebDelegate 作为参数设置进来了！
            // 这里的Event只要拿到 WebDelegate，各种信息就可以完美传递过来了！!
            // 其二，将Url作为WebDelegate的成员变量，
            // 初始化 或者 跳转的时候，都需要创建一个新的 WebDelegate，
            // 一个WebDelegate 对应 一个WebView 对应一个Url
            // 这里可以体会到上面这个设计思想的 方便快捷之处，
            // 拿到WebDelegate了，要拿到 Context 以及 Url 就轻而易举
            final Event event = EventManager.getInstance().createEvent(actionValue);
            if (event != null) {
                event.setAction(actionValue);
                event.setDelegate(DELEGATE);
                event.setContext(DELEGATE.getContext());
                event.setUrl(DELEGATE.getUrl());
                return event.execute(params);
            }
        }

        return null;
    }
}
