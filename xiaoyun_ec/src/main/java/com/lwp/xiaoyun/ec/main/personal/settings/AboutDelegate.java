package com.lwp.xiaoyun.ec.main.personal.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/10 5:41
 *     desc   : “关于”页面
 * </pre>
 */
public class AboutDelegate extends XiaoYunDelegate {

    @BindView(R2.id.tv_info)
    AppCompatTextView mTextView = null;


    @Override
    public Object setLayout() {
        return R.layout.delegate_about;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        OkHttpUtil.create()
                .loader(getContext())
                .sendGetRequest("http://47.100.78.251/RestServer/api/about.php", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //子线程！！！
                        String jsonString = response.body().string();
                        final String info = JSON.parseObject(jsonString).getString("data");

                        XiaoYun.getHandler().post(new Runnable() {
                            //切到主线程！！！
                            @Override
                            public void run() {

                                mTextView.setText(info);
                            }
                        });
                    }
                });

//        RestClient.builder()
//                .url("about.php")
//                .loader(getContext())
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        final String info = JSON.parseObject(response).getString("data");
//                        mTextView.setText(info);
//                    }
//                })
//                .build()
//                .get();
    }
}
