package com.lwp.xiaoyun_core.delegates.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lwp.xiaoyun_core.delegates.web.chromeclient.WebChromeClientImpl;
import com.lwp.xiaoyun_core.delegates.web.client.WebViewClientImpl;
import com.lwp.xiaoyun_core.delegates.web.route.RouteKeys;
import com.lwp.xiaoyun_core.delegates.web.route.Router;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/30 23:30
 *     desc   : WebDelegate 下级，
 *              即，WebDelegate 的 实现Delegate，
 *
 *              在create()的时候配置url；！！
 *
 *              在onCreate()的时候 加载这个 url 到 成员变量中(见 WebDelegate)；！！
 *              在 onBindView 中会使用 getUrl() 拿到这个 成员url，
 *              进而使用 Router中的load方法 进行 WebView的网页加载！！！
 *
 *              另外，观 setLayout()，WebDelegate/WebDelegateImpl 这个碎片的内容，
 *              其实就只是个 WebView！！
 *
 *              初始化 或者 跳转的时候，都需要创建一个新的 WebDelegate，
 *              一个WebDelegate！！ 对应 一个WebView！！对应一个Url！！
 * </pre>
 */
public class WebDelegateImpl extends WebDelegate {

    /**
     * （下列注释，区分 通过Transaction的Fragment加载 及 WebView的网页加载）
     * ！！！
     * 与 WebDelegate 的 加载渲染 （指通过 Transaction的Fragment加载）
     * 以及 Delegate中 webView的 网页加载 息息相关！！！
     * ！！！
     * 在create()的时候配置url，同时，创建一个 WebDelegateImpl（WebDelegate）
     *
     * ！！！！！！
     * 创建之后如果被加载渲染（指通过 Transaction的Fragment加载），
     *
     * 如！初始的！ DiscoverDelegate.onLazyInitView 中的 loadRootFragment()
     *
     * 则 会走 生命周期方法，即——
     * 在onCreate()的时候 加载这个 url 到 成员变量中(见 WebDelegate)
     * 在 onBindView 中会使用 getUrl() 拿到这个 成员url，
     * 进而使用 Router中的load方法 进行 WebView的网页加载！！！
     *
     * ！！！！！！
     * 或者又如 在！跳转！的时候，create被调用 得到一个WebDelegate，
     * 然后 成为一个跳转目的地Delegate 被用start 去跳转 加载渲染（指通过 Transaction的Fragment加载），
     * 如 Router.handleWebUrl 中的start()
     * 同样 会走 生命周期方法！
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

    //下面这些方法都是 new出来一个实例后，实例里边自动执行的，
    // 在 WebDelegate.onCreate()的 initWebView() 中 被使用
    @Override
    public IWebViewInitializer setInitializer() {
        //本类为WebDelegate子类，WebDelegate实现了IWebViewInitializer
        return this;
    }
    //IWebViewInitializer 的方法
    @Override
    public WebView initWebView(WebView webView) {
        //传进去一个 WebView 加工之后 返回
        return new WebViewInitializer().createWebView(webView);
    }
    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this);
        return client;
    }
    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromeClientImpl();
    }
}
