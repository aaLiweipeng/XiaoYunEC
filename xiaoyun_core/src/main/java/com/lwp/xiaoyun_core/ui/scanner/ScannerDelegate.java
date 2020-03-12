package com.lwp.xiaoyun_core.ui.scanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.ui.camera.RequestCodes;
import com.lwp.xiaoyun_core.util.callback.CallbackManager;
import com.lwp.xiaoyun_core.util.callback.CallbackType;
import com.lwp.xiaoyun_core.util.callback.IGlobalCallback;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/12 8:55
 *     desc   : 扫描页
 * </pre>
 */
public class ScannerDelegate extends XiaoYunDelegate implements ZBarScannerView.ResultHandler {

    private ScanView mScanView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mScanView == null) {
            mScanView = new ScanView(getContext());
        }
        mScanView.setAutoFocus(true);//设置自动对焦
        mScanView.setResultHandler(this);
    }

    @Override
    public Object setLayout() {
        return mScanView;
    }
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScanView != null) {
            mScanView.startCamera();//开始扫描
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScanView != null) {
            mScanView.stopCameraPreview();//停止预览
            mScanView.stopCamera();//停止扫面
        }
    }

    // 扫描结果的处理
    // 二维码扫描之后 回到这里处理
    @Override
    public void handleResult(Result result) {
        XiaoYunLogger.d("SCAN_RESULT",result.getContents());

        //这个回调在 IndexDelegate的onBindView()中 添加！！！
        @SuppressWarnings("unchecked")
        final IGlobalCallback<String> callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.ON_SCAN);
        //存在回调方法，则执行之
        if (callback != null) {
            callback.executeCallback(result.getContents());
        }

        getSupportDelegate().pop();//处理完二维码扫描事件之后就 将 扫描Fragment页 出栈退出
    }
}
