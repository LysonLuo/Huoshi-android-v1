package im.huoshi.ui.huoshi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import im.huoshi.BuildConfig;
import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.DailyAsked;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/5/24.
 */
public class DailyAskedDetailActivity extends BaseActivity {
    @ViewInject(R.id.tv_asked_title)
    private TextView mTvAskedTitle;
    @ViewInject(R.id.tv_asked_content)
    private TextView mTvAskedContent;
    @ViewInject(R.id.tv_asked_time)
    private TextView mTvAskedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_asked_detail);
        ViewUtils.inject(this);
        setupViewsByDailyAsked();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText("每日一问");
        mToolbarUtils.setRightViewBg(R.drawable.shape_white_rec_blue_solid);
        mToolbarUtils.setRightViewColor(R.color.text_color_white);
        mToolbarUtils.setRightText("分享");
    }


    @Override
    public void onToolBarRightViewClick(View v) {
        //// TODO: 16/6/23 每日一问分享地址
        ShareUtils.init(this, mLocalRead.getDailyAsked().getTitle(), BuildConfig.WEB_URI + "intercession/share.php?share_id=" + mLocalRead.getDailyAsked().getQuestionId());
    }

    private void setupViewsByDailyAsked() {
        DailyAsked dailyAsked = mLocalRead.getDailyAsked();
        mTvAskedTitle.setText(dailyAsked.getTitle());
        mTvAskedContent.setText(dailyAsked.getContent());
        mTvAskedTime.setText(DateUtils.formatToString(System.currentTimeMillis(), "yyyy年MM月dd日"));
    }
}
