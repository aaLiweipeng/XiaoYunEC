package com.lwp.xiaoyun_core.ui;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lwp.xiaoyun_core.R;
import com.lwp.xiaoyun_core.util.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/19 16:51
 *     desc   : 封装 Loader 的显示和取消显示
 *
 *              框架的 加载动画Loader 是直接在 View 中显示出来的，这样子不行，
 *              我们需要整一个自己的 Dialog 去承载这些个Loader！
 *              尽量使用 v7包 中的 compat里面 的东西（AppCompatDialog）！！提高兼容性
 *
 *              另外，需要创建一个 我们自己的style，达到一个 灰色、透明、弹出的效果
 *
 * </pre>
 */
public class XiaoYunLoader {

    // 让 Dialog 缩小到屏幕的 8 倍
    private static final int LOADER_SIZE_SCALE = 8;
    //偏移量控制
    private static final int LOADER_OFFSET_SCALE = 10;
    //存储所有 承载了 Loader 的 Dialog, 方便统一管理，
    // 比如想统一关掉！！所有创建过的 Loader 的时候，只要遍历这个集合，一一关闭即可
    private static final ArrayList<AppCompatDialog> LOADERS = new ArrayList<>();
    //默认的Loader
    private static final String DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator.name();

    /**
     * 创建Dialog并显示！ 重载，兼容枚举！！
     * @param context
     * @param type
     */
    public static void showLoading(Context context, Enum<LoaderStyle> type) {
        showLoading(context, type.name());
    }
    /**
     * 创建Dialog并显示！
     * @param context 对应显示的Context
     * @param type  需要显示的 Loader 名字
     */
    public static void showLoading(Context context, String type) {

        /*
           创建自己的 Dialog 去承载这些个Loader
         */
        //绑定context，设置样式，
        // 创建 Dialog！！！！！
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.dialog);
        //加载对应的 Loader
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type, context);
        //将Loader 直接作为 Dialog 的根视图
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth = DimenUtil.getScreenWidth();
        int deviceHeight = DimenUtil.getScreenHeight();

        final Window dialogWindow = dialog.getWindow();

        if (dialogWindow != null) {
            //设置 Dialog 长宽
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = deviceWidth / LOADER_SIZE_SCALE;
            lp.height = deviceHeight / LOADER_SIZE_SCALE;
            lp.height = lp.height +  deviceHeight / LOADER_OFFSET_SCALE;//添加偏移
            lp.gravity = Gravity.CENTER;
        }
        //每次创建了 Dialog， 都将其加进 Loaders Dialog 集合！
        LOADERS.add(dialog);

        dialog.show();
    }

    /**
     * 创建Dialog并显示！！
     * 重载方法，显示默认的Loader
     * @param context
     */
    public static void showLoading(Context context) {
        showLoading(context, DEFAULT_LOADER);
    }

    //取消Loader显示
    public static void stopLoading() {
        for (AppCompatDialog dialog : LOADERS) {
            //记得判空，没创建的时候是null，会报错！
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        }
    }
}
