package im.huoshi.ui.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
import im.huoshi.utils.AvatarUtils;
import im.huoshi.utils.QiNiuUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/27.
 */
public class UserInfoActivity extends BaseActivity {
    private static final String Log_Tag = UserInfoActivity.class.getSimpleName();
    private static final int ACTION_CHOOSE_AREA = 10;
    @ViewInject(R.id.layout_avatar)
    private RelativeLayout mAvatarLayout;
    @ViewInject(R.id.textview_setting_id)
    private TextView mIdTextView;
    @ViewInject(R.id.layout_nickname)
    private RelativeLayout mNickNameLayout;
    @ViewInject(R.id.layout_gender)
    private RelativeLayout mGenderLayout;
    @ViewInject(R.id.layout_district)
    private RelativeLayout mDistrictLayout;
    @ViewInject(R.id.layout_birthday)
    private RelativeLayout mBirthdayLayout;
    @ViewInject(R.id.layout_believe_date)
    private RelativeLayout mBelieveLayout;
    @ViewInject(R.id.imageview_setting_avatar)
    private CircleImageView mAvatarImageView;
    @ViewInject(R.id.textview_setting_nickname)
    private TextView mNickNameTextView;
    @ViewInject(R.id.textview_setting_gender)
    private TextView mGenderTextView;
    @ViewInject(R.id.textview_setting_district)
    private TextView mDistrictTextView;
    @ViewInject(R.id.textview_setting_birthday)
    private TextView mBirthdayTextView;
    @ViewInject(R.id.textview_setting_believe_date)
    private TextView mBelieveDateTextView;
    private OptionsPickerView mGenderPickerView;
    private TimePickerView mTimerPickerView;
    private ArrayList<Gender> mGenderList = new ArrayList<>();
    private boolean mIsBirthday;

    private String mNickName;
    private Uri mAvatarUri;
    private int mGender = -1;
    private String mBirthday;
    private String mBelieveDate;
    private String mProvinceName;
    private String mCityName;
    private String mProvinceId;
    private String mCityId;

    private boolean mContentHasChanged = false;//用于标记内容是否改变

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ViewUtils.inject(this);


