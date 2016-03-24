package im.huoshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/3/23.
 */
public class Intercession implements Parcelable {
    private String content;
    @SerializedName("intercession_number")
    private int intercessionNumber;
    private String portrait;
    @SerializedName("nick_name")
    private String nickName;
    private long time;
    private int relationship;
    private String position;

    public String getContent() {
        return content;
    }

    public int getIntercessionNumber() {
        return intercessionNumber;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getNickName() {
        return nickName;
    }

    public long getTime() {
        return time;
    }

    public int getRelationship() {
        return relationship;
    }

    public String getPosition() {
        return position;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIntercessionNumber(int intercessionNumber) {
        this.intercessionNumber = intercessionNumber;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeInt(intercessionNumber);
        dest.writeString(portrait);
        dest.writeString(nickName);
        dest.writeLong(time);
        dest.writeInt(relationship);
        dest.writeString(position);
    }

    private static final Parcelable.Creator<Intercession> CREATOR = new Creator<Intercession>() {
        @Override
        public Intercession createFromParcel(Parcel source) {
            Intercession intercession = new Intercession();
            intercession.setContent(source.readString());
            intercession.setIntercessionNumber(source.readInt());
            intercession.setPortrait(source.readString());
            intercession.setNickName(source.readString());
            intercession.setTime(source.readLong());
            intercession.setRelationship(source.readInt());
            intercession.setPosition(source.readString());
            return intercession;
        }

        @Override
        public Intercession[] newArray(int size) {
            return new Intercession[size];
        }
    };
}
