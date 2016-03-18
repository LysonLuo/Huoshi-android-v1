package im.huoshi.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Lyson on 16/3/17.
 */
public class ScreenUtils {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenWidth(Display display) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
