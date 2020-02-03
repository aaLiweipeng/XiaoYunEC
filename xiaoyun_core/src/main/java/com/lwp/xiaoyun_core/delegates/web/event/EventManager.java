package com.lwp.xiaoyun_core.delegates.web.event;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/3 3:09
 *     desc   : 提供一个 event实例的HashMap
 *              封装event实例的 创建和存储
 * </pre>
 */
public class EventManager {

    //String, Event 对应 事件名，事件实例
    private static final HashMap<String, Event> EVENTS = new HashMap<>();

    //静态内部类单例模式
    private EventManager() {
    }
    private static class Holder {
        private static final EventManager INSTANCE = new EventManager();
    }
    public static EventManager getInstance() {
        return Holder.INSTANCE;
    }

    //添加事件到列表
    // 这里往往是使用了 向上转型，即 concrete子类实例 当做 父类Evnent实例 来用
    //@NonNull 表不能为空
    public EventManager addEvent(@NonNull String name, @NonNull Event event) {
        EVENTS.put(name, event);
        return this;
    }

    //返回事件实例
    public Event createEvent(@NonNull String action) {
        final Event event = EVENTS.get(action);
        if (event == null) {
            return new UndefinedEvent();
        }
        return event;
    }
}

