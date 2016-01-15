package im.huoshi.asynapi.request;

import com.loopj.android.http.RequestParams;

import java.util.Map;
import java.util.TreeMap;

import im.huoshi.BuildConfig;
import im.huoshi.utils.SecurityUtils;

/**
 * Created by Lyson on 16/1/15.
 */
public class BaseRequest {

    protected static String getBasePath(String path) {
        return "v1/" + path;
    }

    protected static RequestParams buildRequestParams(TreeMap<String, String> _params) {
        _params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        _params.put("sign", SecurityUtils.SHA1(buildSignString(_params)));
        return new RequestParams(_params);
    }

    private static String buildSignString(TreeMap<String, String> _params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : _params.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString() + ":" + BuildConfig.APP_PRIVATE_KEY;
    }
}
