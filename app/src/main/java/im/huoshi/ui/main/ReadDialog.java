package im.huoshi.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ReadRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.data.ReadPreference;
import im.huoshi.model.ApiError;
import im.huoshi.model.ReadStat;
import im.huoshi.utils.LogUtils;

/**
 * Created by Lyson on 16/1/1.
 */
public class ReadDialog extends AppCompatDialog {
    public ReadDialog(Context context, int userId, ReadStat readStat) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_read_dialog);
        asynReadData((BaseActivity) context, userId, readStat);
    }


    private void asynReadData(BaseActivity activity, int userId, ReadStat readStat) {
        ReadRequest.readStat(activity, userId, readStat.getLastMinutes(), readStat.getTotalMinutes(), readStat.getContinuousDays(), false, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                ReadStat readStat = new Gson().fromJson(responseString, new TypeToken<ReadStat>() {
                }.getType());
                ReadPreference.getInstance().saveReadStat(readStat);
                ReadPreference.getInstance().updateAddStat(true);
                LogUtils.d("ReadDialog", "同步成功~");
            }

            @Override
            public void onFailure(ApiError apiError) {
                LogUtils.d("ReadDialog", apiError.errorMessage);
            }
        });
    }
}
