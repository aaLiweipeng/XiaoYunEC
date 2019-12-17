package com.lwp.xiaoyun_core.net;

import com.lwp.xiaoyun_core.app.ConfigType;
import com.lwp.xiaoyun_core.app.XiaoYun;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 23:04
 *     desc   : 初始化各种实例， 并提供 get方法
 * </pre>
 */
public class RestCreator {

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    private static final class RetrofitHolder {
        //这里取到了 全局应用配置数据Map中的  HOST 值！！！！赋给局部变量！
        // 这个值便 来自于 框架初始化的时候，
        // XiaoYun.init(this).withApiHost("http://127.0.0.1/").configure(); 中，.withApiHost()配置的值！！！
        private static final String BASE_URL = (String) XiaoYun.getConfigurations().get(ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()//这里是 Android简化版的 建造者模式
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create()) //Gradle处引入的 对应Retrofit的 转换器 可以返回String类型
                .build();
    }

    //这里如果想要进行一些 额外的操作，也是可以的，
    // 比如说 对OkHttp 进行一个 惰性的初始化
    private static final class OKHttpHolder {
        private static final int TIME_OUT = 60;

        //同样是 建造者模式
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS) //设置延时数值，以秒为单位
                .build();
    }

    //对 RestService 创造 一个 内部类
    // 同样完成初始化
    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

}
