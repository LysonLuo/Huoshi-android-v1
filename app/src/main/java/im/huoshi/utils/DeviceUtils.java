package im.huoshi.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import im.huoshi.HuoshiApplication;

/**
 * 设备信息工具类
 * <p/>
 * Created by Lyson on 15/12/30.
 */
public class DeviceUtils {

    /**
     * 获取屏幕宽度
     *
     * @param act
     * @return
     */
    public static int getScreenWidth(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * dip转pixel
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *
     * @return
     */
    public static String getImeiCode() {
        TelephonyManager telephonyManager = (TelephonyManager) HuoshiApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
