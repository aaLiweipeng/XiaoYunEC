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
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.IError;
import com.lwp.xiaoyun_core.net.callback.IFailure;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/24 20:28
 *     desc   : 注册逻辑实现
 *              点击注册按钮时，验证注册信息，post提交注册信息
 * </pre>
 */
public class SignUpDelegate extends XiaoYunDelegate {

    @BindView(R2.id.edit_sign_up_name)
    TextInputEditText mName = null;
    @BindView(R2.id.edit_sign_up_email)
    TextInputEditText mEmail = null;
    @BindView(R2.id.edit_sign_up_phone)
    TextInputEditText mPhone = null;
    @BindView(R2.id.edit_sign_up_password)
    TextInputEditText mPassword = null;
    @BindView(R2.id.edit_sign_up_re_password)
    TextInputEditText mRePassword = null;

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

    //http://mock.fulingjie.com/mock-android/data/user_profile.json
    //http://lcjxg.cn/RestServer/data/user_profile.json
    //https://news.baidu.com/   可以

    @OnClick(R2.id.btn_sign_up)
    void onClickSignUp() {
        //点击 注册按钮

        RestClient.builder()
                .url("http://mock.fulingjie.com/mock-android/data/user_profile.json")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        XiaoYunLogger.json("USER_PROFILE",response);
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        //注册逻辑调用
                        SignHandler.onSignUp(response, mISignListener);
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
            //如果 用户输入的注册信息没问题

//            RestClient.builder()
//                    .url("http://lcjxg.cn/RestServer/data/user_profile.json")
//                    .loader(getContext())
//                    .params("name",mName.getText().toString())
//                    .params("email",mEmail.getText().toString())
//                    .params("phone",mPhone.getText().toString())
//                    .params("password",mPassword.getText().toString())
//                    .success(new ISuccess() {
//                        @Override
//                        public void onSuccess(String response) {
//                            XiaoYunLogger.json("USER_PROFILE",response);
//                        }
//                    })
//                    .build()
//                    .post();




            Toast.makeText(getContext(), "验证通过", Toast.LENGTH_LONG).show();
        }

    }

    //点击Link 跳转到 登录碎片
    @OnClick(R2.id.tv_link_sign_in)
    void onClickLink() {
        start(new SignInDelegate());
    }

    //验证 输入信息 是否符合格式
    private boolean checkForm() {
        final String name = mName.getText().toString();
        final String email = mEmail.getText().toString();
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        final String rePassword = mRePassword.getText().toString();

        boolean isPass = true;

        //验证 输入信息 是否符合格式
        if (name.isEmpty()) {
            mName.setError("请输入姓名~ ");
            isPass = false;
        } else {
            mName.setError(null);
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式 ");
            isPass = false;
        } else {
            mEmail.setError(null);
        }
        if (phone.isEmpty() || phone.length() != 11) {
            mPhone.setError("手机号码错误");
            isPass = false;
        } else {
            mPhone.setError(null);
        }
        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("请填写至少6位数密码 ");
            isPass = false;
        } else {
            mPassword.setError(null);
        }
        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            mRePassword.setError("密码验证错误 ");
            isPass = false;
        } else {
            mRePassword.setError(null);
        }
        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
