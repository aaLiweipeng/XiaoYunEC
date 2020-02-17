package com.lwp.xiaoyun.ec.pay;

/**
 * 支付状态的接口
 *
 * 支付成功
 * 支付中
 * 支付失败
 * 用户取消
 * 支付时出现异常
 */

public interface IAlPayResultListener {

    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();
}
