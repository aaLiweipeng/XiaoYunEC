package com.lwp.xiaoyun_core.net;

import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/17 20:38
 *     desc   : 使用建造者模式，构建 RestClient！！！
 *              此类是建造者模式中，concreteBuilder部分
 * </pre>
 */
public class RestClientBuilder {
    //URL
    private  String mUrl;
    //参数
    private  Map<String,Object> mParams;
    //回调
    private  IRequest mIRequest;
    private  ISuccess mISuccess;
    private  IFailure mIFailure;
    private  IError mIError;
    //请求体
    private  RequestBody mBody;

    //除了 RestClient ，不允许外部 直接new 创建
    RestClientBuilder() {
    }

    /*
        下面是一系列组装方法
     */
    //觉得某个方法已经很完善了，不需要别人修改它，可以加上final，
    // 这样也可以让JVM进行优化（如，禁止重排序）
    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(Map<String, Object> params) {
        this.mParams = params;
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        if (mParams == null) {
            //键值对是可以丢失的，使用弱哈希；更精确一些管理内存；
            // 因为请求的时候，其中存储的 用不上的 值，最好让系统 及时回收掉！！！
            mParams = new WeakHashMap<>();
        }
        this.mParams.put(key, value);
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    //不用写 set什么什么 ，直接简单明了写个success、写个raw、写个params，
    // 中心突出，简洁明了
    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    //检查mParams 是否为空
    //restful 不允许 空的Map
    private Map<String, Object> checkParams() {
        if (mParams == null) {
            //键值对是可以丢失的，使用弱哈希；更精确一些管理内存；
            // 因为请求的时候，其中存储的 用不上的 值，最好让系统 及时回收掉！！！
           return new WeakHashMap<>();
        }
        return mParams;
    }

    /**
     * 最终方法，返回 构建组装 完毕的 RestClient
     * @return
     */
    public final RestClient build() {
        return new RestClient(mUrl, mParams, mIRequest, mISuccess, mIFailure, mIError, mBody);
    }
}
