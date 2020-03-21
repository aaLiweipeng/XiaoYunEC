package com.lwp.xiaoyun.ec.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/19 14:56
 *     desc   :
 *
 *     FragmentStatePagerAdapter 相较于 PagerAdapter 不会保留每一个Pager的状态，
 *     当页面销毁之后相关的页面数据也会随之销毁，所以在这里很适用于商品详情，
 *     因为商品详情页的数据属于一次性的，比较需要及时更新，所以每次退出之后数据需要及时的销毁，
 *     否则可能再用户快速操作的时候出现重复或者得不到更新，出现bug;
 *     而且数据也不需要做什么缓存和保存；
 *
 * </pre>
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    //一个元素为 一个tab名
    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    //一个元素为 一个tab下对应的 一个图片url数组
    private final ArrayList<ArrayList<String>> PICTURES = new ArrayList<>();

    //构造方法，接收一个FragmentManager 和一个Json数据，
    // 将 JSON数据全数转换存储起来，数据比较简单，不用DataConverter
    public TabPagerAdapter(FragmentManager fm, JSONObject data) {
        super(fm);

        //获取tabs信息，注意，这里的tabs是一条信息；
        // 查看数据格式，看到 tabs键下是一个JSONArray，
        // JSONArray中的每一个JSONObject 有 name、picture两个键；
        // name 键下为一个字符串； picture键下为 一个url字符串数组
        final JSONArray tabs = data.getJSONArray("tabs");
        final int size = tabs.size();
        for (int i = 0; i < size; i++) {
            final JSONObject eachTab = tabs.getJSONObject(i);

            //tab 名字
            final String name = eachTab.getString("name");
            //url字符串数组
            final JSONArray pictureUrls = eachTab.getJSONArray("pictures");
            final ArrayList<String> eachTabPicturesArray = new ArrayList<>();

            //存储tab下 对应的 图片url数组
            final int pictureSize = pictureUrls.size();
            for (int j = 0; j < pictureSize; j++) {
                eachTabPicturesArray.add(pictureUrls.getString(j));
            }

            TAB_TITLES.add(name);
            PICTURES.add(eachTabPicturesArray);
        }
    }

    //对Item进行加载，因为这里数据只有两个页面，就只写两个了
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return ImageDelegate.create(PICTURES.get(0));
        } else if (position == 1) {
            return ImageDelegate.create(PICTURES.get(1));
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES.get(position);
    }
}
