package im.huoshi.asynapi.request;

import android.text.TextUtils;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;

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
    public static void pubInterces(BaseActivity activity, int userId, String content, boolean privacy, long updateAt, String location, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("content", content);
        treeMap.put("privacy", String.valueOf(privacy));
        if (updateAt != 0) {
            treeMap.put("updated_at", String.valueOf(updateAt));
        }
        if (!TextUtils.isEmpty(location)) {
            treeMap.put("position", location);
        }
        RestApiClient.post(getBasePath(RestApiPath.PUB_INTERCES), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 代祷列表
     *
     * @param activity
     * @param userId
     * @param callback
     */
    public static void intercesList(BaseActivity activity, int userId, int startPage, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("start_page", String.valueOf(startPage));
        RestApiClient.post(getBasePath(RestApiPath.INTERCES_LIST), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 代祷详情
     *
     * @param activity
     * @param intercessionId
     * @param callback
     */
    public static void intercesById(BaseActivity activity, int userId, int intercessionId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("intercession_id", String.valueOf(intercessionId));
        RestApiClient.post(getBasePath(RestApiPath.INTERCES_BY_ID), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 验证代祷权限
     *
     * @param activity
     * @param userId
     * @param callback
     */
    public static void verifyPermission(BaseActivity activity, int userId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        RestApiClient.post(getBasePath(RestApiPath.VERIFY_PERMISSION), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 获取代祷评论
     *
     * @param activity
     * @param userId
     * @param intercessionId
     * @param callback
     */
    public static void intercesComments(BaseActivity activity, int userId, int intercessionId, int startPage, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("intercession_id", String.valueOf(intercessionId));
        treeMap.put("start_page", String.valueOf(startPage));
        RestApiClient.post(getBasePath(RestApiPath.INTERCES_COMMENTS), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 加入代祷
     *
     * @param activity
     * @param userId
     * @param intercessionId
     * @param callback
     */
    public static void joinInterces(BaseActivity activity, int userId, int intercessionId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("intercession_id", String.valueOf(intercessionId));
        RestApiClient.post(getBasePath(RestApiPath.JOIN_INTERCES), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 更新代祷
     *
     * @param activity
     * @param userId
     * @param intercessionId
     * @param content
     * @param callback
     */
    public static void updateInterces(BaseActivity activity, int userId, int intercessionId, String content, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("intercession_id", String.valueOf(intercessionId));
        treeMap.put("content", content);
        RestApiClient.post(getBasePath(RestApiPath.UPDATE_INTERCES), buildRequestParams(treeMap), activity, new RestApiHandler() {
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

    public static void comment(BaseActivity activity, int userId, int intercessionId, String content, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("intercession_id", String.valueOf(intercessionId));
        treeMap.put("content", content);
        RestApiClient.post(getBasePath(RestApiPath.PUB_COMMENT), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
     * 评论点赞
     *
     * @param activity
     * @param userId
     * @param commentId
     * @param callback
     */
    public static void praiseComment(BaseActivity activity, int userId, int commentId, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("comment_id", String.valueOf(commentId));
        RestApiClient.post(getBasePath(RestApiPath.PRAISE_COMMENT), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
