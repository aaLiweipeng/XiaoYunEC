package com.lwp.xiaoyun.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.ui.R;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/10 6:38
 *     desc   : 自定义控件 —— 星级评分
 * </pre>
 */
public class StarLayout extends LinearLayoutCompat implements View.OnClickListener {

    //空心星星图标
    private static final CharSequence ICON_UN_SELECT = "{fa-star-o}";
    //实心星星图标
    private static final CharSequence ICON_SELECTED = "{fa-star}";
    //默认5个星星上限，也是 STARS 存储星星实例的数量 见initStarIcon()
    private static final int STAR_TOTAL_COUNT = 5;
    //用来 存储 本类实例（LinearLayout）中的 所有星星实例
    private final ArrayList<IconTextView> STARS = new ArrayList<>();

    //处理成 只调用 一个构造方法
    public StarLayout(Context context) {
        this(context, null);
    }
    public StarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public StarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化 星星图标实例
        initStarIcon();
    }


    //初始化 星星图标实例
    // 注意星星默认为不选中状态 默认为空心
    private void initStarIcon() {

        //循环创建 STAR_TOTAL_COUNT个 星星图标实例
        for (int i = 0; i < STAR_TOTAL_COUNT; i++) {
            //星星实例 本质是一个图标组件IconTextView
            final IconTextView star = new IconTextView(getContext());

            //配置布局属性
            star.setGravity(Gravity.CENTER);
            final LayoutParams lp =
                    new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            star.setLayoutParams(lp);

            star.setText(ICON_UN_SELECT);//初始化 默认为 空心星星图标
            star.setTag(R.id.star_count, i);//设置 星星图标 自己的序号
            star.setTag(R.id.star_is_select, false);//设置 星星图标 的选中状态（默认不选中）
            star.setTextSize(26.0f);
            star.setOnClickListener(this);

            //创建好的 星星图标实例 加到ArrayList中，方便 UI控制
            STARS.add(star);

            //直接把星星加进this中！！即本类StarLayout实例中！
            // 即LinearLayoutCompat！（本类是一个 LinearLayout）
            this.addView(star);
        }
    }

    //获得 本类实例（LinearLayout）中的被选中星星的 个数
    public int getStarCount() {
        int count = 0;
        //通过存储星星实例的 ArrayList
        // 遍历 本类实例（LinearLayout）中的 所有星星实例
        for (int i = 0; i < STAR_TOTAL_COUNT; i++) {
            final IconTextView star = STARS.get(i);//星星实例

            final boolean isSelect = (boolean) star.getTag(R.id.star_is_select);//获取选中状态
            //有被选中的就累加
            if (isSelect) {
                count++;
            }
        }
        return count;
    }

    // 点到第count号星星，它是未选中时
    // 从 0号 到 count号 的 星星图标 改成选中状态
    private void selectStar(int count) {
        for (int i = 0; i <= count; i++) {
            final IconTextView star = STARS.get(i);
            star.setText(ICON_SELECTED);//设置为 实心的星星图标
            star.setTextColor(Color.RED);
            star.setTag(R.id.star_is_select, true);//选中状态 置位
        }
    }

    // 点到第count号星星，它是选中时（证明从0到count已经是选中状态了）
    //从 count +1 到 最后一个 星星，改成未选中状态
    private void unSelectStar(int count) {
        for (int i = 0; i < STAR_TOTAL_COUNT; i++) {
            if (i > count) {
                final IconTextView star = STARS.get(i);
                star.setText(ICON_UN_SELECT);
                star.setTextColor(Color.GRAY);
                star.setTag(R.id.star_is_select, false);
            }
        }
    }

    @Override
    public void onClick(View v) {

        final IconTextView star = (IconTextView) v;

        //获取第几个星星
        final int count = (int) star.getTag(R.id.star_count);
        //获取星星的点击状态
        final boolean isSelect = (boolean) star.getTag(R.id.star_is_select);

        if (!isSelect) {
            // 点到第count号星星，它是未选中时
            selectStar(count);
        } else {
            // 点到第count号星星，它是选中时（证明从0到count已经是选中状态了）
            unSelectStar(count);
        }
    }
}
