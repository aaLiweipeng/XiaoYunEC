package com.lwp.xiaoyun_core.app;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/10/6 1:12
 *     desc   :单例模式，工具类；用于配置文件的存储以及获取
 * </pre>
 */
public class Configurator {

    //建立一个存储 配置信息的 数据结构
    private static final HashMap<String, Object> XIAOYUN_CONFIGS = new HashMap<>();

    //为字体图标库建立一个存储空间
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    private Configurator() {
        XIAOYUN_CONFIGS.put(ConfigType.CONFIG_READY.name(), false);//表示配置开始，false表示没有配置完成
    }

    //获取单例！！！！！！！！！！
    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    //返回 配置数据Map
    final HashMap<String, Object> getXiaoyunConfigs(){
        return XIAOYUN_CONFIGS;
    }

    //静态内部类实现单例模式！！！！！！！！
    private static class Holder {

        //放进单例
        private static final Configurator INSTANCE = new Configurator();
    }

    /**
     * 链式调用配置法！！！！
     * 用with方法 每次返回Configurator实例
     * 链式配置
     *
     * 链式配置 完成后 最后调用configure()，
     * 配置完成位 置位 完成配置！！！
     */
    /**
     * 配置API_HOST，配置参数调用时候传进来
     *
     * @param host
     * @return 返回配置好host的Configurator实例，已进行下一个配置，
     *          可以实现链式调用
     */
    public final Configurator withApiHost(String host) {
        XIAOYUN_CONFIGS.put(ConfigType.API_HOST.name(), host);
        return this;
    }
    //加入自己的字体图标
    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }
    //配置完成时，调用本方法，配置完成位置位
    public final void configure() {
        initIcons();//字体图标库是非常通用的，写在这里保证初始化完成时图标库也初始化完成
        XIAOYUN_CONFIGS.put(ConfigType.CONFIG_READY.name(), true);
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
     * 如果没有完成，要先调用配置方法configure()，
     * 将所有配置配置好再说
     * 保证配置的完整性和正确性
     */
    private void checkConfiguration() {
        final boolean isReady = (boolean) XIAOYUN_CONFIGS.get(ConfigType.CONFIG_READY.name());

        //如果配置没有完成，但是急需做后面的操作（提示调用configure() 保证配置好所有配置 ，将 配置完成位 置位）
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready, please call configure() ");
        }
    }

    /**
     * 获取本配置类中 数据Map 的某个配置
     * 注意配合checkConfiguration()
     *
     * 其中，注解的作用是告诉编译器，
     * 这里return的类型我们是没有检测过的；
     * 去掉注解我们会发现return的地方会出现黄底警告
     *
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Enum<ConfigType> key) {
        checkConfiguration();
        return (T) XIAOYUN_CONFIGS.get(key.name());
    }
}
