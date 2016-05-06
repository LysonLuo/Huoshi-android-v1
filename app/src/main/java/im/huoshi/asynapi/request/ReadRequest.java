package im.huoshi.asynapi.request;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 16/2/19.
 */
public class ReadRequest extends BaseRequest {
    /**
     * 阅读统计
     *
     * @param activity
     * @param userId         用户ID
     * @param lastMinutes    上次阅读时间
     * @param totalMinutes   总阅读时间
     * @param isAdd          标志位,true-服务端会保存数据,false-不保存
     * @param continuousDays 持续阅读天数
     * @param callback
     */
    public static void readStat(BaseActivity activity, int userId, int lastMinutes, int yesterdayMinutes, int todayMinutes, int totalMinutes, int continuousDays, boolean isAdd, long lastReadLong, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("last_minutes", String.valueOf(lastMinutes));
        treeMap.put("yesterday_minutes", String.valueOf(yesterdayMinutes));
        treeMap.put("today_minutes", String.valueOf(todayMinutes));
        treeMap.put("total_minutes", String.valueOf(totalMinutes));
        treeMap.put("continuous_days", String.valueOf(continuousDays));
        treeMap.put("is_add", String.valueOf(isAdd));
        treeMap.put("last_read_long", String.valueOf(lastReadLong));
        RestApiClient.post(getBasePath(RestApiPath.READ_STAT), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
