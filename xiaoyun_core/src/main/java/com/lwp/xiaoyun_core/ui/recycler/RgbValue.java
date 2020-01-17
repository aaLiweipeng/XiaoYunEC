package com.lwp.xiaoyun_core.ui.recycler;

import android.telephony.mbms.MbmsErrors;

import com.google.auto.value.AutoValue;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 15:30
 *     desc   : 存储颜色的Bean
 * </pre>
 */
@AutoValue
public abstract class RgbValue {

    public abstract int red();

    public abstract int green();

    public abstract int blue();
    //写到这里就build一下，会自动生成 AutoValue_RgbValue类

    public static RgbValue create(int red, int green, int blue) {
        return new AutoValue_RgbValue(red, green, blue);
    }
}
