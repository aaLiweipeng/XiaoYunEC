package com.lwp.xiaoyun_core.net;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 20:19
 *     desc   : Retrofit 要求准备的接口，
 *              提供一系列的方法，用来具体的请求
 * </pre>
 */
public interface RestService {

    //1.因为这里做的是通用框架，没有针对某个具体的业务，
    // 所以这里GET注解后面 没有用括号补充 具体的路由，应用时 传入什么 就执行什么
    //2.这里第二个参数写的是@QueryMap ，使得在get请求里，
    // 会把@QueryMap中 键值对形式的参数 自动拼接 到URL中
    //3. Value 位 设置为Object ，可以传入任意类型的数据
    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    //Retrofit 默认的下载方式是把 文件 一次性下载到内存里，
    // 当下载完毕之后  再统一地写到 文件系统 里
    // 这种方式存在很大的问题，当文件过大的时候，会导致内存溢出！！！
    // .
    //故而加入 @Streaming  注解，更改下载方式为——边下载文件 边将之写入 文件系统，
    // 应用的时候 需要把 文件的写入操作 单独放在一个线程里，异步处理
    // 这样就避免了 一次性在内存中写入过大的文件 导致内存溢出 造成APP崩溃、报错
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

}
