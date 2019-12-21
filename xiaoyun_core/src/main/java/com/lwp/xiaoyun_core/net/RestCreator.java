package com.lwp.xiaoyun_core.net;

import com.lwp.xiaoyun_core.app.ConfigKeys;
import com.lwp.xiaoyun_core.app.XiaoYun;

import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 23:04
 *     desc   : 本类中 准备各种 静态内部类；
 *              利用静态内部类 的方式 初始化各种实例，（跟Rest请求有关的实例）并提供 getter；
 *              OKHttpHolder、RetrofitHolder、RestServiceHolder、RxRestServiceHolder
 * </pre>
 */
public class RestCreator {

    //静态内部类 初始化 PARAMS
    private static final class ParamsHolder {
        //键值对是可以丢失的，使用弱哈希(WeakHash)；更精确一些管理内存；
        // 因为请求的时候，其中存储的 用不上的 值，最好让系统 及时回收掉！！！
        // .
        //在这里已经初始化完毕！
        private static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }


    //静态内部类 初始化 Retrofit 对象
    private static final class RetrofitHolder {

        //这里取到了 全局应用配置数据Map中的  HOST 值！！！！赋给局部变量！
        // 这个值便 来自于 框架初始化的时候，
        // XiaoYun.init(this).withApiHost("http://127.0.0.1/").configure(); 中，.withApiHost()配置的值！！！
        private static final String BASE_URL = (String) XiaoYun.getConfiguration(ConfigKeys.API_HOST);
//        private static final String BASE_URL = (String) XiaoYun.getConfigurations().get(ConfigKeys.API_HOST.name()); //旧版写法

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()//这里是 Android简化版的 建造者模式
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create()) //Gradle处引入的 对应Retrofit的 转换器 可以返回String类型
                .build();
    }

    //对 RestService 创造 一个 静态内部类
    // 同样完成初始化 Retrofit 要求提供的 接口对象实例
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
    //返回 Retrofit 要求提供的 接口对象实例
    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }


    //静态内部类 初始化 OkHttpClient.Builder
    //这里如果想要进行一些 额外的操作，也是可以的，
    // 比如说 对OkHttp 进行一个 惰性的初始化
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 60;

        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS =
                XiaoYun.getConfiguration(ConfigKeys.INTERCEPTOR);

        private static OkHttpClient.Builder addInterceptor() {

            //把初始时 配置到 Configurator 中(数据Map XIAOYUN_CONFIGS)的 拦截器列表 INTERCEPTORS，
            // 依次 添加到这里的 局部变量 OkHttpClient.Builder BUILDER 中
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }
            return BUILDER;
        }

        //同样是 建造者模式
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS) //设置延时数值，以秒为单位
                .build();
    }

}
