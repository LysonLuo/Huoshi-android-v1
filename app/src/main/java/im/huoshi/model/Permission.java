package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/3/22.
 */
public class Permission extends ApiObject {
    @SerializedName("is_auth")
    private boolean isAuth;
    private int permission;

    public boolean isAuth() {
        return isAuth;
    }

    public int getPermission() {
        return permission;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
