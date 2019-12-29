package com.lwp.xiaoyunec;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 11:03
 *     desc   :
 * </pre>
 */
public class ExampleDelegate extends XiaoYunDelegate {

    // 在 onCreateView() 中，用来绑定根视图！！！
    @Override
    public Object setLayout() {
        //传入布局
        return R.layout.delegate_example;
    }

    //在 onCreateView() 中，绑定根视图完成后调用！！！
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
//        testRestClient();
    }



    //http://mock.fulingjie.com/mock-android/data/user_profile.json
    //http://lcjxg.cn/RestServer/data/user_profile.json
    //https://news.baidu.com   可以
    //http://127.0.0.1/index   可以

    private void testRestClient() {
        Toast.makeText(getContext(), "开始testRestClient()", Toast.LENGTH_LONG).show();

        //测试连缀调用 构建 RestClient
        //构建完毕之后调用get()方法，后续程序就会 进到 RestClient 中的 request()，
        // 通过 switch 初始化了 call 之后，用 enqueue() 异步执行请求
        RestClient.builder()
                .url("http://lcjxg.cn/RestServer/data/user_profile.json")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //测试 RestClient，成功之后把 网页请求内容 用 Toast 弹出来
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();

    }
}