        setupViews();
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        saveInfo();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setRightText("保存");
        mToolbarUtils.setTitleText("个人信息");
        mToolbarUtils.setRightViewBg(R.drawable.shape_white_rec_blue_solid);
        mToolbarUtils.setRightViewColor(R.color.text_color_white);
    }

    @Override
    public void onBackPressed() {
        if (mContentHasChanged) {
            saveInfo();
            return;
        }
        super.onBackPressed();
    }

    private void saveInfo() {
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
        AccountRequest.finish(UserInfoActivity.this, mUser.getUserId(), mGender, mNickName, mBirthday, mBelieveDate, mProvinceId, mProvinceName, mCityId, mCityName, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                dismissProgressDialog();
                User user = new Gson().fromJson(responseString, new TypeToken<User>() {
                }.getType());
                showShortToast("更新成功!");
                mLocalUser.saveUser(user);
                finish();
            }

            @Override
            public void onFailure() {
                dismissProgressDialog();
                showShortToast("更新失败!");
            }
        });
    }


    private void setupViews() {
        mGenderPickerView = new OptionsPickerView(UserInfoActivity.this);
        mGenderList.add(new Gender(0, "女"));
        mGenderList.add(new Gender(1, "男"));
        mGenderPickerView.setPicker(mGenderList);
        mGenderPickerView.setTitle("选择性别");
        mGenderPickerView.setCyclic(false);


        Glide.with(this).load(mUser.getAvatar()).into(mAvatarImageView);
        mIdTextView.setText(mUser.getNickId());
        mNickName = mUser.getNickName();
        mNickNameTextView.setText(mNickName);
        mGender = mUser.getGender();
        mGenderTextView.setText(mGender == -1 ? "" : (mUser.getGender() == 0 ? "女" : "男"));

        mProvinceId = mUser.getProvinceId();
        mProvinceName = mUser.getProvinceName();
        mCityId = mUser.getCityId();
        mCityName = mUser.getCityName();
        mDistrictTextView.setText(mProvinceName + mCityName);

        mBirthday = mUser.getBirthday();
        mBirthdayTextView.setText(mBirthday);
        mBelieveDate = mUser.getBelieveDate();
        mBelieveDateTextView.setText(mBelieveDate);

        mAvatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatarUri = AvatarUtils.showEditAvatarDialog(UserInfoActivity.this);
            }
        });
        mNickNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NickNameDialog dialog = new NickNameDialog(UserInfoActivity.this, mUser.getNickName());
                Window window = dialog.getWindow();
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                window.setAttributes(layoutParams);
                dialog.setINickName(new NickNameDialog.INickName() {
                    @Override
                    public void OnNickName(String newNickName) {
                        mNickName = newNickName;
                        mNickNameTextView.setText(mNickName);
                        mContentHasChanged = true;
                    }
                });
                dialog.show();
            }
        });
        mGenderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenderPickerView.setTitle("选择性别");
                mGenderPickerView.show();
                mContentHasChanged = true;
            }
        });
        mDistrictLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent areaIntent = new Intent(UserInfoActivity.this, AreaChooseActivity.class);
                startActivityForResult(areaIntent, ACTION_CHOOSE_AREA);
                mContentHasChanged = true;
            }
        });
        final TimePickerView.OnTimeSelectListener onTimeSelectListener = new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mContentHasChanged = true;
                if (mIsBirthday) {
                    mBirthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    mBirthdayTextView.setText(mBirthday);
                    return;
                }
                mBelieveDate = new SimpleDateFormat("yyyy").format(date);
                mBelieveDateTextView.setText(mBelieveDate);
            }
        };
        mBirthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBirthday = true;
                mTimerPickerView = new TimePickerView(UserInfoActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
                //控制时间范围
                Calendar calendar = Calendar.getInstance();
                mTimerPickerView.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));
                mTimerPickerView.setTime(new Date());
                mTimerPickerView.setCyclic(false);
                mTimerPickerView.setTitle("选择生日");
                mTimerPickerView.setOnTimeSelectListener(onTimeSelectListener);
                mTimerPickerView.show();
            }
        });

        mBelieveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBirthday = false;
                mTimerPickerView = new TimePickerView(UserInfoActivity.this, TimePickerView.Type.YEAR);
                //控制时间范围
                Calendar calendar = Calendar.getInstance();
                mTimerPickerView.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));
                mTimerPickerView.setTime(new Date());
                mTimerPickerView.setCyclic(false);
                mTimerPickerView.setTitle("选择信主时间");
                mTimerPickerView.setOnTimeSelectListener(onTimeSelectListener);
                mTimerPickerView.show();
            }
        });

        mGenderPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                mContentHasChanged = true;
                mGenderTextView.setText(mGenderList.get(i).value);
                mGender = mGenderList.get(i).key;
            }
        });
    }

    private void preUploadAvatar() {
        getQiNiuToken();
    }

    private void getQiNiuToken() {
        AccountRequest.getQiNiuToken(UserInfoActivity.this, mUser.getUserId(), new RestApiCallback() {
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
        QiNiuUtils.upLoadFile(new File(mAvatarUri.getPath()).getAbsolutePath(), token, new UpCompletionHandler() {
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
                Glide.with(UserInfoActivity.this).load(mUser.getAvatar()).into(mAvatarImageView);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case AvatarUtils.CAMERA_REQUEST_CODE:
                mAvatarUri = AvatarUtils.cropImage(UserInfoActivity.this, mAvatarUri);
                break;
            case AvatarUtils.GALLERY_REQUEST_CODE:
                Uri uri = Uri.parse(intent.getData().toString());
                mAvatarUri = AvatarUtils.cropImage(UserInfoActivity.this, uri);
                break;
            case AvatarUtils.ACTION_IMAGE_CROP:
                preUploadAvatar();
                break;
            case ACTION_CHOOSE_AREA:
                mUser = mLocalUser.getUser();
                mProvinceId = mUser.getProvinceId();
                mProvinceName = mUser.getProvinceName();
                mCityId = mUser.getCityId();
                mCityName = mUser.getCityName();
                mDistrictTextView.setText(mProvinceName + mCityName);
                break;
        }
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, UserInfoActivity.class));
    }
}
