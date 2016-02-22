package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/1/27.
 */
public class User extends ApiObject {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("nation_code")
    private String nationCode;
    private String phone;
    @SerializedName("nick_id")
    private String nickId;
    @SerializedName("nick_name")
    private String nickName;
    private int gender;
    private String avatar;
    private String birthday;
    @SerializedName("believe_date")
    private String believeDate;
    @SerializedName("province_id")
    private String provinceId;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("province_name")
    private String provinceName;
    @SerializedName("city_name")
    private String cityName;

    public int getUserId() {
        return userId;
    }

    public String getNationCode() {
        return nationCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getNickId() {
        return nickId;
    }

    public String getNickName() {
        return nickName;
    }

    public int getGender() {
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBelieveDate() {
        return believeDate;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNickId(String nickId) {
        this.nickId = nickId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setBelieveDate(String believeDate) {
        this.believeDate = believeDate;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
