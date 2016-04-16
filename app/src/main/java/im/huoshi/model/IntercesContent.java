package im.huoshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/4/7.
 */
public class IntercesContent implements Parcelable {

    @SerializedName("create_time")
    private long createTime;
    private String content;

    public long getCreateTime() {
        return createTime;
    }

    public String getContent() {
        return content;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //注意顺序，一定要按照声明顺序！！！
        dest.writeLong(createTime);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<IntercesContent> CREATOR = new Creator<IntercesContent>() {
        @Override
        public IntercesContent createFromParcel(Parcel source) {
            IntercesContent intercesContent = new IntercesContent();
            intercesContent.setCreateTime(source.readLong());
            intercesContent.setContent(source.readString());
            return intercesContent;
        }

        @Override
        public IntercesContent[] newArray(int size) {
            return new IntercesContent[size];
        }
    };
}
