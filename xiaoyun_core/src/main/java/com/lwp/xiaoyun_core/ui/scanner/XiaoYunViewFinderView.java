package com.lwp.xiaoyun_core.ui.scanner;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/12 10:04
 *     desc   : 自定义的扫描View
 * </pre>
 */
public class XiaoYunViewFinderView extends ViewFinderView {

    public XiaoYunViewFinderView(Context context) {
        this(context, null);
    }

    public XiaoYunViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSquareViewFinder = true;//设置扫描框为正方形
        mBorderPaint.setColor(Color.rgb(255,99,71));//边框颜色
        mLaserPaint.setColor(Color.rgb(255,99,71));//激光扫描动画的颜色
    }
}