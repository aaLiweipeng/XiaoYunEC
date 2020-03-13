package com.lwp.xiaoyun.ec.main.index.search;

import android.support.v7.widget.AppCompatTextView;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/14 0:11
 *     desc   : 搜索页 适配器
 * </pre>
 */
public class SearchAdapter extends MultipleRecyclerAdapter {

    protected SearchAdapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(SearchItemType.ITEM_SEARCH, R.layout.item_search);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);

        switch (entity.getItemType()) {
            case SearchItemType.ITEM_SEARCH:
                final AppCompatTextView tvSearchItem = holder.getView(R.id.tv_search_item);//Item 的 文本组件
                final String history = entity.getField(MultipleFields.TEXT);
                tvSearchItem.setText(history);
                break;

            default:
                break;
        }
    }
}
