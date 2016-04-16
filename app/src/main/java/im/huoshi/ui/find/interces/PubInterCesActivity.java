package im.huoshi.ui.find.interces;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.utils.AmapUtils;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/28.
 */
public class PubInterCesActivity extends BaseActivity {
    public static final int ACTION_PUB_OR_UPDATE_INTERCES = 101;
    @ViewInject(R.id.edit_interces_content)
    private EditText mContentEdit;
    @ViewInject(R.id.layout_remind)
    private LinearLayout mRemidLayout;
    @ViewInject(R.id.layout_time)
    private LinearLayout mTimeLayout;
    @ViewInject(R.id.checkbox_interces_loc)
    private CheckBox mLocCheckBox;
    @ViewInject(R.id.checkbox_interces_pre)
    private CheckBox mPreCheckBox;
    @ViewInject(R.id.textview_interces_year)
    private TextView mYearTextView;
    @ViewInject(R.id.textview_interces_month)
    private TextView mMonthTextView;
    @ViewInject(R.id.textview_interces_day)
    private TextView mDayTextView;
    @ViewInject(R.id.textview_interces_hour)
    private TextView mHourTextView;
    private boolean mLocSuccess = true;
    private boolean mIsUpdateInterces = false;//标志，是否更新代祷
    private int mIntercessionId;
    private TimePickerView mTimerPickerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_interces);
        ViewUtils.inject(this);

        setupViews();
        //发布代祷才需要定位
        if (!mIsUpdateInterces) {
            getLocation();
        }
    }

    private void setupViews() {
        mIsUpdateInterces = getIntent().getBooleanExtra("is_update", false);
        mIntercessionId = getIntent().getIntExtra("intercession_id", -1);
        if (mIsUpdateInterces) {
            mRemidLayout.setVisibility(View.GONE);
            mTimeLayout.setVisibility(View.GONE);
            mLocCheckBox.setVisibility(View.GONE);
            return;
        }
        mTimerPickerView = new TimePickerView(PubInterCesActivity.this, TimePickerView.Type.YEAR_MONTH_DAY_HOUR);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        mTimerPickerView.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        mTimerPickerView.setTime(new Date());
        mTimerPickerView.setCyclic(false);
        mTimerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                setPreUpdateTime(true, date.getTime());
            }
        });

        mLocCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerPickerView.show();
            }
        };
        mYearTextView.setOnClickListener(onClickListener);
        mMonthTextView.setOnClickListener(onClickListener);
        mDayTextView.setOnClickListener(onClickListener);
        mHourTextView.setOnClickListener(onClickListener);
        mPreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPreUpdateTime(isChecked, 0);
            }
        });
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 500) {
                    mToolbarUtils.setMiddleRightText("超出" + (s.toString().trim().length() - 500) + "字");
                    mToolbarUtils.setRightViewEnable(false);
                    return;
                }
                mToolbarUtils.setMiddleRightText("");
                mToolbarUtils.setRightViewEnable(true);
            }
        });
    }

    private void setPreUpdateTime(boolean isChecked, long time) {
        mYearTextView.setEnabled(isChecked);
        mMonthTextView.setEnabled(isChecked);
        mDayTextView.setEnabled(isChecked);
        mHourTextView.setEnabled(isChecked);
        if (isChecked) {
            String currentTime = DateUtils.formatToString(time == 0 ? (System.currentTimeMillis()) : time, "yyyy,MM,dd,HH");
            String[] times = currentTime.split(",");
            mYearTextView.setText(times[0]);
            mMonthTextView.setText(times[1]);
            mDayTextView.setText(times[2]);
            int startHour;
            if (time == 0) {
                startHour = (Integer.parseInt(times[3]) + 1);
            } else {
                startHour = (Integer.parseInt(times[3]));
            }
            mHourTextView.setText(startHour + "");
            return;
        }
        mYearTextView.setText("");
        mMonthTextView.setText("");
        mDayTextView.setText("");
        mHourTextView.setText("");
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        if (mIsUpdateInterces) {
            mToolbarUtils.setTitleText("更新代祷");
        } else {
            mToolbarUtils.setTitleText("发布代祷");
        }
        mToolbarUtils.setRightViewBg(R.drawable.drawable_button_blue_gray_selector);
        mToolbarUtils.setRightViewColor(R.color.text_color_white);
        mToolbarUtils.setMiddleRightViewColor(R.color.text_color_red_light);
        mToolbarUtils.setRightText("发布");
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        pubOrUpdateInterces();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!TextUtils.isEmpty(mContentEdit.getText().toString())) {
            new AlertDialog.Builder(PubInterCesActivity.this)
                    .setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }
    }

    private void pubOrUpdateInterces() {
        String content = mContentEdit.getText().toString();
        String location = "";
        if (TextUtils.isEmpty(content)) {
            showShortToast(mIsUpdateInterces ? "代祷更新内容不能为空！" : "代祷内容不能为空！");
            return;
        }
        if (mIsUpdateInterces) {
            InterCesRequest.updateInterces(PubInterCesActivity.this, mUser.getUserId(), mIntercessionId, content, new RestApiCallback() {
                @Override
                public void onSuccess(String responseString) {
                    showShortToast("更新成功！");
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure() {
                    showShortToast("更新失败！");
                }
            });
            return;
        }

        long preTime = 0;
        if (mPreCheckBox.isChecked()) {
            try {
                preTime = DateUtils.formatToLong(mYearTextView.getText().toString() + "-" + mMonthTextView.getText().toString() + "-" + mDayTextView.getText().toString() + ":" + mHourTextView.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (mLocSuccess) {
            location = mLocCheckBox.getText().toString();
        }
        if (preTime != 0 && preTime < System.currentTimeMillis()) {
            showShortToast("更新代祷时间不能小于当前时间");
            return;
        }
        InterCesRequest.pubInterces(PubInterCesActivity.this, mUser.getUserId(), content, true, preTime, location, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                showShortToast("发布成功！");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure() {
                showShortToast("发布失败！");
            }
        });
    }

    private void getLocation() {
        new AmapUtils(this, new AmapUtils.GetLoc() {
            @Override
            public void onLoc(final String provinceName, final String cityName, final double latitude, final double longitude) {
                String province = provinceName.length() > 2 ? provinceName.replace("省", "") : provinceName;
                String city = cityName.length() > 2 ? cityName.replaceAll("市", "") : cityName;
                mLocCheckBox.setText(province + city);
                mLocSuccess = true;
                mLocCheckBox.setEnabled(true);
            }

            @Override
            public void onLocFailure() {
                mLocCheckBox.setText("点我获取位置信息");
                mLocCheckBox.setEnabled(false);
                mLocSuccess = false;
            }
        });
    }

    public static void launch(BaseActivity activity, boolean isUpdate, int intercessionId) {
        Intent intent = new Intent(activity, PubInterCesActivity.class);
        intent.putExtra("is_update", isUpdate);
        intent.putExtra("intercession_id", intercessionId);
        activity.startActivityForResult(intent, ACTION_PUB_OR_UPDATE_INTERCES);
    }
}
