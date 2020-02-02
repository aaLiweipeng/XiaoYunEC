package com.lwp.xiaoyun_core.delegates.web.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.delegates.web.WebDelegate;
import com.lwp.xiaoyun_core.delegates.web.WebDelegateImpl;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/1 15:51
 *     desc   : 封装
 *              路由的截断和处理 逻辑
 *              页面加载、跳转 逻辑
 *
 * </pre>
 */
public class Router {

    //静态内部类 线程安全 单例
    private Router() {
    }
    private static class Holder {
        private static final Router INSTANCE = new Router();
    }
    public static Router getInstance() {
        return Holder.INSTANCE;
    }

    //用于处理url的方法——
    //这个布尔值 用于配置 WebViewClientImpl 的 shouldOverrideUrlLoading，
    // 返回的是false 则由WebView来处理事件
    // 如果是true，则表示由我们自己来接管处理
    public final boolean handleWebUrl(WebDelegate delegate, String url) {

        //如果JavaScript中有电话链接，也就是包含了tel协议
        //如果是电话协议
        if (url.contains("tel:")) {
            callPhone(delegate.getContext(), url);
            return true;
        }

        //！！！！跳转！！进行原生的跳转！！！！
        //
        // 根据 getTopDelegate() 逻辑，
        // （有指定则基于指定Delegate跳转，没指定则基于WebDelegate跳转）
        //
        // 如果不是null，
        // 即我们在create WebDelegateImpl 之后有指定(setTopDelegate)了一个 TopDelegate 给它，
        // 如 DiscoverDelegate.onLazyInitView 中delegate.setTopDelegate()，
        // 则基于这个指定了的 TopDelegate 进行跳转（start）；
        //
        // 如果为null，
        // 则使用时没有指定 TopDelegate，
        // 则 基于 WebDelegate 本身进行跳转（WebDelegate的内容就只是一个WebView）
        //
        // 以上跳转，指在 WebDelegate 或者 指定的TopDelegate 这个FrameLayout的位置，
        // 重新整一个WebDelegate，加载替换上去

        //下面拿到的是DiscoverDelegate.onLazyInitView 中指定好的 delegate.setTopDelegate()，！！！
        // 下面的 delegate，是未跳转时的 WebDelegate实例！！！
        final XiaoYunDelegate topDelegate = delegate.getTopDelegate();
        // 下面这个 ！！url参数 ！！
        // 乃是来自 WebViewClientImpl 的回调方法 shouldOverrideUrlLoading，
        // 即 要跳转过去的 url ，即web页面的事件如 a标签 对应的 url，
        // 这里再创建另外一个新的 WebDelegate 实例 ，这个实例没有setTopDelegate！！
        // 这里是基于上一个WebDelegate的getTopDelegate 进行跳转！！！
        final WebDelegateImpl webDelegate = WebDelegateImpl.create(url);
        //基于 topDelegate跳转（加载替换）WebDelegate
        topDelegate.start(webDelegate);


//        //首先判断 当前Delegate 有没有 上层容器Delegate，
//        //有则从 上层容器Delegate 进行跳转，否则在当前跳转
//        final XiaoYunDelegate parentDelegate = delegate.getParentDelegate();
//        //！！！！！！
//        // 下面这个 ！！url参数 ！！
//        // 乃是来自 WebViewClientImpl 的回调方法 shouldOverrideUrlLoading，
//        // 即
//        final WebDelegateImpl webDelegate = WebDelegateImpl.create(url);
//        if (parentDelegate == null) {
//            delegate.start(webDelegate);
//        } else {
//            parentDelegate.start(webDelegate);
//        }

        //！！！内容拦截至此初步完成，web页面凡是有location.host或者是 a标签，！！！
        // 全部都会在 WebViewClientImpl.shouldOverrideUrlLoading中被拦截下来，
        // 然后在原生中强制进行跳转
        //----------

        return true;
    }

    //渲染页面 的方法
    private void loadWebPage(WebView webView, String url) {
        if (webView != null) {
            //webView 页面渲染
            webView.loadUrl(url);
        } else {
            throw new NullPointerException("WebView is Null ！！！");
        }
    }
    //在项目的assets包下写的 js、html、样式，都会以！本地页面！的形式来进行 渲染
    private void loadLocalPage(WebView webView, String url) {
        //手动加个文件头 再渲染
        loadWebPage(webView, "file:///android_asset/" + url);
    }

    private void loadPage(WebView webView, String url) {
        if (URLUtil.isNetworkUrl(url) || URLUtil.isAssetUrl(url)) {
            //是网络链接 或者 包含了asset文件头
            loadWebPage(webView, url);
        } else {
            loadLocalPage(webView, url);
        }
    }
    public final void loadPage(WebDelegate delegate, String url) {
        loadPage(delegate.getWebView(), url);
    }

    //处理有 电话协议 的 url
    private void callPhone(Context context, String uri) {
        //打电话有两种方式
        // 一种是不经过用户同意 直接拨打，（比较耍流氓）
        // 一种是跳转到系统的拨号Activity让用户决定是否要拨打（这里只用这一种）
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        final Uri data = Uri.parse(uri);
        intent.setData(data);
        ContextCompat.startActivity(context, intent, null);
    }

}
