package com.lwp.xiaoyun_core.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 7:24
 *     desc   :
 * </pre>
 */
public abstract class BaseWXEntryActivity extends BaseWXActivity {

    //用户登录成功后 回调，往下看， 在第二次向微信平台请求成功后调用这个方法，
    // 这个方法在 WXEntryTemplate 中 覆盖实现
    protected abstract void onSignInSuccess(String userInfo);

    //微信 发送请求到 第三方应用 后的回调
    @Override
    public void onReq(BaseReq baseReq) {

    }

    //第三方应用 发送请求到 微信 后的回调
    //即在 XiaoYunWeChat 中发送的请求 signIn()
    @Override
    public void onResp(BaseResp baseResp) {

        final String code = ((SendAuth.Resp) baseResp).code;
        final StringBuilder authUrl = new StringBuilder();
        //拿到code 之后进行第一次请求 授权相关； 来自微信官方文档！！
        authUrl
                .append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")//微信文档规定
                .append(XiaoYunWeChat.APP_ID)
                .append("&secret=")
                .append(XiaoYunWeChat.APP_SECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");

        XiaoYunLogger.d("authUrl", authUrl.toString());
        getAuth(authUrl.toString());
    }

    private void getAuth(String authUrl) {
        RestClient
                .builder()
                .url(authUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        //第一次请求成功 拿到 accessToken 和 openId
                        final JSONObject authObj = JSON.parseObject(response);
                        final String accessToken = authObj.getString("access_token");
                        final String openId = authObj.getString("openid");

                        //第二次请求 获取用户的真正信息 如昵称、头像、地理位置等
                        final StringBuilder userInfoUrl = new StringBuilder();
                        userInfoUrl
                                .append("https://api.weixin.qq.com/sns/userinfo?access_token=")
                                .append(accessToken)
                                .append("&openid=")
                                .append(openId)
                                .append("&lang=")
                                .append("zh_CN");//把信息 以中文的形式返回

                        XiaoYunLogger.d("userInfoUrl", userInfoUrl.toString());
                        getUserInfo(userInfoUrl.toString());

                    }
                })
                .build()
                .get();
    }

    //第二次请求 获取用户的真正信息 如昵称、头像、地理位置等
    private void getUserInfo(String userInfoUrl) {
        RestClient
                .builder()
                .url(userInfoUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        onSignInSuccess(response);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

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
