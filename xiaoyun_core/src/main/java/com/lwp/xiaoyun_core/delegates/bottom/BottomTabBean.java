package com.lwp.xiaoyun_core.delegates.bottom;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/3 1:23
 *     desc   :
 * </pre>
 */
public final class BottomTabBean {

    private final CharSequence ICON;
    private final CharSequence TITLE;

    public BottomTabBean(CharSequence icon, CharSequence title) {
        ICON = icon;
        TITLE = title;
    }

    public CharSequence getIcon() {
        return ICON;
    }
    public CharSequence getTitle() {
        return TITLE;
    }
}
