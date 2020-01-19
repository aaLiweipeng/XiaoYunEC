package com.lwp.xiaoyun.ec.main.sort.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lwp.xiaoyun_core.ui.recycler.DataConverter;
import com.lwp.xiaoyun_core.ui.recycler.ItemType;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;



import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/19 3:56
 *     desc   : VerticalListList 的 数据转换器
 * </pre>
 */
public final class VerticalListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final ArrayList<MultipleItemEntity> dataList = new ArrayList<>();
        final JSONArray dataArray = JSON.parseObject(getJsonData()).
                getJSONObject("data").getJSONArray("list");

        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);

            final int id = data.getInteger("id");
            final String name = data.getString("name");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.VERTICAL_MENU_LIST)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.TEXT, name)
                    .setField(MultipleFields.TAG,false)
                    .build();

            dataList.add(entity);

        }
        //设置第一个Item的点击状态，默认初始化第一个Item被选中
        dataList.get(0).setField(MultipleFields.TAG, true);

        return dataList;
    }
}
