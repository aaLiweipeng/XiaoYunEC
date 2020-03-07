package com.lwp.xiaoyun.ec.main.personal.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ec.main.personal.PersonalDelegate;
import com.lwp.xiaoyun.ec.main.sort.content.SectionDataConverter;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/19 0:54
 *     desc   : 订单状态（待付款、待收货等）的页面
 * </pre>
 */
public class OrderListDelegate extends XiaoYunDelegate {

    /**
     * mType 是为了根据不同的Type 来判断、获取 不同的OrderListDelegate页面
     * 在onCreate()中加载获取赋值，在onLazyInitView()用于网络请求,
     * 达到不同Type启动不同内容的目的
     */
    private String mType = null;

    private List<MultipleItemEntity> data = new ArrayList<>();
    private OrderListAdapter mAdapter = new OrderListAdapter(data);

    @BindView(R2.id.rv_order_list)
    RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        mType = args.getString(PersonalDelegate.ORDER_TYPE);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

//        "http://lcjxg.cn/RestServer/api/order_list.php?type=" + mType，
        OkHttpUtil.create()
                .sendGetRequest(
                        "http://47.100.78.251/RestServer/api/order_list.php" ,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(getContext(), "没有对应页面数据或者网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                //子线程！！！

                                //根据不同的 Type，请求 同一接口模板 而不同数据源的 API接口
                                // 根据获取的JSON 来加载RecyclerView
                                data = new OrderListDataConverter().setJsonData(response.body().string()).convert();

                                XiaoYun.getHandler().post(new Runnable() {
                                    //切到主线程！！！
                                    @Override
                                    public void run() {
                                        mAdapter.setNewData(data);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });

//                          mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));
                            }
                        });

//        RestClient.builder()
//                .loader(getContext())
//                .url("http://lcjxg.cn/RestServer/api/order_list.php")
//                .params("type", mType)//根据不同的 Type，请求 同一接口模板 而不同数据源的 API接口
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//
//                        //根据不同的 Type，请求 同一接口模板 而不同数据源的 API接口
//                        // 根据获取的JSON 来加载RecyclerView
//                        data = new OrderListDataConverter().setJsonData(response).convert();
//                        mAdapter.setNewData(data);
//                        mAdapter.notifyDataSetChanged();
//
////                        mRecyclerView.addOnItemTouchListener(new OrderListClickListener(OrderListDelegate.this));
//                    }
//                })
//                .build()
//                .get();
    }
}
