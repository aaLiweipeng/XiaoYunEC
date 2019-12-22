package com.lwp.xiaoyun.ec.launcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.util.timer.BaseTimerTask;
import com.lwp.xiaoyun_core.util.timer.ITimerListener;

import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/22 18:17
 *     desc   :
 * </pre>
 */
public class LauncherDelegate extends XiaoYunDelegate implements ITimerListener {

    //记得要先编译一下，R2才会出来
    @BindView(R2.id.tv_launcher_timer)
    AppCompatTextView mTvTimer = null;

    private Timer mTimer = null;

    @OnClick(R2.id.tv_launcher_timer)
    void onClickTimerView() {

    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task,0,1000);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Override
    public void onTimer() {

    }
}
