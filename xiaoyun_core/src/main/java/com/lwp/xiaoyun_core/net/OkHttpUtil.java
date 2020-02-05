package com.lwp.xiaoyun_core.net;

import android.content.Context;
import android.os.Handler;

import com.lwp.xiaoyun_core.ui.loader.LoaderStyle;
import com.lwp.xiaoyun_core.ui.loader.XiaoYunLoader;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 5:39
 *     desc   :
 *              用例：
 *              String address = "http://www/baidu.com";
 *              HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
 *                          @Override
 *                          public void onFinish(String response){
 *                              //在这里根据返回内容执行具体的逻辑
 *                          }
 *                          @Override
 *                          public void onError(Exception e){
 *                              //在这里对异常情况进行处理
 *                          }
 *                      });
 * </pre>
 */
public class OkHttpUtil {

    private static final Handler HANDLER = new Handler();

    //Loader
    private  LoaderStyle mLoaderStyle;
    private  Context mContext;


    public static OkHttpUtil build() {
        return new OkHttpUtil();
    }

    public void sendOkHttpRequest(String address, okhttp3.Callback callback) {

        //展示 Loading！！（请求开始时）
        // 对应的 关闭的话在 RequestCallBacks 中 实现（请求结束时关闭！！）
        if (mLoaderStyle != null) {
            XiaoYunLoader.showLoading(mContext, mLoaderStyle);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();

        client.newCall(request).enqueue(callback);

        stopLoading();
    }

    //配置Loader
    public final OkHttpUtil loader(Context context) {
        mContext = context;
        mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }
    public final OkHttpUtil loader(Context context,LoaderStyle loaderStyle) {
        mContext = context;
        mLoaderStyle = loaderStyle;
        return this;
    }


    //关闭 Loader
    private void stopLoading() {
        if (mLoaderStyle != null) {
            //延迟两秒调用，用于测试
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //关闭 Loader！！
                    XiaoYunLoader.stopLoading();
                }
            },1000);
        }
    }


}
