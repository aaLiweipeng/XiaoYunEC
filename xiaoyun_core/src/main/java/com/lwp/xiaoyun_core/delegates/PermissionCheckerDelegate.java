package com.lwp.xiaoyun_core.delegates;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lwp.xiaoyun_core.ui.camera.XiaoYunCamera;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 9:43
 *     desc   :引入一个中间层，
 *              用来进行（基于Android 6.0之后的动态权限）权限的判定
 * </pre>
 */
//Register an Activity or Fragment to handle permissions 在框架中注册一个需要处理权限的活动或者碎片
@RuntimePermissions
public abstract class PermissionCheckerDelegate extends BaseDelegate {

    //本方法不是 直接拿来调用的，是用来生成代码的
    // （这里具体实现，在 生成类PermissionCheckerDelegatePermissionsDispatcher中 抽象调用）
    // 动态申请权限成功后，执行本方法
    // .
    // 所以它不能是private static，否则annotationProcessor或者注解 没办法正常读取解析
    //@NeedsPermission注解的意义：
    // Annotate a method which performs the action that requires one or more permissions
    // 注释执行需要一个或多个权限的操作的方法
    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startCamera() {
        // 动态申请权限（相机、读写）成功后！！！ 执行本方法！！！
        XiaoYunCamera.start(this);
    }

    //开始请求权限
    public void startCameraWithCheck() {
        //开始请求权限（相机、读写）
        PermissionCheckerDelegatePermissionsDispatcher.startCameraWithPermissionCheck(this);
    }

    //注解在当用户拒绝了权限请求时需要调用的方法上
    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraDenied() {
        Toast.makeText(getContext(), "不允许拍照", Toast.LENGTH_LONG).show();
    }

    //注解在当用户选中了 授权窗口中的 不再询问复选框 后 并拒绝了权限请求时需要调用的方法，
    // 一般可以向用户解释 为何申请此权限，并根据 实际需求 决定 是否再次弹出权限请求对话框
    @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraNever() {
        Toast.makeText(getContext(), "拒绝使用相机，不再询问，下次使用请在系统设置", Toast.LENGTH_LONG).show();
    }

    //注解在用于向用户解释为什么需要调用该权限的方法上，
    //只有当第一次请求权限被用户拒绝，下次请求权限之前会调用
    @OnShowRationale({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraRationale(PermissionRequest request) {
        showRationaleDialog(request);
    }
    private void showRationaleDialog(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("更新头像需要使用相机或者访问本地相册喔~\n上次被您拒绝了，现在麻烦同意一下权限可以嘛老铁？")
                .show();
    }

    //请求权限的结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //PermissionCheckerDelegatePermissionsDispatcher 是注解自动生成的！！
        PermissionCheckerDelegatePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
