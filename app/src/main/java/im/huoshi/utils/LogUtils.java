package im.huoshi.utils;

import android.util.Log;

import im.huoshi.BuildConfig;

/**
 * 日制工具打印类
 * <p/>
 * Created by Lyson on 15/12/24.
 */
public class LogUtils {
    private static boolean IS_DEBUG = BuildConfig.DEBUG;

    public static void d(String tag, String msg) {
        if (!IS_DEBUG) return;
        Log.d(tag, msg);
    }
}
