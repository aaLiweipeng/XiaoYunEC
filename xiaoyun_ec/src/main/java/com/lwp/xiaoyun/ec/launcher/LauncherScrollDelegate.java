package com.lwp.xiaoyun.ec.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.app.AccountManager;
import com.lwp.xiaoyun_core.app.IUserChecker;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.ui.launcher.ILauncherListener;
import com.lwp.xiaoyun_core.ui.launcher.LauncherHolderCreator;
import com.lwp.xiaoyun_core.ui.launcher.OnLauncherFinishTag;
import com.lwp.xiaoyun_core.ui.launcher.ScrollLauncherTag;
import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/23 3:35
 *     desc   : 轮播图启动图逻辑
 * </pre>
 */
public class LauncherScrollDelegate extends XiaoYunDelegate implements OnItemClickListener {

    //查看源码，可以看到这里的 ConvenientBanner的泛型，
    //就是 数据的类型；
    //这里要传入的数据其实就是资源文件，也就是图片（资源id），
    // 所以要传入的泛型乃是Integer！
    private ConvenientBanner<Integer> mConvenientBanner = null;
    //用于存储图片（资源id） 的 ArrayList
    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();

    private ILauncherListener mILauncherListener = null;


    private void initBanner() {
        INTEGERS.add(R.mipmap.launcher_01);
        INTEGERS.add(R.mipmap.launcher_02);
        INTEGERS.add(R.mipmap.launcher_03);
        INTEGERS.add(R.mipmap.launcher_04);
        INTEGERS.add(R.mipmap.launcher_05);

        //setPages()这里，类似于RecyclerView的Adapter，
        // 方法是用于 设置Banner页，一参为LauncherHolder，即实现页面内容的逻辑，二参为对应的数据
        mConvenientBanner
                .setPages(new LauncherHolderCreator(), INTEGERS)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)//设置指示器点的位置
                .setOnItemClickListener(this)//设置 子项监听
                .setCanLoop(false);//设置可以循环

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
    }

    //配置根视图布局 同时 初始化组件
    @Override
    public Object setLayout() {
        /* 这里 动态添加 布局组件 */

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
        //如果点击了最后一个滚动图，
        // 至此APP启动结束

        if (position == INTEGERS.size() - 1) {
            //sharePreference 工具
            // 设置一个标志键值
            //用于 判断APP是不是第一次进入
            //第一次使用，传入键值为true，之后就不用再使用 滚动启动图组件了；
            // 非第一次使用，就false；没有这个键，默认为 false
            XiaoyunPreference.setAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);

            //检查用户是否已经登录
            AccountManager.checkAccout(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void onNoSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }
}
