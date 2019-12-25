package com.lwp.xiaoyun_core.delegates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwp.xiaoyun_core.activities.ProxyActivity;

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

    //！！用法： ！！！
    //1.其名为set，什么时候set？即在子类继承本类时，需要强制实现本方法，
    // 到那个时候，在子类实现的这个方法中，
    // 使用 return 的方式 “传入”布局——可以是layout的id 也可以是是个View ！！！！！！！
    // 实现之后，只要调用setLayout() ，
    // 就会通过return 把 “传入”的布局 返回 ！！！！！
    // ------
    // 2.因为 本方法在 onCreateView() 的时候会自动调用，（见 BaseDelegate）
    //   所以除了用来配置布局，用来 初始化组件 也是不错的选择 （见 LauncherScrollDelegate）
    // *****************************************
    // 在 onCreateView() 中，用来绑定根视图！！！
    public abstract Object setLayout();

    //视图绑定完成之后！！！ 需要进行的 **一系列的操作** ，封装在下面这个方法中，
    // 通过 abstract 强制子类必须 `实现`并`执行`
    // *******************************
    //！！用法： ！！！
    //在 onCreateView() 中，绑定根视图完成后调用！！！
    // 如果绑定根视图完成后， 有什么需要进行的逻辑，就写在这里！！！！
    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //根视图！
        final View rootView;

        if (setLayout() instanceof Integer) {
            //如果setLayout() 返回的是一个Layout 的id
            rootView = inflater.inflate((Integer) setLayout(), container, false);

        } else if (setLayout() instanceof View) {
            //如果setLayout() 返回的是一个 View
            rootView = (View)setLayout();
        }else{
            //出现问题时可以 快速找到 案发现场！！！
            //setLayout() 返回的类型，必须是 id 或者 View 类型！！
            throw new ClassCastException("setLayout() type must be int or View!!!");
        }
        //如果rootView 不为空， 则开始绑定资源,
        //将本Fragment 跟 根视图 **绑定** 起来
        mUnbinder = ButterKnife.bind(this, rootView);
        onBindView(savedInstanceState, rootView);

        return rootView;
    }

    public final ProxyActivity getProxyActivity() {
        return (ProxyActivity) _mActivity;
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
