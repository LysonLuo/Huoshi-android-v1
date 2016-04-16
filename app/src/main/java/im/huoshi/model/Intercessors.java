package im.huoshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/4/7.
 */
public class Intercessors implements Parcelable {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("nick_name")
    private String nickName;

    public int getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //注意顺序，一定要按照声明顺序！！！
        dest.writeInt(userId);
        dest.writeString(nickName);
    }

    public static final Parcelable.Creator<Intercessors> CREATOR = new Creator<Intercessors>() {
        @Override
        public Intercessors createFromParcel(Parcel source) {
            Intercessors intercessors = new Intercessors();
            intercessors.setUserId(source.readInt());
            intercessors.setNickName(source.readString());
            return intercessors;
        }

        @Override
        public Intercessors[] newArray(int size) {
            return new Intercessors[size];
        }
    };
}
