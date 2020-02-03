package com.lwp.xiaoyunec.evnet;

import android.webkit.WebView;
import android.widget.Toast;

import com.lwp.xiaoyun_core.delegates.web.event.Event;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/4 1:27
 *     desc   :
 * </pre>
 */
public class TestEvent extends Event {

    @Override
    public String execute(String params) {
        Toast.makeText(getContext(), getAction(), Toast.LENGTH_LONG).show();
        if (getAction().equals("test")) {

            final WebView webView = getWebView();
            //这样 保证了 post.run() 中的代码是 运行在主线程中的
            webView.post(new Runnable() {
                @Override
                public void run() {
                    //原生调用web.JavaScript方法
                    webView.evaluateJavascript("nativeCall();",null);
                }
            });
        }
        return null;
    }
}
