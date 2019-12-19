package com.lwp.xiaoyun_core.net;

import android.content.Context;

import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.net.callback.RequestCallBacks;
import com.lwp.xiaoyun_core.ui.LoaderStyle;
import com.lwp.xiaoyun_core.ui.XiaoYunLoader;

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

    //Loader
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;

    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      Context context,
                      LoaderStyle loaderStyle) {
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    //封装请求操作
    private void request(HttpMethod method) {
        //获取为 Retrofit 框架 准备的 接口对象实例！！！
        final RestService service = RestCreator.getRestService();

        //用来接请求操作
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        //展示 Loading！！（请求开始时）
        // 对应的 关闭的话在 RequestCallBacks 中 实现（请求结束时关闭！！）
        if (LOADER_STYLE != null) {
            XiaoYunLoader.showLoading(CONTEXT, LOADER_STYLE);
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
            //如果 call不空，说明 需要进行请求的操作和参数 已经设定完毕
            //这里是选enqueue() 进行 异步请求方式，执行请求
            call.enqueue(getRequestCallback());
        }
    }

    //返回一个 RequestCallBacks 实例
    private Callback<String> getRequestCallback() {
        return new RequestCallBacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }

    //增删改查，依次如下
    public final void post() {
        request(HttpMethod.POST);
    }
    public final void delete() {
        request(HttpMethod.DELETE);
    }
    public final void put() {
        request(HttpMethod.PUT);
    }
    public final void get() {
        request(HttpMethod.GET);
    }
}
