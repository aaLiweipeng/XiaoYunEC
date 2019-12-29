package com.lwp.xiaoyun_core.app;

import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/27 19:30
 *     desc   : 封装了 管理 配置 获取 判断 用户登录状态 的方法，
 *              以及
 *              根据用户是否已经登录(是否有用户信息存储在SQLite中） 执行相关回调，
 *              回调的具体实现， 通过 接口 ，留给 调用（checkAccout() ）方 负责
 * </pre>
 */
public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }
    //保存用户登录状态，
    // 登录后 成功，把 网络请求拿到的用户数据 存储在SQLite中 后 调用
    public static void setSignState(boolean state) {
        XiaoyunPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }
    //判断是不是已经登录了！
    private static boolean isSignIn() {
        return XiaoyunPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    /**
     * 在 APP启动结束的时候 调用本方法
     *
     * APP启动结束 的 定义   见 ILauncherListener 文件头注释
     *
     * @param checker 调用本方法时，用匿名内部类 实现 这个接口
     */
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
