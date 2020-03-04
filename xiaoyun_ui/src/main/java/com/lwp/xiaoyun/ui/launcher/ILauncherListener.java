package com.lwp.xiaoyun.ui.launcher;

import com.lwp.ui.R;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/29 15:53
 *     desc   : Finish 有 登录成功 和 不成功两种状态
 *              这里写关于 它俩的 回调
 *
 *              启动结束时候
 *              在 实现了 IUserChecker 的 匿名内部类中 调用！！！！
 *
 *              AccountManager.checkAccout(new IUserChecker() {
 *                 @Override
 *                 public void onSignIn() {
 *                     if (mILauncherListener != null) {
 *                         mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED); }
 *                 }
 *                 @Override
 *                 public void onNoSignIn() {
 *                     if (mILauncherListener != null) {
 *                         mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED); }
 *                 }
 *             });
 *
 *              启动结束：
 *              1.第一次启动APP，倒计时启动图 和 轮播启动图 都展示完了，便是启动结束
 *              LauncherScrollDelegate.onItemClick()
 *              2.非第一次启动APP，倒计时启动图 展示完了，便是启动结束
 *              LauncherDelegate.checkIsShowScroll()
 * </pre>
 */
public interface ILauncherListener {

    void onLauncherFinish(OnLauncherFinishTag tag);

}


