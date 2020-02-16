package com.lwp.xiaoyun.ec.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun_core.ui.recycler.MultipleViewHolder;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lwp.xiaoyun_core.ui.recycler.MultipleFields.*;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/10 4:05
 *     desc   :
 * </pre>
 */
public class ShopCartAdapter extends MultipleRecyclerAdapter {

    //全选图标的tag
    private boolean mIsSelectedAll = false;
    private ICartItemListener mCartItemListener = null;
    //购物车所有Item总价加起来的总价
    private double mTotalPrice = 0.00;

    /**
     * 构造方法，设置成 protected，不被外部调用，就给下面的 简单工厂模式调用
     *
     * @param data 包含了 RecycleView 的每一个Item的 数据 的List
     */
    protected ShopCartAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加购物测item布局
        addItemType(ShopCartItemType.SHOP_CART_ITEM, R.layout.item_shop_cart);
    }

    //设置全选图标的tag，供ShopCartDelegate.onClickSelectAll()调用
    public void setIsSelectedAll(boolean isSelectedAll) {
        mIsSelectedAll = isSelectedAll;
    }

    // 总价的逻辑接口 回调机制
    //供 ShopCartDelegate 调用
    public void setCartItemListener(ICartItemListener listener) {
        mCartItemListener = listener;
    }
    //获得总价
    public double getTotalPrice() {
        return mTotalPrice;
    }
    public void initTotalData() {

        mTotalPrice = 0.00;

        //初始化总价
        for (MultipleItemEntity data : mData) {
            final double price = data.getField(ShopCartItemFields.PRICE);
            final int count = data.getField(ShopCartItemFields.COUNT);
            final double total = price * count;//单个Item的总价
            mTotalPrice = mTotalPrice + total;
        }
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);

        switch (holder.getItemViewType()) {
            case ShopCartItemType.SHOP_CART_ITEM:

                //先取出所有值
                final int id = entity.getField(ID);
                final String thumb = entity.getField(IMAGE_URL);
                final String title = entity.getField(ShopCartItemFields.TITLE);
                final String desc = entity.getField(ShopCartItemFields.DESC);
                final int count = entity.getField(ShopCartItemFields.COUNT);
                final double price = entity.getField(ShopCartItemFields.PRICE);
                //取出所以控件
                final AppCompatImageView imgThumb = holder.getView(R.id.image_item_shop_cart);//缩略图
                final AppCompatTextView tvTitle = holder.getView(R.id.tv_item_shop_cart_title);//商品标题
                final AppCompatTextView tvDesc = holder.getView(R.id.tv_item_shop_cart_desc);//描述
                final AppCompatTextView tvPrice = holder.getView(R.id.tv_item_shop_cart_price);//
                final IconTextView iconMinus = holder.getView(R.id.icon_item_minus);//
                final IconTextView iconPlus = holder.getView(R.id.icon_item_plus);//
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);//Item商品数
                final IconTextView iconIsSelected = holder.getView(R.id.icon_item_shop_cart);//
                //加载 赋值
                tvTitle.setText(title);
                tvDesc.setText(desc);
                tvPrice.setText(String.valueOf(price));
                tvCount.setText(String.valueOf(count));
                Glide.with(mContext)
                        .load(thumb)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgThumb);

                //在ShopCartDelegate.onClickSelectAll()，即全选图标被点击之后，
                // 会处理tag值等数据配置，最后调用notify...()，通知RecyclerView刷新数据与UI，
                // 则onBindViewHolder--即这里的convert()会再次被回调
                //而在Item的左侧对勾的新一轮的 渲染之前，
                // 将其选中状态 与 全选图标的选中状态 同步！！起到刷新UI的作用
                entity.setField(ShopCartItemFields.IS_SELECTED, mIsSelectedAll);

                final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
                //根据数据状态显示 Item左侧选勾
                if (isSelected) {
                    //选中了
                    iconIsSelected.setTextColor(
                            ContextCompat.getColor(XiaoYun.getApplicationContext(), R.color.app_main));
                } else {
                    //没有选中
                    iconIsSelected.setTextColor(Color.GRAY);
                }

                //添加左侧选勾点击事件
                iconIsSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //没次点击都要重新取，不能用全局成员 isSelected
                        final boolean currentSelected = entity.getField(ShopCartItemFields.IS_SELECTED);

                        if (currentSelected) {
                            //如果是选中了的，点击之后则变成 没选中的状态
                            iconIsSelected.setTextColor(Color.GRAY);
                            entity.setField(ShopCartItemFields.IS_SELECTED, false);
                        } else {
                            //如果是没选中，点击之后则变成 选中的状态
                            iconIsSelected.setTextColor(
                                    ContextCompat.getColor(XiaoYun.getApplicationContext(), R.color.app_main));
                            entity.setField(ShopCartItemFields.IS_SELECTED, true);
                        }
                    }
                });

                //添加加减按钮事件
                iconMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int currentCount = entity.getField(ShopCartItemFields.COUNT);

                        //告诉服务器这里要 减1 了，等于1 则要删除掉item，不能减了
                        if (Integer.parseInt(tvCount.getText().toString()) > 1) {

                            //告诉服务器这里要 减1
                            OkHttpUtil.build()
                                    .loader(mContext)
                                    .addPostKV("count", currentCount)
                                    .sendPostRequest("http://lcjxg.cn/RestServer/api/shop_cart_count.php",
                                            new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {

                                            //服务器成功响应，则数据上报完毕，
                                            // 接着便 更新本地UI
                                            int countNum = Integer.parseInt(tvCount.getText().toString());
                                            countNum--;
                                            tvCount.setText(String.valueOf(countNum));

                                            //关于总价的 逻辑处理
                                            if (mCartItemListener != null) {
                                                mTotalPrice = mTotalPrice - price;
                                                final double itemTotal = countNum * price;
                                                mCartItemListener.onItemClick(itemTotal);//把总价传出去
                                            }
                                        }
                                    });
