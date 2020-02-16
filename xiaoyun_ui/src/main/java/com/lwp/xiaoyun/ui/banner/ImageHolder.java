package com.lwp.xiaoyun.ui.banner;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lwp.xiaoyun_core.R;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/14 9:38
 *     desc   : 提供 Banner页面内容渲染逻辑
 *              泛型填 String ，因为这里图片用的就是一个URL 联网下载
 * </pre>
 */
public class ImageHolder implements Holder<String> {

    private AppCompatImageView mImageView = null;

    //两个必要实现的回调方法 具体意义注释可见 LauncherHolder
    @Override
    public View createView(Context context) {
        mImageView = new AppCompatImageView(context);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context)
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存方式使用全缓存，原始图片和剪裁过后的图片都会被缓存起来
                .dontAnimate()//这里已经有RecyclerView的动画，不需要图片的动画了
                .centerCrop()
                .into(mImageView);
    }
}
