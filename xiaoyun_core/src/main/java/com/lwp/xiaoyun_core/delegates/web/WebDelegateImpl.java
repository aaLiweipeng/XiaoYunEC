package com.lwp.xiaoyun_core.delegates.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lwp.xiaoyun_core.delegates.web.chromeclient.WebChromeClienImpl;
import com.lwp.xiaoyun_core.delegates.web.client.WebViewClientImpl;
import com.lwp.xiaoyun_core.delegates.web.route.RouteKeys;
import com.lwp.xiaoyun_core.delegates.web.route.Router;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/30 23:30
 *     desc   : WebDelegate 下级，具体实现
 * </pre>
 */
public class WebDelegateImpl extends WebDelegate {

    /**
     * ！！！即，WebDelegate 的 实现Delegate，
     * 在create()的时候配置url，在onCreate()的时候 加载这个 url 到 成员变量中(见 WebDelegate)
     * 在 onBindView 中会使用 getUrl() 拿到这个 成员url，进而使用 Router中的方法 进行 加载！！！
     *
     * 传入一个url 封装成Bundle 设置进一个 WebDelegateImpl实例，返回这个实例
     * ！！！这个Bundle在onCreate() 中被get！！！详细见 WebDelegate
     *
     * @param url
     * @return
     */
    public static WebDelegateImpl create(String url) {

        final Bundle args = new Bundle();
        args.putString(RouteKeys.URL.name(), url);

        final WebDelegateImpl delegate = new WebDelegateImpl();

        //在此配置url，在onCreate()的时候 加载这个 url 到 成员变量中(见 WebDelegate)
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return getWebView();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //在刚刚打开加载好页面的时候

        if (getUrl() != null) {

            //用 原生的方式 模拟 Web跳转 并进行 页面加载
            Router.getInstance().loadPage(this, getUrl());
        }
    }

    @Override
    public IWebViewInitializer setInitializer() {
        //本类为WebDelegate子类，WebDelegate实现了IWebViewInitializer
        return this;
    }
    //IWebViewInitializer 的方法
    @Override
    public WebView initWebView(WebView webView) {
        //传进去一个 WebView 加工之后 返回
        return new WebViewInitializer().createView(webView);
    }
    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this);
        return client;
    }
    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromeClienImpl();
    }
}