//                            //告诉服务器这里要 减1
//                            RestClient.builder()
//                                    .url("shop_cart_count.php")//通知
//                                    .loader(mContext)
//                                    .params("count", currentCount)
//                                    .success(new ISuccess() {
//                                        @Override
//                                        public void onSuccess(String response) {
//                                            //服务器成功响应，则数据上报完毕，
//                                            // 接着便 更新本地UI
//                                            int countNum = Integer.parseInt(tvCount.getText().toString());
//                                            countNum--;
//                                            tvCount.setText(String.valueOf(countNum));
//
//                                            //关于总价的 逻辑处理
//                                            if (mCartItemListener != null) {
//                                                mTotalPrice = mTotalPrice - price;
//                                                final double itemTotal = countNum * price;
//                                                mCartItemListener.onItemClick(itemTotal);//把总价传出去
//                                            }
//                                        }
//                                    })
//                                    .build()
//                                    .post();
                        }
                    }
                });
                iconPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int currentCount = entity.getField(ShopCartItemFields.COUNT);

                        OkHttpUtil.build()
                                .loader(mContext)
                                .addPostKV("count", currentCount)
                                .sendPostRequest("http://lcjxg.cn/RestServer/api/shop_cart_count.php",
                                        new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {

                                                int countNum = Integer.parseInt(tvCount.getText().toString());
                                                countNum++;
                                                tvCount.setText(String.valueOf(countNum));

                                                //关于总价的 逻辑处理
                                                if (mCartItemListener != null) {
                                                    mTotalPrice = mTotalPrice + price;
                                                    final double itemTotal = countNum * price;
                                                    mCartItemListener.onItemClick(itemTotal);
                                                }
                                            }
                                        });

//                        RestClient.builder()
//                                .url("shop_cart_count.php")
//                                .loader(mContext)
//                                .params("count", currentCount)
//                                .success(new ISuccess() {
//                                    @Override
//                                    public void onSuccess(String response) {
//                                        int countNum = Integer.parseInt(tvCount.getText().toString());
//                                        countNum++;
//                                        tvCount.setText(String.valueOf(countNum));
//
//                                        //关于总价的 逻辑处理
//                                        if (mCartItemListener != null) {
//                                            mTotalPrice = mTotalPrice + price;
//                                            final double itemTotal = countNum * price;
//                                            mCartItemListener.onItemClick(itemTotal);
//                                        }
//                                    }
//                                })
//                                .build()
//                                .post();
                    }
                });


                break;
            default:
                break;
        }
    }
}
