package com.lwp.xiaoyun.ui.recycler;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 7:01
 *     desc   : 用来存储 RecycleView 的 每一个Item的 数据 ！！！
 *              一个 Item的数据 用一个 MultipleItemEntity / FIELDS_REFERENCE 实例 来存储 ！！！
 *
 *              每个Item的数据 用键值对的实行，有序存储在 软应用LinkedHashMap中
 *
 *              存储核心 —— 全局成员变量 —— 软引用LinkedHashMap，
 *              SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE
 *
 *
 *              观察下面的 SoftReference 的源码：
 *              ！！！！！
 *              源码的 T 即是 这里的  LinkedHashMap<Object, Object>
 *              用 LinkedHashMap<Object, Object> 补位源码中的 T ，
 *              实现一个
 *              存储核心 —— 全局成员变量 —— 软引用LinkedHashMap，
*               SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE
 *              ！！！！！
 *
 *              public class SoftReference<T> extends Reference<T>
 *
 *              public SoftReference(T referent, ReferenceQueue<? super T> q) {
 *               super(referent, q);
 *               this.timestamp = clock;
 *             }
 *
 * </pre>
 */
public class MultipleItemEntity implements MultiItemEntity {

    //** 这里两个变量
    // ！！！仅仅为软引用SoftReference<>() 构造方法 提供规定的参数变量 而已
    private final LinkedHashMap<Object, Object> MULTIPLE_FIELDS = new LinkedHashMap<>();
    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();


    //本类存储 Item数据 的核心
    //每个Item的数据 用键值对的实行，有序存储在 软应用LinkedHashMap中
    private final SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE
            = new SoftReference<>(MULTIPLE_FIELDS, ITEM_QUEUE);

    //构造方法 在 MultipleEntityBuilder.build() 中使用
    MultipleItemEntity(LinkedHashMap<Object, Object> fields) {

        //FIELDS_REFERENCE.get() 是拿到一个 软引用的 LinkedHashMap
        FIELDS_REFERENCE.get().putAll(fields);
    }
    //返回一个 MultipleEntityBuilder 实例
    public static MultipleEntityBuilder builder() {
        return new MultipleEntityBuilder();
    }


    @Override
    public int getItemType() {
        //为了 控制RecycleView 每一个Item的样式和表现特征
        return (int)FIELDS_REFERENCE.get().get(MultipleFields.ITEM_TYPE);
    }


    //getter 和 setter
    @SuppressWarnings("unchecked")
    public final <T> T getField(Object key) {
        return (T) FIELDS_REFERENCE.get().get(key);
    }
    public final LinkedHashMap<?, ?> getFields() {
        // 返回 存储了整一个Item数据 的 LinkedHashMap<Object, Object>
        return FIELDS_REFERENCE.get();
    }
    public final MultipleItemEntity setField(Object key, Object value) {
        //FIELDS_REFERENCE.get() 是拿到一个 软引用的 LinkedHashMap
        //这里是 往这个 LinkedHashMap 中添加一个 键值对
        FIELDS_REFERENCE.get().put(key, value);
        return this;
    }
}
