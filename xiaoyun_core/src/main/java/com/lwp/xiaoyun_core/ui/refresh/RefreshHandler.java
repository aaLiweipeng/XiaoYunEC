package com.lwp.xiaoyun_core.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;

import com.lwp.xiaoyun_core.app.XiaoYun;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 2:43
 *     desc   :
 * </pre>
 */
public class RefreshHandler implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;

    // 构造方法 接收外部传进来的 SwipeRefreshLayout实例
    // 以及 为之 设置 刷新监听器——即本类，（注意回顾 SwipeRefreshLayout的 刷新监听方法）
    // 本类继承自 OnRefreshListener，可以封装实现更多的刷新逻辑，放置到 监听回调方法 中
    public RefreshHandler(SwipeRefreshLayout refresh_layout) {
        REFRESH_LAYOUT = refresh_layout;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    //测试 封装固有的 刷新前准备套路
    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        XiaoYun.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新处理——如 进行一些网络请求

                //处理完毕，停止刷新动画，可以放在网络请求的Success方法中
                REFRESH_LAYOUT.setRefreshing(false);
            }
        }, 2000);//两秒后消失
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
