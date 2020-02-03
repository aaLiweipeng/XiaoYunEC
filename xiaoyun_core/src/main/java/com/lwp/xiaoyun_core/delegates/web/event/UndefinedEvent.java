package com.lwp.xiaoyun_core.delegates.web.event;

import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/3 21:27
 *     desc   : 未定义的空的事件
 * </pre>
 */
public class UndefinedEvent extends Event {

    @Override
    public String execute(String params) {
        XiaoYunLogger.e("UndefinedEvent",params);
        return null;
    }
}
