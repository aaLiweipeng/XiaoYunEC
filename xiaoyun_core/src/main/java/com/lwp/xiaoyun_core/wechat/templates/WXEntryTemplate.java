package com.lwp.xiaoyun_core.wechat.templates;

import com.lwp.xiaoyun_core.activities.ProxyActivity;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.wechat.BaseWXEntryActivity;
import com.lwp.xiaoyun_core.wechat.XiaoYunWeChat;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 2:46
 *     desc   :
 * </pre>
 */
public class WXEntryTemplate extends BaseWXEntryActivity {

    @Override
    protected void onResume() {
        super.onResume();
        //微信登录完了 会回到一个我们不想回到的页面，就是本模板生成的Activity
        //处理方法：就是把这个 Activity 设置成透明的，等登录完成之后，马上把它Finish掉
        finish();
        overridePendingTransition(0, 0);//设置成 不需要有转场动画，悄悄消失就好
    }

    @Override
    protected void onSignInSuccess(String userInfo) {
        XiaoYunWeChat.getInstance().getSignInCallback().onSignInSuccess(userInfo);
    }
}
