package com.lwp.xiaoyun.ui.recycler;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/8 3:43
 *     desc   : BaseRecycleViewAdapterHelper 它要求传入的是一个 int 值，
 *              所以这里就用 int 存储，没用 枚举
 *
 *              用来存储 Item样式 的类
 * </pre>
 */
public class ItemType {

    public static final int TEXT = 1;
    public static final int IMAGE = 2;//只有图的
    public static final int TEXT_IMAGE = 3;//图文并茂
    public static final int BANNER = 4;//轮播图

    public static final int VERTICAL_MENU_LIST = 5;//分类页左侧List

    public static final int SINGLE_BIG_IMAGE = 6;//商品详情页中的图片Delegate
}
