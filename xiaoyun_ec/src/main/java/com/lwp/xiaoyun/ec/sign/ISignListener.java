package com.lwp.xiaoyun.ec.sign;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/28 16:04
 *     desc   :
 * </pre>
 */
public interface ISignListener {

    //登录成功回调
    void onSignInSuccess();
    //注册成功回调
    void onSignUpSuccess();
}
