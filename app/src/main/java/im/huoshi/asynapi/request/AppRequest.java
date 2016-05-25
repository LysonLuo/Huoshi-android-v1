package im.huoshi.asynapi.request;

import java.util.TreeMap;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.common.RestApiClient;
import im.huoshi.asynapi.common.RestApiPath;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.base.BaseActivity;

/**
 * app管理相关请求
 * <p>
 * Created by Lyson on 16/5/19.
 */
public class AppRequest extends BaseRequest {

    /**
     * 获取最新版本
     *
     * @param baseActivity
     * @param callback
     */
    public static void getVersion(BaseActivity baseActivity, final RestApiCallback callback) {
        TreeMap treeMap = initParams();
        RestApiClient.post(getBasePath(RestApiPath.GET_VERSION), buildRequestParams(treeMap), baseActivity, new RestApiHandler() {
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
