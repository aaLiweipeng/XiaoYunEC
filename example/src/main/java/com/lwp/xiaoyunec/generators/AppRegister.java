package com.lwp.xiaoyunec.generators;

import com.lwp.xiaoyun_annotations.AppRegisterGenerator;
import com.lwp.xiaoyun_core.wechat.templates.AppRegisterTemplate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 3:28
 *     desc   :
 * </pre>
 */
@AppRegisterGenerator(
        packageName = "com.lwp.xiaoyunec",
        registerTemplete = AppRegisterTemplate.class
)
public interface AppRegister {
}
