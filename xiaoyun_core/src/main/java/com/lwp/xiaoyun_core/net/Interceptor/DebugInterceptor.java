package com.lwp.xiaoyun_core.net.Interceptor;

import android.support.annotation.RawRes;

import com.lwp.xiaoyun_core.util.file.FileUtil;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/22 2:53
 *     desc   : 封装拦截器的拦截逻辑
 * </pre>
 */
public class DebugInterceptor extends BaseInterceptor {

    //模拟URL（Debug URL）
    private final String DEBUG_URL;
    //这个项目调试时， 把JSON文件 都放在 res 下 raw 文件夹中，
    // 编译器会自动为其生成 int 型的 id
    private final int DEBUG_RAW_ID;

    //构造方法，构造的时候传入 拦截的关键字段 以及其 对应要返回的json文件
    public DebugInterceptor(String debugUrl, int rawId) {
        DEBUG_URL = debugUrl;
        DEBUG_RAW_ID = rawId;
    }

    //返回 请求成功的 模拟Response
    private Response getResponse(Chain chain, String json) {
        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"),json))
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    private Response debugResponse(Chain chain, @RawRes int rawId) {
        //根据Id 获取到 封装了 对应的JSON文件 的 Response
        final String json= FileUtil.getRawFile(rawId);
        return getResponse(chain, json);
    }

    //拦截方法，拦截 请求中的URL，
    // 判断是否含有关键词，有则层层调用 返回对应的json文件
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取到 拦截到的 url
        final String url = chain.request().url().toString();
        //如果 拦截到的 url 包含了 我们需求的关键字
        if (url.contains(DEBUG_URL)) {
            //返回 模拟的 （对应的 DEBUG_URL 关键字）JSON数据 ！！！
            //URL请求中 包含了 某个关键字！！
            // 就能 得到 服务器（拦截器）对应的 某个 JSON文件！！
            // ！！！这个“对应”，！！！
            // ！调用  DebugInterceptor(String debugUrl, int rawId) 来设定！！！
            return debugResponse(chain, DEBUG_RAW_ID);
        }
        return chain.proceed(chain.request());
    }
}
