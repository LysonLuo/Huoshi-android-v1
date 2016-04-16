package im.huoshi.utils;

import android.text.TextUtils;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 16/2/1.
 */
public class CTextUtils {
    public static boolean judgetPhone(BaseActivity activity, String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(activity, activity.getString(R.string.text_warn_phone_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() != 11) {
            Toast.makeText(activity, "手机号码长度不对!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean judgetPwd(BaseActivity activity, String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(activity, activity.getString(R.string.text_warn_pwd_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(pwd.length() >= 6 && pwd.length() <= 19)) {
            Toast.makeText(activity, activity.getString(R.string.text_warn_pwd_length), Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]+$");
        Matcher matcher = pattern.matcher(pwd);
        if (!matcher.find()) {
            Toast.makeText(activity, activity.getString(R.string.text_warn_pwd_regex), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static String getRelation(int relation) {
        switch (relation) {
            case 0:
                return "";
            case 1:
                return "朋友";
            case 2:
                return "朋友的朋友";
            case 3:
                return "朋友代祷过";
        }
        return "";
    }
}
