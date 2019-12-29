package com.lwp.xiaoyun.ec.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lwp.xiaoyun.ec.database.DatabaseManager;
import com.lwp.xiaoyun.ec.database.UserProfile;
import com.lwp.xiaoyun_core.app.AccountManager;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;


/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/26 16:19
 *     desc   : 登录、注册成功时候 回调
 * </pre>
 */
public class SignHandler {

    public static void onSignIn(String response, ISignListener signListener) {

        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long userId = profileJson.getLong("userId");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");

        final UserProfile profile = new UserProfile(userId, name, avatar, gender, address);
        //把数据插入到 数据库 中
        DatabaseManager.getInstance().getDao().insert(profile);
        XiaoYunLogger.v("SIGNHANDLER_INSERT", "插入数据库成功");

        //保存用户状态！！！！已经注册 并登录成功了！！
        AccountManager.setSignState(true);

        //登录成功 的回调
        signListener.onSignInSuccess();
    }

    //加载 注册页面网络请求 返回的json 并将之插入数据库
    public static void onSignUp(String response, ISignListener signListener) {

        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
        final long userId = profileJson.getLong("userId");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");

        final UserProfile profile = new UserProfile(userId, name, avatar, gender, address);
        //把数据插入到 数据库 中
        DatabaseManager.getInstance().getDao().insert(profile);
        XiaoYunLogger.v("SIGNHANDLER_INSERT", "插入数据库成功");

        //保存用户状态！！！！已经注册 并登录成功了！！
        AccountManager.setSignState(true);

        //注册成功 的回调
        signListener.onSignUpSuccess();
    }
}
