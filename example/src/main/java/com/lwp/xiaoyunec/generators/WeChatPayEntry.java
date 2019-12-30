package com.lwp.xiaoyunec.generators;

import com.lwp.xiaoyun_annotations.PayEntryGenerator;
import com.lwp.xiaoyun_core.wechat.templates.WXPayEntryTemplate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 3:28
 *     desc   :
 * </pre>
 */
@PayEntryGenerator(
        packageName ="com.lwp.xiaoyunec" ,
        payEntryTemplete = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
