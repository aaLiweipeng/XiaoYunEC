package com.lwp.xiaoyun_core.net;

import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 18:11
 *     desc   : 使用建造者模式，构建 RestClient！！！
 *              此类是建造者模式中，Product部分
 * </pre>
 */
public class RestClient {

    //1. 思考 网络请求 一般需要 哪些参数：URL 传入的值 文件 回调 Loader（加载圈）
    //2. 建造者模式每次build的时候，都会生成一个全新的实例，
    //   里边的参数是一次构建完毕，绝不允许更改

    //URL
    private final String URL;
    //参数
    private final Map<String,Object> PARAMS;
    //回调
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    //请求体
    private final RequestBody BODY;

    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body) {
        URL = url;
        PARAMS = params;
        REQUEST = request;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = error;
        BODY = body;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }
}
