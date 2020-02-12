package com.lwp.xiaoyun.ec.main.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun_core.ui.recycler.DataConverter;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/10 2:55
 *     desc   :
 * </pre>
 */
public class ShopCartDataConverter extends DataConverter {


    @Override
    public ArrayList<MultipleItemEntity> convert() {

        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");

        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);

            final String thumb = data.getString("thumb");
            final String desc = data.getString("desc");
            final String title = data.getString("title");
            final int id = data.getInteger("id");
            final int count = data.getInteger("count");//购物车中的个数
            final double price = data.getDouble("price");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, ShopCartItemType.SHOP_CART_ITEM)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.IMAGE_URL, thumb)
                    .setField(ShopCartItemFields.TITLE, title)
                    .setField(ShopCartItemFields.DESC, desc)
                    .setField(ShopCartItemFields.COUNT, count)
                    .setField(ShopCartItemFields.PRICE, price)
                    .setField(ShopCartItemFields.IS_SELECTED, false)//默认没有点击
                    .setField(ShopCartItemFields.POSITION, i)
                    .build();

            ENTITIES.add(entity);
        }


        return ENTITIES;
    }
}
