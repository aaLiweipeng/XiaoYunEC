package com.lwp.xiaoyun_core.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.lwp.xiaoyun_core.R;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import me.yokeyword.fragmentation.SupportActivity;

public abstract class ProxyActivity extends SupportActivity {

    //用来返回 根Delegate
    public abstract XiaoYunDelegate setRootDelegate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContainer(savedInstanceState);
    }

    //初始化视图
    private void initContainer(@Nullable Bundle savedInstanceState) {
        //一般用来容纳 Fragment 的容器 都是 FrameLayout
        final FrameLayout container = new FrameLayout(this);
        //这里AS 不建议直接传入Id，而是通过资源文件
        container.setId(R.id.delegate_container);

        setContentView(container);

        if (savedInstanceState == null) {
            //即第一次加载 savedInstanceState 的时候，传入 根Delegate
            loadRootFragment(R.id.delegate_container, setRootDelegate());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //垃圾回收，这两个垃圾回收方法写了不一定会执行，但是可以写上
        System.gc();
        System.runFinalization();
    }
}
