package im.huoshi.asynapi.request;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;

import java.util.TreeMap;

/**
 * Created by Lyson on 16/2/22.
 */
public class HuoshiRequest extends BaseRequest {
    public static void tab(BaseActivity activity, int userId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        RestApiClient.post(getBasePath(RestApiPath.HUOSHI_TAB), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
