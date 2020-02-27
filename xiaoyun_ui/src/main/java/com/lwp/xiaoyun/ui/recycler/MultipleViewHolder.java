package com.lwp.xiaoyun.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/8 6:49
 *     desc   :
 *              提取出来，必要时可以封装业务逻辑
 *              使用 简单工厂模式 返回实例
 * </pre>
 */
public class MultipleViewHolder extends BaseViewHolder {

    MultipleViewHolder(View view) {
        super(view);
    }

    public static MultipleViewHolder create(View view) {
        return new MultipleViewHolder(view);
    }
}
