package im.huoshi.asynapi.request;

import android.text.TextUtils;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 16/1/15.
 */
public class AccountRequest extends BaseRequest {
    /**
     * 获取验证码
     *
     * @param activity
     * @param phone      手机号
     * @param nationCode 国家代码
     * @param callback   回调
     */
    public static void getVerifyCode(BaseActivity activity, String phone, String nationCode, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("phone", phone);
        if (!TextUtils.isEmpty(nationCode)) {
            treeMap.put("nation_code", nationCode);
        }
        RestApiClient.post(getBasePath(RestApiPath.GET_VERIFICATION_CODE), buildRequestParams(treeMap), activity, new RestApiHandler() {
            @Override
            public void onSuccess(String responseString) {
                callback.onSuccess(responseString);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    /**
     * 注册
     *
     * @param activity
     * @param phone
     * @param nationCode
     * @param smsCode
     * @param password
     * @param callback
     */
    public static void register(BaseActivity activity, String phone, String nationCode, String smsCode, String password, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("phone", phone);
        if (!TextUtils.isEmpty(nationCode)) {
            treeMap.put("nation_code", nationCode);
        }
        treeMap.put("sms_code", smsCode);
        treeMap.put("password", password);
        RestApiClient.post(getBasePath(RestApiPath.USER_REGISTER), buildRequestParams(treeMap), activity, new RestApiHandler() {
            @Override
            public void onSuccess(String responseString) {
                callback.onSuccess(responseString);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    /**
     * 获取七牛上传凭证
     *
     * @param activity
     * @param userId
     * @param callback
     */
    public static void getQiNiuToken(BaseActivity activity, int userId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        RestApiClient.post(getBasePath(RestApiPath.GET_QINIU_TOKEN), buildRequestParams(treeMap), activity, new RestApiHandler() {
            @Override
            public void onSuccess(String responseString) {
                callback.onSuccess(responseString);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public static void login(BaseActivity activity, String phone, String pwd, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("phone", phone);
        treeMap.put("password", pwd);
        RestApiClient.post(getBasePath(RestApiPath.USER_LOGIN), buildRequestParams(treeMap), activity, new RestApiHandler() {
            @Override
            public void onSuccess(String responseString) {
                callback.onSuccess(responseString);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public static void finish(BaseActivity activity, int userId, int gender, String nickName, String birthday, String believeDate, String provinceId, String provinceName, String cityId, String cityName, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("gender", String.valueOf(gender));
        treeMap.put("nick_name", nickName);
        treeMap.put("birthday", birthday);
        treeMap.put("believe_date", believeDate);
        if (!TextUtils.isEmpty(provinceId)) {
            treeMap.put("province_id", provinceId);
            treeMap.put("province_name", provinceName);
            treeMap.put("city_id", cityId);
            treeMap.put("city_name", cityName);
        }
        RestApiClient.post(getBasePath(RestApiPath.USER_DATA), buildRequestParams(treeMap), activity, new RestApiHandler() {
            @Override
            public void onSuccess(String responseString) {
                callback.onSuccess(responseString);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });

    }
}
