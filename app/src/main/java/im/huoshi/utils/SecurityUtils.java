package im.huoshi.utils;

import android.util.Base64;

import java.security.MessageDigest;

import im.huoshi.BuildConfig;

/**
 * Created by Lyson on 16/1/15.
 */
public class SecurityUtils {

    public static String base64() {
        byte[] bytes = Base64.encode((BuildConfig.APP_KEY + ":" + BuildConfig.APP_SECRET).getBytes(), Base64.DEFAULT);
        return "Basic " + new String(bytes);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
