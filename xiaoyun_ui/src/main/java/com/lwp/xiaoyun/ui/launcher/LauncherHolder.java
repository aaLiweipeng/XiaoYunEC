package com.lwp.xiaoyun.ui.launcher;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/24 14:40
 *     desc   : 用来存储 轮播图资源 ，
 *              图片 id 都是 int，
 *              所以这里泛型为 int；
 * </pre>
 */
public class LauncherHolder implements Holder<Integer> {

    //因为这里 轮播图滚动组件的 每一个子View 都是一张图片，
    // 所以这里声明一个ImageView
    private AppCompatImageView mImageView = null;

    /*
        这里的两个重写方法，类似于 RecyclerView.Adapter 的 几个重写方法
     */
    //实现 Item页面的组件
    @Override
    public View createView(Context context) {
        mImageView = new AppCompatImageView(context);
        return mImageView;
    }

    //每次滑动触发的逻辑，为Item页面组件设置数据
    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        //图片 设置为background ， 而不是 src， 方便占据整个屏幕,
        // 数据来自于 LauncherScrollDelegate中
        // mConvenientBanner.setPages(new LauncherHolderCreator(), INTEGERS)的 INTEGERS
        mImageView.setBackgroundResource(data);

    }
}
