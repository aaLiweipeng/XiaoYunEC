package com.lwp.xiaoyun_core.delegates.web.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.delegates.web.WebDelegate;
import com.lwp.xiaoyun_core.delegates.web.WebDelegateImpl;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/1 15:51
 *     desc   :
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

        //进行原生的跳转，
        //首先判断 当前Delegate 有没有 上层容器Delegate，
        //有则从 上层容器Delegate 进行跳转，否则在当前跳转
        final XiaoYunDelegate parentDelegate = delegate.getParentDelegate();
        final WebDelegateImpl webDelegate = WebDelegateImpl.create(url);
        if (parentDelegate == null) {
            delegate.start(webDelegate);
        } else {
            parentDelegate.start(webDelegate);
        }

        //内容拦截至此初步完成，web页面凡是有location.host或者是a标签，
        // 全部都会在 WebViewClientImpl.shouldOverrideUrlLoading中被拦截下来，
        // 然后在原生中强制进行跳转
        //----------

        return true;
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
