package com.lwp.xiaoyun.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;
import com.lwp.xiaoyun_core.wechat.XiaoYunWeChat;
import com.lwp.xiaoyun_core.wechat.callbacks.IWeChatSignInCallback;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/26 1:32
 *     desc   : 登录逻辑实现
 * </pre>
 */
public class SignInDelegate extends XiaoYunDelegate {

    @BindView(R2.id.edit_sign_in_email)
    TextInputEditText mEmail = null;
    @BindView(R2.id.edit_sign_in_password)
    TextInputEditText mPassword = null;

    private ISignListener mISignListener = null;

    //注意这里是 Support Activity 中的 onAttach
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            //如果绑定的 Activity 实现了 ISignListener！！！
            mISignListener = (ISignListener) activity;
        }
    }

    @OnClick(R2.id.btn_sign_in)
    void onClickSignIn() {


        //https://mock.fulingjie.com/mock-android/data/user_profile.json
        //http://lcjxg.cn/RestServer/data/user_profile.json
        //https://news.baidu.com/   可以
        //http://127.0.0.1/index   可以

        //点击 登录按钮
        RestClient.builder()
                .url("http://lcjxg.cn/RestServer/data/user_profile.json")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        XiaoYunLogger.json("USER_PROFILE",response);
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        //登录成功逻辑 调用
                        SignHandler.onSignIn(response, mISignListener);
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

        if (checkForm()) {
        }
    }

    //微信登录的内容
    @OnClick(R2.id.icon_sign_in_wechat)
    void onClickWeChat() {
        //配置好接口，然后登录
        XiaoYunWeChat.getInstance().onSignSuccess(new IWeChatSignInCallback() {
            @Override
            public void onSignInSuccess(String userInfo) {
                Toast.makeText(getContext(), userInfo, Toast.LENGTH_LONG).show();
            }
        }).signIn();
    }

    //还没有注册的情况，跳转到 注册碎片
    @OnClick(R2.id.tv_link_sign_up)
    void onClickLink() {
        start(new SignUpDelegate());
    }

    //验证 输入信息 是否符合格式
    private boolean checkForm() {

        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;

        //验证 输入信息 是否符合格式
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式 ");
            isPass = false;
        } else {
            mEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("请填写至少6位数密码 ");
            isPass = false;
        } else {
            mPassword.setError(null);
        }
        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
