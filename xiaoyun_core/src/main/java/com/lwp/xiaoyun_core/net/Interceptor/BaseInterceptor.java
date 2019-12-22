package com.lwp.xiaoyun_core.net.Interceptor;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/22 0:57
 *     desc   : 封装 拦截器从请求中 获取参数的方法；
 *
 *          模拟服务器，需要获取参数，
 *          get请求方法就要从其 URL 获取参数；
 *          post 则从 请求体获取参数
 *
 *          HashMap 是无序的 ； LinkedHashMap 有序；！！！！
 * </pre>
 */
public abstract class BaseInterceptor implements Interceptor {

    //面向get请求，获取 URL！！ 里面的参数,存进有序的LinkedHashMap中，
    // 返回这个Map
    protected LinkedHashMap<String, String> getUrlParameters(Chain chain) {

        final HttpUrl url = chain.request().url();

        //获取请求参数的个数
        int size = url.querySize();

        //把 get请求中 的 所有参数
        // 有序地！依次传入到 LinkedHashMap 中！
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {

            //·queryParameterName()是通过索引，获取Parameter的名字；
            //·queryParameterValue()是通过索引，获取Parameter的值；
            params.put(url.queryParameterName(i), url.queryParameterValue(i));
        }
        return params;
    }
    //重载，面向get请求，获取 URL！！ 里面的参数
    // 直接通过 key值 获取对应的value
    protected String getUrlParameters(Chain chain, String key) {
        final Request request = chain.request();
        return request.url().queryParameter(key);
        //·query()是获取完整的url参数，就会以 ?和& 的形式拼接过来；
        //·queryParameter()是传入一个key，返回它的value；
    }

    //面向post请求，获取 请求体！！ 里面的参数 ,存进有序的LinkedHashMap中，
    //返回这个Map
    protected LinkedHashMap<String, String> getBodyParameters(Chain chain) {
        final FormBody formBody = (FormBody) chain.request().body();
        final LinkedHashMap<String, String> params = new LinkedHashMap<>();
        int size = formBody.size();
        for (int i = 0; i < size; i++) {
            //·name()通过索引获取参数名；
            //·value()通过索引获取参数值；
            params.put(formBody.name(i), formBody.value(i));
        }
        return params;
    }

    //重载，面向post请求，获取 请求体！！ 里面的参数
    // 直接通过 key值 获取对应的value
    protected String getBodyParameters(Chain chain, String key) {
        return getBodyParameters(chain).get(key);
    }
}
