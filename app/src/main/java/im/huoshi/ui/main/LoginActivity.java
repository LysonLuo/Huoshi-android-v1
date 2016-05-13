package im.huoshi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.AccountRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.User;
import im.huoshi.utils.CTextUtils;
import im.huoshi.utils.SecurityUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.edit_login_phone)
    private EditText mPhoneEdit;
    @ViewInject(R.id.edit_login_pwd)
    private EditText mPwdEdit;
    @ViewInject(R.id.button_login)
    private Button mLoginButton;
    @ViewInject(R.id.textview_login_register)
    private TextView mRegisterTextView;
    private String mPhoneNumber;
    private String mPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        setupViews();
    }

    private void setupViews() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumber = mPhoneEdit.getText().toString();
                mPwd = mPwdEdit.getText().toString();
                if (CTextUtils.judgetPhone(LoginActivity.this, mPhoneNumber) && CTextUtils.judgetPwd(LoginActivity.this, mPwd)) {
                    showProgressDialog("正在登录...");
                    AccountRequest.login(LoginActivity.this, mPhoneNumber, SecurityUtils.md5(mPwd), new RestApiCallback() {
                        @Override
                        public void onSuccess(String responseString) {
                            dismissProgressDialog();
                            showShortToast(getString(R.string.text_login_success));
                            User user = new Gson().fromJson(responseString, new TypeToken<User>() {
                            }.getType());
                            mLocalUser.saveUser(user);
                            mLocalRead.updateTotalMinutes(user.getTotalMinutes());
                            mLocalRead.updateContinuousDays(user.getContinuousDays());
                            mLocalRead.updateContinuousIntercesDays(user.getContinuousIntercesDays());
                            mLocalRead.updateTotalShareTimes(user.getTotalShareTimes());
                            mLocalRead.updateTotalJoinIntercession(user.getTotalJoinIntercession());
                            mLocalRead.updateAddStat(false);
                            mLocalRead.updateYesterdayMinutes(user.getYesterdayMinutes());
                            mLocalRead.updateTodayMinutes(user.getTodayMinutes());
                            mLocalRead.updateLastReadLong(user.getLastReadLong());
                            mLocalRead.updateLastIntercesTime(user.getLastIntercesTime());
                            finish();
                        }

                        @Override
                        public void onFailure() {
                            dismissProgressDialog();
                        }
                    });
                }
            }
        });

        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.launch(LoginActivity.this);
                finish();
            }
        });
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
}
