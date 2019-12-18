package com.lwp.xiaoyun_core.net.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/18 17:34
 *     desc   : 提供给RestClient 中的 Call.enqueue()
 *              封装，请求成功时，请求失败时，需要做的事情！
 * </pre>
 */
public class RequestCallBacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public RequestCallBacks(IRequest request, ISuccess success, IFailure failure, IError error) {
        REQUEST = request;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = error;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            //如果请求成功
            if (call.isExecuted()) {
                //如果 RestClient 中的 Call 已经执行了
                //严谨地多一层判空之后，调用onSuccess方法
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure();
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
    }
}
