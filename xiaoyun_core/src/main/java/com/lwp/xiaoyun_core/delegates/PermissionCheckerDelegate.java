package com.lwp.xiaoyun_core.delegates;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lwp.xiaoyun_core.ui.camera.CameraImageBean;
import com.lwp.xiaoyun_core.ui.camera.RequestCodes;
import com.lwp.xiaoyun_core.ui.camera.XiaoYunCamera;
import com.lwp.xiaoyun_core.ui.scanner.ScannerDelegate;
import com.lwp.xiaoyun_core.util.callback.CallbackManager;
import com.lwp.xiaoyun_core.util.callback.CallbackType;
import com.lwp.xiaoyun_core.util.callback.IGlobalCallback;
import com.yalantis.ucrop.UCrop;

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
    // 动态申请权限成功后，执行本方法！！！！
    // .
    // 所以它不能是private static，否则annotationProcessor或者注解 没办法正常读取解析
    //@NeedsPermission注解的意义：
    // Annotate a method which performs the action that requires one or more permissions
    // 注释执行需要一个或多个权限的操作的方法
    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startCamera() {
        // 动态申请权限（相机、读写）成功后！！！ 执行本方法！！！

        //弹出Dialog弹框 内容：三个按钮，拍照 选图 取消
        XiaoYunCamera.start(this);
    }
    //开始请求权限，
    //在进行 需要权限的操作时！！！ 调用；
    // 如项目中 点击个人具体信息页的头像时！
    public void startCameraWithCheck() {
        //开始请求权限（相机、读写）
        PermissionCheckerDelegatePermissionsDispatcher.startCameraWithPermissionCheck(this);
    }


    //扫描二维码(不直接调用)
    //本方法不是 直接拿来调用的，是用来生成代码的
    // （这里具体实现，在 生成类PermissionCheckerDelegatePermissionsDispatcher中 抽象调用）
    // 动态申请权限成功后，执行本方法！！！！
    // .   类同startCamera()
    @NeedsPermission(Manifest.permission.CAMERA)
    void startScan(BaseDelegate delegate) {
        // 动态申请权限（相机）成功后！！！ 执行本方法！！！

        //从 ndexDelegate的ParentDelegate 跳转到 ScannerDelegate（主页到扫描页）
        delegate.getSupportDelegate().startForResult(new ScannerDelegate(), RequestCodes.SCAN);
    }
    public void startScanWithCheck(BaseDelegate delegate) {
        //BaseDelegate delegate 传进来 IndexDelegate的ParentDelegate实例

        //开始请求权限（相机）
        PermissionCheckerDelegatePermissionsDispatcher.startScanWithPermissionCheck(this, delegate);
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

    // 本方法是对应 startActivityForResult()的回调
    // 前两个case 与 CameraHandler中的 DELEGATE.startActivityForResult() 相对应
    // .
    // 后两个case 与 前两个case中的 UCrop.start()相对应，
    // 实际上 UCrop.start()源码中，封装了startActivityForResult()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case RequestCodes.TAKE_PHOTO:
                    //获取到 存储照片的 临时文件路径
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    UCrop.of(resultUri, resultUri)//一参为 欲剪裁图片的路径，二参为 放置剪切完图片的路径
                            .withMaxResultSize(400, 400)
                            .start(getContext(), this);//start()用意看源码
                    break;

                case RequestCodes.PICK_PHOTO:

                    if (data != null) {
                        final Uri pickPath = data.getData();//拿到用户选择的图片的路径

                        //从相册选择后 需要有个路径 来存放 剪裁过的图片
                        final String pickCropPath = XiaoYunCamera.createCropFile().getPath();

                        UCrop.of(pickPath, Uri.parse(pickCropPath))
                                .withMaxResultSize(400, 400)
                                .start(getContext(), this);
                    }
                    break;

                case RequestCodes.CROP_PHOTO:

                    final Uri cropUri = UCrop.getOutput(data);

                    //拿到剪裁后的图片，进行后续的处理，
                    // 处理的内容通过接口回调暴露给别处具体实现，这里仅负责 抽象调用
                    @SuppressWarnings("unchecked")
                    final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);//拿到回调接口
                    if (callback != null) {
                        callback.executeCallback(cropUri);//执行回调接口方法，进行后续处理
                    }
                    break;

                case RequestCodes.CROP_ERROR:
                    Toast.makeText(getContext(), "剪裁出错", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
