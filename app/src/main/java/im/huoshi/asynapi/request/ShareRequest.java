package im.huoshi.asynapi.request;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 16/4/5.
 */
public class ShareRequest extends BaseRequest {
    /**
     * 统计分享次数
     *
     * @param activity
     * @param userId
     * @param callback
     */
    public static void shareRecord(BaseActivity activity, int userId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        RestApiClient.post(getBasePath(RestApiPath.SHARE_RECORD), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
