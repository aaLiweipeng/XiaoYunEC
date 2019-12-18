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
    private  static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    //回调
    private  IRequest mIRequest;
    private  ISuccess mISuccess;
    private  IFailure mIFailure;
    private  IError mIError;
    //请求体
    private  RequestBody mBody;

    //除了 RestClient（同包内的类），不允许外部 直接new 创建！！！
    //这里没 声明权限，即使用 Java 的 默认权限，
    // 即 只有 本类+同包 可以访问！！！只有 同包内的子类可以继承！！！！！
    RestClientBuilder() {
    }

    /*
        下面是本建造者模式的 一系列组装方法

        PS：
        关于 建造者模式 对产品的组装：
        1.可以 Product 类中 除了准备 成员字段，还为每一个 成员字段 准备 set方法；构造方法为空！
        在 concreteBuilder 中声明一个 Product 全局实例，在 concreteBuilder 中的每一个组装方法中，
        都调用这个 Product 全局实例 的对应的 成员字段的 set方法，来接收 传进组装方法（如下的url()/params()等） 的参数，以完成组装；
        组装方法返回值设置为 concreteBuilder （return this），即可完成 链式调用；
        最后 build 的时候，返回这个 组装好的 Product 全局实例；完毕！

        2.也可以如下，Product 类中 只是准备 成员字段，构造方法需初始化 所有字段！ 需要不可改 可以设置 final；
        在 concreteBuilder 中 也准备好同 Product 类型 一模一样的成员字段，
        在 concreteBuilder 中的每一个组装方法中，调用 concreteBuilder 自身 对应的成员字段，来接收 传进组装方法 的参数，以完成组装；
        组装方法返回值设置为 concreteBuilder （return this），即可完成 链式调用；
        最后 build 的时候，把 concreteBuilder 的所有字段（没配置的字段要初始化） 通过 new 赋给 Product 的构造函数，
        然后返回这个 接收了 concreteBuilder所有字段的 Product实例，完毕！
     */
    //觉得某个方法已经很完善了，不需要别人修改它，可以加上final，
    // 这样也可以让JVM进行优化（如，禁止重排序）
    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
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

//    //检查mParams 是否为空
//    //restful 不允许 空的Map
//    private Map<String, Object> checkParams() {
//        if (mParams == null) {
//            //键值对是可以丢失的，使用弱哈希；更精确一些管理内存；
//            // 因为请求的时候，其中存储的 用不上的 值，最好让系统 及时回收掉！！！
//           return new WeakHashMap<>();
//        }
//        return mParams;
//    }

    /**
     * 最终方法，返回 构建组装 完毕的 RestClient
     * @return
     */
    public final RestClient build() {
        return new RestClient(mUrl, PARAMS, mIRequest, mISuccess, mIFailure, mIError, mBody);
    }
}
