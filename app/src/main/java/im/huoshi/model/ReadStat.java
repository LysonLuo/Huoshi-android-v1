package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/2/19.
 */
public class ReadStat extends ApiObject {
    @SerializedName("last_minutes")
    private int lastMinutes;//上次阅读时间
    @SerializedName("continuous_days")
    private int continuousDays;//连续阅读天数
    @SerializedName("total_minutes")
    private int totalMinutes;//总阅读时间
    @SerializedName("yesterday_minutes")
    private int yesterdayMinutes;//昨日阅读时间
    @SerializedName("today_minutes")
    private int todayMinutes;//今日阅读时间
    private String notice;//一句话提示


    public int getContinuousDays() {
        return continuousDays;
    }

    public int getLastMinutes() {
        return lastMinutes;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getYesterdayMinutes() {
        return yesterdayMinutes;
    }

    public int getTodayMinutes() {
        return todayMinutes;
    }

    public String getNotice() {
        return notice;
    }

    public void setContinuousDays(int continuousDays) {
        this.continuousDays = continuousDays;
    }

    public void setLastMinutes(int lastMinutes) {
        this.lastMinutes = lastMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public void setYesterdayMinutes(int yesterdayMinutes) {
        this.yesterdayMinutes = yesterdayMinutes;
    }

    public void setTodayMinutes(int todayMinutes) {
        this.todayMinutes = todayMinutes;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
