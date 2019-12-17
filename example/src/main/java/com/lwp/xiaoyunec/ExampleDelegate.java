package com.lwp.xiaoyunec;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 11:03
 *     desc   :
 * </pre>
 */
public class ExampleDelegate extends XiaoYunDelegate {

    @Override
    public Object setLayout() {
        //传入布局
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
