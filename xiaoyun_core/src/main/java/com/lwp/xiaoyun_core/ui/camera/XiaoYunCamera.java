package com.lwp.xiaoyun_core.ui.camera;

import android.net.Uri;

import com.lwp.xiaoyun_core.delegates.PermissionCheckerDelegate;
import com.lwp.xiaoyun_core.util.file.FileUtil;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/2 5:37
 *     desc   : 照相机功能模块函数调用等
 * </pre>
 */
public class XiaoYunCamera {

    //需要剪裁的文件
    public static Uri createCropFile() {
        return Uri.parse(FileUtil.createFile("crop_image",
                FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    public static void start(PermissionCheckerDelegate delegate) {
        new CameraHandler(delegate).beginCameraDialog();//弹出Dialog弹框 内容：三个按钮，拍照 选图 取消
    }
}
