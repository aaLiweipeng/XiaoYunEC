package com.lwp.xiaoyun.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/14 9:34
 *     desc   : 圆形文本组件
 * </pre>
 */
public class CircleTextView extends AppCompatTextView {

    private final Paint PAINT;
    private final PaintFlagsDrawFilter FILTER;


    public CircleTextView(Context context) {
        this(context, null);
    }
    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        PAINT = new Paint();
        //用于 为画布设置抗锯齿
        FILTER = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        PAINT.setColor(Color.WHITE);//初始颜色
        PAINT.setAntiAlias(true);//抗锯齿
    }

    //设置画笔颜色
    public final void setCircleBackground(@ColorInt int color) {
        PAINT.setColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = getMeasuredWidth();
        final int height = getMaxHeight();
        final int max = Math.max(width, height);
        setMeasuredDimension(max, max);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(FILTER);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2,
                Math.max(getWidth(), getHeight()) / 2, PAINT);
        super.draw(canvas);
    }
}
