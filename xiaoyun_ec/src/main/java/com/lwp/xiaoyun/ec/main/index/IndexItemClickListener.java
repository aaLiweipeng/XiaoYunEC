package com.lwp.xiaoyun.ec.main.index;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.lwp.xiaoyun.ec.detail.GoodsDetailDelegate;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 20:08
 *     desc   :
 * </pre>
 */
public class IndexItemClickListener extends SimpleClickListener {

    //存储父级delegate
    private final XiaoYunDelegate DELEGATE;

    //简单工厂模式
    private IndexItemClickListener(XiaoYunDelegate delegate) {
        DELEGATE = delegate;
    }
    public static SimpleClickListener create(XiaoYunDelegate delegate) {
        return new IndexItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final GoodsDetailDelegate delegate = GoodsDetailDelegate.create();
        DELEGATE.start(delegate);//跳转到商品详情
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
    }
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }
    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
    }
}
