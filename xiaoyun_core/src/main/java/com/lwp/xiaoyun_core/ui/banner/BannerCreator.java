package com.lwp.xiaoyun_core.ui.banner;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lwp.xiaoyun_core.R;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/9 7:04
 *     desc   : 封装 convenientBanner 的 初始化 以及 配置逻辑
 * </pre>
 */
public class BannerCreator {

    /**
     * 封装 convenientBanner 的 初始化 以及 配置逻辑
     *
     * @param convenientBanner 一个具体的 convenientBanner 实例 Java动态创建组件 还是 findView xml静态定义的组件皆可
     * @param banners  页面数据
     * @param clickListener 点击接口实现类实例
     */
    public static void setDefault(ConvenientBanner<String> convenientBanner,
                                  ArrayList<String> banners, OnItemClickListener clickListener) {
        convenientBanner
                .setPages(new HolderCreator(),banners)
                .setPageIndicator(new int[]{R.drawable.dot_normal,R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)//设置指示器点的位置
                .setOnItemClickListener(clickListener)//设置 子项监听
                .setPageTransformer(new DefaultTransformer())//翻页动画效果
                .startTurning(3000)//设置自动轮播效果，每三秒轮播一次
                .setCanLoop(true);//设置可以循环

    }
}
