package im.huoshi.ui.huoshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.HuoshiRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.model.DailyAsked;
import im.huoshi.model.HuoshiData;
import im.huoshi.model.event.RefreshHuoshiEvent;
import im.huoshi.ui.find.interces.InterCesActivity;
import im.huoshi.ui.main.LoginActivity;
import im.huoshi.ui.main.MainActivity;
import im.huoshi.ui.main.SearchFragment;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/14.
 */
public class HuoshiFragment extends BaseFragment {
    @ViewInject(R.id.layout_guide_login)
    private RelativeLayout mGuideLoginLayout;
    @ViewInject(R.id.layout_normal)
    private LinearLayout mNormalLayout;
    @ViewInject(R.id.textview_continuous_read_days)
    private TextView mReadTextView;
    @ViewInject(R.id.textview_continuous_interces_days)
    private TextView mIntercesTextView;
    @ViewInject(R.id.tv_interces)
    private TextView mTvInterces;
    @ViewInject(R.id.tv_search)
    private TextView mTvSearch;
    @ViewInject(R.id.textview_share_number)
    private TextView mShareNumberTextView;
    @ViewInject(R.id.ll_daily_asked)
    private LinearLayout mLlDailyAsked;
    @ViewInject(R.id.tv_asked_title)
    private TextView mTvAskedTitle;
    @ViewInject(R.id.tv_asked_content)
    private TextView mTvAskedContent;

    private SearchFragment mSearchFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(RefreshHuoshiEvent refreshHuoshiEvent) {
        setupViewsByHuoshi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_huoshi, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        handleData();
        loadData();
        loadDailyAsked();
        return contentView;
    }

    private void setupViews() {
        mGuideLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.launch((BaseActivity) getActivity());
            }
        });
        mLlDailyAsked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyAsked dailyAsked = mLocalRead.getDailyAsked();
                if (dailyAsked.getQuestionId() != 0 && !TextUtils.isEmpty(dailyAsked.getContent())) {
                    startActivity(new Intent(getActivity(), DailyAskedDetailActivity.class));
                }
            }
        });
        mTvInterces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterCesActivity.launch((BaseActivity) getActivity());
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSearchFragment();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            mNormalLayout.setVisibility(View.VISIBLE);
            mGuideLoginLayout.setVisibility(View.GONE);
        } else {
            mNormalLayout.setVisibility(View.GONE);
            mGuideLoginLayout.setVisibility(View.VISIBLE);
        }
        setupViewsByHuoshi();
    }

    private void handleData() {
        int dayBetweenRead = DateUtils.getDayBetween(mLocalRead.getLastReadLong());
        int dayBetweenInterces = DateUtils.getDayBetween(mLocalRead.getLastIntercesTime());
        if (dayBetweenRead > 1) {
            //时间间隔超过一天，就应该重置了！
            mLocalRead.updateContinuousDays(0);
            mLocalRead.updateTodayMinutes(0);
            mLocalRead.updateYesterdayMinutes(0);
            mLocalRead.updateLastReadLong(0);
            mLocalRead.updateLastMinutes(0);
            mLocalRead.updateAddStat(true);
        }
        if (dayBetweenInterces > 1) {
            mLocalRead.updateContinuousIntercesDays(0);
            mLocalRead.updateLastIntercesTime(0);
        }
    }

    private void loadData() {
        reloadLocalData();
        HuoshiRequest.tab((BaseActivity) getActivity(), mUser.getUserId(), mHuoshiData.getContinuousIntercesDays(), mHuoshiData.getLastIntercesTime(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                HuoshiData huoshiData = new Gson().fromJson(responseString, new TypeToken<HuoshiData>() {
                }.getType());
                mLocalRead.saveHuoshiData(huoshiData);
                setupViewsByHuoshi();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void loadDailyAsked() {
        long dailyAskedTime = mLocalRead.getDailyAskedTime();
        int dayBetweenLast = DateUtils.getDayBetween(dailyAskedTime);
        DailyAsked dailyAsked = mLocalRead.getDailyAsked();
        if (dailyAskedTime != 0 && dayBetweenLast == 0 && dailyAsked.getQuestionId() != 0 && !TextUtils.isEmpty(dailyAsked.getContent())) {
            setupViewsByDailyAsked(dailyAsked);
            return;
        }
        HuoshiRequest.getDailyAsked((BaseActivity) getActivity(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                DailyAsked dailyAsked = new Gson().fromJson(responseString, new TypeToken<DailyAsked>() {
                }.getType());

                mLocalRead.saveDailyAsked(dailyAsked, 0);
                setupViewsByDailyAsked(dailyAsked);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void setupViewsByDailyAsked(DailyAsked dailyAsked) {
        if (dailyAsked != null && dailyAsked.getQuestionId() != 0) {
            mTvAskedTitle.setText(dailyAsked.getTitle());
            mTvAskedContent.setText(dailyAsked.getContent());
        }
    }

    private void setupViewsByHuoshi() {
        reloadLocalData();
        mReadTextView.setText(mReadStat.getContinuousDays() + "");
        mIntercesTextView.setText(mHuoshiData.getContinuousIntercesDays() + "");
        mShareNumberTextView.setText("昨日兄弟姐妹共分享活石" + mHuoshiData.getShareNumber() + "次");
    }

    private void addSearchFragment() {
        ((MainActivity) getActivity()).addSearchFragment();
    }
}
