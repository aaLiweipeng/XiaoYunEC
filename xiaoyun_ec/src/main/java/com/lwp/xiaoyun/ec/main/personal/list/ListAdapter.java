package com.lwp.xiaoyun.ec.main.personal.list;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lwp.xiaoyun.ec.R;

import java.util.List;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/18 16:37
 *     desc   : “我的”页面
 * </pre>
 */
public class ListAdapter extends BaseMultiItemQuickAdapter<ListBean, BaseViewHolder> {

    /**
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ListAdapter(List<ListBean> data) {
        super(data);
        addItemType(ListItemType.ITEM_NORMAL, R.layout.arrow_item_layout);
        addItemType(ListItemType.ITEM_AVATAR, R.layout.arrow_item_avatar);
        addItemType(ListItemType.ITEM_SWITCH,R.layout.arrow_switch_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean item) {

        switch (helper.getItemViewType()) {
            case ListItemType.ITEM_NORMAL:
                //个人信息的Item
                helper.setText(R.id.tv_arrow_text, item.getText());
                helper.setText(R.id.tv_arrow_value, item.getValue());
                break;

            case ListItemType.ITEM_AVATAR:
                //头像
                Glide.with(mContext)
                        .load(item.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img_arrow_avatar));
                break;
            case ListItemType.ITEM_SWITCH:
//                helper.setText(R.id.tv_arrow_switch_text,item.getText());
//                final SwitchCompat switchCompat = helper.getView(R.id.list_item_switch);
//                switchCompat.setChecked(true);
//                switchCompat.setOnCheckedChangeListener(item.getmOnCheckedChangeListener());
                break;

            default:
                break;
        }
    }
}
