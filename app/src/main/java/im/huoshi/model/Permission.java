package im.huoshi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lyson on 16/3/22.
 */
public class Permission extends ApiObject {
    @SerializedName("is_synced")
    private int isAuth;
    private int permission;

    public int isAuth() {
        return isAuth;
    }

    public int getPermission() {
        return permission;
    }

    public void setAuth(int auth) {
        isAuth = auth;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
