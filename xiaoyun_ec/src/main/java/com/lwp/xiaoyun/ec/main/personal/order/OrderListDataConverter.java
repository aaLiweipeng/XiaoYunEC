package com.lwp.xiaoyun.ec.main.personal.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun.ui.recycler.DataConverter;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;


import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/18 20:13
 *     desc   :
 * </pre>
 */
public class OrderListDataConverter extends DataConverter {

    //用于实现 数据转化过程 Json to JavaBean/JavaEntity;
    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = array.getJSONObject(i);
            final String thumb = data.getString("thumb");
            final String title = data.getString("title");
            final int id = data.getInteger("id");
            final double price = data.getDouble("price");
            final String time = data.getString("time");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(OrderListItemType.ITEM_ORDER_LIST)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.IMAGE_URL,thumb)
                    .setField(MultipleFields.TITLE,title)
                    .setField(OrderItemFields.PRICE,price)
                    .setField(OrderItemFields.TIME,time)
                    .build();

            //来自父类DataConverter 的 ENTITIES
            ENTITIES.add(entity);

        }//遍历JsonArray 的 for循环 出口！！

        return ENTITIES;
    }
}
