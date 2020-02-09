package com.lwp.xiaoyun.ec.main;

import android.graphics.Color;

import com.lwp.xiaoyun.ec.main.cart.ShopCartDelegate;
import com.lwp.xiaoyun.ec.main.discover.DiscoverDelegate;
import com.lwp.xiaoyun.ec.main.index.IndexDelegate;
import com.lwp.xiaoyun.ec.main.sort.SortDelegate;
import com.lwp.xiaoyun_core.delegates.bottom.BaseBottomDelegate;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.delegates.bottom.BottomTabBean;
import com.lwp.xiaoyun_core.delegates.bottom.ItemBuilder;

import java.util.LinkedHashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/4 3:23
 *     desc   : 页面容器Fragment 的 业务实现者
 *              作为 Activity 的 第一个正文内容Fragment 启动
 * </pre>
 */
public class EcBottomDelegate extends BaseBottomDelegate {

    //一下三个方法，都是 BaseBottomDelegate 的抽象方法
    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {

        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();

        items.put(new BottomTabBean("{fa-home}", "主页"), new IndexDelegate());
        items.put(new BottomTabBean("{fa-sort}", "分类"), new SortDelegate());
        items.put(new BottomTabBean("{fa-compass}", "发现"), new DiscoverDelegate());
        items.put(new BottomTabBean("{fa-shopping-cart}", "购物车"), new ShopCartDelegate());
        items.put(new BottomTabBean("{fa-user}", "我的"), new IndexDelegate());

        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#ff007FFF");
    }
}
