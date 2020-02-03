package com.lwp.xiaoyun_core.delegates.web.event;

import android.content.Context;
import android.webkit.WebView;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.delegates.web.WebDelegate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/3 3:15
 *     desc   : 用来抽象 每一个 具体的事件，
 *
 *              把事件需要的属性 置为 成员变量
 *
 *              ！！！！
 *              这里把 每一个事件 作为 类的某一个实例 来处理
 *              ！！！
 *              不同类型的事件 则作为 继承本类的不同子类 区分开来
 *              ！！！！
 * </pre>
 */
public abstract class Event implements IEvent {
    private Context mContent = null;
    private String mAction = null;
    private WebDelegate mDelegate = null;
    private String mUrl = null;
    private WebView mWebView = null;

    public WebView getWebView() {
        return mDelegate.getWebView();
    }

    public Context getContext() {
        return mContent;
    }

    public void setContext(Context mContent) {
        this.mContent = mContent;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String mAction) {
        this.mAction = mAction;
    }

    public WebDelegate getDelegate() {
        return mDelegate;
    }

    public void setDelegate(WebDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
