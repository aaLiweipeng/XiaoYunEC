package com.lwp.xiaoyun_core.ui.camera;


import com.yalantis.ucrop.UCrop;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/2 5:38
 *     desc   : 请求码存储
 * </pre>
 */
public class RequestCodes {
    public static final int TAKE_PHOTO = 4;//拍照
    public static final int PICK_PHOTO = 5;//选择图片
    public static final int CROP_PHOTO = UCrop.REQUEST_CROP;//剪裁
    public static final int CROP_ERROR = UCrop.RESULT_ERROR;//剪裁错误
    public static final int SCAN = 7;//扫描二维码
}
