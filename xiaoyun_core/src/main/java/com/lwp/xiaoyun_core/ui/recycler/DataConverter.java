package com.lwp.xiaoyun_core.ui.recycler;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 6:57
 *     desc   : 制定数据转化工具类的约束（必须为接口或者抽象类）
 *              兼 数据转化工具类 Json to JavaBean/JavaEntity
 * </pre>
 */
public abstract class DataConverter {

    //一个 MultipleItemEntity实例 存储着一个 Item的数据，
    // 这里用一个 ArrayList 存储所有的 MultipleItemEntity实例，用例见 IndexDataConverter
    protected final ArrayList<MultipleItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;

    //用于实现 数据转化过程 Json to JavaBean/JavaEntity
    public abstract ArrayList<MultipleItemEntity> convert();

    public DataConverter setJsonData(String json) {
        this.mJsonData = json;
        return this;
    }

    protected String getJsonData() {
        if (mJsonData == null || mJsonData.isEmpty()) {
            throw new NullPointerException("DATA IS NULL!!! ( DataConverter.mJsonData ) ");
        }
        return mJsonData;
    }
}
