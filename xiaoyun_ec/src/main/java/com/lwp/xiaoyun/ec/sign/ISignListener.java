package com.lwp.xiaoyun.ec.sign;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/28 16:04
 *     desc   : 在 主体 Activity中 被实现
 *              实现的实例对象 在 SignInDelegate、SignUpDelegate 中被传递给 SignHandler
 *              在 SignHandler 中被调用
 *
 * </pre>
 */
public interface ISignListener {

    //登录成功回调
    void onSignInSuccess();
    //注册成功回调
    void onSignUpSuccess();
}
