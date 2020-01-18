package com.lwp.xiaoyun.ec.main.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.main.sort.list.VerticalListDelegate;
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

    //把渲染逻辑写在这里，这样的话，在打开其他平级的时候，
    // 打开到别的tab，如主页的时候，分类是不会加载的，
    // 只有点到分类tab按钮，FrameLayout容器切到本页，本页才会开始渲染布局！！
    // 如果直接写在 onBindView中，打开其他tab对应的页时，本页已经开始加载了，这是不好的
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        //加载根布局
        final VerticalListDelegate listDelegate = new VerticalListDelegate();
        loadRootFragment(R.id.vertical_list_container, listDelegate);
    }
}
