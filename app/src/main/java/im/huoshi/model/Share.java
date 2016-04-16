package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/4/5.
 */
public class Share extends ApiObject {
    @SerializedName("total_share_times")
    private int totalShareTimes;

    public int getTotalShareTimes() {
        return totalShareTimes;
    }

    public void setTotalShareTimes(int totalShareTimes) {
        this.totalShareTimes = totalShareTimes;
    }
}
