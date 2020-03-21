package com.lwp.xiaoyun.ec.detail;

import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ui.recycler.ItemType;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun.ui.recycler.MultipleViewHolder;

import java.util.List;

public class RecyclerImageAdapter extends MultipleRecyclerAdapter {

    protected RecyclerImageAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(ItemType.SINGLE_BIG_IMAGE, R.layout.item_image);
    }

    //加载布局
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);

        final int type = holder.getItemViewType();
        switch (type) {
            case ItemType.SINGLE_BIG_IMAGE:

                //Item布局中就一个图片组件而已
                final AppCompatImageView imageView = holder.getView(R.id.image_rv_item);
                final String url = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .dontAnimate()
                        .into(imageView);
                break;
            default:
                break;
        }
    }
}
