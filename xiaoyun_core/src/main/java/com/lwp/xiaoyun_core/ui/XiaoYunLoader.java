package com.lwp.xiaoyun_core.ui;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/19 16:51
 *     desc   : 框架的 加载动画Loader 是直接在 View 中显示出来的，这样子不行，
 *              我们需要整一个自己的 Dialog 去承载这些个Loader！
 *              尽量使用 v7包 中的 compat里面 的东西（AppCompatDialog）！！提高兼容性
 *
 *              另外，需要创建一个 我们自己的style，达到一个 灰色、透明、弹出的效果
 *
 * </pre>
 */
public class XiaoYunLoader {

    public static void showLoading(Context context, String type) {

        //创建自己的 Dialog 去承载这些个Loader
        final AppCompatDialog dialog = new AppCompatDialog(context);


    }
}
