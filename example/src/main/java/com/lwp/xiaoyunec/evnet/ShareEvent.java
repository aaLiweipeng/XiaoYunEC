package com.lwp.xiaoyunec.evnet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun_core.delegates.web.event.Event;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;
import com.mob.MobSDK;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/13 4:50
 *     desc   : 分享事件
 * </pre>
 */
public class ShareEvent extends Event {

    @Override
    public String execute(String params) {

        XiaoYunLogger.d("ShareEvent", params);

        final JSONObject object = JSON.parseObject(params).getJSONObject("params");
        final String title = object.getString("title");
        final String url = object.getString("url");
        final String imageUrl = object.getString("imageUrl");
        final String text = object.getString("text");

        final OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setText(text);
        oks.setImageUrl(imageUrl);
        oks.setUrl(url);
        oks.show(getContext());

        return null;
    }
}

