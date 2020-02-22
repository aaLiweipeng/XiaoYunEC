package com.lwp.xiaoyun.ec.main.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ec.main.personal.list.ListAdapter;
import com.lwp.xiaoyun.ec.main.personal.list.ListBean;
import com.lwp.xiaoyun.ec.main.personal.list.ListItemType;
import com.lwp.xiaoyun.ec.main.personal.order.OrderListDelegate;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/18 13:35
 *     desc   :
 * </pre>
 */
public class PersonalDelegate extends BottomItemDelegate {


    //用作 传递给 OrderListDelegate 的 Bundle的 key
    public static final String ORDER_TYPE = "ORDER_TYPE";
    private Bundle mArgs = null;

    @BindView(R2.id.rv_personal_setting)
    RecyclerView mRvSettings = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    /**
     * 当点击了 “全部订单”字样，配置Bundle 然后 启动OrderListDelegate
     *
     * ORDER_TYPE 是为了根据不同的Type 来判断、获取 不同的OrderListDelegate页面
     * 在 OrderListDelegate的 onCreate()中加载获取赋值，在onLazyInitView()用于网络请求,
     * 达到不同Type启动不同内容的目的
     */
    @OnClick(R2.id.tv_all_order)
    void onClickAllOrder() {
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }
    // 用于启动打开 OrderListDelegate 页面
    // 根据不同的type 来打开不同的 OrderListDelegate
    private void startOrderListByType() {
        final OrderListDelegate delegate = new OrderListDelegate();
        delegate.setArguments(mArgs);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化Bundle
        mArgs = new Bundle();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

        //一般这些数据都是写死的
        final ListBean address = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setText("收货地址")
                .build();

        final ListBean system = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setText("系统设置")
                .build();

        final List<ListBean> data = new ArrayList<>();
        data.add(address);
        data.add(system);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvSettings.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRvSettings.setAdapter(adapter);

    }
}
