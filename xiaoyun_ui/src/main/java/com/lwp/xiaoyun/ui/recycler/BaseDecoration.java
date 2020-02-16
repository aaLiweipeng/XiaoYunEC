package com.lwp.xiaoyun.ui.recycler;

import android.support.annotation.ColorInt;

import com.choices.divider.DividerItemDecoration;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 0:55
 *     desc   : 分隔线框架
 * </pre>
 */
public class BaseDecoration extends DividerItemDecoration {

    /**
     * 简单工厂模式创建
     *  @param color 分隔线的颜色
     * @param size  分隔线的粗细
     */
    private BaseDecoration(@ColorInt int color, int size) {
        setDividerLookup(new DividerLookupImpl(color, size));
    }
    public static BaseDecoration create(@ColorInt int color, int size) {
        return new BaseDecoration(color, size);
    }
}
