package com.lwp.xiaoyun_core.delegates;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/5 15:53
 *     desc   : 监听 web加载事件
 * </pre>
 */
public interface IPageLoadListener {

    void onLoadStart();

    void onLoadEnd();
}
