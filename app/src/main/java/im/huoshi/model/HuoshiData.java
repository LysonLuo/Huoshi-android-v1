package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/2/22.
 */
public class HuoshiData extends ApiObject {
    @SerializedName("continuous_interces_days")
    private int continuousIntercesDays;
    @SerializedName("share_number")
    private int shareNumber;
    @SerializedName("share_today")
    private String shareToday;

    public int getContinuousIntercesDays() {
        return continuousIntercesDays;
    }


    public int getShareNumber() {
        return shareNumber;
    }

    public String getShareToday() {
        return shareToday;
    }

    public void setContinuousIntercesDays(int continuousIntercesDays) {
        this.continuousIntercesDays = continuousIntercesDays;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public void setShareToday(String shareToday) {
        this.shareToday = shareToday;
    }
}
