package com.lwp.xiaoyun_core.app;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.lwp.xiaoyun_core.delegates.web.event.Event;
import com.lwp.xiaoyun_core.delegates.web.event.EventManager;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/6 1:12
 *     desc   :单例模式 + 简化版建造者模式；
 *             有些配置，是初始化的时候配置一次，后续就基本不用怎么修改的，
 *             也有些配置，是属于整个应用级别的，而不归属于某个功能模块，
 *             比如，ICON框架配置、拦截器配置等；
 *             这些配置字段，在这里进行初始化！并且提供一个 Map 来进行存储！
 *             每个 配置字段 以一个 键值对的形式 存储在 这个Map中！
 *
 *             提供一个全局静态final Map 用于存放各种配置数据 以及 其他 数据全局结合实例（如字符数组List、拦截器数组List）；
 *             提供各种配置方法，用于初始化配置时候调用；（基于返回Configurator的this的链式调用设计 以及 各种全局数据集合的 put 实现）；
 *             提供链式配置链尾方法、检查配置是否完成方法；
 *             提供Configurator单例的getter、用于 获取本配置类中 存储配置数据的 Map 的某个元素的getter；
 * </pre>
 */
public class Configurator {

    //建立一个存储!!! 配置信息的 数据结构
    //这里的 Key 不再用String 而是用 Object ，这样一来，
    // 用int、float、Enum、String等任何类型来做key都可以，提高程序灵活性
    private static final HashMap<Object, Object> XIAOYUN_CONFIGS = new HashMap<>();
    //为字体图标库建立一个存储空间
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    //拦截器
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();
    //返回 配置数据Map
    final HashMap<Object, Object> getXiaoyunConfigs(){
        return XIAOYUN_CONFIGS;
    }

    //全局 Handler 实例 <基于主线程！！！> 以便于在子线程中 随时随地把任务 post到主线程处理
    private static final Handler HANDLER = new Handler();


    //表示配置开始，false表示没有配置完成
    private Configurator() {
        XIAOYUN_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        XIAOYUN_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }
    //静态内部类实现单例模式！！！！！！！！
    private static class Holder {
        //放进单例
        private static final Configurator INSTANCE = new Configurator();
    }
    //获取本类单例！！！！！！！！！！
    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 链式调用配置法！！！！
     * 用with方法 每次返回Configurator实例
     *
     * 链式配置 完成后 最后调用configure()，
     * 配置完成位 置位 完成配置！！！
     */
    //配置API_HOST
    public final Configurator withApiHost(String host) {
        XIAOYUN_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }
    //加入自己的字体图标
    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }
    //配置 拦截器
    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        XIAOYUN_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }
    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        XIAOYUN_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }
    public final Configurator withWeChatAppId(String appId) {
        XIAOYUN_CONFIGS.put(ConfigKeys.WE_CHAT_APP_ID, appId);
        return this;
    }
    public final Configurator withWeChatAppSecret(String appSecret) {
        XIAOYUN_CONFIGS.put(ConfigKeys.WE_CHAT_APP_SECRET, appSecret);
        return this;
    }
    public final Configurator withActivity(Activity activity) {
        XIAOYUN_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }
    public final Configurator withJavaScriptInterface(@NonNull String name) {
        //在JavaScriptInterface标志名需要改变的时候，我们设计成在配置项里面改就可以了，
        // 就不用每次都跑去WebDelegate里边改了
        XIAOYUN_CONFIGS.put(ConfigKeys.JAVASCRIPT_INTERFACE, name);
        return this;
    }
    public final Configurator withWebEvent(@NonNull String name, @NonNull Event event) {

        final EventManager manager = EventManager.getInstance();
        //这里自然是使用了 向上转型，即 TestEvent实例 当做 父类Evnent实例 来用
        manager.addEvent(name, event);
        return this;
    }
    //浏览器加载的Host
    public final Configurator withWebHost(String host) {
        XIAOYUN_CONFIGS.put(ConfigKeys.WEB_HOST, host);
        return this;
    }
    //配置完成时，调用本方法，配置完成位置位
    public final void configure() {
        initIcons();//字体图标库是非常通用的，写在这里保证初始化完成时图标库也初始化完成
        XIAOYUN_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
        Utils.init(XiaoYun.getApplicationContext());//初始化工具包库
    }


    //初始化字体图标库空间
    public void initIcons() {
        //如果空间中有字体
        if (ICONS.size() > 0) {
            //取出空间中的第一个
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            //初始化  字体图标库 数组空间中  剩下的项
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }


    /**
     * 功能：检查 配置完成位 ；查看是不是配置完成！！
     *
     * 在获取其他配置位的时候调用，在获取其他配置之前，需要检测配置是否完成，
     * 如果没有完成，要先调用配置方法configure()，将所有配置配置好再说
     * 保证配置的完整性和正确性
     */
    private void checkConfiguration() {
        final boolean isReady = (boolean) XIAOYUN_CONFIGS.get(ConfigKeys.CONFIG_READY);

        //如果配置没有完成，但是急需做后面的操作（提示调用configure() 保证配置好所有配置 ，将 配置完成位 置位）
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready, please call configure() ");
        }
    }
    /**
     * 用于 获取本配置类中 存储配置数据的 Map 的某个元素！！！   注意配合checkConfiguration()！
     *
     * 其中，注解的作用是告诉编译器，这里return的类型我们是没有检测过的；
     * 去掉注解我们会发现return的地方会出现黄底警告
     *
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        //如果配置完成

        final Object value = XIAOYUN_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }

        return (T) XIAOYUN_CONFIGS.get(key);
    }

    //旧版
//    final <T> T getConfiguration(Enum<ConfigKeys> key) {
//        checkConfiguration();
//        return (T) XIAOYUN_CONFIGS.get(key.name());
//    }

}
