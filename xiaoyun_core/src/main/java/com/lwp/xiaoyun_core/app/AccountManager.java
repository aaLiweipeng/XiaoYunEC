package com.lwp.xiaoyun_core.app;

import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/27 19:30
 *     desc   : 管理用户登录状态， 根据用户登录状态 执行相关回调
 * </pre>
 */
public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }
    //保存用户登录状态，登录后调用
    public static void setSignState(boolean state) {
        XiaoyunPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }
    //判断是不是已经登录了！
    private static boolean isSignIn() {
        return XiaoyunPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccout(IUserChecker checker) {
        if (isSignIn()) {
            //如果已经登录了， 执行登录的回调
            checker.onSignIn();
        } else {
            //执行 没有登录的回调
            checker.onNoSignIn();
        }
    }
}
