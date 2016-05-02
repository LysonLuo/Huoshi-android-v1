package im.huoshi.data;

import android.content.Context;
import android.content.SharedPreferences;

import im.huoshi.HuoshiApplication;
import im.huoshi.model.HuoshiData;
import im.huoshi.model.LastHistory;
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
        editor.putInt("yesterday_minutes", readStat.getYesterdayMinutes());
        editor.putInt("today_minutes", readStat.getTodayMinutes());
        editor.putString("notice", readStat.getNotice());
        editor.apply();
    }

    public ReadStat getReadStat() {
        ReadStat readStat = new ReadStat();
        readStat.setLastMinutes(mPreference.getInt("last_minutes", 0));
        readStat.setTotalMinutes(mPreference.getInt("total_minutes", 0));
        readStat.setContinuousDays(mPreference.getInt("continuous_days", 0));
        readStat.setYesterdayMinutes(mPreference.getInt("yesterday_minutes", 0));
        readStat.setTodayMinutes(mPreference.getInt("today_minutes", 0));
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

    /**
     * 保存上次阅读记录
     *
     * @param history
     */
    public void saveLastHistory(LastHistory history) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("book_id", history.getBookId());
        editor.putString("book_name", history.getBookName());
        editor.putInt("chapter_no", history.getChapterNo());
        editor.putInt("section_no", history.getSectionNo());
        editor.putInt("last_position", history.getLastPosition());
        editor.putLong("time", history.getTime());
        editor.apply();
    }

    /**
     * 获取上次阅读记录
     *
     * @return
     */
    public LastHistory getLastHistory() {
        LastHistory lastHistory = new LastHistory();
        lastHistory.setBookId(mPreference.getInt("book_id", 0));
        lastHistory.setBookName(mPreference.getString("book_name", ""));
        lastHistory.setChapterNo(mPreference.getInt("chapter_no", 0));
        lastHistory.setSectionNo(mPreference.getInt("section_no", 0));
        lastHistory.setLastPosition(mPreference.getInt("last_position", 0));
        lastHistory.setTime(mPreference.getLong("time", 0));
        return lastHistory;
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

    public void updateTotalMinutes(int totalMinutes) {
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

    /**
     * 更新昨日阅读时间，直接取今日阅读时间
     */
    public void updateYesterdayMinutes() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("yesterday_minutes", mPreference.getInt("today_minutes", 0));
        editor.apply();
    }

    /**
     * 更新昨日阅读时间，存储服务器返回数据
     *
     * @param yesterdayMinutes
     */
    public void updateYesterdayMinutes(int yesterdayMinutes) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("yesterday_minutes", yesterdayMinutes);
        editor.apply();
    }

    /**
     * 置空昨日阅读时间
     */
    public void clearYesterdayMinutes() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("yesterday_minutes", 0);
        editor.apply();
    }

    /**
     * 更新今日阅读时间，在原有基础上累加
     *
     * @param currentMinutes 本次阅读时间
     */
    public void addTodayMinutes(int currentMinutes) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("today_minutes", mPreference.getInt("today_minutes", 0) + currentMinutes);
        editor.apply();
    }

    /**
     * 更新今日阅读时间，存储服务端返回数据/今日初次阅读时间
     *
     * @param todayMinutes
     */
    public void updateTodayMinutes(int todayMinutes) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("today_minutes", todayMinutes);
        editor.apply();
    }

    /**
     * 置空今日阅读时间，从阅读统计弹框进入的时候调用
     */
    public void clearTodayMinutes(){
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("today_minutes", 0);
        editor.apply();
    }


    public long getLastReadLong() {
        return mPreference.getLong("last_read_long", 0);
    }

    /**
     * 设置今天是否应该累加，遇到这种情况，上一次阅读是从前一天到第二天凌晨的时候，需要这个标志
     */
    public void setTodayShouldAdd(boolean shouldAdd) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean("today_should_add", shouldAdd);
        editor.apply();
    }

    public boolean todayShouldAdd() {
        return mPreference.getBoolean("today_should_add", false);
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

    public void updateTotalShareTimes(int totalShareTimes) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("total_share_times", totalShareTimes);
        editor.apply();
    }

    public int getTotalShareTimes() {
        return mPreference.getInt("total_share_times", 0);
    }

    public void updateTotalJoinIntercession(int totalJoinIntercession) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt("total_join_intercession", totalJoinIntercession);
        editor.apply();
    }

    public int getTotalJoinIntercession() {
        return mPreference.getInt("total_join_intercession", 0);
    }
}
