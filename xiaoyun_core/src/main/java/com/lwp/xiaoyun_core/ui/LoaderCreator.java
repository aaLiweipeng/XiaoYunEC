package com.lwp.xiaoyun_core.ui;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/18 22:23
 *     desc   : 1.封装反射过程(从获取 Loader 名 再去拼接啊去反射加载啊 最后获取Loader实例)；
 *              2.对 反射加载 做一个缓存！框架是利用 反射，！！！
 *
 *              通过取 Loader 的名字来 加载！！ Loader 的；！！！
 *              如果每次请求都去进行 反射 的话，性能不行；
 *              这里通过这个类来进行一个改进；！！
 *              核心是用 WeakHashMap 全局实例 做一个缓存！！！
 *
 * </pre>
 */
public final class LoaderCreator {

    //每一个 Loader 同它的 名字 以及 indicator实例 一一对应,
    // 并且每一个 Loader名字 是唯一的，对应的 Loader 也只有一个，故而用 Map ！！！存储再合适不过！！！！
    //有些实例如果太久没用了，可以先回收，用 Weak ！！，万一再用时，重新加载就是了！！！
    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    /**
     * 传进来 Loader 的 名字， 以及对应应用处的 context，
     * 为 context 创建 AVLoadingIndicatorView 实例；
     * 完成加载/取出 Loader，
     * 把 加载/取出 好的 Loader（indicator） set进 context 对应的 AVLoadingIndicatorView 实例中；
     * 返回这个 AVLoadingIndicatorView 实例！！！
     *
     * @param type
     * @param context
     * @return
     */
    static AVLoadingIndicatorView create(String type, Context context) {

        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);

        //没有加载过放在Map里的，就加载，然后存到 Map 里面，下次传来对应的名字，
        // 从 Map 里面拿就可以了，就不用每次都用反射去加载！ 浪费性能！！ 本类核心！
        if (LOADING_MAP.get(type) == null) {
            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type, indicator);
        }
        //Map 里面有，就是曾经 反射加载 过了，直接取出来用即可！！
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    //根据 Loader 名 返回 indicator 实例
    //封装反射过程(从获取 Loader 名 再去拼接啊去反射加载啊 最后获取Loader实例)；
    private static Indicator getIndicator(String name) {
        //常规判空
        if (name == null || name.isEmpty()) {
            return null;
        }
        //因为这里有很多拼接操作，所以用 StringBuilder 效率会更高一些
        final StringBuilder drawableClassName = new StringBuilder();

        if (!name.contains(".")) {
            //如果不包含点，说明传入的只是 一个类名，没有写 前缀包名！！！

            // 那么先获取 AVLoadingIndicatorView 的包名，即 —— com.wang.avi.这部分
            final String defaultPackageName = AVLoadingIndicatorView.class.getPackage().getName();

            //因为框架的每一个 AVLoading 都是在这么一个包下 —— com.wang.avi.indicators.！！！！
            // 所以这里直接就拼接进来，具体可以查看AVLoadingIndicatorView 的源码！！！
            drawableClassName.append(defaultPackageName)
                    .append("indicators")
                    .append(".");

            //到此完成 "com.wang.avi.indicators." 的拼接，后面再加入传入的 最后类名即可
        }
        drawableClassName.append(name);

        try {
            final Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
