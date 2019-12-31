package com.lwp.xiaoyun_core.wechat.callbacks;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 17:44
 *     desc   : 登录成功后回调
 *              在 WXEntryTemplate 中被调用，
 *              究根则是在 BaseWXEntryActivity 中，
 *              第二次请求 获取用户的真正信息时候，请求成功时调用
 *
 *              在 SignDelegate 中实现
 * </pre>
 */
public interface IWeChatSignInCallback {
    void onSignInSuccess(String userInfo);
}
