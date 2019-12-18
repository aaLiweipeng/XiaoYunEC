package com.lwp.xiaoyun_core.net;

import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.net.callback.RequestCallBacks;

import java.util.Map;
import java.util.WeakHashMap;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

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
    private  static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
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
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    //封装请求操作
    private void request(HttpMethod method) {
        //获取为 Retrofit 框架 准备的 接口对象实例
        final RestService service = RestCreator.getRestService();

        //用来接请求操作
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            default:
                break;
        }

        if (call != null) {
            //选择异步请求方式
            call.enqueue(getRequestCallback());
        }
    }

    //返回一个 RequestCallBacks 实例
    private Callback<String> getRequestCallback() {
        return new RequestCallBacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR
        );
    }


}
