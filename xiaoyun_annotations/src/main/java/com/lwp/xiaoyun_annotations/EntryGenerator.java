package com.lwp.xiaoyun_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/30 3:46
 *     desc   : 用来传入 包名 以及 微信所需要的模板代码
 *              即绕过微信的包名限制，不用再主程序里面写这些套路的东西
 * </pre>
 */
//这个注解是告诉编译器，这个注解类 使用在 类 上，而不是用在方法或者属性上
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)//告诉编译器在处理本注解类的时候，是在源码阶段处理的；
// 也就是说打包成APK或者运行的时候，就不再使用它了，这个好处就是对性能几乎没有影响；
public @interface EntryGenerator {

    String packageName();

    Class<?> entryTemplete();
}
