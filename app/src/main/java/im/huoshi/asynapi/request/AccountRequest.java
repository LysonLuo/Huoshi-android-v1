package im.huoshi.asynapi.request;

import android.text.TextUtils;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;

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
        TreeMap<String, String> params = new TreeMap<>();
        params.put("phone", phone);
        if (!TextUtils.isEmpty(nationCode)) {
            params.put("nation_code", nationCode);
        }
        RestApiClient.get(getBasePath(RestApiPath.GET_VERIFICATION_CODE), buildRequestParams(params), activity, new RestApiHandler() {
            @Override
            public void onSuccess(String responseString) {
                callback.onSuccess(responseString);
            }

            @Override
            public void onFailure(ApiError apiError) {
                callback.onFailure(apiError);
            }
        });
    }
}
