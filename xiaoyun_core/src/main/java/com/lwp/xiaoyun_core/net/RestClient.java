package com.lwp.xiaoyun_core.net;

import android.content.Context;

import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.net.callback.RequestCallBacks;
import com.lwp.xiaoyun_core.net.download.DownloadHandler;
import com.lwp.xiaoyun_core.ui.LoaderStyle;
import com.lwp.xiaoyun_core.ui.XiaoYunLoader;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    //回调接口
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    //文件下载用 变量，注意，一般！！传入了前两个就不用传入NAME，传入NAME了就不用传入前两个
    private String DOWNLOAD_DIR;//指定 下载文件 在本地sd卡的 目录名
    private String EXTENSION;//文件后缀名
    private String NAME;//完整的文件名
    //请求体
    private final RequestBody BODY;
    //Loader
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    //文件上传
    private final File FILE;

    public RestClient(String url,
                      Map<String, Object> params,
                      String downloadDir,
                      String extension,
                      String name,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body,
                      File file,
                      Context context,
                      LoaderStyle loaderStyle) {
        this.URL = url;
        PARAMS.putAll(params);
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.FILE = file;
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
        } else {
            XiaoYunLoader.showLoading(CONTEXT);
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                call = service.postRaw(URL, BODY);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                //以 Form 的方式 提交
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(),requestBody);
                call = service.upload(URL, body);
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

    /*
           提供给外部调用的操作封装方法！！

           所有调用 RestService 获取 Call 对象实例的 调用逻辑，以及 call 的请求执行逻辑，
           都是在 RestClient 这里完成的，只有 download 的 RestService调用以及 call 的请求执行，
           是跳转到 DownloadHandler 中完成的
     */
    //增删改查，依次如下
    public final void post() {
        //BODY 或者 PARAMS 为不为空，就看 RestClientBuilder 构建时有没有配置；
        // RestClientBuilder 构建时 有配置的 字段，则不空，否则默认为空

        if (BODY == null) {
            //如果 BODY 为空，即是 一般的 PARAMS post 方式
            request(HttpMethod.POST);
        } else {
            //如果 BODY 不空，那只能是 post原始数据 BODY ！！
            if (!PARAMS.isEmpty()) {
                //如果 BODY 不空，那只能是 post原始数据 BODY，
                // 这种 post 情况的话，要求参数 PARAMS 必须为空！
                //如果参数 PARAMS 不空
                throw new RuntimeException("params must be null");
            }
            //如果参数 PARAMS 为空
            request(HttpMethod.POST_RAW);

        }
    }
    public final void delete() {
        request(HttpMethod.DELETE);
    }
    public final void put() {
        if (BODY == null) {
            //如果 BODY 为空，即是 一般的 PARAMS put 方式
            request(HttpMethod.PUT);
        } else {
            //如果 BODY 不空，那只能是 put原始数据 BODY ！！
            if (!PARAMS.isEmpty()) {
                //如果 BODY 不空，那只能是 put原始数据 BODY，
                // 这种 put 情况的话，要求参数 PARAMS 必须为空！
                //如果参数 PARAMS 不空
                throw new RuntimeException("params must be null");
            }
            //如果参数 PARAMS 为空
            request(HttpMethod.PUT_RAW);
        }
    }
    public final void get() {
        request(HttpMethod.GET);
    }

    //文件上传
    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    //文件下载
    public final void download() {
        new DownloadHandler(URL, PARAMS,REQUEST, DOWNLOAD_DIR, EXTENSION, NAME,
                SUCCESS, FAILURE, ERROR)
                .handleDownload();
    }
}
