package im.huoshi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static String getDayBetweenString(long dayLong) {
        int dayBetweenInt = getDayBetween(dayLong);
        switch (dayBetweenInt) {
            case 0:
                return formatToString(dayLong, "HH:MM");
            case 1:
                return "一天前";
            case 2:
                return "两天前";
            case 3:
                return "三天前";
            default:
                return formatToString(dayLong, "yyyy-MM-dd");
        }
    }

    public static String formatToString(long timestamp, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(new Date((timestamp)));
    }

    public static long formatToLong(String timeString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd:HH").parse(timeString).getTime();
    }
}
