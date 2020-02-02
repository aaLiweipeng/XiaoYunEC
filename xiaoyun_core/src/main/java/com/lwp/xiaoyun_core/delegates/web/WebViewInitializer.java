package com.lwp.xiaoyun_core.delegates.web;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/2 2:59
 *     desc   :
 * </pre>
 */
public class WebViewInitializer {

    //传进来一个WebView 加工之后 返回
    public WebView createView(WebView webView) {

        WebView.setWebContentsDebuggingEnabled(true);

        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //配置ua字段  用于判断是否是我们的APP打开网页
        final String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + "XiaoYun");
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        //缓存相关（默认配置）
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);


        return webView;
    }
}
