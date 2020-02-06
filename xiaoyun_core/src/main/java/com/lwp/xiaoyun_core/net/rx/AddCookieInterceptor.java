package com.lwp.xiaoyun_core.net.rx;

import android.support.annotation.NonNull;

import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/5 20:40
 *     desc   :
 * </pre>
 */
public final class AddCookieInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        //拦截 原始请求
        final Request.Builder builder = chain.request().newBuilder();
        Observable
                .just(XiaoyunPreference.getCustomAppProfile("cookie"))//在WebViewClientImpl中从WebView取出 并放入SP文件，持久化
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String cookie) throws Exception {
                        //给原生API请求 附带上 WebView里面拦截下来的Cookie
                        builder.addHeader("Cookie", cookie);//完成注入
                    }
                });

        return chain.proceed(builder.build());
    }
}
