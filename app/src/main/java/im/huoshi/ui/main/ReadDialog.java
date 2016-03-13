package im.huoshi.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ReadRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.data.ReadPreference;
import im.huoshi.model.ReadStat;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/1.
 */
public class ReadDialog extends AppCompatDialog {
    @ViewInject(R.id.textview_continuous_read_days)
    private TextView mDayTextView;
    @ViewInject(R.id.textview_last_read_minutes)
    private TextView mLastTextView;
    @ViewInject(R.id.textview_total_read_minutes)
    private TextView mTotalTextView;
    @ViewInject(R.id.textview_remind_info)
    private TextView mInfoTextView;
    private ReadStat mReadStat;
    private TranslateAnimation mDayTransAnimation;

    public ReadDialog(Context context, int userId, ReadStat readStat) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_read_dialog);
        mReadStat = readStat;
        ViewUtils.inject(this);
        setupViews();
        asynReadData((BaseActivity) context, userId);
    }

    private void setupViews() {
        mDayTextView.setText(mReadStat.getContinuousDays() + "");
        mLastTextView.setText(mReadStat.getLastMinutes() + "分钟");
        mTotalTextView.setText(mReadStat.getTotalMinutes() + "分钟");
        mInfoTextView.setText(mReadStat.getNotice());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mDayTransAnimation = new TranslateAnimation(0, 0, 0, mDayTextView.getMeasuredHeight());
        mDayTransAnimation.setRepeatCount(Animation.INFINITE);
        mDayTransAnimation.setRepeatMode(Animation.RESTART);
        mDayTransAnimation.setDuration(1000);
        mDayTextView.startAnimation(mDayTransAnimation);
    }

    private void asynReadData(BaseActivity activity, int userId) {
        ReadRequest.readStat(activity, userId, mReadStat.getLastMinutes(), mReadStat.getTotalMinutes(), mReadStat.getContinuousDays(), ReadPreference.getInstance().getAddStat(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                ReadStat readStat = new Gson().fromJson(responseString, new TypeToken<ReadStat>() {
                }.getType());
                ReadPreference.getInstance().saveReadStat(readStat);
                ReadPreference.getInstance().updateAddStat(false);
                setupViews();
                stopAnim();
                LogUtils.d("ReadDialog", "同步成功~");
            }

            @Override
            public void onFailure() {
                stopAnim();
            }
        });
    }

    private void stopAnim() {
        if (mDayTransAnimation != null) {
            mDayTextView.clearAnimation();
        }
    }
}
