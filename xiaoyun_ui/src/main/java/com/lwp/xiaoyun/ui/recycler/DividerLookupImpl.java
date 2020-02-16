package com.lwp.xiaoyun.ui.recycler;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 0:59
 *     desc   : 实现 框架规定的一个接口
 *              处理分隔线的 横向 或者 竖向的一些属性
 * </pre>
 */
public class DividerLookupImpl implements DividerItemDecoration.DividerLookup {

    //分割线的颜色和大小
    private final int COLOR;
    private final int SIZE;

    public DividerLookupImpl(int color, int size) {
        COLOR = color;
        SIZE = size;
    }


    @Override
    public Divider getVerticalDivider(int position) {
        return new Divider.Builder()
                .size(SIZE)
                .color(COLOR)
                .build();
    }

    @Override
    public Divider getHorizontalDivider(int position) {
        return new Divider.Builder()
                .size(SIZE)
                .color(COLOR)
                .build();
    }
}
