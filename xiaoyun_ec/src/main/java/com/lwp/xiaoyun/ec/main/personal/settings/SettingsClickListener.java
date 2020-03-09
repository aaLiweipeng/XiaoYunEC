package com.lwp.xiaoyun.ec.main.personal.settings;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.lwp.xiaoyun.ec.main.personal.list.ListBean;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/10 5:34
 *     desc   : “系统设置”页的点击事件
 * </pre>
 */
public class SettingsClickListener extends SimpleClickListener {

    //接收SettingsDelegate实例 用于跳转
    private final XiaoYunDelegate DELEGATE;

    public SettingsClickListener(XiaoYunDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);

        int id = bean.getId();

        switch (id) {
            case 1:
                //这是消息推送的开关
                break;
            case 2:
                //“关于”的Item
                DELEGATE.getSupportDelegate().start(bean.getDelegate());
                break;
            default:
                break;
        }
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