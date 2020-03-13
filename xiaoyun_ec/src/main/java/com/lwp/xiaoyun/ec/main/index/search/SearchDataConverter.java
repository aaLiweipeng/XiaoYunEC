package com.lwp.xiaoyun.ec.main.index.search;

import com.alibaba.fastjson.JSONArray;
import com.lwp.xiaoyun.ui.recycler.DataConverter;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/14 0:12
 *     desc   :
 * </pre>
 */
public class SearchDataConverter extends DataConverter {

    public static final String TAG_SEARCH_HISTORY = "search_history";

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        //在 SearchDelegate 中 网络请求添加进SharePreference的，在这里get，
        // 这里是一个 List<String> 转化成的 JSON字符串 ，即变成 JSONArray
        final String jsonStr =
                XiaoyunPreference.getCustomAppProfile(TAG_SEARCH_HISTORY);

        if (!jsonStr.equals("")) {
            //如果 JSON字符串 不空

            final JSONArray array = JSONArray.parseArray(jsonStr);

            final int size = array.size();
            for (int i = 0; i < size; i++) {

                final String historyItemText = array.getString(i);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(SearchItemType.ITEM_SEARCH)
                        .setField(MultipleFields.TEXT, historyItemText)//把值传入Bean
                        .build();
                ENTITIES.add(entity);
            }
        }

        return ENTITIES;
    }
}

