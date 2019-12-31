package com.lwp.xiaoyun_core.wechat;

import android.app.Activity;

import com.bigkoo.convenientbanner.holder.Holder;
import com.lwp.xiaoyun_core.app.ConfigKeys;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.wechat.callbacks.IWeChatSignInCallback;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 4:31
 *     desc   : 单例模式，初始化微信api 以及 编写登录逻辑
 * </pre>
 */
public class XiaoYunWeChat {
    public static final String APP_ID = XiaoYun.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
    public static final String APP_SECRET = XiaoYun.getConfiguration(ConfigKeys.WE_CHAT_APP_SECRET);

    private final IWXAPI WXAPI;

    private IWeChatSignInCallback mSignInCallback = null;

    private static final class Holder {
        private static final XiaoYunWeChat INSTANCE = new XiaoYunWeChat();
    }

    public static XiaoYunWeChat getInstance() {
        return Holder.INSTANCE;
    }

    //初始化微信API
    private XiaoYunWeChat() {
        final Activity activity = XiaoYun.getConfiguration(ConfigKeys.ACTIVITY);
        WXAPI = WXAPIFactory.createWXAPI(activity, APP_ID, true);//最后一位是 是否要校验
        WXAPI.registerApp(APP_ID);
    }

    public final IWXAPI getWXAPI() {
        return WXAPI;
    }

    public XiaoYunWeChat onSignSuccess(IWeChatSignInCallback callback) {
        //方便 链式配置
        this.mSignInCallback = callback;
        return this;
    }
    public IWeChatSignInCallback getSignInCallback() {
        return mSignInCallback;
    }

    //微信规定的 登录api 的方法
    public final void signIn() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "random_state";
        WXAPI.sendReq(req);
    }

}
