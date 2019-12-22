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

import java.text.MessageFormat;
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

    //计时任务 没开始时！ 或者 任务结束时！！！，
    // 都将之置为 null ！！
    //要用的时候 调用 initTimer() 进行初始化即可
    private Timer mTimer = null;
    //倒计时 5s
    private int mCount = 5;

    @OnClick(R2.id.tv_launcher_timer)
    void onClickTimerView() {

    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        //立即执行 每秒执行一次
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
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //每次执行的 任务单元

                if (mTvTimer != null) {
                    mTvTimer.setText(MessageFormat.format("跳过\n{0}s",mCount));
                    mCount--;
                    if (mCount < 0) {
                        //倒计时 数完了 技术任务就可以取消了
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                    }
                }
            }
        });
    }
}
