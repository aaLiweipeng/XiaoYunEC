package com.lwp.xiaoyun.ec.main.index;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ui.recycler.RgbValue;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 15:04
 *     desc   : 自定义一个Behavior
 *              这里需要根据依赖而变化的TagView是Toolbar，
 *              所以直接传入Toolbar给泛型即可；
 * </pre>
 */
public class TranslucentBehavior extends CoordinatorLayout.Behavior<Toolbar> {

    //顶部距离
    private int mDistanceY = 0;
    //颜色变化速率
    private static final int SHOW_SPEED = 3;
    //定义变化的颜色
    private final RgbValue RGB_VALUE = RgbValue.create(255, 85, 60);


    //注意！！！
    // CoordinatorLayout.Behavior 的实例化是一定需要有一个 构造方法的，
    // 并且这里需要这个有两个参数的构造方法，否则会报错，即这里需要一个 布局文件相关的参数AttributeSet attrs，
    // 因为 ——
    // 这里源码中是通过 布局文件 反射 获取到TranslucentBehavior的完整类名，
    // 然后实例化得到实例，再进行联动逻辑处理的,详见CoordinatorLayout源码中的parseBehavior()！

    public TranslucentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //返回要依赖 哪个View 而变化，即返回 被依赖的View
    //这里Toolbar要依赖的自然是界面中的RecyclerView
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull Toolbar child, @NonNull View dependency) {
        return dependency.getId() == R.id.rv_index;
    }

    //返回 true 表示接管事件
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    //用于处理具体逻辑的，这里的child 即 需要根据依赖而变化的TagView
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        //增加滑动距离，每次滑动，距离变化
        // (？上拉RecyclerView时 dy 为正，mDistanceY 不断加，下拉反之？）
        mDistanceY += dy;

        //toolbar的高度
        final int targetHeight = child.getBottom();
        //当处于滑动状态（mDistanceY>0） 并且 距离小于toolbar高度的时候，调整渐变颜色
        if (mDistanceY > 0 && mDistanceY <= targetHeight) {
            final float scale = (float) mDistanceY / targetHeight;
            final float alpha = scale * 255;
            child.setBackgroundColor(Color.argb((int) alpha, RGB_VALUE.red(), RGB_VALUE.green(), RGB_VALUE.blue()));
        } else if (mDistanceY > targetHeight) {
            //滑动距离超过toolbar高度、或者滑动过快
            child.setBackgroundColor(Color.rgb(RGB_VALUE.red(), RGB_VALUE.green(), RGB_VALUE.blue()));
        }
    }
}
