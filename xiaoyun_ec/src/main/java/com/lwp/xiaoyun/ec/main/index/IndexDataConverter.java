package com.lwp.xiaoyun.ec.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun_core.ui.recycler.DataConverter;
import com.lwp.xiaoyun_core.ui.recycler.ItemType;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;


import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 9:30
 *     desc   : 数据转化工具类 Json to JavaBean/JavaEntity
 * </pre>
 */
public class IndexDataConverter extends DataConverter {

    /**
     * 实现 数据转化过程 Json to JavaBean/JavaEntity
     * 将含有 若干个Item数据 的 Json（JsonArray）
     * 转换成 一个包含了 若干个对应的 JavaBean/JavaEntity 的 ArrayList
     *
     * 应用时，使用父类的DataConverter.setJsonData() 设置了Json数据 之后，
     * 在这里 通过 继承得到的DataConverter.getJsonData() 获取这个 Json数据，
     * 然后实现 数据转化
     *
     * @return 返回转化结果，即上述的
     * 一个包含了 若干个对应的 JavaBean/JavaEntity (MultipleItemEntity) 的 ArrayList (ENTITIES)
     */
    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();

        for (int i = 0; i < size; i++) {

            //从 第一个到最后一个
            // 取出jsonArray 中的 每一个 元素（jsonObject）！！！
            final JSONObject data = dataArray.getJSONObject(i);

            final String imageUrl = data.getString("imageUrl");
            final String text = data.getString("text");
            final int spanSize = data.getInteger("spanSize");
            final int id = data.getInteger("goodsId");
            final JSONArray banners = data.getJSONArray("banners");

            //用来存储 banner数组 的值
            final ArrayList<String> bannersImages = new ArrayList<>();
            int type = 0;
            if (imageUrl == null && text != null) {
                type = ItemType.TEXT;
            } else if (imageUrl != null && text == null) {
                type = ItemType.IMAGE;

                //01 10 11 00 往后就是图文都有或者都没有的了
            } else if (imageUrl != null) {
                //图文并茂
                type = ItemType.TEXT_IMAGE;

                //往后就是 图文都没有的了
            } else if (banners != null) {
                //图文都没有，那是不是有banners，有的话

                type = ItemType.BANNER;
                //Banner 初始化
                final int bannerSize = banners.size();
                for (int j = 0; j < bannerSize; j++) {
                    final String banner = banners.getString(j);
                    bannersImages.add(banner);
                }
            }

            //上面是Json数据转化到 局部变量，
            // 下面是 将这些 局部变量 存储到 LinkedHashMap 中
            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setField(MultipleFields.ITEM_TYPE, type)
                    .setField(MultipleFields.SPAN_SIZE, spanSize)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.TEXT, text)
                    .setField(MultipleFields.IMAGE_URL, imageUrl)
                    .setField(MultipleFields.BANNERS, bannersImages)
                    .build();

            //来自父类DataConverter 的 ENTITIES
            ENTITIES.add(entity);

        }//遍历JsonArray 的 for循环 出口！！

        return ENTITIES;
    }
}
