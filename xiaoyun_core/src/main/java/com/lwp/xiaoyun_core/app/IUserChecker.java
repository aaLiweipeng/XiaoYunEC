package com.lwp.xiaoyun_core.app;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/27 19:26
 *     desc   :
 * </pre>
 */
public interface IUserChecker {

    //有用户信息
    void onSignIn();
    //没有用户信息
    void onNoSignIn();
}
