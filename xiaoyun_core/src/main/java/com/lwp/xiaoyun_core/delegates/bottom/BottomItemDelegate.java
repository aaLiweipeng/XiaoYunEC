package com.lwp.xiaoyun_core.delegates.bottom;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.lwp.xiaoyun_core.R;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import java.io.PipedReader;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/1 22:22
 *     desc   : 每一个子页面的基类（父类）
 *              每一个子页面都要继承本类
 *              如主页、分类页等
 *
 *              ！！！
 *              作为 容器Fragment（BaseBottomDelegate） 中 FrameLayout 的 所有内容页面的父类
 *
 *              封装基本操作
 * </pre>
 */
public abstract class BottomItemDelegate extends XiaoYunDelegate {

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        //System.currentTimeMillis() - TOUCH_TIME  乃是两次点击 的间隔，
        //即 短时间内点击两次
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            //记录第一次点击的时间
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "双击退出" + XiaoYun.getApplicationContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        //返回true 表示我这里已经把事件消耗掉了（如果返回false 不消耗这个事件）
        return true;
    }
}
