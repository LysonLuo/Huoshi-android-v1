package im.huoshi.data;

import android.content.Context;
import android.content.SharedPreferences;
import im.huoshi.HuoshiApplication;
import im.huoshi.model.HuoshiData;
import im.huoshi.model.ReadStat;

/**
 * Created by Lyson on 16/2/21.
 */
public class ReadPreference {

    private static ReadPreference mInstance;
    private SharedPreferences mPreference;

    private ReadPreference() {
        Context context = HuoshiApplication.getInstance();
        mPreference = context.getSharedPreferences("read_preference", Context.MODE_PRIVATE);
    }

    public static synchronized ReadPreference getInstance() {
        if (mInstance == null) {
            synchronized (ReadPreference.class) {
                if (mInstance == null) {
                    mInstance = new ReadPreference();
                }
            }
        }
        return mInstance;
    }

    public void saveReadStat(ReadStat readStat) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("last_minutes", readStat.getLastMinutes());
        editor.putInt("total_minutes", readStat.getTotalMinutes());
        editor.putInt("continuous_days", readStat.getContinuousDays());
        editor.putString("notice", readStat.getNotice());
        editor.apply();
    }

    public ReadStat getReadStat() {
        ReadStat readStat = new ReadStat();
        readStat.setLastMinutes(mPreference.getInt("last_minutes", 0));
        readStat.setTotalMinutes(mPreference.getInt("total_minutes", 0));
        readStat.setContinuousDays(mPreference.getInt("continuous_days", 0));
        readStat.setNotice(mPreference.getString("notice", ""));
        return readStat;
    }

    public void saveHuoshiData(HuoshiData huoshiData) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("continuous_interces_days", huoshiData.getContinuousIntercesDays());
        editor.putInt("share_number", huoshiData.getShareNumber());
        editor.putString("share_today", huoshiData.getShareToday());
        editor.apply();
    }

    public HuoshiData getHuoshiData() {
        HuoshiData huoshiData = new HuoshiData();
        huoshiData.setContinuousIntercesDays(mPreference.getInt("continuous_interces_days", 0));
        huoshiData.setShareNumber(mPreference.getInt("share_number", 0));
        huoshiData.setShareToday(mPreference.getString("share_today", ""));
        return huoshiData;
    }

    public void updateAddStat(boolean isAdd) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean("is_add", isAdd);
        editor.apply();
    }

    public boolean getAddStat() {
        return mPreference.getBoolean("is_add", false);
    }

    public void updateLastMinutes(int lastMinutes) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("last_minutes", lastMinutes);
        editor.apply();
    }

    public void updateTotalMinutes() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("total_minutes", mPreference.getInt("last_minutes", 0) + mPreference.getInt("total_minutes", 0));
        editor.apply();
    }

    public void updateTotalMinutes(int totalMinutes){
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("total_minutes", totalMinutes);
        editor.apply();
    }

    public void updateContinuousDays() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("continuous_days", mPreference.getInt("continuous_days", 0) + 1);
        editor.apply();
    }


    public void updateContinuousDays(int newDay) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("continuous_days", newDay);
        editor.apply();
    }

    public void updateLastReadLong(long lastReadLong) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong("last_read_long", lastReadLong);
        editor.apply();
    }

    public void updateContinuousIntercesDays(int newDay) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("continuous_interces_days", newDay);
        editor.apply();
    }

    public long getLastReadLong() {
        return mPreference.getLong("last_read_long", 0);
    }

    public void clearData() {
        String shareToday = mPreference.getString("share_today", "");
        int shareNumber = mPreference.getInt("share_number", 0);
        SharedPreferences.Editor editor = mPreference.edit();
        editor.clear();
        editor.putString("share_today", shareToday);
        editor.putInt("share_number", shareNumber);
        editor.apply();
    }
}
