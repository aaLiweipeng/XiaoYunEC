package com.lwp.xiaoyun_core.ui.camera;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.FileUtils;
import com.lwp.xiaoyun_core.R;
import com.lwp.xiaoyun_core.delegates.PermissionCheckerDelegate;
import com.lwp.xiaoyun_core.util.file.FileUtil;

import java.io.File;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/2 5:36
 *     desc   : 照片处理类
 * </pre>
 */
public class CameraHandler implements View.OnClickListener {

    private final AlertDialog DIALOG;
    private final PermissionCheckerDelegate DELEGATE;

    public CameraHandler(PermissionCheckerDelegate delegate) {
        this.DELEGATE = delegate;
        DIALOG = new AlertDialog.Builder(delegate.getContext()).create();
    }

    //弹出Dialog弹框 内容：三个按钮，拍照 选图 取消
    final void beginCameraDialog() {
        DIALOG.show();

        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera_panel);//设置弹框布局
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;//设置幕布黑暗程度
            window.setAttributes(params);

            //设置弹框中 组件的监听
            window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(this);//取消按钮
            window.findViewById(R.id.photodialog_btn_take).setOnClickListener(this);//拍照按钮
            window.findViewById(R.id.photodialog_btn_native).setOnClickListener(this);//本地按钮
        }
    }

    private String getPhotoName() {
        //获取一个 模板格式化后的 文件名（模板：文件头_当前时间.后缀）
        return FileUtil.getFileNameByTime("IMG", "jpg");
    }
    //拍照取图
    public void takePhoto() {
        final String currentPhotoName = getPhotoName();
        //拍照意图
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File 临时文件句柄   临时文件：这里是系统相册目录下的当前文件名的文件临时句柄
        //CAMERA_PHOTO_DIR 系统相册目录
        final File tempFile = new File(FileUtil.CAMERA_PHOTO_DIR, currentPhotoName);

        //兼容7.0及以上的写法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getPath());
            //使用 ContentProvider 的方式
            final Uri uri = DELEGATE.getContext().getContentResolver().
                    insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            //需要讲Uri路径转化为实际路径
            final File realFile =
                    FileUtils.getFileByPath(FileUtil.getRealFilePath(DELEGATE.getContext(), uri));

            final Uri realUri = Uri.fromFile(realFile);
            CameraImageBean.getInstance().setPath(realUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {

            final Uri fileUri = Uri.fromFile(tempFile);
            CameraImageBean.getInstance().setPath(fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }

        //使用startActivityForResult()的形式 启动Activity
        DELEGATE.startActivityForResult(intent, RequestCodes.TAKE_PHOTO);
    }

    //本地取图
    private void pickPhoto() {
        final Intent intent = new Intent();
        intent.setType("image/*");//所有的Image类型
        intent.setAction(Intent.ACTION_GET_CONTENT);//获取内容
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //使用startActivityForResult()的形式 启动Activity
        // createChooser 创建选择器
        DELEGATE.startActivityForResult
                (Intent.createChooser(intent, "选择获取图片的方式"), RequestCodes.PICK_PHOTO);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.photodialog_btn_cancel) {
            DIALOG.cancel();
        } else if (id == R.id.photodialog_btn_take) {
            //拍照取图
            takePhoto();
            DIALOG.cancel();
        } else if (id == R.id.photodialog_btn_native) {
            //本地取图
            pickPhoto();
            DIALOG.cancel();
        }
    }
}
