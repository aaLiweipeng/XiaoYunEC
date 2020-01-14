package com.lwp.xiaoyun_core.ui.refresh;

import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 2:43
 *     desc   : 封装 刷新逻辑
 *              可以在 delegate 的 onBindView中被 使用 并 初始化
 *
 *              private RefreshHandler mRefreshHandler = null;
 *              public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
 *                  mRefreshHandler = new RefreshHandler(mRefreshLayout);
 *              }
 *
 * </pre>
 */
public class RefreshHandler implements SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;

    private static final String TAG = "RefreshHandler";

    // 构造方法 接收外部传进来的 SwipeRefreshLayout实例
    // 以及 为之 设置 刷新监听器——即本类，（注意回顾 SwipeRefreshLayout的 刷新监听方法）
    // 本类继承自 OnRefreshListener，可以封装实现更多的刷新逻辑，放置到 监听回调方法 中
    public RefreshHandler(SwipeRefreshLayout swipeRefreshLayout) {
        REFRESH_LAYOUT = swipeRefreshLayout;
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

    //请求首页数据
    public void firstPage(String url) {
//        RestClient.builder()
//                .url(url)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Toast.makeText(XiaoYun.getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .build()
//                .get();
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Looper.prepare();
//                //测试成功
//                Toast.makeText(XiaoYun.getApplicationContext(), response.body().string(), Toast.LENGTH_SHORT).show();
//                Looper.loop();// 进入loop中的循环，查看消息队列
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
