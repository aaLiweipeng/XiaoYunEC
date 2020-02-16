package com.lwp.xiaoyun.ui.recycler;

import java.util.LinkedHashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 8:29
 *     desc   : 建造者模式 ，本类也是可以用 静态内部类的形式
 *              写在 MultipleItemEntity 中的，当然也可以提取在这里，
 *              减少 MultipleItemEntity 的代码量
 * </pre>
 */
public class MultipleEntityBuilder {

    //没用的键值对 会自动回收
    private static final LinkedHashMap<Object, Object> FIELDS = new LinkedHashMap<>();

    MultipleEntityBuilder() {
        //注意这里清除是非常重要的，
        // 因为 使用Entity的时候，每次插入的都是 新的数据，
        // 不能把上次的数据也追加上来；本次就展示本次的数据即可，
        // 不清除的话，后面的轮次 会把 前面轮次的数据 也展示了，这是不行的！！
        FIELDS.clear();
    }

    public final MultipleEntityBuilder setItemType(int itemType) {
        FIELDS.put(MultipleFields.ITEM_TYPE, itemType);
        return this;
    }

    public final MultipleEntityBuilder setField(Object key, Object value) {
        FIELDS.put(key, value);
        return this;
    }

    public final MultipleEntityBuilder setFields(LinkedHashMap<?,?> map) {
        FIELDS.putAll(map);
        return this;
    }

    public final MultipleItemEntity build() {
        return  new MultipleItemEntity(FIELDS);
    }
}
