package com.lwp.xiaoyun_core.delegates.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun_core.R;
import com.lwp.xiaoyun_core.R2;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/1 22:18
 *     desc   : 页面容器
 * </pre>
 */
public abstract class BaseBottomDelegate extends XiaoYunDelegate implements View.OnClickListener {

    //存储所有的 子Fragment
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();

    //记录当前Fragment的索引
    private int mCurrentDelegate = 0;
    //第一个要显示的Fragment的索引
    private int mIndexDelegate = 0;
    //点击 tab 之后变色
    private int mClickedColor = Color.BLUE;

    @BindView(R2.id.bottom_bar)
    LinearLayoutCompat mBottomBar = null;

    //由 子类 继承并实现 具体的 build逻辑，完成 map 的返回
    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder);
    public abstract int setIndexDelegate();
    //这个注解，告诉编译器 这必须是一个颜色
    @ColorInt
    public abstract int setClickedColor();
    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom;
    }

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
            //把 tab按钮 一个一个加载到 底部bar中
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_item_icon_text_layout, mBottomBar);
            //把刚刚加载好的 底部tab按钮item 取出来
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);

            //！！设置每个 item 的 角标、点击事件！！！
            item.setTag(i);
            item.setOnClickListener(this);

            final IconTextView itemIcon = (IconTextView)item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            final BottomTabBean bean = TAB_BEANS.get(i);
//            初始化数据
            itemIcon.setText(bean.getIcon());
            itemTitle.setText(bean.getTitle());
            if (i == mIndexDelegate) {
                //设置点击后的颜色
                itemIcon.setTextColor(mClickedColor);
                itemTitle.setTextColor(mClickedColor);
            }
        }

//        把Delegate 的ArrayList 转换成 Array
        final SupportFragment[] deleagateArray = ITEM_DELEGATES.toArray(new SupportFragment[size]);
        //动态加载 加载多个同级根Fragment
        loadMultipleRootFragment(R.id.bottom_bar_delegate_container, mIndexDelegate, deleagateArray);
    }

    //重置颜色
    private void resetColor() {
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            final IconTextView itemIcon = (IconTextView)item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            //设置 未点击的颜色
            itemIcon.setTextColor(Color.GRAY);
            itemTitle.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();
        //先重置所有tabItem的颜色，然后使被点击的tabItem变亮
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;
        final IconTextView itemIcon = (IconTextView)item.getChildAt(0);
        itemIcon.setTextColor(mClickedColor);
        final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
        itemTitle.setTextColor(mClickedColor);

        //！！！注意一下 两行代码的 先后顺序
        // showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment)
        // 切换Fragment， 显示 被点击的tabItem对应的Fragment，隐藏 “当前（点击tabItem前）”的Fragment
        showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
        // 接着把 切换后的 被点击的tabItem 的索引，覆盖到 当前（点击tabItem前）Fragment的 索引，
        // 至此事件切换完毕，被点击的tabItem索引 变成 当前索引
        mCurrentDelegate = tag;
    }
}

