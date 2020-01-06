package com.lwp.xiaoyun_core.net;

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

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(address)
                .build();

        client.newCall(request).enqueue(callback);
    }


}
