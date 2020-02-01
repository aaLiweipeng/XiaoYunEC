package com.lwp.xiaoyun_core.delegates.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/28 8:55
 *     desc   : 用于 初始化WebView的接口
 * </pre>
 */
public interface IWebViewInitializer {

    //初始化 WebView
    WebView initWebView(WebView webView);

    // 初始化 WebViewClient
    // WebViewClient 是针对于浏览器本身行为的一个控制
    WebViewClient initWebViewClient();

    // 初始化 WebChromeClient
    // WebChromeClient 是针对于内部页面的一个控制
    WebChromeClient initWebChromeClient();
}
