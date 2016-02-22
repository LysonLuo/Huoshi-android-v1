package im.huoshi.asynapi.common;

import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import im.huoshi.BuildConfig;
import im.huoshi.asynapi.handler.RestApiHandler;
import im.huoshi.asynapi.handler.RestApiTextHttpHandler;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.SecurityUtils;

/**
 * Created by Lyson on 16/1/4.
 */
public class RestApiClient {
    private static final String LOG_TAG = RestApiClient.class.getSimpleName();

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void addCustomHeader() {
        client.addHeader("Authorization", SecurityUtils.base64());
    }

    public static void get(final String path, RequestParams params, BaseActivity activity, final RestApiHandler handler) {
        if (!checkNetWork()) {
            handler.onFailure(ApiError.errorFromString("请求失败，请重试！", 0));
            return;
        }
        if (params == null) {
            params = new RequestParams();
        }
        addCustomHeader();
        client.get(BuildConfig.API_URI + path, params, new RestApiTextHttpHandler(activity) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                dispatchSuccessResult(path, responseString, handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseString = responseString.replace("[","").replace("]","");
                dispatchFailureResult(path, statusCode, responseString, handler);
            }
        });
    }


    public static void post(final String path, RequestParams params, final BaseActivity activity, final RestApiHandler handler) {
        if (!checkNetWork()) {
            handler.onFailure(ApiError.errorFromString("请求失败，请重试！", 0));
            return;
        }
        if (params == null) {
            params = new RequestParams();
        }
        addCustomHeader();
        client.post(BuildConfig.API_URI + path, params, new RestApiTextHttpHandler(activity) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                dispatchSuccessResult(path, responseString, handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 0) {
                    Toast.makeText(activity, "网络不太给力,请重试!", Toast.LENGTH_SHORT).show();
                    return;
                }
                responseString = responseString.replace("[","").replace("]","");
                dispatchFailureResult(path, statusCode, responseString, handler);
            }
        });
    }

    /**
     * 统一处理请求成功的结果
     *
     * @param path         路径
     * @param resultString json字符串
     * @param handler
     */
    private static void dispatchSuccessResult(String path, String resultString, RestApiHandler handler) {
        try {
            if (isDebug()) {
                LogUtils.d(LOG_TAG, "path = " + path + ", result= ");
                LogUtils.d(LOG_TAG, resultString);
            }
            handler.onSuccess(resultString);
        } catch (Exception e) {
            LogUtils.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * 统一处理请求失败的结果
     *
     * @param path         路径
     * @param statusCode   状态码
     * @param resultString json字符串
     * @param handler
     */
    private static void dispatchFailureResult(String path, int statusCode, String resultString, RestApiHandler handler) {
        try {
            if (isDebug()) {
                LogUtils.d(LOG_TAG, "path = " + path + ", result= ");
                LogUtils.d(LOG_TAG, resultString);
            }
            ApiError apiError = ApiError.errorFromString(resultString, statusCode);
            handler.onFailure(apiError);
        } catch (Exception e) {
            LogUtils.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * 检查网络状况
     *
     * @return
     */
    private static boolean checkNetWork() {
//        if (!ConnectivityUtils.isConnected(PandaApplication.getInstance())) {
//            Toast.makeText(PandaApplication.getInstance(), "网络不太给力，请重试！", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    /**
     * 是否开启调试，输出日志
     *
     * @return true开启；false关闭；
     */
    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
