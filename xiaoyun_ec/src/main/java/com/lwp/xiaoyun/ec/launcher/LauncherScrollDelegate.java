package com.lwp.xiaoyun.ec.launcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.ui.launcher.LauncherHolderCreator;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/23 3:35
 *     desc   :
 * </pre>
 */
public class LauncherScrollDelegate extends XiaoYunDelegate implements OnItemClickListener {

    //查看源码，可以看到这里的 ConvenientBanner的泛型，
    //就是 数据的类型；
    //这里要传入的数据其实就是资源文件，也就是图片，
    // 所以要传入的泛型乃是Integer！
    private ConvenientBanner<Integer> mConvenientBanner = null;
    //用于存储图片（id） 的 ArrayList
    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();

    private void initBanner() {
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);
        mConvenientBanner
                .setPages(new LauncherHolderCreator(), INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)//设置指示器点的位置
                .setOnItemClickListener(this)
                .setCanLoop(false);//设置可以循环

    }

    //配置根视图布局 同时 初始化组件
    @Override
    public Object setLayout() {
        //因为 本方法在 onCreateView() 的时候会自动调用，（见 BaseDelegate）
        // 所以除了用来配置布局，用来 初始化组件 也是不错的选择
        mConvenientBanner = new ConvenientBanner<>(getContext());
        //因为 本Delegate（碎片） 只用这么一个 组件 就可以 完整显示业务了，
        // 所以这里直接 置入这个 轮播组件 即可
        return mConvenientBanner;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initBanner();
    }

    @Override
    public void onItemClick(int position) {

    }
}
