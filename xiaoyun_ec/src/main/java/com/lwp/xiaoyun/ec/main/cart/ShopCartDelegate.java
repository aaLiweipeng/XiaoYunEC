package com.lwp.xiaoyun.ec.main.cart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ec.pay.FastPay;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

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
public class ShopCartDelegate extends BottomItemDelegate implements ICartItemListener{

    private List<MultipleItemEntity> mData = new ArrayList<>();
    private ShopCartAdapter mAdapter = null;
    //购物车数量标记
    private int mCurrentCount = 0;//
    private int mTotalCount = 0;//
    private double mTotalPrice = 0.00;//总价

    @BindView(R2.id.rv_shop_cart)
    RecyclerView mRecyclerView = null;
    //全选图标
    @BindView(R2.id.icon_shop_cart_select_all)
    IconTextView mIconSelectAll = null;
    @BindView(R2.id.stub_no_item)
    ViewStubCompat mStubNoItem = null;
    @BindView(R2.id.tv_shop_cart_total_price)
    AppCompatTextView mTvTotalPrice = null;//显示总价的TextView


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

        OkHttpUtil.create()
                .sendGetRequest(
                        "http://47.100.78.251/RestServer/api/shop_cart.php",
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
                //设置总价回调接口
                mAdapter.setCartItemListener(ShopCartDelegate.this);
                //设置 新数据
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();

                //初始化总价 以及 相关UI
                mAdapter.initTotalData();
                mTotalPrice = mAdapter.getTotalPrice();
                mTvTotalPrice.setText(String.valueOf(mTotalPrice));

                checkItemCount();
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

            //更新总价
            mAdapter.initTotalData();
            mTvTotalPrice.setText(String.valueOf(mAdapter.getTotalPrice()));

        } else {
            //点击时，全选图标为 选中状态，则点击后 改为 未选中状态
            mIconSelectAll.setTextColor(Color.GRAY);//变色
            mIconSelectAll.setTag(0);
            mAdapter.setIsSelectedAll(false);
            //更新RecyclerView的 显示状态！！！
            mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());

            //更新总价
            mAdapter.initTotalData();
            mTvTotalPrice.setText(String.valueOf(mAdapter.getTotalPrice()));
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
        checkItemCount();
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
        checkItemCount();
    }

    @OnClick(R2.id.tv_shop_cart_pay)
    void onClickPay() {
//        createOrder();
        FastPay.create(this).beginPayDialog();
    }
    //创建订单，注意，和支付是没有关系的
    private void createOrder() {

        final String orderUrl = "自己服务器的生成订单的API";
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();

        orderParams.put("userid",123456);
        orderParams.put("amount",0.01);//订单总价
        orderParams.put("comment", "测试支付");//描述（需要传递给服务器的备注信息）
        //自己服务器的订单API的约定（参考）
        orderParams.put("type", 1);
        orderParams.put("ordertype", 0);//订单类型
        orderParams.put("isanonymous", true);
        orderParams.put("followeduser", 0);
        //加入我们自己准备的需要的订单的参数
        RestClient.builder()
                .url(orderUrl)
                .loader(getContext())
                .params(orderParams)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        XiaoYunLogger.d("ORDER", response);
                        final int orderId = JSON.parseObject(response).getInteger("result");
//                        FastPay.create(ShopCartDelegate.this)
//                                .setPayResultListener(ShopCartDelegate.this)
//                                .setOrderId(orderId)
//                                .beginPayDialog();
                    }
                })
                .build()
                .post();

    }

    //检查Adapter中的 数据量，
     @SuppressLint("RestrictedApi")
    public void checkItemCount() {
        final int count = mAdapter.getItemCount();//查看 Adapter中 是否还有数据

         if (count == 0) {
             //如果 Adapter中没有数据
             final View stubView = mStubNoItem.inflate();
             final AppCompatTextView tvToBuy =
                     (AppCompatTextView) stubView.findViewById(R.id.tv_stub_to_buy);
             tvToBuy.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Toast.makeText(getContext(), "这里空空如也，去逛逛吧~", Toast.LENGTH_SHORT).show();
                 }
             });
             mRecyclerView.setVisibility(View.GONE);
         } else {
             //如果 Adapter中 有数据
             mRecyclerView.setVisibility(View.VISIBLE);
         }
    }

    /**
     * ICartItemListener 总价逻辑回调接口
     * 此处负责 具体实现 在ShopCartAdapter.加减按钮点击事件中抽象调用
     * @param itemTotalPrice 来自 ShopCartAdapter 的 加减按钮点击事件
     *                         单个item的总价钱
     */
    @Override
    public void onItemClick(double itemTotalPrice) {
        //拿到整个购物车页面的总价
        final double price = mAdapter.getTotalPrice();
        mTvTotalPrice.setText(String.valueOf(price));
    }
}
