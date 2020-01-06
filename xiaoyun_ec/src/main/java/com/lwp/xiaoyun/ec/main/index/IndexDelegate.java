package com.lwp.xiaoyun.ec.main.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.ui.refresh.RefreshHandler;

import butterknife.BindView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/4 3:25
 *     desc   :
 * </pre>
 */
public class IndexDelegate extends BottomItemDelegate {

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan = null;
    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchView = null;

    private RefreshHandler mRefreshHandler = null;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mRefreshHandler = new RefreshHandler(mRefreshLayout);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_purple,
                android.R.color.holo_blue_bright
        );
        //一参数：使得下拉过程中，加载动画球会 由小变大，
        // 界面往上回弹的时候，又会 由大变小；
        // 二三参指的是 动画球的起始高度和终止高度
        mRefreshLayout.setProgressViewOffset(true,120,300);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        mRefreshHandler.firstPage("http://lcjxg.cn/RestServer/data/index_data.json");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }
}
