package xlk.takstar.paperless.util;

import android.util.Log;

import xlk.takstar.paperless.App;

/**
 * @author Created by xlk on 2020/11/27.
 * @desc
 */
public class LogUtil {
    public static void d(String tag, String msg) {
        if (App.isDebug) {
            Log.d("cdck======= " + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (App.isDebug) {
            Log.e("cdck======= " + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (App.isDebug) {
            Log.i("cdck======= " + tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (App.isDebug) {
            Log.v("cdck======= " + tag, msg);
        }
    }
}
