package com.lwp.xiaoyun.ec.main.personal.order;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ui.widget.AutoPhotoLayout;
import com.lwp.xiaoyun.ui.widget.StarLayout;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.util.callback.CallbackManager;
import com.lwp.xiaoyun_core.util.callback.CallbackType;
import com.lwp.xiaoyun_core.util.callback.IGlobalCallback;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/10 6:23
 *     desc   : 评价晒单页面
 * </pre>
 */
public class OrderCommentDelegate extends XiaoYunDelegate {

    @BindView(R2.id.custom_star_layout)
    StarLayout mStarLayout = null;
    @BindView(R2.id.custom_auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout = null;

    //提交 按钮
    @OnClick(R2.id.top_tv_comment_commit)
    void onClickSubmit() {
        Toast.makeText(getContext(), "评分： " + mStarLayout.getStarCount(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_comment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        mAutoPhotoLayout.setDelegate(this);

        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(@Nullable Uri args) {
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
    }
}

