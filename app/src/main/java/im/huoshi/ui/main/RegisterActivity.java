package im.huoshi.ui.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.AccountRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.ApiError;
import im.huoshi.utils.DeviceUtils;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;
import im.huoshi.utils.ViewWrapperUtils;

/**
 * Created by Lyson on 15/12/28.
 */
public class RegisterActivity extends BaseActivity {
    private static final String Log_Tag = RegisterActivity.class.getSimpleName();
    @ViewInject(R.id.layout_phone)
    private LinearLayout mPhoneLayout;
    @ViewInject(R.id.layout_pwd)
    private LinearLayout mPwdLayout;
    @ViewInject(R.id.button_next)
    private Button mNextButton;
    @ViewInject(R.id.textview_to_login)
    private TextView mToLoginTextView;
    @ViewInject(R.id.button_send_verification_code)
    private Button mSendButton;
    private ViewWrapperUtils mPhoneWrapper;
    private ViewWrapperUtils mPwdWrapper;
    private static final int ANIM_TIME_MILL = 1000;
    private boolean mHasPhone = true;//标志位，用于标志输电话号码页面还是密码页面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);

        setupViews();
    }

    private void setupViews() {
        mPhoneWrapper = new ViewWrapperUtils(mPhoneLayout);
        mPwdWrapper = new ViewWrapperUtils(mPwdLayout);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHasPhone) {
                    mHasPhone = !mHasPhone;
                    hidePhoneLayout();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showPwdLayout();
                        }
                    }, ANIM_TIME_MILL + 100);
                    return;
                }
                RegisterSettingActivity.launch(RegisterActivity.this);
            }
        });
        mToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.launch(RegisterActivity.this);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountRequest.getVerifyCode(RegisterActivity.this, "15920822457", "", new RestApiCallback() {
                    @Override
                    public void onSuccess(String responseString) {
                        LogUtils.d(Log_Tag, "success");
                    }

                    @Override
                    public void onFailure(ApiError apiError) {
                        LogUtils.d(Log_Tag, "failure");
                    }
                });
            }
        });
    }

    private void hidePhoneLayout() {
        ObjectAnimator.ofInt(mPhoneWrapper, "height", DeviceUtils.dip2px(RegisterActivity.this, 60), 0).setDuration(ANIM_TIME_MILL).start();
    }

    private void showPwdLayout() {
        ObjectAnimator.ofInt(mPwdWrapper, "height", 0, DeviceUtils.dip2px(RegisterActivity.this, 130)).setDuration(ANIM_TIME_MILL).start();
    }

    public static void launch(MainActivity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }
}
