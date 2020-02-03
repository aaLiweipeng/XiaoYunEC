package com.lwp.xiaoyun_core.delegates.web.event;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/3 3:12
 *     desc   :
 * </pre>
 */
public interface IEvent {

    //为 abstract class Event 所实现，
    // 即 Event的所有子类，即 所有Concrete事件子class必须 具体实现本方法
    // （事件对应要实现的业务的逻辑 即是 本方法的内容）
    // 具体实现后
    String execute(String params);
}
