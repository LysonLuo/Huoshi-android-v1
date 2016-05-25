package im.huoshi.ui.huoshi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.DailyAsked;
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
        mToolbarUtils.setTitleText("今日一问");
    }

    private void setupViewsByDailyAsked() {
        DailyAsked dailyAsked = mLocalRead.getDailyAsked();
        mTvAskedTitle.setText(dailyAsked.getTitle());
        mTvAskedContent.setText(dailyAsked.getContent());
    }
}
