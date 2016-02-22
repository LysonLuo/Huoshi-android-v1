package im.huoshi.asynapi.request;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;

import java.util.TreeMap;

/**
 * Created by Lyson on 16/2/19.
 */
public class InterCesRequest extends BaseRequest {
    /**
     * 发布代祷
     *
     * @param activity
     * @param userId
     * @param content
     * @param privacy
     * @param callback
     */
    public static void pubInterces(BaseActivity activity, int userId, String content, boolean privacy, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("content", content);
        treeMap.put("privacy", String.valueOf(privacy));
        RestApiClient.post(getBasePath(RestApiPath.PUB_INTERCES), buildRequestParams(treeMap), activity, new RestApiHandler() {
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

    /**
     * 代祷列表
     *
     * @param activity
     * @param userId
     * @param callback
     */
    public static void intercesList(BaseActivity activity, int userId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        RestApiClient.post(getBasePath(RestApiPath.INTERCES_LIST), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
