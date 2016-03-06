package im.huoshi.ui.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.update.UmengUpdateAgent;

import de.hdodenhof.circleimageview.CircleImageView;
import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.data.ReadPreference;
import im.huoshi.ui.main.MainActivity;
import im.huoshi.ui.main.RegisterActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class MeFragment extends BaseFragment {
    @ViewInject(R.id.imageview_user)
    private CircleImageView mAvatarImageView;
    @ViewInject(R.id.textview_nickname)
    private TextView mNickNameTextView;
    @ViewInject(R.id.textview_read_minutes)
    private TextView mReadMinutesTextView;
    @ViewInject(R.id.layout_user_info)
    private RelativeLayout mUserLayout;
    @ViewInject(R.id.textview_my_prayer)
    private TextView mPrayerTextView;
    @ViewInject(R.id.textview_logout)
    private TextView mLogoutTextView;
    @ViewInject(R.id.textview_check_update)
    private TextView mCheckUpdateTextview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_me, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            Glide.with(this).load(mUser.getAvatar()).into(mAvatarImageView);
            mNickNameTextView.setText(mUser.getNickName());
            mReadMinutesTextView.setText("阅读时间：" + mReadStat.getTotalMinutes() + "分钟");
            return;
        }
        mAvatarImageView.setImageResource(R.mipmap.image_default_avatar);
        mNickNameTextView.setText("");
    }

    private void setupViews() {
        mUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotLogin()) {
                    return;
                }
                UserInfoActivity.launch((BaseActivity) getActivity());
            }
        });
        mPrayerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotLogin()) {
                    return;
                }
                MyPrayerActivity.launch((MainActivity) getActivity());
            }
        });
        mLogoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocalUser.logout();
                ReadPreference.getInstance().clearData();
                RegisterActivity.launch((MainActivity) getActivity());
            }
        });
        mCheckUpdateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.forceUpdate(getActivity());
            }
        });
    }
}
