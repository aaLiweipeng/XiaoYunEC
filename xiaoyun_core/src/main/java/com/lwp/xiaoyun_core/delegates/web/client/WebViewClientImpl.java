package com.lwp.xiaoyun_core.delegates.web.client;

import android.graphics.Bitmap;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lwp.xiaoyun_core.app.ConfigKeys;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.IPageLoadListener;
import com.lwp.xiaoyun_core.delegates.web.WebDelegate;
import com.lwp.xiaoyun_core.delegates.web.route.Router;
import com.lwp.xiaoyun_core.ui.loader.LoaderStyle;
import com.lwp.xiaoyun_core.ui.loader.XiaoYunLoader;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;
import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;


import retrofit2.http.DELETE;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/1 15:43
 *     desc   : 针对于浏览器本身行为的一个控制
 *              如下面的 路由的截断和处理
 * </pre>
 */
public class WebViewClientImpl extends WebViewClient {

    private final WebDelegate DELEGATE;
    private IPageLoadListener mIPageLoadListener = null;

    private static final Handler HANDLER = XiaoYun.getHandler();

    public void setIPageLoadListener(IPageLoadListener listener) {
        mIPageLoadListener = listener;
    }

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

    //页面打开的一瞬间
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        //执行回调
        if (mIPageLoadListener != null) {
            mIPageLoadListener.onLoadStart();
        }
        //showLoader
        XiaoYunLoader.showLoading(view.getContext(), LoaderStyle.LineScalePartyIndicator);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        //同步cookie用
        syncCookie();

        if (mIPageLoadListener != null) {
            mIPageLoadListener.onLoadEnd();
        }
        //stopLoader
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                XiaoYunLoader.stopLoading();
            }
        },1000);
    }

    //获取浏览器cookie
    private void syncCookie() {
        //注意 WebViewInitializer 对应代码
        final CookieManager manager = CookieManager.getInstance();
        /*
          注意，这里的Cookie和API请求的Cookie是不一样的，这个在网页不可见
         */
        final String webHost = XiaoYun.getConfiguration(ConfigKeys.WEB_HOST);
        if (webHost != null) {//如果有配置了 WebHost
            if (manager.hasCookies()) { //如果 存在cookie
                final String cookieStr = manager.getCookie(webHost);//通过manager 把 网页的cookie 取出来
                if (cookieStr != null && !cookieStr.equals("")) {//网页的cookie 非空非null
                    //把网页的cookie 加进SP文件，在 AddCookieInterceptor 中 拦截 从SP文件取出 并同步
                    XiaoyunPreference.addCustomAppProfile("cookie",cookieStr);
                }
            }
        }
    }
}
