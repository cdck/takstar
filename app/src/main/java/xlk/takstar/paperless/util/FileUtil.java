package xlk.takstar.paperless.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import xlk.takstar.paperless.BuildConfig;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.model.WpsModel;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class FileUtil {
    private static final String TAG = "FileUtil-->";

    public static boolean isDocument(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return isDoc(fileName) || isPPT(fileName) || isXLS(fileName);
    }

    public static boolean isDoc(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return fileName.endsWith(".doc") ||
                fileName.endsWith(".docx") ||
                fileName.endsWith(".txt") ||
                fileName.endsWith(".log") ||
                fileName.endsWith(".pdf");
    }

    public static boolean isPPT(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return fileName.endsWith(".pptx") ||
                fileName.endsWith(".ppt");
    }

    public static boolean isXLS(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return fileName.endsWith(".xls") ||
                fileName.endsWith(".xlsx");
    }

    public static boolean isPicture(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return fileName.endsWith(".jpg") ||
                fileName.endsWith(".png") ||
                fileName.endsWith(".gif") ||
                fileName.endsWith(".img") ||
                fileName.endsWith(".img") ||
                fileName.endsWith(".jpeg");
    }

    public static boolean isVideo(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return fileName.endsWith(".mp4") ||
                fileName.endsWith(".mp3") ||
                fileName.endsWith(".3gp") ||
                fileName.endsWith(".rmvb") ||
                fileName.endsWith(".avi") ||
                fileName.endsWith(".mkv") ||
                fileName.endsWith(".flv");
    }

    public static boolean isOther(String fileName) {
        if (TextUtils.isEmpty(fileName)) return false;
        return !isDocument(fileName) && !isPicture(fileName) && !isVideo(fileName);
    }

    public static void openFile(Context context, String fileName, int mediaid) {
        File file = new File(Constant.download_dir + fileName);
        boolean fileExists = FileUtils.isFileExists(Constant.download_dir + fileName);
        if (fileExists) {
            if (GlobalValue.downloadingFiles.contains(mediaid)) {
                ToastUtils.showShort(R.string.file_downloading);
            } else {
                openFile(context, file);
            }
        } else {
            JniHelper.getInstance().creationFileDownload(Constant.download_dir + fileName,
                    mediaid, 1, 0, Constant.DOWNLOAD_SHOULD_OPEN_FILE);
        }
    }

    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String fileName = file.getName();
        if (FileUtil.isVideo(fileName)) {
            return;
        } else if (FileUtil.isPicture(fileName)) {
            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_PREVIEW_IMAGE).objects(Constant.download_dir + fileName).build());
            return;
        } else if (FileUtil.isDocument(fileName)) {
            //通知注册WPS广播
            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_WPS_RECEIVER).objects(true).build());
            Bundle bundle = new Bundle();
            bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
//            bundle.putBoolean(WpsModel.ENTER_REVISE_MODE, true); // 以修订模式打开文档

            bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 文件关闭时是否发送广播
            bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true); // 文件保存时是否发送广播
            bundle.putBoolean(WpsModel.HOMEKEY_DOWN, true); // 单机home键是否发送广播
            bundle.putBoolean(WpsModel.BACKKEY_DOWN, true); // 单机back键是否发送广播

            bundle.putBoolean(WpsModel.SAVE_PATH, true); // 文件这次保存的路径
            bundle.putString(WpsModel.THIRD_PACKAGE, WpsModel.PackageName.NORMAL); // 第三方应用的包名，用于对改应用合法性的验证
//            bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
//            bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
            intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
            intent.putExtras(bundle);
        }
        uriX(context, intent, Constant.download_dir + fileName);
    }

    /**
     * 抽取成工具方法
     *
     * @param context
     * @param intent
     * @param filepath
     */
    public static void uriX(Context context, Intent intent, String filepath) {
        File file = new File(filepath);
        if (Build.VERSION.SDK_INT > 23) {//android 7.0以上时，URI不能直接暴露
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtils.showShort(R.string.no_wps_software_found);
            e.printStackTrace();
        }
    }


    /**
     * 将BitMap保存到指定目录下
     *
     * @param bitmap
     * @param file
     */
    public static void saveBitmap(Bitmap bitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                LogUtil.e(TAG, "saveBitmap :  回收 --> ");
                bitmap.recycle();
            }
        }
    }

    /**
     * 将文本内容写入到文件中
     */
    public static boolean writeFileFromString(@NonNull File file, @NonNull String content) {
        return writeFileFromString(file, content, false);
    }

    /**
     * 将文本内容写入到文件中
     *
     * @param content 文本内容
     * @param append  =true则进行追加内容，=false则清空再增加内容
     */
    public static boolean writeFileFromString(@NonNull File file, @NonNull String content, boolean append) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
