package com.lwp.xiaoyun_core.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.callback.IRequest;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.file.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2019/12/20 11:34
 *     desc   : 具体处理 文件下载任务
 *              文件下载 ，即 把网络文件输入流 转化成 本地文件的过程
 *              另外，封装一个 自动安装 下载好的apk文件的方法
 * </pre>
 */
public class SaveFileTask extends AsyncTask<Object, Void, File> {

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest request, ISuccess success) {
        REQUEST = request;
        SUCCESS = success;
    }

    @Override
    protected File doInBackground(Object... params) {

        String downloadDir = (String) params[0];//指定 下载文件 在本地sd卡的 目录名
        String extension = (String) params[1];
        final ResponseBody body = (ResponseBody) params[2];
        final String name = (String) params[3];// 完整的文件名

        final InputStream is = body.byteStream();

        if (downloadDir == null || downloadDir.equals("")) {
            //默认的下载目录， 记得根据业务修改
            downloadDir = "down_loads";
        }
        if (extension == null || extension.equals("")) {
            extension = "";
        }
        //开始下载文件
        if (name == null) {
            //传入的是 downloadDir +  extension 的形式

            //把传进来的 网络文件输入流 ，写入到 本地sd卡中
            //并返回 在本地sd卡中 下载完成的 创建好的下载文件目录下的 文件实例句柄
            return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
        } else {
            //传入的是 name 的形式（downloadDir +  extension 为空）

            //使用的是 默认的downloadDir 以及 writeToDisk()的重载方法
            return FileUtil.writeToDisk(is, downloadDir, name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null) {
            SUCCESS.onSuccess(file.getParent());
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
    }


    //文件下载经常会有这样的操作，比如 文件更新、下载的是一个 APP更新的apk文件
    //这里实现 直接安装这个 Apk
    private void autoInstallApk(File file) {
        if (FileUtil.getExtension(file.getPath()).equals("apk")) {
            //getExtension 获取文件的后缀名，判断是不是 apk 安装文件
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            //一参为 下载好的 apk 文件，
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            //进行 默认的自动的安装！
            XiaoYun.getApplicationContext().startActivity(install);
        }
    }
}
