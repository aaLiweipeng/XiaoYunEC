package com.lwp.xiaoyun.ec.pay;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun_core.app.ConfigKeys;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.ui.loader.XiaoYunLoader;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;
import com.lwp.xiaoyun_core.wechat.XiaoYunWeChat;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;


public class FastPay implements View.OnClickListener {

    //设置支付回调监听
    private IAlPayResultListener mIAlPayResultListener = null;
    private Activity mActivity = null;

    private AlertDialog mDialog = null;//自定义一个Dialog
    private int mOrderID = -1;

    private FastPay(XiaoYunDelegate delegate) {
        this.mActivity = delegate.getProxyActivity();
        this.mDialog = new AlertDialog.Builder(delegate.getContext()).create();
    }

    //静态工厂方法
    public static FastPay create(XiaoYunDelegate delegate) {
        return new FastPay(delegate);
    }

    //自定义一个Dialog
    public void beginPayDialog() {
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            //加载布局，设置UI、动画
            window.setContentView(R.layout.dialog_pay_panel);//Dialog布局
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);//Dialog动画
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置透明

            //设置 布局属性 —— 先获取 后调整 重新配置（set）进去
            final WindowManager.LayoutParams params = window.getAttributes();//获取原有的属性
            params.width = WindowManager.LayoutParams.MATCH_PARENT;//下面调整它
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);

            //点击事件
            window.findViewById(R.id.btn_dialog_pay_alpay).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pay_wechat).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pay_cancel).setOnClickListener(this);
        }
    }

    public FastPay setPayResultListener(IAlPayResultListener listener) {
        this.mIAlPayResultListener = listener;
        return this;
    }

    public FastPay setOrderId(int orderId) {
        this.mOrderID = orderId;
        return this;
    }

    private void alPay(int orderId) {
        final String singUrl = "你的服务端支付地址" + orderId;
        //获取签名字符串
        RestClient.builder()
                .url(singUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String paySign = JSON.parseObject(response).getString("result");
                        XiaoYunLogger.d("PAY_SIGN", paySign);
                        //必须是异步的调用客户端支付接口
//                        final PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mIAlPayResultListener);
//                        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign);
                    }
                })
                .build()
                .post();
    }

    private void weChatPay(int orderId) {
        XiaoYunLoader.stopLoading();
        final String weChatPrePayUrl = "你的服务端微信预支付地址" + orderId;
        XiaoYunLogger.d("WX_PAY", weChatPrePayUrl);

        final IWXAPI iwxapi = XiaoYunWeChat.getInstance().getWXAPI();
        final String appId = XiaoYun.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
        iwxapi.registerApp(appId);
        RestClient.builder()
                .url(weChatPrePayUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject result =
                                JSON.parseObject(response).getJSONObject("result");
                        final String prepayId = result.getString("prepayid");
                        final String partnerId = result.getString("partnerid");
                        final String packageValue = result.getString("package");
                        final String timestamp = result.getString("timestamp");
                        final String nonceStr = result.getString("noncestr");
                        final String paySign = result.getString("sign");

                        final PayReq payReq = new PayReq();
                        payReq.appId = appId;
                        payReq.prepayId = prepayId;
                        payReq.partnerId = partnerId;
                        payReq.packageValue = packageValue;
                        payReq.timeStamp = timestamp;
                        payReq.nonceStr = nonceStr;
                        payReq.sign = paySign;

                        iwxapi.sendReq(payReq);
                    }
                })
                .build()
                .post();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dialog_pay_alpay) {
            alPay(mOrderID);
            mDialog.cancel();

        } else if (id == R.id.btn_dialog_pay_wechat) {
            weChatPay(mOrderID);
            mDialog.cancel();

        } else if (id == R.id.btn_dialog_pay_cancel) {
            mDialog.cancel();
        }
    }
}
