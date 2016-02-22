package im.huoshi.data;

import android.content.Context;
import android.content.SharedPreferences;
import im.huoshi.HuoshiApplication;
import im.huoshi.model.User;

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

    public void saveUser(User user) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("userId", user.getUserId());
        editor.putInt("gender", user.getGender());
        editor.putString("nickId", user.getNickId());
        editor.putString("nickName", user.getNickName());
        editor.putString("avatar", user.getAvatar());
        editor.putString("phone", user.getPhone());
        editor.putString("nationCode", user.getNationCode());
        editor.putString("birthday", user.getBirthday());
        editor.putString("believeDate", user.getBelieveDate());
        editor.putString("provinceId", user.getProvinceId());
        editor.putString("cityId", user.getCityId());
        editor.putString("provinceName", user.getProvinceName());
        editor.putString("cityName", user.getCityName());
        editor.apply();
    }

    public User getUser() {
        User user = new User();
        user.setUserId(mPreference.getInt("userId", -1));
        user.setGender(mPreference.getInt("gender", -1));
        user.setNickId(mPreference.getString("nickId", ""));
        user.setNickName(mPreference.getString("nickName", ""));
        user.setAvatar(mPreference.getString("avatar", ""));
        user.setPhone(mPreference.getString("phone", ""));
        user.setNationCode(mPreference.getString("nationCode", ""));
        user.setBirthday(mPreference.getString("birthday", ""));
        user.setBelieveDate(mPreference.getString("believeDate", ""));
        user.setProvinceId(mPreference.getString("provinceId", ""));
        user.setCityId(mPreference.getString("cityId", ""));
        user.setProvinceName(mPreference.getString("provinceName", ""));
        user.setCityName(mPreference.getString("cityName", ""));
        return user;
    }

    public void updateArea(String provinceId, String provinceName, String cityId, String cityName) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString("provinceId", provinceId);
        editor.putString("cityId", cityId);
        editor.putString("provinceName", provinceName);
        editor.putString("cityName", cityName);
        editor.apply();
    }

    public void updateAvatar(String avatar) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString("avatar", avatar);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.clear();
        //清除了用户数据,但是用户已经使用过APP,此时是否第一次使用的标志应该保持存在
        editor.putBoolean("isFirstStart", false);
        editor.commit();
    }
}
