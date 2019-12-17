package com.lwp.xiaoyun.ec.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/12 17:05
 *     desc   :
 * </pre>
 */
public class FontEcModule implements IconFontDescriptor {

    //传进ttf文件
    @Override
    public String ttfFileName() {
        return "iconfont.ttf";
    }

    //对应的一个Icon子类.values()
    @Override
    public Icon[] characters() {
        return EcIcons.values();
    }
}
