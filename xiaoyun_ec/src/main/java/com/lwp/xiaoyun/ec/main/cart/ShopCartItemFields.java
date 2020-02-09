package com.lwp.xiaoyun.ec.main.cart;

/**
 * 下面这些是具体的业务信息，
 * 就不要整到 MultipleFields中，即也不要整到core模块中
 */

enum ShopCartItemFields {
    TITLE,
    DESC,
    COUNT,
    PRICE,
    IS_SELECTED,
    POSITION
}