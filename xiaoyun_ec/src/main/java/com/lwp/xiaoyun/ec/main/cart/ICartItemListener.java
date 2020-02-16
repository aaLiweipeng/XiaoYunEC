package com.lwp.xiaoyun.ec.main.cart;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/13 23:52
 *     desc   : 当点击 RecyclerView外部的 全选或者结算等按钮 的时候，
 *              需要把 RecyclerView内部的 总价 给传出去
 *              此时可以利用 接口的回调机制 来实现
 * </pre>
 */
public interface ICartItemListener {
    void onItemClick(double itemTotalPrice);
}
