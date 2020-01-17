package com.lwp.xiaoyun_core.delegates;

import java.util.Map;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/16 9:44
 *     desc   :
 * </pre>
 */
public abstract class XiaoYunDelegate extends PermissionCheckerDelegate {

    //泛型指的是 类型为 XiaoYunDelegate 即可,
    // 返回父级Delegate
    public <T extends XiaoYunDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }
}
