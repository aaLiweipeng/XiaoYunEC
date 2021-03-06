package com.lwp.xiaoyun_core.util.callback;

import android.support.annotation.Nullable;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/5 6:22
 *     desc   : 全局回调 使用泛型T 响应式编程
 * </pre>
 */
public interface IGlobalCallback<T> {

    //泛型接口，一般回调的时候需要给回调方法传递一些值，
    // 关于值的类型，这里 指为泛型 比 传入Object好很多，
    // 性能、可拓展性、健壮性提高很多
    // @Nullable 可以为空
    void executeCallback(@Nullable T args);
}
