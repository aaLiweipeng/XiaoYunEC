package com.lwp.xiaoyun.ui.recycler;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 7:18
 *     desc   : RecycleView 的 每一个Item的 可能具备的 数据的键
 * </pre>
 */
public enum MultipleFields {
    ITEM_TYPE,
    TEXT,
    IMAGE_URL,
    BANNERS,
    SPAN_SIZE,
    ID,
    NAME,
    TAG//VerticalListDelegate 中 每一个Item是否被点击的Tag，false表没点击，true表点击了
}
