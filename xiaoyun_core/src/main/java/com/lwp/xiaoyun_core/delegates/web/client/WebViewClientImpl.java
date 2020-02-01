package com.lwp.xiaoyun_core.delegates.web.client;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lwp.xiaoyun_core.delegates.web.WebDelegate;
import com.lwp.xiaoyun_core.delegates.web.route.Router;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import retrofit2.http.DELETE;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/1 15:43
 *     desc   :
 * </pre>
 */
public class WebViewClientImpl extends WebViewClient {

    private final WebDelegate DELEGATE;


    public WebViewClientImpl(WebDelegate delegate) {
        DELEGATE = delegate;
    }

    //本方法返回一个布尔值
    //返回的是false 则由WebView来处理事件 如果是true，则表示由我们自己来接管处理
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        XiaoYunLogger.d("shouldOverrideUrlLoading", url);
        //做 路由的截断和处理


        return Router.getInstance().handleWebUrl(DELEGATE, url);
    }
}
