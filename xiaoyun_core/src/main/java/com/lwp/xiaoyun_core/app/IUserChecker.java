package com.lwp.xiaoyun_core.app;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/27 19:26
 *     desc   : 用户是否登录成功接口
 *              具体执行哪一个方法，见 AccountManager.checkAccout()
 * </pre>
 */
public interface IUserChecker {

    //APP数据库中 有用户信息，即登录成功
    void onSignIn();
    //APP数据库中 没有用户信息，即登录不成功
    void onNoSignIn();
}
