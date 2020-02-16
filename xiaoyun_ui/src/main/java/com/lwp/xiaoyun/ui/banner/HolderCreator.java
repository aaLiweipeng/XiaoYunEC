package com.lwp.xiaoyun.ui.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/14 9:38
 *     desc   : 用法注释如 LauncherHolderCreator
 * </pre>
 */
public class HolderCreator implements CBViewHolderCreator<ImageHolder> {
    @Override
    public ImageHolder createHolder() {
        return new ImageHolder();
    }
}
