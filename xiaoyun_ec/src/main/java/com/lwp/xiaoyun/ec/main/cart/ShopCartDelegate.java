package com.lwp.xiaoyun.ec.main.cart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ec.main.sort.content.SectionDataConverter;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/7 16:33
 *     desc   :
 * </pre>
 */
public class ShopCartDelegate extends BottomItemDelegate {

    private List<MultipleItemEntity> mData = new ArrayList<>();
    private ShopCartAdapter mAdapter = null;
    //购物车数量标记
    // 当前 被选中的（要删除的）Item （的对勾Icon）的数量   列表的Item总数
    private int mCurrentCount = 0;//
    private int mTotalCount = 0;//
    private double mTotalPrice = 0.00;

    @BindView(R2.id.rv_shop_cart)
    RecyclerView mRecyclerView = null;
    //全选图标
    @BindView(R2.id.icon_shop_cart_select_all)
    IconTextView mIconSelectAll = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

        //取消默认的刷新动画，解决RecyclerView的 刷新闪烁问题
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mAdapter = new ShopCartAdapter(mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        //注意这里要对 全选图标的tag 初始化！！初始默认全选图标未选中
        // （不然点击事件直接getTag为空指针报错！）
        mIconSelectAll.setTag(0);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        OkHttpUtil.build()
                .sendOkHttpRequest(
                        "http://lcjxg.cn/RestServer/api/shop_cart.php",
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(getContext(), "没有对应页面数据或者网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //更新 数据
                                final String jsonString = response.body().string();
                                successHandler(jsonString);
                            }
                        });

//        RestClient.builder()
//                .url("shop_cart.php")
//                .loader(getContext())
//                .success(this)
//                .build()
//                .get();
    }

    private void successHandler(final String responseString) {
        //子线程
        mData = new ShopCartDataConverter()
                .setJsonData(responseString)
                .convert();


        XiaoYun.getHandler().post(new Runnable() {
            @Override
            public void run() {
                //主线程

//                Toast.makeText(getContext(), responseString, Toast.LENGTH_SHORT).show();

                //设置 新数据
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    //    @Override
//    public void onSuccess(String response) {
//        final ArrayList<MultipleItemEntity> data =
//                new ShopCartDataConverter()
//                        .setJsonData(response)
//                        .convert();
//        mAdapter = new ShopCartAdapter(data);
//        mAdapter.setCartItemListener(this);
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setAdapter(mAdapter);
//        mTotalPrice = mAdapter.getTotalPrice();
//        mTvTotalPrice.setText(String.valueOf(mTotalPrice));
//        checkItemCount();
//    }

    //全选图标的点击事件
    @OnClick(R2.id.icon_shop_cart_select_all)
    void onClickSelectAll() {

        final int tag = (int) mIconSelectAll.getTag();
        if (tag == 0) {
            //点击时，全选图标为 未选中状态，则点击后改为 选中状态
            mIconSelectAll.setTextColor(
                    ContextCompat.getColor(getContext(), R.color.app_main));//变色
            mIconSelectAll.setTag(1);//用于 全选图标 点击事件判断
            mAdapter.setIsSelectedAll(true);//用于控制Item
            //更新RecyclerView的 显示状态！！！onBindViewHolder--即adapter的convert()会再次被回调
            mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());
        } else {
            //点击时，全选图标为 选中状态，则点击后 改为 未选中状态
            mIconSelectAll.setTextColor(Color.GRAY);//变色
            mIconSelectAll.setTag(0);
            mAdapter.setIsSelectedAll(false);
            //更新RecyclerView的 显示状态！！！
            mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());
        }
    }

    //删除事件
    @OnClick(R2.id.tv_top_shop_cart_remove_selected)
    void onClickRemoveSelectedItem() {

        final int size = mAdapter.getItemCount();
        for (int i = size - 1; i >= 0; i--) {
            MultipleItemEntity item = mAdapter.getItem(i);
            if (item.getField(ShopCartItemFields.IS_SELECTED)) {
                mAdapter.remove(i);
            }
        }

//        //取到 Adapter中的所有数据
//        final List<MultipleItemEntity> data = mAdapter.getData();
//
//        //用于存储 要删除的数据
//        final List<MultipleItemEntity> deleteEntities = new ArrayList<>();
//        for (MultipleItemEntity entity : data) {
//            //遍历一个一个的Item数据
//            // 把 被选中的Item的数据 加到 deleteEntities
//            final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
//            if (isSelected) {
//                deleteEntities.add(entity);
//            }
//        }
//        for (MultipleItemEntity entity : deleteEntities) {
//            int removePosition;
//            final int entityPosition = entity.getField(ShopCartItemFields.POSITION);
//
//            if (entityPosition > mCurrentCount - 1) {
//                removePosition = entityPosition - (mTotalCount - mCurrentCount);
//            } else {
//                removePosition = entityPosition;
//            }
//            if (removePosition <= mAdapter.getItemCount()) {
//                mAdapter.remove(removePosition);
//                mCurrentCount = mAdapter.getItemCount();//
//                //更新数据
//                mAdapter.notifyItemRangeChanged(removePosition, mAdapter.getItemCount());
//            }
//        }
    }

    //清空所有数据
    @OnClick(R2.id.tv_top_shop_cart_clear)
    void onClickClear() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
//        checkItemCount();
    }
}
