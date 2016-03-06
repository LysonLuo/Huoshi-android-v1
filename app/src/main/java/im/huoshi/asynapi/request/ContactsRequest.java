package im.huoshi.asynapi.request;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;

/**
 * Created by Lyson on 16/2/25.
 */
public class ContactsRequest extends BaseRequest {
    public static void asynContacts(BaseActivity activity, int userId, String contacts, final RestApiCallback callback) {
        TreeMap<String, String> treeMap = initParams();
        treeMap.put("user_id", String.valueOf(userId));
        treeMap.put("contacts", contacts);
        RestApiClient.spost(getBasePath(RestApiPath.ASYN_CONTACTS), buildRequestParams(treeMap), activity, new RestApiHandler() {
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
