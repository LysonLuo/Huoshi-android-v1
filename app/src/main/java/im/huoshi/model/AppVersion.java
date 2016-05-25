package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/5/20.
 */
public class AppVersion extends ApiObject {
    @SerializedName("latest_version")
    private String latestVersion;
    private String description;
    @SerializedName("download_url")
    private String downloadUrl;
    @SerializedName("update_at")
    private long updateAt;

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }
}
