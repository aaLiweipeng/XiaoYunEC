package com.lwp.xiaoyun.ec.main.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/4 3:25
 *     desc   : 容器FrameLayout中的 分类页面 需要三个Fragment：底层、左边List、右边content
 *              加入 EcBottomDelegate
 * </pre>
 */
public class SortDelegate extends BottomItemDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
