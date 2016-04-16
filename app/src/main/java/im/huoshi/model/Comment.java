package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/4/1.
 */
public class Comment extends ApiObject {
    @SerializedName("comment_id")
    private int commentId;
    @SerializedName("user_id")
    private int userId;
    private String content;
    @SerializedName("created_at")
    private long createAt;
    private String avatar;
    @SerializedName("nick_name")
    private String nickName;
    @SerializedName("praise_number")
    private int praiseNumber;
    @SerializedName("is_praised")
    private int isPraised;
    private int gender;

    public int getCommentId() {
        return commentId;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public long getCreateAt() {
        return createAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public int getIsPraised() {
        return isPraised;
    }

    public int getGender() {
        return gender;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public void setPraised(int praised) {
        isPraised = praised;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
