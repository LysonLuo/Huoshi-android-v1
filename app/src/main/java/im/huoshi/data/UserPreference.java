package im.huoshi.data;

import android.content.Context;
import android.content.SharedPreferences;

import im.huoshi.HuoshiApplication;

/**
 * Created by Lyson on 15/12/24.
 */
public class UserPreference {
    private static UserPreference mInstance;
    private SharedPreferences mPreference;

    private UserPreference() {
        Context context = HuoshiApplication.getInstance();
        mPreference = context.getSharedPreferences("user_preference", Context.MODE_PRIVATE);
    }

    public static synchronized UserPreference getInstance() {
        if (mInstance == null) {
            synchronized (UserPreference.class) {
                if (mInstance == null) {
                    mInstance = new UserPreference();
                }
            }
        }
        return mInstance;
    }
}
