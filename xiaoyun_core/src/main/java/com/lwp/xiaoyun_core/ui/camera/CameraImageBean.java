package com.lwp.xiaoyun_core.ui.camera;

import android.net.Uri;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/2 5:39
 *     desc   : 存储中间值
 * </pre>
 */
public final class CameraImageBean {

    private Uri mPath = null;

    //单例模式
    private static final CameraImageBean INSTANCE = new CameraImageBean();
    public static CameraImageBean getInstance(){
        return INSTANCE;
    }

    public Uri getPath() {
        return mPath;
    }

    public void setPath(Uri mPath) {
        this.mPath = mPath;
    }
}
