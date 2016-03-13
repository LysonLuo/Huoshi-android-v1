package im.huoshi.ui.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.AccountRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.User;
import im.huoshi.utils.CTextUtils;
import im.huoshi.utils.DeviceUtils;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.SecurityUtils;
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
    @ViewInject(R.id.edit_register_phone)
    private EditText mPhoneEditText;
    @ViewInject(R.id.edit_register_code)
    private EditText mCodeEditText;
    @ViewInject(R.id.edit_register_pwd)
    private EditText mPwdEditText;
    @ViewInject(R.id.button_next)
    private Button mNextButton;
    @ViewInject(R.id.textview_to_login)
    private TextView mToLoginTextView;
    @ViewInject(R.id.button_send_verification_code)
    private Button mSendButton;
    private ViewWrapperUtils mPhoneWrapper;
    private ViewWrapperUtils mPwdWrapper;
    private static final int ANIM_TIME_MILL = 800;
    private String mPhoneNumber;
    private String mVerificationCode;
    private String mPwd;
    private boolean mIsPhoneUI = true;//标志位，用于标志输电话号码页面还是密码页面
    private boolean mHasSendCode = false;//标志位,用于标志是否点击发送验证码
    private CountDownTimer mCountDownTimer;

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
                //处理电话号码
                if (mIsPhoneUI) {
                    handlePhone();
                    return;
                }
                //处理验证码,密码
                handleCodeAndPwd();
            }
        });
        mToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.launch(RegisterActivity.this);
                finish();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer = new CountDownTimer(60100, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mSendButton.setText("重新获取(" + millisUntilFinished / 1000 + ")");
                        mSendButton.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        mSendButton.setText(getString(R.string.text_send_code));
                        mSendButton.setEnabled(true);
                    }

                }.start();
                AccountRequest.getVerifyCode(RegisterActivity.this, mPhoneNumber, "", new RestApiCallback() {
                    @Override
                    public void onSuccess(String responseString) {
                        mHasSendCode = true;
                        showShortToast(getString(R.string.text_verification_sent_success));
                    }

                    @Override
                    public void onFailure() {
                        LogUtils.d(Log_Tag, "failure");
                        mCountDownTimer.cancel();
                        mCountDownTimer.onFinish();
                    }
                });
            }
        });
    }

    private void handlePhone() {
        mPhoneNumber = mPhoneEditText.getText().toString();
        if (CTextUtils.judgetPhone(RegisterActivity.this, mPhoneNumber)) {
            mIsPhoneUI = !mIsPhoneUI;
            hidePhoneLayout();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPwdLayout();
                    return;
                }
            }, ANIM_TIME_MILL + 100);
        }
    }

    private void handleCodeAndPwd() {
        if (!mHasSendCode) {
            showShortToast(getString(R.string.text_click_to_send_verification));
            return;
        }
        mVerificationCode = mCodeEditText.getText().toString();
        mPwd = mPwdEditText.getText().toString();
        if (TextUtils.isEmpty(mVerificationCode)) {
            showShortToast(getString(R.string.text_warn_verification_empty));
            return;
        }
        if (mVerificationCode.length() != 6) {
            showShortToast(getString(R.string.text_warn_verification_length));
            return;
        }
        if (CTextUtils.judgetPwd(RegisterActivity.this, mPwd)) {
            AccountRequest.register(RegisterActivity.this, mPhoneNumber, "", mVerificationCode, SecurityUtils.md5(mPwd), new RestApiCallback() {
                @Override
                public void onSuccess(String responseString) {
                    showShortToast(getString(R.string.text_register_success));
                    User user = new Gson().fromJson(responseString, new TypeToken<User>() {
                    }.getType());
                    mLocalUser.saveUser(user);
                    RegisterSettingActivity.launch(RegisterActivity.this);
                    finish();
                }

                @Override
                public void onFailure() {
                    //// TODO: 16/1/26 做各种判断,什么被注册,什么不合法啊..
                }
            });
        }
    }

    private void hidePhoneLayout() {
        ObjectAnimator.ofInt(mPhoneWrapper, "height", DeviceUtils.dip2px(RegisterActivity.this, 50), 0).setDuration(ANIM_TIME_MILL).start();
    }

    private void showPwdLayout() {
        ObjectAnimator.ofInt(mPwdWrapper, "height", 0, DeviceUtils.dip2px(RegisterActivity.this, 130)).setDuration(ANIM_TIME_MILL).start();
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }
}
