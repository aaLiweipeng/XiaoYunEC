package com.lwp.xiaoyun.ec.main.cart;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun_core.ui.recycler.MultipleViewHolder;

import java.util.List;

import static com.lwp.xiaoyun_core.ui.recycler.MultipleFields.*;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/10 4:05
 *     desc   :
 * </pre>
 */
public class ShopCartAdapter extends MultipleRecyclerAdapter {
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

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
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
                final AppCompatTextView tvCount = holder.getView(R.id.tv_item_shop_cart_count);//
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

                break;
            default:
                break;
        }
    }
}
