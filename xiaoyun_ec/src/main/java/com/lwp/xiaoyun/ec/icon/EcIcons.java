package com.lwp.xiaoyun.ec.icon;

import com.joanzapata.iconify.Icon;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/12 17:11
 *     desc   :
 * </pre>
 */
public enum EcIcons implements Icon {
    icon_ali_test1('\ue6b8'),
    icon_ali_test2('\ue635');

    private char character;

    EcIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
