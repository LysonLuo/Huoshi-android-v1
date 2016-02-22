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


    public int getContinuousDays() {
        return continuousDays;
    }

    public int getLastMinutes() {
        return lastMinutes;
    }

    public int getTotalMinutes() {
        return totalMinutes;
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
}
