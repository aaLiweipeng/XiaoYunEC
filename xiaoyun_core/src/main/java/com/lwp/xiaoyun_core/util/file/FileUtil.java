package com.lwp.xiaoyun_core.util.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.lwp.xiaoyun_core.app.XiaoYun;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *              参考自 傅令杰
 *     time   : 2019/12/20 14:42
 *     desc   : 文件类相关工具
 * </pre>
 */
public final class FileUtil {

    //格式化的模板
    private static final String TIME_FORMAT = "_yyyyMMdd_HHmmss";

    private static final String SDCARD_DIR =
            Environment.getExternalStorageDirectory().getPath();

    //默认本地上传图片目录
    public static final String UPLOAD_PHOTO_DIR =
            Environment.getExternalStorageDirectory().getPath() + "/a_upload_photos/";

    //网页缓存地址
    public static final String WEB_CACHE_DIR =
            Environment.getExternalStorageDirectory().getPath() + "/app_web_cache/";

    //系统相机目录！！！
    public static final String CAMERA_PHOTO_DIR =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/Camera/";

    //按照 模板 返回 由 文件头 和 当前系统时间 组合而成的 命名字符串
    private static String getTimeFormatName(String timeFormatHeader) {
        final Date date = new Date(System.currentTimeMillis());
        //必须要加上单引号 下面是是格式化模板
        final SimpleDateFormat dateFormat = new SimpleDateFormat("'" + timeFormatHeader + "'" + TIME_FORMAT, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * @param timeFormatHeader 调用者自己指定的文件头(除去时间部分)
     * @param extension         文件的后缀名，同样由 调用者指定
     * @return 返回 模板格式化后 的文件名（指定文件头 + 格式化的时间）！！
     *
     *          模板：文件头_当前时间.后缀
     */
    public static String getFileNameByTime(String timeFormatHeader, String extension) {
        return getTimeFormatName(timeFormatHeader) + "." + extension;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File createDir(String sdcardDirName) {
        //拼接成 指向SD卡目录的 完整的dir
        final String dir = SDCARD_DIR + "/" + sdcardDirName + "/";
        final File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }

    /**
     *
     * @param sdcardDirName 调用者指定的 sd卡根目录的下级目录名
     *                      （sd卡根目录再往下的 用于存储所创建文件目录，
     *                        创建的时候 由调用者 指定）
     * @param fileName  要创建的 文件名
     * @return 在本地sd卡 创建 文件的目录 以及 文件实例句柄，以及返回这个文件句柄
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createFile(String sdcardDirName, String fileName) {

        //在本地sd卡 创建 文件的目录 以及 文件实例句柄，以及返回这个文件句柄
        return new File(createDir(sdcardDirName), fileName);
    }


    private static File createFileByTime(String sdcardDirName, String timeFormatHeader, String extension) {
        //给 文件名前面 添加 时间字符串，组成 最终文件名，
        // 即根据时间创建 文件名
        //返回时间格式化后的文件名（文件头 + 时间 + 文件名）！！！！！！！！
        // （具体的细节，跟上去getFileNameByTime() 的源码 就知道！！)
        final String fileName = getFileNameByTime(timeFormatHeader, extension);

        //在本地sd卡 创建下载文件的目录 以及 文件实例句柄，以及返回这个文件句柄
        return createFile(sdcardDirName, fileName);
    }

    //获取文件的MIME
    public static String getMimeType(String filePath) {
        final String extension = getExtension(filePath);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    //获取文件的后缀名
    public static String getExtension(String filePath) {
        String suffix = "";
        final File file = new File(filePath);
        final String name = file.getName();
        final int idx = name.lastIndexOf('.');
        if (idx > 0) {
            //加不加一的话 应用时调试一下！
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /**
     * 保存Bitmap到SD卡中
     *
     * @param dir      目录名,只需要写自己的相对目录名即可
     * @param compress 压缩比例 100是不压缩,值约小压缩率越高
     * @return 返回该文件
     */
    public static File saveBitmap(Bitmap mBitmap, String dir, int compress) {

        final String sdStatus = Environment.getExternalStorageState();
        // 检测sd是否可用
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File fileName = createFileByTime(dir, "DOWN_LOAD", "jpg");
        try {
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, compress, bos);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {

                if (bos != null) {
                    bos.flush();
                }
                if (bos != null) {
                    bos.close();
                }
                //关闭流
                if (fos != null) {
                    fos.flush();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        refreshDCIM();

        return fileName;
    }


    /**
     * writeToDisk() 的重载
     * 把传进来的 网络文件输入流 ，写入到 本地sd卡中
     *
     * 传入的是 name 的形式（downloadDir +  extension 为空）
     *
     * @param is 从网站下载下来的 面向本地的 文件输入流
     * @param dir 默认指定的 下载文件 在本地sd卡的 目录名
     * @param name 传入的文件名
     * @return 在本地sd卡中 下载完成的 创建好的下载文件目录下的 文件实例句柄
     */
    public static File writeToDisk(InputStream is, String dir, String name) {

        //没有结合格式化文件名模板   创建 文件实例
        // 直接返回 直通 在本地sd卡 创建的下载文件目录下  的文件实例句柄
        final File file = FileUtil.createFile(dir, name);

        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte data[] = new byte[1024 * 4];

            int count;
            while ((count = bis.read(data)) != -1) {
                bos.write(data, 0, count);
            }

            bos.flush();
            fos.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 把传进来的 网络文件输入流 ，写入到 本地sd卡中
     * downloadDir +  extension （name为空）的形式
     *
     * @param is 从网站下载下来的 面向本地的 文件输入流
     * @param dir 指定 下载文件 在本地sd卡的 目录名
     * @param prefix  提供一个文件头 给 加入了时间元素的 格式化文件头的 模块
     *                应用时，传入 extension.toUpperCase() 即可
     * @param extension 文件名
     * @return 在本地sd卡中 下载完成的 创建好的下载文件目录下的 文件实例句柄
     */
    public static File writeToDisk(InputStream is, String dir, String prefix, String extension) {

        //结合格式化文件名模板（文件头 + 时间 + 文件名）  创建 文件实例
        // 返回直通 在本地sd卡 创建的下载文件目录下  的文件实例句柄
        final File file = FileUtil.createFileByTime(dir, prefix, extension);
        //输入流实例
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);

            //把 本地文件 file 句柄  封装成 文件输出流（写入用）
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            //字节缓冲数组
            byte data[] = new byte[1024 * 4];
            //接收 bis.read(data) 返回的 缓冲流长度
            int count;

            //转化/读写：读出 网络文件输入流 -- 转 data 字节缓冲数组 -- 写入 本地文件输出流
            while ((count = bis.read(data)) != -1) {
                //把 文件字节缓冲流 写入本地文件
                bos.write(data, 0, count);
            }

            bos.flush();
            fos.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 通知系统刷新系统相册，使照片展现出来
     */
    private static void refreshDCIM() {
        if (Build.VERSION.SDK_INT >= 19) {
            //兼容android4.4版本，只扫描存放照片的目录
            MediaScannerConnection.scanFile(XiaoYun.getApplicationContext(),
                    new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()},
                    null, null);
        } else {
            //扫描整个SD卡来更新系统图库，当文件很多时用户体验不佳，且不适合4.4以上版本
            XiaoYun.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
                    Environment.getExternalStorageDirectory())));
        }
    }

    /**
     * 读取raw目录中的文件,并返回为字符串
     */
    public static String getRawFile(int id) {
        final InputStream is = XiaoYun.getApplicationContext().getResources().openRawResource(id);
        final BufferedInputStream bis = new BufferedInputStream(is);
        final InputStreamReader isr = new InputStreamReader(bis);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }


    public static void setIconFont(String path, TextView textView) {
        final Typeface typeface = Typeface.createFromAsset(XiaoYun.getApplicationContext().getAssets(), path);
        textView.setTypeface(typeface);
    }

    /**
     * 读取assets目录下的文件,并返回字符串
     */
    public static String getAssetsFile(String name) {
        InputStream is = null;
        BufferedInputStream bis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = null;
        final AssetManager assetManager = XiaoYun.getApplicationContext().getAssets();
        try {
            is = assetManager.open(name);
            bis = new BufferedInputStream(is);
            isr = new InputStreamReader(bis);
            br = new BufferedReader(isr);
            stringBuilder = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (is != null) {
                    is.close();
                }
                assetManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (stringBuilder != null) {
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            final Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}

