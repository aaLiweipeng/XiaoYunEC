package com.lwp.xiaoyun.ec.main;

import com.lwp.xiaoyun_core.delegates.bottom.BaseBottomDelegate;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.delegates.bottom.BottomTabBean;
import com.lwp.xiaoyun_core.delegates.bottom.ItemBuilder;

import java.util.LinkedHashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/4 3:23
 *     desc   :
 * </pre>
 */
public class EcBottomDelegate extends BaseBottomDelegate {

    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> item = new LinkedHashMap<>();
        return null;
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return 0;
    }
}
