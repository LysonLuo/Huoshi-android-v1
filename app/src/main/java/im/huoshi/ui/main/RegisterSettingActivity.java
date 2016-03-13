package im.huoshi.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.AccountRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.Gender;
import im.huoshi.model.QiNiuToken;
import im.huoshi.model.User;
import im.huoshi.ui.me.NickNameDialog;
import im.huoshi.utils.AvatarUtils;
import im.huoshi.utils.QiNiuUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/28.
 */
public class RegisterSettingActivity extends BaseActivity {
    @ViewInject(R.id.imageview_avatar)
    private CircleImageView mAvatarImageView;
    @ViewInject(R.id.button_finish_register)
    private Button mFinishButton;
    @ViewInject(R.id.textview_nick_name)
    private TextView mNickNameTextView;
    @ViewInject(R.id.textview_gender)
    private TextView mGenderTextView;
    @ViewInject(R.id.textview_birthday)
    private TextView mBirthdayTextView;
    @ViewInject(R.id.textview_believe_date)
    private TextView mBelieveDateTextView;
    private OptionsPickerView mGenderPickerView;
    private TimePickerView mTimerPickerView;
    private ArrayList<Gender> mGenderList = new ArrayList<>();
    private boolean mIsBirthday;
    private Uri mAvatarUri;
    private int mGender = -1;
    private String mNickName;
    private String mBirthday;
    private String mBelieveDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_setting);
        ViewUtils.inject(this);

        setupViews();
    }

    private void setupViews() {
        mGenderPickerView = new OptionsPickerView(RegisterSettingActivity.this);
        mTimerPickerView = new TimePickerView(RegisterSettingActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        mGenderList.add(new Gender(0, "女"));
        mGenderList.add(new Gender(1, "男"));
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        mTimerPickerView.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));
        mTimerPickerView.setTime(new Date());
        mTimerPickerView.setCyclic(false);
        mGenderPickerView.setPicker(mGenderList);
        mGenderPickerView.setTitle("选择性别");
        mGenderPickerView.setCyclic(false);
        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarUri = AvatarUtils.showEditAvatarDialog(RegisterSettingActivity.this);
            }
        });
        mNickNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NickNameDialog dialog = new NickNameDialog(RegisterSettingActivity.this, mUser.getNickName());
                Window window = dialog.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                window.setAttributes(layoutParams);
                dialog.setINickName(new NickNameDialog.INickName() {
                    @Override
                    public void OnNickName(String newNickName) {
                        mNickName = newNickName;
                        mNickNameTextView.setText(mNickName);
                    }
                });
                dialog.show();
            }
        });
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGender == -1) {
                    showShortToast(getString(R.string.text_choose_gender));
                    return;
                }
                if (TextUtils.isEmpty(mNickName)) {
                    showShortToast(getString(R.string.text_empty_nick_name));
                    return;
                }
                if (TextUtils.isEmpty(mBirthday)) {
                    showShortToast(getString(R.string.text_empty_birthday));
                    return;
                }
                if (TextUtils.isEmpty(mBelieveDate)) {
                    showShortToast(getString(R.string.text_empty_believe_date));
                    return;
                }
                showProgressDialog("正在保存...");
                AccountRequest.finish(RegisterSettingActivity.this, mUser.getUserId(), mGender, mNickName, mBirthday, mBelieveDate, "", "", "", "", new RestApiCallback() {
                    @Override
                    public void onSuccess(String responseString) {
                        dismissProgressDialog();
                        User user = new Gson().fromJson(responseString, new TypeToken<User>() {
                        }.getType());
                        mLocalUser.saveUser(user);
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        dismissProgressDialog();
                    }
                });
            }
        });
        mGenderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenderPickerView.show();
            }
        });

        mGenderPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                mGenderTextView.setText(mGenderList.get(i).value);
                mGender = mGenderList.get(i).key;
            }
        });
        mBirthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBirthday = true;
                mTimerPickerView.setTitle("选择生日");
                mTimerPickerView.show();
            }
        });
        mBelieveDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBirthday = false;
                mTimerPickerView.setTitle("选择信主时间");
                mTimerPickerView.show();
            }
        });
        mTimerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (mIsBirthday) {
                    mBirthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    mBirthdayTextView.setText(mBirthday);
                    return;
                }
                mBelieveDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                mBelieveDateTextView.setText(mBelieveDate);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case AvatarUtils.CAMERA_REQUEST_CODE:
                mAvatarUri = AvatarUtils.cropImage(RegisterSettingActivity.this, mAvatarUri);
                break;
            case AvatarUtils.GALLERY_REQUEST_CODE:
                Uri uri = Uri.parse(data.getData().toString());
                mAvatarUri = AvatarUtils.cropImage(RegisterSettingActivity.this, uri);
                break;
            case AvatarUtils.ACTION_IMAGE_CROP:
                preUploadAvatar();
                break;
        }
    }

    private void preUploadAvatar() {
        getQiNiuToken();
    }

    private void getQiNiuToken() {
        AccountRequest.getQiNiuToken(RegisterSettingActivity.this, mUser.getUserId(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                QiNiuToken qiNiuToken = new Gson().fromJson(responseString, new TypeToken<QiNiuToken>() {
                }.getType());
                uploadAvatar(qiNiuToken.getToken());

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void uploadAvatar(String token) {
        QiNiuUtils.upLoadFile(new File(getFilesDir() + "/Avatar.png").getAbsolutePath(), token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (!info.isOK()) {
                    showShortToast("头像修改失败！");
                    return;
                }
                showShortToast("头像修改成功！");
                User user = new Gson().fromJson(response.toString(), new TypeToken<User>() {
                }.getType());
                mLocalUser.updateAvatar(user.getAvatar());
                mUser = mLocalUser.getUser();
                Glide.with(RegisterSettingActivity.this).load(mUser.getAvatar()).into(mAvatarImageView);
            }
        });
    }

    public static void launch(RegisterActivity activity) {
        activity.startActivity(new Intent(activity, RegisterSettingActivity.class));
    }
}
