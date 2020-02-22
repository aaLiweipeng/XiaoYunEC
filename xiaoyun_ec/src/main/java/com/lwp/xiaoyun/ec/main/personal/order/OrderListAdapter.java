package com.lwp.xiaoyun.ec.main.personal.order;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/18 22:45
 *     desc   : 订单适配器
 * </pre>
 */
public class OrderListAdapter extends MultipleRecyclerAdapter {

    /**
     * some initialization data.
     * 构造方法，设置成 protected，不被外部调用，就给下面的 简单工厂模式调用
     *
     * @param data A new list is created out of this one to avoid mutable list
     *             这里指的就是 包含了 RecycleView 的每一个Item的 数据 的List
     */
    protected OrderListAdapter(List<MultipleItemEntity> data) {
        super(data);

        addItemType(OrderListItemType.ITEM_ORDER_LIST, R.layout.item_order_list);

    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);

        switch (holder.getItemViewType()) {
            case OrderListItemType.ITEM_ORDER_LIST:

                //实例化布局组件
                final AppCompatImageView imageView = holder.getView(R.id.image_order_list);
                final AppCompatTextView title = holder.getView(R.id.tv_order_list_title);
                final AppCompatTextView price = holder.getView(R.id.tv_order_list_price);
                final AppCompatTextView time = holder.getView(R.id.tv_order_list_time);

                //取值
                final String titleVal = entity.getField(MultipleFields.TITLE);
                final String timeVal = entity.getField(OrderItemFields.TIME);
                final double priceVal = entity.getField(OrderItemFields.PRICE);
                final String imageUrl = entity.getField(MultipleFields.IMAGE_URL);

                //加载数据 到 布局组件
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(imageView);
                title.setText(titleVal);
                price.setText("价格：" + String.valueOf(priceVal));
                time.setText("时间：" + timeVal);

                break;

            default:
                break;
        }
    }
}
