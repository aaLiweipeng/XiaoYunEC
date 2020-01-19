package com.lwp.xiaoyun.ec.main.sort.list;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.main.sort.SortDelegate;
import com.lwp.xiaoyun_core.ui.recycler.ItemType;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun_core.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/19 6:03
 *     desc   :
 * </pre>
 */
public class SortRecyclerAdapter extends MultipleRecyclerAdapter {

    //把容器Delegate传进来，用于控制作用的关联关系
    private final SortDelegate DELEGATE;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * 构造方法，设置成 protected，不被外部调用，就给下面的 简单工厂模式调用
     *
     * @param data A new list is created out of this one to avoid mutable list
     *             这里指的就是 包含了 RecycleView 的每一个Item的 数据 的List
     */
    protected SortRecyclerAdapter(List<MultipleItemEntity> data, SortDelegate delegate) {
        super(data);
        DELEGATE = delegate;
        //添加垂直菜单布局
        addItemType(ItemType.VERTICAL_MENU_LIST, R.layout.item_vertical_menu_list);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);

        switch (holder.getItemViewType()) {
            case ItemType.VERTICAL_MENU_LIST:

                final String text = entity.getField(MultipleFields.TEXT);//item文本的内容
                final boolean isClicked = entity.getField(MultipleFields.TAG);//点击状态
                final AppCompatTextView name = holder.getView(R.id.tv_vertical_item_name);//item文本的实例
                final View line = holder.getView(R.id.view_line);//边框线
                final View itemView = holder.itemView;//一个item实例

                //设置点击事件
                //这里为了避免乱，点击事件单独添加在这里，而没有写在整个类上
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                if (!isClicked) {
                    //如果 当前item是 没有被点击的状态
                    line.setVisibility(View.INVISIBLE);//把 边框线设置为不可见
                    name.setTextColor(ContextCompat.getColor(mContext, R.color.we_chat_black));//文本组件文字配色
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.item_background));//整个item的背景色
                } else {
                    //如果 当前item是 被点击的状态
                    line.setVisibility(View.VISIBLE);
                    line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.app_main));
                    name.setTextColor(ContextCompat.getColor(mContext, R.color.app_main));
                    itemView.setBackgroundColor(Color.WHITE);
                }

                holder.setText(R.id.tv_vertical_item_name, text);//设置item文本

                break;
            default:
                break;
        }
    }
}
