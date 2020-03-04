package com.lwp.xiaoyun_core.util.callback;

import java.util.WeakHashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/5 6:22
 *     desc   : 【回调机制】
 *              集成本工具包，可以在任何位置设置回调！！！
 * </pre>
 */
public class CallbackManager {

    //用来存储所有IGlobalCallback
    private static final WeakHashMap<Object, IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    //静态内部类型单例模式
    private static class Holder {
        private static final CallbackManager INSTANCE = new CallbackManager();
    }
    public static CallbackManager getInstance() {
        return Holder.INSTANCE;
    }

    public CallbackManager addCallback(Object tag, IGlobalCallback callback) {
        CALLBACKS.put(tag, callback);
        return this;
    }
    public IGlobalCallback getCallback(Object tag) {
        return CALLBACKS.get(tag);
    }
}
