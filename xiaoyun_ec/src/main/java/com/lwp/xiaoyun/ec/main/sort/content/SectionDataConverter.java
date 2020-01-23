package com.lwp.xiaoyun.ec.main.sort.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/20 6:06
 *     desc   : 将对应的服务器JSON数据 转化成 List<SectionBean>
 * </pre>
 */
public class SectionDataConverter {

    final List<SectionBean> convert(String json) {
        final List<SectionBean> dataList = new ArrayList<>();
        //取出data键下所有数据，是一个 dataArray
        final JSONArray dataArray = JSON.parseObject(json).getJSONArray("data");


        final int size = dataArray.size();

        for (int i = 0; i < size; i++) {
            //这个 dataArray（JSONArray） 下是一个JsonObject数组，
            // 每一个JsonObject都是三个键——id、section、goods的JsonArray

            //处理前两个键，id、section ----------------------------------------------------
            final JSONObject data = dataArray.getJSONObject(i);
            final int id = data.getInteger("id");
            final String title = data.getString("section");

            //添加title
            //不写true 则默认不是头部
            final SectionBean sectionTitleBean = new SectionBean(true, title);
            sectionTitleBean.setId(id);
            sectionTitleBean.setIsMore(true);
            //加进数据List
            dataList.add(sectionTitleBean);


            //处理第三个键 goods的JsonArray ----------------------------------------------------
            final JSONArray goods = data.getJSONArray("goods");

            //商品内容循环
            final int goodSize = goods.size();

            for (int j = 0; j < goodSize; j++) {
                final JSONObject contentItem = goods.getJSONObject(j);
                final int goodsId = contentItem.getInteger("goods_id");
                final String goodsName = contentItem.getString("goods_name");
                final String goodsThumb = contentItem.getString("goods_thumb");

                //获取内容
                final SectionContentItemEntity itemEntity = new SectionContentItemEntity();
                itemEntity.setGoodsId(goodsId);
                itemEntity.setGoodsName(goodsName);
                itemEntity.setGoodsThumb(goodsThumb);
                //加进数据List
                dataList.add(new SectionBean(itemEntity));
            }
            //商品内容循环结束
        }
        //Section循环结束

        return dataList;
    }
}