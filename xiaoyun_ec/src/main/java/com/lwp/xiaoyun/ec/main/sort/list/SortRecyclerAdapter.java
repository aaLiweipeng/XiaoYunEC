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
    //记录上一个Item的位置，默认第一个item是选中的
    private int mPrePosition = 0;

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
    protected void convert(final MultipleViewHolder holder, final MultipleItemEntity entity) {
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

                        //拿到 当前被点击的item 的位置
                        final int currentPosition = holder.getAdapterPosition();
                        if (mPrePosition != currentPosition) {

                            //如果上一个item位置 不等于 当前被点击的item的位置，
                            // 则还原上一个 Item的样式——将TAG归位，并通知Adapter
                            getData().get(mPrePosition).setField(MultipleFields.TAG, false);
                            notifyItemChanged(mPrePosition);

                            //更新 当前被选中的item的数据，通知Adapter ——
                            // 这里的entity本就属于被点击的item的，所以更新被点击的item的数据直接拿entity操作即可，
                            // 而上一个item的entity在这里（被选中的item的onClick中）是拿不到的，
                            // 所以上面更新它的数据只能用getData()，通过修改Adapter中的数据去更新设置；
                            entity.setField(MultipleFields.TAG, true);
                            notifyItemChanged(currentPosition);

                            //数据配置完毕了，“当前”就变成 下一轮“上一个”了（类似思想 见BaseBottomDelegate）
                            mPrePosition = currentPosition;

                            final int contentId = getData().get(currentPosition).getField(MultipleFields.ID);

                        }
                    }
                });

                //onClick中调用notifyItemChanged()时 onBindViewHolder() 会重新回调，下面这个部分同样被回调，
                // 则 Item样式 可以重新被 判断并渲染，即 局部刷新
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

                //设置文本
                holder.setText(R.id.tv_vertical_item_name, text);//设置item文本

                break;
            default:
                break;
        }
    }
}
