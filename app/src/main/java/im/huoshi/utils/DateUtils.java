package im.huoshi.utils;

import java.util.Calendar;

/**
 * Created by Lyson on 16/2/22.
 */
public class DateUtils {
    public static int getDayBetween(long dayLong) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (dayLong + offSet) / 86400000;
        return Math.abs((int) (start - today));
    }
}
