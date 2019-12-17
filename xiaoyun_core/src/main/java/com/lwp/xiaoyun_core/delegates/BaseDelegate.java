package com.lwp.xiaoyun_core.delegates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/15 15:42
 *     desc   : 1.判断加载到根布局的方式类型（资源id 或者 View）并 加载；
 *              2.绑定根布局与Fragment；绑定完成后强制 子类 实现 onBindView()
 *              3.onDestroyView的时候，解绑
 * </pre>
 */
public abstract class BaseDelegate extends SwipeBackFragment {

    //Unbinder 是 ButterKnife 的一个类型
    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder = null;

    //其名为set，什么时候set？即在子类继承本类时，需要强制实现本方法，
    // 到那个时候，在子类实现的这个方法中，使用 return 的方式 “传入”布局——可以是layout的id 也可以是是个View
    // 实现之后，只要调用setLayout() ，就会通过return 把 “传入”的布局 返回
    public abstract Object setLayout();

    //视图绑定完成之后，有 **一系列的操作** 是必须进行的，
    // 把这些操作逻辑封装起来，放在下面这个方法中，强制子类必须 `实现`并`执行`
    // 这里的“强制”，通过abstract 可以实现
    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //根视图！
        View rootView = null;

        if (setLayout() instanceof Integer) {
            //如果setLayout() 返回的是一个Layout 的id
            rootView = inflater.inflate((Integer) setLayout(), container, false);

        } else if (setLayout() instanceof View) {
            //如果setLayout() 返回的是一个 View
            rootView = (View)setLayout();

        }

        if (rootView != null) {
            //如果rootView 不为空， 则开始绑定资源,
            //将本Fragment 跟 根视图 **绑定** 起来
            mUnbinder = ButterKnife.bind(this, rootView);
            onBindView(savedInstanceState, rootView);
        }

        return rootView;
    }

    //记得最后处理一下 UnBinder
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();//解除绑定
        }
    }
}
