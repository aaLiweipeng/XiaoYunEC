package com.lwp.xiaoyun.ui.date;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/29 5:20
 *     desc   :
 * </pre>
 */
public class DateDialogUtil {

    //当选择一个日期时，要进行回调的逻辑
    public interface IDateListener {
        /**
         * @param date （格式化后的）被选中的时间
         * @see DateDialogUtil onDateChanged()
         */
        void onDateChange(String date);
    }
    private IDateListener mDateListener = null;
    public void setDateListener(IDateListener listener) {
        mDateListener = listener;
    }

    public void showDialog(Context context) {

        final LinearLayout ll = new LinearLayout(context);

        final DatePicker picker = new DatePicker(context);
        final LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        picker.setLayoutParams(lp);//设置 组件的布局参数

        //初始化时间 以及 设置监听回调
        picker.init(1990, 1, 1, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                //格式化一下时间
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
                final String data = format.format(calendar.getTime());
                if (mDateListener != null) {

                    //抽象调用回调方法 具体逻辑暴露给外部
                    mDateListener.onDateChange(data);
                }
            }
        });

        //组件加入布局
        ll.addView(picker);

        //以上是 初始化布局和Picker，以及配置Picker的回调，
        //下面把这个Picker 配置进Dialog 弹出来
        new AlertDialog.Builder(context)
                .setTitle("选择日期")
                .setView(ll)//设置布局
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })//设置 确定按钮的 监听逻辑
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })//设置 取消按钮的 监听逻辑
                .show();
    }
}
