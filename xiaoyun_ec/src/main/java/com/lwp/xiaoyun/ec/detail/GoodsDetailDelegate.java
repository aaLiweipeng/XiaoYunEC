package com.lwp.xiaoyun.ec.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 21:14
 *     desc   : 商品详情页
 * </pre>
 */
public class GoodsDetailDelegate extends XiaoYunDelegate {

    public static GoodsDetailDelegate create() {
        return new GoodsDetailDelegate();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();//设定横向转场切入动画
    }
}
