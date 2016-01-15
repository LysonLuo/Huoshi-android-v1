package im.huoshi.asynapi.handler;

import com.loopj.android.http.TextHttpResponseHandler;

import im.huoshi.base.BaseActivity;
import im.huoshi.utils.LogUtils;

/**
 * Created by Lyson on 16/1/4.
 */
abstract public class RestApiTextHttpHandler extends TextHttpResponseHandler {
    private static final String Log_Tag = RestApiTextHttpHandler.class.getSimpleName();
    protected BaseActivity mActivity;

    public RestApiTextHttpHandler(BaseActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(Log_Tag, "onStart");
    }

    @Override
    public void onFinish() {
        super.onFinish();
        LogUtils.d(Log_Tag, "onFinish");
    }
}
