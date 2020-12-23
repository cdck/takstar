package xlk.takstar.paperless.util;

import android.util.Log;

import xlk.takstar.paperless.MyApplication;

/**
 * @author Created by xlk on 2020/11/27.
 * @desc
 */
public class LogUtil {
    public static void d(String tag, String msg) {
        if (MyApplication.isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (MyApplication.isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (MyApplication.isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (MyApplication.isDebug) {
            Log.v(tag, msg);
        }
    }
}
