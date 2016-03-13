package im.huoshi.asynapi.callback;


/**
 * Created by Lyson on 16/1/4.
 */
abstract public class RestApiCallback {
    abstract public void onSuccess(String responseString);

    abstract public void onFailure();
}
