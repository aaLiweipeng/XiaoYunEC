package com.lwp.xiaoyun.ec.main.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.delegates.web.WebDelegateImpl;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/27 0:47
 *     desc   : 注意这里要加上 网络权限
 * </pre>
 */
public class DiscoverDelegate extends BottomItemDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_discover;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        final WebDelegateImpl delegate = WebDelegateImpl.create("index.html");

        //文件加载到WebView上，webView设置在Delegate中，Delegate加载到 Framelayout上
        loadRootFragment(R.id.web_discovery_container, delegate);

    }
}
