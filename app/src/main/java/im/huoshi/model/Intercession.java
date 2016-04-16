package im.huoshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyson on 16/3/23.
 */
public class Intercession implements Parcelable {
    @SerializedName("user_id")
    private int userId;
    private int gender;
    @SerializedName("intercession_id")
    private int intercessionId;
    @SerializedName("content_list")
    private List<IntercesContent> contentList;
    @SerializedName("intercession_number")
    private int intercessionNumber;
    @SerializedName("avatar")
    private String portrait;
    @SerializedName("nick_name")
    private String nickName;
    private long time;
    private int relationship;
    private String position;
    @SerializedName("intercessors_list")
    private List<Intercessors> intercessorsList;
    @SerializedName("is_interceded")
    private boolean isInterceded;

    public int getUserId() {
        return userId;
    }

    public int getGender() {
        return gender;
    }

    public int getIntercessionId() {
        return intercessionId;
    }

    public List<IntercesContent> getContentList() {
        return contentList;
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

    public List<Intercessors> getIntercessorsList() {
        return intercessorsList;
    }

    public boolean isInterceded() {
        return isInterceded;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setIntercessionId(int intercessionId) {
        this.intercessionId = intercessionId;
    }

    public void setContentList(List<IntercesContent> contentList) {
        this.contentList = contentList;
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

    public void setIntercessorsList(List<Intercessors> intercessorsList) {
        this.intercessorsList = intercessorsList;
    }

    public void setInterceded(boolean interceded) {
        isInterceded = interceded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //注意顺序，一定要按照声明顺序！！！
        dest.writeInt(userId);
        dest.writeInt(gender);
        dest.writeInt(intercessionId);
        dest.writeList(contentList);
        dest.writeInt(intercessionNumber);
        dest.writeString(portrait);
        dest.writeString(nickName);
        dest.writeLong(time);
        dest.writeInt(relationship);
        dest.writeString(position);
        dest.writeList(intercessorsList);
        //不能直接写入boolean https://code.google.com/p/android/issues/detail?id=5973
        dest.writeInt(isInterceded ? 1 : 0);
    }

    public static final Parcelable.Creator<Intercession> CREATOR = new Creator<Intercession>() {
        @Override
        public Intercession createFromParcel(Parcel source) {
            Intercession intercession = new Intercession();
            intercession.setUserId(source.readInt());
            intercession.setGender(source.readInt());
            intercession.setIntercessionId(source.readInt());

            List<IntercesContent> intercesContentList = new ArrayList<>();
            source.readList(intercesContentList, intercession.getClass().getClassLoader());
            intercession.setContentList(intercesContentList);

            intercession.setIntercessionNumber(source.readInt());
            intercession.setPortrait(source.readString());
            intercession.setNickName(source.readString());
            intercession.setTime(source.readLong());
            intercession.setRelationship(source.readInt());
            intercession.setPosition(source.readString());

            List<Intercessors> intercessorsList = new ArrayList<>();
            source.readList(intercessorsList, intercession.getClass().getClassLoader());
            intercession.setIntercessorsList(intercessorsList);
            intercession.setInterceded(source.readInt() == 1 ? true : false);
            return intercession;
        }

        @Override
        public Intercession[] newArray(int size) {
            return new Intercession[size];
        }
    };
}
