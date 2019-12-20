package com.lwp.xiaoyun_core.net.download;

import android.os.AsyncTask;

import com.lwp.xiaoyun_core.net.RestCreator;
import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/20 11:05
 *     desc   : Restful请求 的 状态回调层级的处理
 *              以及 下载任务 SaveFileTask 的 表层调用
 *
 *              ------------------------------------------------------------
 *              除了这里的文件下载操作，其他 CRUD的请求参数，
 *              都可以统一用 RequestCallBacks 这个自定义接口，赋值给 call.enqueue！！！
 *
 *              因为 文件下载操作 请求 成功之后，要进行 特殊于 其他 CRUD 的处理，
 *              如调用 AsyncTask ，真正写入文件到 sd卡；
 *
 *              另外最直接的是，其他操作请求的返回值都是String（Callback<String>），
 *              而download操作是ResponseBody（Callback<ResponseBody>）！！！
 *              具体可以看一下 RestService 定义 ；
 *
 *
 * </pre>
 */
public class DownloadHandler {
    private final String URL;
    private final WeakHashMap<String, Object> PARAMS;
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;//指定 下载文件 在本地sd卡的 目录名
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(String url,
                           WeakHashMap<String, Object> params,
                           IRequest request,
                           String downDir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error) {
        this.URL = url;
        this.PARAMS = params;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = downDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }

    public final void handleDownload() {
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        RestCreator.getRestService().download(URL, PARAMS)
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        //请求成功

                        //执行 AsyncTask
                        final ResponseBody responseBody = response.body();
                        final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DOWNLOAD_DIR,
                                EXTENSION, responseBody, NAME);//是以线程池的形式执行；

                        //这里，下载完的结束回调的时候，一定要判断 AsyncTask 是否已经结束了
                        // 如果 AsyncTask 没有结束的时候，我们就回调结束的方法的话，
                        // 有可能会导致 文件下载不全！！！！
                        if (task.isCancelled()) {
                            //如果 AsyncTask 已经结束了，下面才能进行 结束的回调
                            if (REQUEST != null) {
                                REQUEST.onRequestEnd();
                            }
                        }
                    } else {
                        //请求失败

                        if (ERROR != null) {
                            ERROR.onError(response.code(), response.message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (FAILURE != null) {
                        FAILURE.onFailure();
                    }
                }
            });
    }
}
