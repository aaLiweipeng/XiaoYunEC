package com.lwp.xiaoyun_core.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.lwp.xiaoyun_core.app.XiaoYun;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/19 19:12
 *     desc   :
 * </pre>
 */
public class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = XiaoYun.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = XiaoYun.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
