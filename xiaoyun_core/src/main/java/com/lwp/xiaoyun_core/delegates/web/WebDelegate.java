package com.lwp.xiaoyun_core.delegates.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.delegates.web.route.RouteKeys;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/28 8:33
 *     desc   : 承载Web页面的 基础核心
 *
 *              这里使用 软引用/弱引用 来引用WebView，因为它内存比较敏感
 * </pre>
 */
public abstract class WebDelegate extends XiaoYunDelegate implements IWebViewInitializer {

    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String mUrl = null;
    //在get WebView的时候会进行一个判断，如果WebView没有初始化完成，
    // 我们就get不了这个WebView，这样保证我们的WebView一定是可用的。
    // 这种架构参考自 SDK25 中的WebFragment
    private boolean mIsWebViewAvailable = false;

    public WebDelegate() {
    }

    //抽象方法，强制子类需要配置这个
    // 用于 初始化WebDelegate的接口，实现时 返回一个 IWebViewInitializer实例 即可
    public abstract IWebViewInitializer setInitializer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        mUrl = args != null ? args.getString(RouteKeys.URL.name()) : null;

        initWebView();

    }

    //用于 初始化WebView
    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        //需要做判断，避免 WebView重复初始化 和 内存泄漏
        if (mWebView != null) {
            //清楚所有的View 之后 销毁
            mWebView.removeAllViews();
            mWebView.destroy();
        } else {

            //如果 没有初始化WebView过
            final IWebViewInitializer initializer = setInitializer();
            if (initializer != null) {
                //弱引用处理
                final WeakReference<WebView> webViewWeakReference =
                        new WeakReference<>(new WebView(getContext()), WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();

                //具体初始化留给子类实现
                mWebView = initializer.initWebView(mWebView);
                mWebView.setWebViewClient(initializer.initWebViewClient());
                mWebView.setWebChromeClient(initializer.initWebChromeClient());

                //添加JavaScript的接口，用于WebView跟原生进行交互
                mWebView.addJavascriptInterface(XiaoYunWebInterface.create(this), "xiaoyun");
                mIsWebViewAvailable = true;
            } else {
                //如果初始器——initializer为null
                throw new NullPointerException("Initializer is null!!!!!!");
            }
        }
    }

    //获得 WebView实例
    public WebView getWebView() {
        if (mWebView == null) {
            throw new NullPointerException("WebView is null!!!!!!!!!");
        }
        return mIsWebViewAvailable ? mWebView : null;
    }

    public String getUrl() {
        if (mUrl == null) {
            throw new NullPointerException("mUrl is null!!!!!!!!!");
        }
        return mUrl;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsWebViewAvailable = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
