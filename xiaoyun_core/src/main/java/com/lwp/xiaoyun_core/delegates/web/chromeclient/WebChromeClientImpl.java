package com.lwp.xiaoyun_core.delegates.web.chromeclient;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/2 3:51
 *     desc   : 关于WebView内部的一些处理和控制
 *              如下面的 拦截web页面的 Alert 并进行自己的处理，比如 弹出我们自己的对话框
 * </pre>
 */
public class WebChromeClientImpl extends WebChromeClient {

    //拦截web页面的 Alert 并进行自己的处理，比如 弹出我们自己的对话框
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }
}
