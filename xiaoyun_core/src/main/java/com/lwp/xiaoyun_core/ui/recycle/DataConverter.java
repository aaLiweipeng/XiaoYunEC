package com.lwp.xiaoyun_core.ui.recycle;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/7 6:57
 *     desc   : 数据转化的约束（必须为接口或者抽象类）
 * </pre>
 */
public abstract class DataConverter {

    protected final ArrayList<MultipleItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;

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
