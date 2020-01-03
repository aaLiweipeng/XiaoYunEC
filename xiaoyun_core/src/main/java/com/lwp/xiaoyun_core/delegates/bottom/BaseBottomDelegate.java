package com.lwp.xiaoyun_core.delegates.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/1 22:18
 *     desc   : 页面容器
 * </pre>
 */
public abstract class BaseBottomDelegate extends XiaoYunDelegate {

    //存储所有的 子Fragment
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();

    private int mCurrentDelegate = 0;
    private int mIndexDelegate = 0;
    //点击 tab 之后变色
    private int mClickedColor = Color.BLUE;

    //由 子类 继承并实现 具体的 build逻辑，完成 map 的返回
    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder);
    public abstract int setIndexDelegate();
    //这个注解，告诉编译器 这必须是一个颜色
    @ColorInt
    public abstract int setClickedColor();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        if (setClickedColor() != 0) {
            mClickedColor = setClickedColor();
        }

        final ItemBuilder builder = ItemBuilder.builder();
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(builder);
        ITEMS.putAll(items);//把来自子类的 map 加到 本类的全局变量 中
        for (Map.Entry<BottomTabBean, BottomItemDelegate> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final int size = ITEMS.size();
        // 不能直接把 ITEMS.size(); 写在for语句中，
        // 必须先将其提取出来，不然每循环一次都需要 ITEMS.size();一次
        for (int i = 0; i < size; i++) {
//            LayoutInflater.from(getContext(),)
        }
    }
}

