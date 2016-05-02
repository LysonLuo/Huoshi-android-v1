package im.huoshi.ui.huoshi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.HuoshiRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.model.HuoshiData;
import im.huoshi.ui.main.LoginActivity;
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
    @ViewInject(R.id.textview_share_number)
    private TextView mShareNumberTextView;
    @ViewInject(R.id.textview_today_share)
    private TextView mShareTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_huoshi, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        handleReadData();
        loadData();
        return contentView;
    }

    private void setupViews() {
        mGuideLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.launch((BaseActivity) getActivity());
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

    private void handleReadData() {
        int dayBetween = DateUtils.getDayBetween(mLocalRead.getLastReadLong());
        if (dayBetween > 1) {
            //时间间隔超过一天，就应该重置了！
            mLocalRead.updateContinuousDays(0);
        }
    }

    private void loadData() {
        HuoshiRequest.tab((BaseActivity) getActivity(), mUser.getUserId(), new RestApiCallback() {
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

    private void setupViewsByHuoshi() {
        mReadTextView.setText(mReadStat.getContinuousDays() + "");
        mIntercesTextView.setText(mHuoshiData.getContinuousIntercesDays() + "");
        mShareTextView.setText(mHuoshiData.getShareToday());
        mShareNumberTextView.setText("昨日兄弟姐妹共分享活石" + mHuoshiData.getShareNumber() + "次");
    }
}
