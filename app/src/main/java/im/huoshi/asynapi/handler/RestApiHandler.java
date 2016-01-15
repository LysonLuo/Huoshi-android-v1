package im.huoshi.asynapi.handler;

import im.huoshi.model.ApiError;

/**
 * Created by Lyson on 16/1/4.
 */
abstract public class RestApiHandler {
    abstract public void onSuccess(String responseString);

    abstract public void onFailure(ApiError apiError);
}
