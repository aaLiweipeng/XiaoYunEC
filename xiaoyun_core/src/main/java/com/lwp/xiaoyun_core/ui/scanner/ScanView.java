package com.lwp.xiaoyun_core.ui.scanner;

import android.content.Context;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/12 8:59
 *     desc   : 自定义的扫描用的View —— 主要是在 createViewFinderView()中实现
 * </pre>
 */
public class ScanView extends ZBarScannerView {

    //设置成 最终之调用 两个参数的构造方法
    public ScanView(Context context) {
        this(context, null);
    }
    public ScanView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    // 打开了二维码扫描之后，中间有个扫描框，
    // 下面 创建这个View
    @Override
    protected IViewFinder createViewFinderView(Context context) {
        return new XiaoYunViewFinderView(context);
    }
}
