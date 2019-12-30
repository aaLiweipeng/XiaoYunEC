package com.lwp.xiaoyunec.generators;

import com.lwp.xiaoyun_annotations.EntryGenerator;
import com.lwp.xiaoyun_core.wechat.templates.WXEntryTemplate;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/31 3:27
 *     desc   :
 * </pre>
 */
@EntryGenerator(
        packageName ="com.lwp.xiaoyunec" ,
        entryTemplete = WXEntryTemplate.class
)
public interface WeChatEntry {
}
