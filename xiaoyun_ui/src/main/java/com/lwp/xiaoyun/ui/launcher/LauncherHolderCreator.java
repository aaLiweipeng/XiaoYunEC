package com.lwp.xiaoyun.ui.launcher;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/24 14:35
 *     desc   : 实现 CBViewHolderCreator 接口，返回一个具体的Holder，
 *              为 使用时候 setPages() 提供参数
 *              mConvenientBanner.setPages(new LauncherHolderCreator(), INTEGERS)的 INTEGERS
 *              CBViewHolderCreator 是一个泛型接口，只有一个方法，具体可以看下源码
 *
 * </pre>
 */
public class LauncherHolderCreator implements CBViewHolderCreator<LauncherHolder> {

    @Override
    public LauncherHolder createHolder() {
        return new LauncherHolder();
    }


}
