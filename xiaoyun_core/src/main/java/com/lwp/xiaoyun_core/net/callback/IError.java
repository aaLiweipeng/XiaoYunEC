package com.lwp.xiaoyun_core.net.callback;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/17 20:47
 *     desc   :
 * </pre>
 */
public interface IError {

    void onError(int code, String msg);
}
