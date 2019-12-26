package com.lwp.xiaoyun.ec.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/26 15:46
 *     desc   : 抽取 ReleaseOpenHelper 业务
 * </pre>
 */
public class DatabaseManager {

    //这里两个都是写完 Entity Rebuild 自动生成的
    private DaoSession mDaoSession = null;
    private UserProfileDao mDao = null;

    private DatabaseManager() {
    }

    public DatabaseManager init(Context context) {
        initDao(context);
        return this;//返回 初始化好 UserProfileDao 的 DatabaseManager
    }

    //静态内部类 单例模式
    private static final class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }
    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    //初始化 UserProfileDao
    private void initDao(Context context) {
        //传入context 和 数据库名称 创建数据库工具句柄
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, "xiaoyun_ec.db");
        //使能句柄
        final Database db = helper.getWritableDb();
        //句柄 新建 会话
        mDaoSession = new DaoMaster(db).newSession();
        mDao = mDaoSession.getUserProfileDao();
    }

    public final UserProfileDao getDao() {
        return mDao;
    }
}
