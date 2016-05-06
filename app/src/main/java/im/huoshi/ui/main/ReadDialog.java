package im.huoshi.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ReadRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.data.ReadPreference;
import im.huoshi.database.dao.CourageDao;
import im.huoshi.model.CourageWord;
import im.huoshi.model.ReadStat;
import im.huoshi.utils.DateUtils;
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
    private CourageDao mCourageDao;

    public ReadDialog(Context context, int userId, ReadStat readStat) {
        super(context, R.style.CustomPopup);
        setContentView(R.layout.widget_read_dialog);
        mReadStat = readStat;
        ViewUtils.inject(this);
        mCourageDao = new CourageDao();
        setupViews();
        handleNote();
        asynReadData((BaseActivity) context, userId);
    }

    private void setupViews() {
        mDayTextView.setText(mReadStat.getContinuousDays() + "");
        mLastTextView.setText(mReadStat.getYesterdayMinutes() + "分钟");
        mTotalTextView.setText(mReadStat.getTotalMinutes() + "分钟");
    }

    private void handleNote() {
        //上次阅读结束到现在的时间间隔
        int dayBetweenLast = DateUtils.getDayBetween(ReadPreference.getInstance().getLastReadLong());
        if (dayBetweenLast > 1) {
            ReadPreference.getInstance().clearYesterdayMinutes();
            ReadPreference.getInstance().clearTodayMinutes();
        }
        int todayMinutes = mReadStat.getTodayMinutes();
        CourageWord courageWord;
        String note = "您真是一个虔诚的教徒，希望您再接再厉";
        if (todayMinutes < 1) {
            //类型1
            courageWord = mCourageDao.getCourageByTypeAndStatus(1, true);
            if (6 == courageWord.getId()) {
                mCourageDao.updateStatusByTypeAndId(1, courageWord.getId(), false);
                mCourageDao.updateStatusByTypeAndId(1, 1, true);
            } else {
                mCourageDao.updateStatusByTypeAndId(1, courageWord.getId(), false);
                mCourageDao.updateStatusByTypeAndId(1, courageWord.getId() + 1, true);
            }
            note = courageWord.getWord();

        } else if (todayMinutes >= 1 && todayMinutes < 60) {
            //类型2
            courageWord = mCourageDao.getCourageByTypeAndStatus(2, true);
            if (16 == courageWord.getId()) {
                mCourageDao.updateStatusByTypeAndId(2, courageWord.getId(), false);
                mCourageDao.updateStatusByTypeAndId(2, 7, true);
            } else {
                mCourageDao.updateStatusByTypeAndId(2, courageWord.getId(), false);
                mCourageDao.updateStatusByTypeAndId(2, courageWord.getId() + 1, true);
            }
            note = courageWord.getWord();

        } else if (todayMinutes >= 60 && todayMinutes < 120) {
            //类型3
            courageWord = mCourageDao.getCourageByTypeAndStatus(3, true);
            if (26 == courageWord.getId()) {
                mCourageDao.updateStatusByTypeAndId(3, courageWord.getId(), false);
                mCourageDao.updateStatusByTypeAndId(3, 17, true);
            } else {
                mCourageDao.updateStatusByTypeAndId(3, courageWord.getId(), false);
                mCourageDao.updateStatusByTypeAndId(3, courageWord.getId() + 1, true);
            }
            note = courageWord.getWord();

        } else if (todayMinutes >= 120 || mReadStat.getContinuousDays() > 30) {
            //类型4
            courageWord = mCourageDao.getCourageByTypeAndStatus(4, true);
            note = courageWord.getWord();
        }
        mInfoTextView.setText(note);
    }


    private void asynReadData(BaseActivity activity, int userId) {
        ReadRequest.readStat(activity, userId, mReadStat.getLastMinutes(), mReadStat.getYesterdayMinutes(), mReadStat.getTodayMinutes(), mReadStat.getTotalMinutes(), mReadStat.getContinuousDays(), ReadPreference.getInstance().getAddStat(), mReadStat.getLastReadLong(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                ReadStat readStat = new Gson().fromJson(responseString, new TypeToken<ReadStat>() {
                }.getType());
                ReadPreference.getInstance().saveReadStat(readStat);
                ReadPreference.getInstance().updateAddStat(false);
                setupViews();
                LogUtils.d("ReadDialog", "同步成功~");
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
