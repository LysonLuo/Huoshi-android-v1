package im.huoshi.asynapi.request;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 16/2/22.
 */
public class HuoshiRequest extends BaseRequest {
    public static void tab(BaseActivity activity, int userId, int continuousIntercesDays, long lastIntercesTime, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("last_interces_time", String.valueOf(lastIntercesTime));
        treeMap.put("continuous_interces_days", String.valueOf(continuousIntercesDays));
        RestApiClient.post(getBasePath(RestApiPath.HUOSHI_TAB), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 获取今日一问
     *
     * @param activity
     * @param callback
     */
    public static void getDailyAsked(BaseActivity activity, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        RestApiClient.post(getBasePath(RestApiPath.DAILY_ASKED), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
