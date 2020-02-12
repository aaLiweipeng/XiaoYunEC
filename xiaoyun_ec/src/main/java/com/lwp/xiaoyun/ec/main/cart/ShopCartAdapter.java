package com.lwp.xiaoyun.ec.main.cart;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.app.XiaoYun;
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

    //全选图标的tag
    private boolean mIsSelectedAll = false;

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

                break;
            default:
                break;
        }
    }
}
