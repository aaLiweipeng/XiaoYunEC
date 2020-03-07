package com.lwp.xiaoyun.ec.main.personal;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.lwp.xiaoyun.ec.main.personal.list.ListBean;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/6 5:00
 *     desc   : “我的”页面的点击事件
 * </pre>
 */
public class PersonalClickListener extends SimpleClickListener {

    //把“我的”页面 PersonalDelegate 实例，传进来
    private final XiaoYunDelegate DELEGATE;

    public PersonalClickListener(XiaoYunDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        //源码，Listener中有 成员变量baseQuickAdapter，存储了RecyclerView的 Adapter！！！
        // 不同 SimpleClickListener 中 拿到的 Adapter实例及其数据是不一样的！！！
        //这里 拿到对应Item的数据Bean
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);

        //id 来自 PersonalDelegate！！！
        int id = bean.getId();

        switch (id) {
            case 1:
                //拿到 AddressDelegate实例， 在 PersonalDelegate 中已经set过了, 用作 跳转目的地
                DELEGATE.getParentDelegate().getSupportDelegate().start(bean.getDelegate());
                break;
            case 2:
                DELEGATE.getParentDelegate().getSupportDelegate().start(bean.getDelegate());
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
